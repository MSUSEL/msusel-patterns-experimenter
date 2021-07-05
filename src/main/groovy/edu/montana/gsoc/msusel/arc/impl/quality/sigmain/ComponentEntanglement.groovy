/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.montana.gsoc.msusel.arc.impl.quality.sigmain

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.common.graph.MutableNetwork
import com.google.common.graph.NetworkBuilder
import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.detstrat.GraphUtils
import edu.isu.isuese.detstrat.impl.GraphElementFactory
import edu.isu.isuese.detstrat.impl.NamespaceRelation
import edu.isu.isuese.detstrat.impl.Node
import edu.isu.isuese.detstrat.impl.Relationship
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.metrics.annotations.*
import groovy.util.logging.Log4j2
import groovyx.gpars.GParsExecutorsPool

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Sig Component Entanglement",
        primaryHandle = "sigComponentEntanglement",
        description = "",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = []
)
@Log4j2
class ComponentEntanglement extends SigMainComponentMetricEvaluator {

    MutableNetwork<Node, Relationship> graph

    ComponentEntanglement(ArcContext context) {
        super(context)
    }

    @Override
    protected double evaluate(Project proj) {
        // 1. create component graph
        createGraph(proj)

        // 2. calc communication density
        double commDensity = ((double) graph.nodes().size()) / graph.edges().size()

        // 3. calc communication violation ratio
        if (graph.edges().size() == 0) {
            return 1.0
        } else {
            GraphUtils.getInstance().markCycles(graph)
            double numCyclic = graph.edges().findAll { it.cyclic }.size()
            double commViolationRatio = numCyclic / graph.edges().size()

            // 4. calculate component entanglement
            return commDensity * commViolationRatio
        }
    }

    @Override
    protected String getMetricName() {
        "sigComponentEntanglement"
    }

    private void createGraph(Project proj) {
        Map<String, Node> nsMap = Maps.newHashMap()

        graph = NetworkBuilder.directed()
                .allowsParallelEdges(false)
                .allowsSelfLoops(false)
                .build()

        context.open()
        List<Namespace> namespaces = Lists.newArrayList(proj.getNamespaces())
        context.close()

        GParsExecutorsPool.withPool(8) {
            namespaces.eachParallel { Namespace ns ->
                context.open()
                if (!ns.getName().isEmpty()) {
                    Node node = GraphElementFactory.getInstance().createNode(ns)
                    nsMap.put(ns.getNsKey(), node)
                    graph.addNode(node)
                }
                context.close()
            }
        }

        GParsExecutorsPool.withPool(8) {
            AtomicInteger j = new AtomicInteger(1)
            namespaces.eachParallel { Namespace ns ->
                context.open()
                Node nsNode = nsMap.get(ns)
                String nsName = ns.getName()
                context.close()
                Set<String> inNs = Sets.newConcurrentHashSet()
                Set<String> outNs = Sets.newConcurrentHashSet()

                if (!nsName.isEmpty()) {
                    context.open()
                    List<Type> types = Lists.newArrayList(ns.getAllTypes())
                    context.close()
                    AtomicInteger i = new AtomicInteger(1)
                    GParsExecutorsPool.withPool(8) {
                        types.eachParallel { Type type ->
                            log.info "processing ns ${j.get()}/${namespaces.size()} type ${i.getAndIncrement()} of ${types.size()}"
                            context.open()
                            // incoming
                            inNs += type.getRealizedBy()*.getParentNamespace()*.getNsKey()
                            inNs += type.getGeneralizes()*.getParentNamespace()*.getNsKey()
                            inNs += type.getUseFrom()*.getParentNamespace()*.getNsKey()
                            inNs += type.getAssociatedFrom()*.getParentNamespace()*.getNsKey()
                            inNs += type.getAggregatedFrom()*.getParentNamespace()*.getNsKey()
                            inNs += type.getComposedFrom()*.getParentNamespace()*.getNsKey()

                            inNs.remove(ns.getNsKey())

                            // outgoing
                            outNs += type.getRealizes()*.getParentNamespace()*.getNsKey()
                            outNs += type.getGeneralizedBy()*.getParentNamespace()*.getNsKey()
                            outNs += type.getUseTo()*.getParentNamespace()*.getNsKey()
                            outNs += type.getAssociatedTo()*.getParentNamespace()*.getNsKey()
                            outNs += type.getAggregatedTo()*.getParentNamespace()*.getNsKey()
                            outNs += type.getComposedTo()*.getParentNamespace()*.getNsKey()

                            outNs.remove(ns.getNsKey())
                            context.close()
                        }
                    }
                }

                context.open()
                inNs.each { key ->
                    Node other = nsMap.get(key)
                    if (nsNode && other && !graph.hasEdgeConnecting(nsNode, other))
                        graph.addEdge(nsNode, other, new NamespaceRelation())
                }
                outNs.each { key ->
                    Node other = nsMap.get(key)
                    if (nsNode && other && !graph.hasEdgeConnecting(other, nsNode))
                        graph.addEdge(other, nsNode, new NamespaceRelation())
                }
                context.close()
                j.getAndIncrement()
            }
        }
    }
}
