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

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.MetricsRegistrar
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition
import groovy.util.logging.Log4j2
import groovyx.gpars.GParsPool

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ArcMetricsTool {

    MetricsRegistrar registrar
    ArcContext context
    List<MetricEvaluator> evaluatorList
    List<MetricEvaluator> secondaryList

    ArcMetricsTool(ArcContext context) {
        this.context = context
        registrar = MetricsToolMetricsProvider.getRegistrar()
    }

    void init() {
        evaluatorList = Lists.newArrayList()
        evaluatorList.addAll(registrar.getCategoryEvaluators("all"))
        evaluatorList.addAll(registrar.getCategoryEvaluators("not-methods"))
        secondaryList = registrar.getSecondaryEvaluators()
        Collections.sort(evaluatorList)
    }

    void exec() {
        Project proj = context.getProject()
        log.info "Resetting Evaluator State"
        evaluatorList*.resetState()
        secondaryList*.resetState()
        log.info "Measuring Primary Metrics"
        context.open()
        streamAndMeasureProject(proj, evaluatorList, true)
        log.info "Measuring Secondary Metrics"
        streamAndMeasureProject(proj, secondaryList, false)
        context.close()
    }

    private void streamAndMeasureMethods(Type type, List<MetricEvaluator> evaluatorList) {
        type.getMethods().each { method ->
            GParsPool.withPool(8) {
                evaluatorList.eachParallel { metricEvaluator ->
                    MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                    log.info "Measuring Method using ${mdef.primaryHandle()}"
                    (metricEvaluator as MetricEvaluator).measure(method)
                }
            }
        }
    }

    private void streamAndMeasureTypes(Namespace ns, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        ns.getAllTypes().each { type ->
            if (measureMethods) {
                List<MetricEvaluator> methodEvals = Lists.newArrayList()
                methodEvals.addAll(registrar.getCategoryEvaluators("all"))
                methodEvals.addAll(registrar.getCategoryEvaluators("methods-only"))
                streamAndMeasureMethods(type, methodEvals)
            }

            GParsPool.withPool(8) {
                evaluatorList.eachParallel { metricEvaluator ->
                    MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                    log.info "Measuring Types using ${mdef.primaryHandle()}"
                    (metricEvaluator as MetricEvaluator).measure(type)
                }
            }
        }
    }

    private void streamAndMeasureFiles(Project proj, List<MetricEvaluator> evaluatorList) {
        proj.getFilesByType(FileType.SOURCE).each { file ->
            GParsPool.withPool(8) {
                evaluatorList.eachParallel { metricEvaluator ->
                    MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                    log.info "Measuring Files using ${mdef.primaryHandle()}"
                    (metricEvaluator as MetricEvaluator).measure(file)
                }
            }
        }
    }

    private void streamAndMeasureNamespaces(Project proj, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        proj.getNamespaces().each { ns ->
            streamAndMeasureTypes(ns, evaluatorList, measureMethods)
            GParsPool.withPool(8) {
                evaluatorList.eachParallel { metricEvaluator ->
                    MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                    log.info "Measuring Namespaces using ${mdef.primaryHandle()}"
                    (metricEvaluator as MetricEvaluator).measure(ns)
                }
            }
        }
    }

    private void streamAndMeasureModules(Project project, List<MetricEvaluator> evaluatorList) {
        project.getModules().each { mod ->
            GParsPool.withPool(8) {
                evaluatorList.eachParallel { metricEvaluator ->
                    MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                    log.info "Measuring Modules using ${mdef.primaryHandle()}"
                    (metricEvaluator as MetricEvaluator).measure(mod)
                }
            }
        }
    }

    private void streamAndMeasureProject(Project proj, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        streamAndMeasureNamespaces(proj, evaluatorList, measureMethods)
        streamAndMeasureFiles(proj, evaluatorList)
        streamAndMeasureModules(proj, evaluatorList)

        GParsPool.withPool(8) {
            evaluatorList.eachParallel { metricEvaluator ->
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class)
                log.info "Measuring Projects using ${mdef.primaryHandle()}"
                (metricEvaluator as MetricEvaluator).measure(proj)
            }
        }
    }
}
