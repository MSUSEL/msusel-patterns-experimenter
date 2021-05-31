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

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Metric
import edu.isu.isuese.datamodel.MetricRepository
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition

abstract class SigMainMetricEvaluator extends MetricEvaluator implements Rateable {

    protected Map<RiskCategory, Double> profile
    protected Table<Integer, RiskCategory, Range<Double>> ratingTable = HashBasedTable.create()
    protected Map<RiskCategory, Range<Double>> riskMap

    def measure(Measurable node) {
        if (node instanceof Project) {
            profile = [
                    RiskCategory.LOW       : 0.0d,
                    RiskCategory.MODERATE  : 0.0d,
                    RiskCategory.HIGH      : 0.0d,
                    RiskCategory.VERY_HIGH : 0.0d
            ]
            Project proj = node as Project

            evaluate(proj)

            double sysSize = Measure.valueFor(MetricsConstants.METRICS_REPO_KEY, "SLOC", node)
            normalize(sysSize)

            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${getMetricName()}.LOW").on(node).withValue(profile[RiskCategory.LOW])
            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${getMetricName()}.MOD").on(node).withValue(profile[RiskCategory.MODERATE])
            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${getMetricName()}.HIGH").on(node).withValue(profile[RiskCategory.HIGH])
            Measure.of("${SigMainConstants.SIGMAIN_REPO_KEY}:${getMetricName()}.VHIGH").on(node).withValue(profile[RiskCategory.VERY_HIGH])
        }
    }

    abstract def evaluate(Project proj)

    def normalize(sysSize) {
        profile[RiskCategory.LOW] /= sysSize
        profile[RiskCategory.MODERATE] /= sysSize
        profile[RiskCategory.HIGH] /= sysSize
        profile[RiskCategory.VERY_HIGH] /= sysSize
    }

    void categorize(Component comp, String handle) {
        double size = Measure.valueFor(MetricsConstants.METRICS_REPO_KEY, "SLOC", comp)
        double value = Measure.valueFor(MetricsConstants.METRICS_REPO_KEY, handle, comp)

        riskMap.each {cat, range ->
            if (range.containsWithinBounds(value)) {
                profile[cat] += size
                return
            }
        }

        profile[RiskCategory.VERY_HIGH] += size
    }

    MetricRater getMetricRater() {
        return new MultiValueRater(this.getMetricName())
    }

    protected abstract String getMetricName()

    Metric toMetric(MetricRepository repository) {
        repo = repository
        MetricDefinition mdef = this.getClass().getAnnotation(MetricDefinition.class)
        String primaryHandle = mdef.primaryHandle()
        String metricName = mdef.name()
        String metricDescription = mdef.description()

        Metric metricLow = Metric.findFirst("metricKey = ?", "${repository.getRepoKey()}:${primaryHandle}.LOW")
        Metric metricMod = Metric.findFirst("metricKey = ?", "${repository.getRepoKey()}:${primaryHandle}.MOD")
        Metric metricHigh = Metric.findFirst("metricKey = ?", "${repository.getRepoKey()}:${primaryHandle}.HIGH")
        Metric metricVHigh = Metric.findFirst("metricKey = ?", "${repository.getRepoKey()}:${primaryHandle}.VHIGH")
        Metric metricRating = Metric.findFirst("metricKey = ?", "${repository.getRepoKey()}:${primaryHandle}.RATING")
        if (!metricLow) {
            createMetric(repository, primaryHandle, metricName, metricDescription, "LOW")
        }
        if (!metricMod) {
            createMetric(repository, primaryHandle, metricName, metricDescription, "MOD")
        }
        if (!metricHigh) {
            createMetric(repository, primaryHandle, metricName, metricDescription, "HIGH")
        }
        if (!metricVHigh) {
            createMetric(repository, primaryHandle, metricName, metricDescription, "VHIGH")
        }
        if (!metricRating) {
            createMetric(repository, primaryHandle, metricName, metricDescription, "RATING")
        }
    }

    private Metric createMetric(MetricRepository repository, String primaryHandle, String metricName, String metricDescription, String postName) {
        Metric metric = Metric.builder()
                .key("${repository.getRepoKey()}:${primaryHandle}.${postName}")
                .handle("${primaryHandle}.${postName}")
                .name("${metricName}.${postName}")
                .description(metricDescription)
                .evaluator(this.class.getCanonicalName())
                .create()
        repository.addMetric(metric)
    }
}
