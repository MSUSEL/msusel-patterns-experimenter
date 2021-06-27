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
import com.google.common.collect.Sets
import com.google.common.util.concurrent.AtomicDouble
import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.metrics.annotations.*
import groovyx.gpars.GParsPool

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Component Independence",
        primaryHandle = "sigComponentIndependence",
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
class ComponentIndependence extends SigMainComponentMetricEvaluator {

    ComponentIndependence(ArcContext context) {
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
        AtomicDouble hiddenSize = new AtomicDouble(0)
        context.open()
        List<Namespace> namespaces = Lists.newArrayList(proj.getNamespaces())
        context.close()

        GParsPool.withPool(8) {
            namespaces.eachParallel { Namespace ns ->
                context.open()
                double ca = ns.getValueFor("${MetricsConstants.METRICS_REPO_KEY}:Ca")
                context.close()

                if (ca > 0.0d) {
                    context.open()
                    String nsName = ns.getFullName()
                    List<Type> types = ns.getAllTypes()
                    context.close()

                    types.eachParallel { Type type ->
                        context.open()
                        double typeCa = it.getValueFor("${MetricsConstants.METRICS_REPO_KEY}:Ca")
                        if (typeCa > 0.0d) {
                            Set<String> set = findCouplings(type, nsName)
                            double size = it.getValueFor("${MetricsConstants.METRICS_REPO_KEY}:SLOC")
                            if (set.isEmpty()) {
                                hiddenSize.addAndGet(size)
                            }
                        }
                        context.close()
                    }
                }
            }
        }

        context.open()
        double projSize = proj.getValueFor("${MetricsConstants.METRICS_REPO_NAME}:SLOC")
        context.close()

        return (1 - (hiddenSize.get() / projSize)) * 100
    }

    private Set<String> findCouplings(Type type, String nsName) {
        Set<String> set = Sets.newHashSet()
        set.addAll(type.getRealizedBy()*.getCompKey())
        set.addAll(type.getGeneralizes()*.getCompKey())
        set.addAll(type.getAssociatedFrom()*.getCompKey())
        set.addAll(type.getAggregatedFrom()*.getCompKey())
        set.addAll(type.getComposedFrom()*.getCompKey())
        set.addAll(type.getDependencyFrom()*.getCompKey())
        set.addAll(type.getUseFrom()*.getCompKey())

        set.removeIf { it.contains(nsName) }

        set
    }

    @Override
    protected String getMetricName() {
        "sigComponentIndependence"
    }

}
