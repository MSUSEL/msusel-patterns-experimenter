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
package edu.montana.gsoc.msusel.arc.impl.metrics;

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.*;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.metrics.MetricEvaluator;
import edu.montana.gsoc.msusel.metrics.MetricsRegistrar;
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class ArcMetricsTool {

    MetricsRegistrar registrar;
    ArcContext context;
    List<MetricEvaluator> evaluatorList;
    List<MetricEvaluator> secondaryList;

    public ArcMetricsTool(ArcContext context) {
        this.context = context;
        registrar = MetricsToolMetricsProvider.getRegistrar();
    }

    public void init() {
        evaluatorList = Lists.newArrayList();
        evaluatorList.addAll(registrar.getCategoryEvaluators("all"));
        evaluatorList.addAll(registrar.getCategoryEvaluators("not-methods"));
        secondaryList = registrar.getSecondaryEvaluators();
        Collections.sort(evaluatorList);
    }

    public void exec() {
        Project proj = context.getProject();
        log.info("Resetting Evaluator State");
        evaluatorList.forEach(MetricEvaluator::resetState);
        secondaryList.forEach(MetricEvaluator::resetState);
        log.info("Measuring Primary Metrics");
        context.open();
        streamAndMeasureProject(proj, evaluatorList, true);
        log.info("Measuring Secondary Metrics");
        streamAndMeasureProject(proj, secondaryList, false);
        context.close();
    }

    private void streamAndMeasureMethods(Type type, List<MetricEvaluator> evaluatorList) {
        type.getMethods().forEach(method -> {
            evaluatorList.forEach(metricEvaluator -> {
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
                log.info("Measuring Method using " + mdef.primaryHandle());
                metricEvaluator.measure(method);
            });
        });
    }

    private void streamAndMeasureTypes(Namespace ns, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        ns.getAllTypes().forEach(type -> {
            if (measureMethods) {
                List<MetricEvaluator> methodEvals = Lists.newArrayList();
                methodEvals.addAll(registrar.getCategoryEvaluators("all"));
                methodEvals.addAll(registrar.getCategoryEvaluators("methods-only"));
                streamAndMeasureMethods(type, methodEvals);
            }
            evaluatorList.forEach(metricEvaluator -> {
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
                log.info("Measuring Types using " + mdef.primaryHandle());
                metricEvaluator.measure(type);
            });
        });
    }

    private void streamAndMeasureFiles(Project proj, List<MetricEvaluator> evaluatorList) {
        proj.getFilesByType(FileType.SOURCE).forEach(file -> {
            evaluatorList.forEach(metricEvaluator -> {
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
                log.info("Measuring Files using " + mdef.primaryHandle());
                metricEvaluator.measure(file);
            });
        });
    }

    private void streamAndMeasureNamespaces(Project proj, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        proj.getNamespaces().forEach(ns -> {
            streamAndMeasureTypes(ns, evaluatorList, measureMethods);
            evaluatorList.forEach(metricEvaluator -> {
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
                log.info("Measuring Namespaces using " + mdef.primaryHandle());
                if (!ns.getName().isEmpty())
                    metricEvaluator.measure(ns);
            });
        });
    }

    private void streamAndMeasureModules(Project project, List<MetricEvaluator> evaluatorList) {
        project.getModules().forEach(mod -> {
            evaluatorList.forEach(metricEvaluator -> {
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
                log.info("Measuring Modules using " + mdef.primaryHandle());
                metricEvaluator.measure(mod);
            });
        });
    }

    private void streamAndMeasureProject(Project proj, List<MetricEvaluator> evaluatorList, boolean measureMethods) {
        streamAndMeasureNamespaces(proj, evaluatorList, measureMethods);
        streamAndMeasureFiles(proj, evaluatorList);
        streamAndMeasureModules(proj, evaluatorList);
        evaluatorList.forEach(metricEvaluator -> {
            MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
            log.info("Measuring Projects using " + mdef.primaryHandle());
            metricEvaluator.measure(proj);
        });
    }
}
