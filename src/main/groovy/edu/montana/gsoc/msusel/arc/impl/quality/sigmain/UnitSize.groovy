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

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.metrics.annotations.MetricCategory
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition
import edu.montana.gsoc.msusel.metrics.annotations.MetricProperties
import edu.montana.gsoc.msusel.metrics.annotations.MetricScale
import edu.montana.gsoc.msusel.metrics.annotations.MetricScope
import edu.montana.gsoc.msusel.metrics.annotations.MetricType
import groovyx.gpars.GParsExecutorsPool
import org.apache.commons.lang3.tuple.Pair

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Unit Size",
        primaryHandle = "sigUnitSize",
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
class UnitSize extends SigMainMetricEvaluator {

    UnitSize(ArcContext context) {
        super(context)
        riskMap[RiskCategory.LOW] = Pair.of(0.0, 15.0)
        riskMap[RiskCategory.MODERATE] = Pair.of(15.0,30.0)
        riskMap[RiskCategory.HIGH] = Pair.of(30.0, 60.0)
    }

    def evaluate(Project proj) {
        List<Method> methods = []
        context.open()
        methods = Lists.newArrayList(proj.getAllMethods())
        context.close()

        GParsExecutorsPool.withPool(8) {
            methods.eachParallel { Method method ->
                context.open()
                categorize(method, "SLOC")
                context.close()
            }
        }
    }

    @Override
    protected String getMetricName() {
        return "sigUnitSize"
    }
}
