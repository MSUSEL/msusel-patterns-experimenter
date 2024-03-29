/*
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

import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.annotations.MetricCategory
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition
import edu.montana.gsoc.msusel.metrics.annotations.MetricProperties
import edu.montana.gsoc.msusel.metrics.annotations.MetricScale
import edu.montana.gsoc.msusel.metrics.annotations.MetricScope
import edu.montana.gsoc.msusel.metrics.annotations.MetricType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Modularity",
        primaryHandle = "sigModularity",
        description = "The degree to which a system or computer program is composed of disrete components such that a change to one component has minimal impact on other components.",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = ["ISO/IEC 25010"]
)
class ModularityEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        measureValue(node)
    }

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            double moduleCoupling   = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigModuleCoupling.RATING", node)
            double compBalance      = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigComponentBalance.RATING", node)
            double compIndependence = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigComponentIndependence.RATING", node)
            double compEntanglement = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigComponentEntanglement.RATING", node)

            def factors = [
                    [0.25, moduleCoupling],
                    [0.25, compBalance],
                    [0.25, compIndependence],
                    [0.25, compEntanglement]
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:sigModularity").on(node).withValue(value)
        }
    }
}
