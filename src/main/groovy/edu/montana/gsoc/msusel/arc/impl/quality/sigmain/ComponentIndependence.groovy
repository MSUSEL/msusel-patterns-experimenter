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

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.metrics.annotations.*

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

    ComponentIndependence() {}

    @Override
    protected double evaluate(Project proj) {
        double hiddenSize = 0
        proj.getNamespaces().each { ns ->
            ns.getAllTypes().each {
                Set<Type> set = Sets.newHashSet()

                set += it.getRealizedBy()
                set += it.getGeneralizes()
                set += it.getUseFrom()
                set += it.getAssociatedFrom()
                set += it.getAggregatedFrom()
                set += it.getComposedFrom()

                set.removeIf { Type t ->
                    t.getParentNamespace() == ns
                }

                double size = it.getValueFor("${MetricsConstants.METRICS_REPO_NAME}:SLOC")
                if (set.isEmpty()) {
                    hiddenSize += size
                }
            }
        }

        double projSize = proj.getValueFor("${MetricsConstants.METRICS_REPO_NAME}:SLOC")
        (hiddenSize / projSize)
    }

    @Override
    protected String getMetricName() {
        "sigComponentIndependence"
    }
}
