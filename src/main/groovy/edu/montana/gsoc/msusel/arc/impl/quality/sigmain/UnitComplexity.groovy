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

import edu.isu.isuese.datamodel.Project
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
        name = "SIG Unit Complexity",
        primaryHandle = "sigUnitComplexity",
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
class UnitComplexity extends SigMainMetricEvaluator {

    UnitSize() {
        ratingTable.put(5, RiskCategory.MODERATE, (0d..30d))
        ratingTable.put(5, RiskCategory.HIGH, (0d..5d))
        ratingTable.put(5, RiskCategory.VERY_HIGH, (0..0))

        ratingTable.put(4, RiskCategory.MODERATE, (30..41))
        ratingTable.put(4, RiskCategory.HIGH, (5..18))
        ratingTable.put(4, RiskCategory.VERY_HIGH, (0..5))

        ratingTable.put(3, RiskCategory.MODERATE, (41..50))
        ratingTable.put(3, RiskCategory.HIGH, (18..25))
        ratingTable.put(3, RiskCategory.VERY_HIGH, (5..7))

        ratingTable.put(2, RiskCategory.MODERATE, (50..60))
        ratingTable.put(2, RiskCategory.HIGH, (25..30))
        ratingTable.put(2, RiskCategory.VERY_HIGH, (7..10))

        riskMap[RiskCategory.LOW] = (1..10)
        riskMap[RiskCategory.MODERATE] = (11..20)
        riskMap[RiskCategory.HIGH] = (21..50)
    }

    def evaluate(Project proj) {
        proj.getAllMethods().each {
            categorize(it, "MCC")
        }
    }

    @Override
    protected String getMetricName() {
        return "sigUnitComplexity"
    }
}
