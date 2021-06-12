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

import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Metric
import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.arc.impl.pattern4.resultsdm.Project
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
        name = "SIG Volume",
        primaryHandle = "sigVolume",
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
class Volume extends SigAbstractMetricEvaluator implements Rateable {

    @Override
    def measure(Measurable node) {
        if (node instanceof Project) {
            double systemSize = Measure.valueFor(MetricsConstants.METRICS_REPO_NAME, "SLOC", node)
            double technologyFactor = 0.00136d

            double rebuildValue = systemSize * technologyFactor * 12

            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:sigVolume.RAW").on(node).withValue(rebuildValue)
        }
    }

    MetricRater getMetricRater() {
        return new SingleValueRater("sigVolume")
    }

    Metric toMetric(MetricRepository repository) {
        this.toMetric(repository, ["RAW", "RATING"])

        null
    }
}
