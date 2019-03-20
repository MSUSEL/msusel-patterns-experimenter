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
package edu.montana.gsoc.msusel.arc.impl.metrics

import com.google.inject.Inject
import edu.montana.gsoc.msusel.arc.AbstractRepoProvider
import edu.montana.gsoc.msusel.datamodel.DataModelMediator
import edu.montana.gsoc.msusel.datamodel.measures.Metric
import edu.montana.gsoc.msusel.datamodel.measures.MetricRepository
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition
import org.reflections.Reflections
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MetricsRepoProvider extends AbstractRepoProvider {

    @Inject
    DataModelMediator mediator
    Set<Class<MetricEvaluator>> metrics
    static final String METRICS_PKG = "edu.montana.gsoc.msusel.metrics.impl"

    @Override
    def loadData() {
        Reflections reflections = new Reflections(METRICS_PKG)

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(MetricDefinition.class)

        metrics = annotated.findAll {
            MetricDefinition mdef = it.getAnnotation(MetricDefinition.class)
            (mdef.name() != null && !mdef.name().isEmpty()) && (mdef.primaryHandle() != null && !mdef.primaryHandle().isEmpty())
        }
    }

    @Override
    def updateDatabase() {
        MetricRepository repo = MetricRepository.builder().repoKey("arc_metrics").name("ARC Metrics").create()
        mediator.addMetricRepository(repo)
        metrics.each {
            Metric m = it.newInstance().toMetric(repo)
            repo << m
            mediator.addMetric(m)
        }
        mediator.updateMetricRepository(repo)
    }
}
