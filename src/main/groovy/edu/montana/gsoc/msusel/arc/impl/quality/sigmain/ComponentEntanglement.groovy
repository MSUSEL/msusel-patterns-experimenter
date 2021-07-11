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
import groovy.sql.Sql
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
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            Project proj = node as Project

            double value = evaluate(proj)

            context.open()
            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${getMetricName()}.RAW").on(proj).withValue(value)
            context.close()
        }
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
        Map<String, Node> nsMap = Maps.newConcurrentMap()

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

//        GParsExecutorsPool.withPool(8) {
            int j = 1
            namespaces.each { Namespace ns ->
                int index = j++
                log.info "processing namespace ${index} / ${namespaces.size()}"
                context.open()
                Node nsNode = nsMap.get(ns.getNsKey())
                String nsName = ns.getName()
                int nsid = ns.getId()
                context.close()
                Set<String> inNs = Sets.newConcurrentHashSet()
                Set<String> outNs = Sets.newConcurrentHashSet()

                if (!nsName.isEmpty()) {
                    // incoming
                    context.open()
                    inNs += incomingNamespacesFromNamespace(nsid, index)*.getNsKey()

                    // outgoing
                    outNs += outgoingNamespacesFromNamespace(nsid, index)*.getNsKey()
                    context.close()
                }

                inNs.remove(ns.getNsKey())
                outNs.remove(ns.getNsKey())

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

//                dropViews(index)
            }
//        }
    }

    List<Type> outgoingTypesFromType(String compKey) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view outgoingRefKeys as
select distinct y.refKey
from types as t
inner join refs as r on r.refKey = ${compKey}
inner join relations on r.id = relations.to_id
inner join refs as y on y.id = relations.from_id;
""")
        List<Type> types = Type.findBySQL("select types.* from types inner join outgoingRefKeys on types.compKey = outgoingRefKeys.refKey")
        return types
    }

    List<Type> incomingTypesFromType(String compKey) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view incomingRefKeys as
select distinct y.refKey
from types as t
inner join refs as r on r.refKey = ${compKey}
inner join relations on r.id = relations.from_id
inner join refs as y on y.id = relations.to_id;
""")
        List<Type> types = Type.findBySQL("select types.* from types inner join incomingRefKeys on types.compKey = incomingRefKeys.refKey")
        return types
    }

    List<Namespace> outgoingNamespacesFromNamespace(int nsid, int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view outgoingNsRefs${index} as
select distinct ns.id as nsID, y.refKey
from namespaces as ns
    inner join types as t on t.namespace_id = ${nsid}
    inner join refs as r on t.compKey = r.refKey
    inner join relations on r.id = relations.from_id
    inner join refs as y on y.id = relations.to_id;
""")
        sql.close()

        List<Namespace> namespaces = Namespace.findBySQL("""\
select distinct ns.*
from namespaces as ns
inner join types on types.namespace_id = ns.id
inner join outgoingNsRefs${index} on types.compKey = outgoingNsRefs${index}.refKey
where ns.id != ${nsid};
""")
        return namespaces
    }

    List<Namespace> incomingNamespacesFromNamespace(int nsid, int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("""\
create or replace view incomingNsRefs${index} as
select distinct ns.id as nsID, y.refKey
from namespaces as ns
    inner join types as t on t.namespace_id = ${nsid}
    inner join refs as r on t.compKey = r.refKey
    inner join relations on r.id = relations.to_id
    inner join refs as y on y.id = relations.from_id;
""")
        sql.close()

        List<Namespace> namespaces = Namespace.findBySQL("""\
select distinct ns.*
from namespaces as ns
inner join types on types.namespace_id = ns.id
inner join incomingNsRefs${index} on types.compKey = incomingNsRefs${index}.refKey
where ns.id != ${nsid};
""")
        return namespaces
    }

    void dropViews(int index) {
        def sql = Sql.newInstance(context.getDBCreds().url, context.getDBCreds().user, context.getDBCreds().pass, context.getDBCreds().driver)
        sql.execute("drop view outgoingNsRefs${index}")
        sql.execute("drop view incomingNsRefs${index}")
        sql.close()
    }
}
