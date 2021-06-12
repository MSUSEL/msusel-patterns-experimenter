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
package edu.montana.gsoc.msusel.arc.impl.quality.sigmain;

import edu.isu.isuese.datamodel.FileType;
import edu.isu.isuese.datamodel.Namespace;
import edu.isu.isuese.datamodel.Project;
import edu.isu.isuese.datamodel.Type;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;
import edu.montana.gsoc.msusel.metrics.MetricEvaluator;
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class SigCalibrationCommand extends SecondaryAnalysisCommand {

    SigCalibrationMetricProvider provider;

    public SigCalibrationCommand(SigCalibrationMetricProvider provider) {
        super(SigMainConstants.SIGCAL_CMD_NAME);
        this.provider = provider;
    }

    @Override
    public void execute(ArcContext context) {
        log.info("Starting Sig Maintainability Calibration Analysis");

        context.open();
        streamAndMeasureProject(context.getProject(), provider.getRegistrar().getPrimaryEvaluators());

        provider.getRegistrar().getSecondaryEvaluators().forEach(metricEvaluator -> {
            MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
            log.info("Measuring project with: " + mdef.primaryHandle());
            metricEvaluator.measure(context.getProject());
        });
        context.close();

        log.info("Finished Sig Maintainability Calibration Analysis");
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

    private void streamAndMeasureTypes(Namespace ns, List<MetricEvaluator> evaluatorList) {
        ns.getAllTypes().forEach(type -> {
            streamAndMeasureMethods(type, evaluatorList);
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

    private void streamAndMeasureNamespaces(Project proj, List<MetricEvaluator> evaluatorList) {
        proj.getNamespaces().forEach(ns -> {
            streamAndMeasureTypes(ns, evaluatorList);
            evaluatorList.forEach(metricEvaluator -> {
                MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
                log.info("Measuring Namespaces using " + mdef.primaryHandle());
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

    private void streamAndMeasureProject(Project proj, List<MetricEvaluator> evaluatorList) {
        streamAndMeasureNamespaces(proj, evaluatorList);
        streamAndMeasureFiles(proj, evaluatorList);
        streamAndMeasureModules(proj, evaluatorList);
        evaluatorList.forEach(metricEvaluator -> {
            MetricDefinition mdef = metricEvaluator.getClass().getAnnotation(MetricDefinition.class);
            log.info("Measuring Projects using " + mdef.primaryHandle());
            metricEvaluator.measure(proj);
        });
    }
}
