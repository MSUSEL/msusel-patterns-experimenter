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


import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Component Balance",
        primaryHandle = "sigComponentBalance",
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
class ComponentBalance extends SigMainComponentMetricEvaluator {

    ComponentBalance() {}

    @Override
    protected double evaluate(Project proj) {
        List<Double> sizes = createSizesList(proj)
        Collections.sort(sizes)

        double giniCoefficient
        if (sizes.size() - 1 == 1) {
            giniCoefficient = 0.0
        } else {
            giniCoefficient = detemineGiniCoefficient(sizes)
        }

        return 1 - giniCoefficient
    }

    @Override
    protected String getMetricName() {
        "sigComponentBalance"
    }

    double detemineGiniCoefficient(List<Double> sizes) {
        int numPartitions
        int partitionSize
        (numPartitions, partitionSize) = createPartitions(sizes)
        double total = sizes.sum()
        List<Double> totals = calculatePartitionTotals(numPartitions, sizes, partitionSize)

        List<Double> frequencies = []
        List<Double> cummulative = []
        buildFrequencyLists(totals, frequencies, total, cummulative)

        calculateGiniCoef(cummulative, frequencies)
    }

    List<Double> createSizesList(Project proj) {
        List<Double> sizes = []
        proj.getNamespaces().each {
            sizes << it.getValueFor("${repo.getRepoKey()}:SLOC")
        }
//        sizes.removeIf { it == null }
        sizes
    }

    List createPartitions(List<Double> sizes) {
        int partitionSize, numPartitions
        if (sizes.size() <= 5) {
            numPartitions = sizes.size()
            partitionSize = 1
        } else {
            numPartitions = 5
            partitionSize = Math.ceil((double) sizes.size() / 5.0)
        }
        [numPartitions, partitionSize]
    }

    List<Double> calculatePartitionTotals(int numPartitions, List<Double> sizes, int partitionSize) {
        int currentPartition = 0
        int subIndex = 0
        List<Double> totals = []

        int fullSize = numPartitions * partitionSize
        int oneLess = fullSize - (sizes.size() % fullSize)
        int fullPartitions = numPartitions - oneLess

        numPartitions.times {
            totals << 0.0
        }
        sizes.each {
            totals[currentPartition] += it
            subIndex++
            if (subIndex == partitionSize) {
                currentPartition++
                subIndex = 0
            }
            if (currentPartition == fullPartitions) {
                partitionSize -= 1
            }
        }
        totals
    }

    void buildFrequencyLists(List<Double> totals, List<Double> frequencies, double total, List<Double> cummulative) {
        double runningTotal = 0

        totals.sort()

        totals.each {
            frequencies << it / total
            runningTotal += it
            cummulative << runningTotal / total
        }
    }

    double calculateGiniCoef(List<Double> cummulative, List<Double> frequencies) {
        double rects = 0
        double tris = 0

        for (int i = 0; i < cummulative.size() - 1; i++) {
            rects += (1.0 / cummulative.size()) * cummulative[i]
        }

        for (int i = 0; i < frequencies.size(); i++) {
            tris += (1.0 / frequencies.size()) * frequencies[i] * 0.5
        }

        double areaB = rects + tris

        double areaA = 0.5 - areaB

        return areaA / (areaA + areaB)
    }
}
