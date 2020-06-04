package edu.montana.gsoc.msusel.arc.impl.metrics;

import edu.isu.isuese.datamodel.File;
import edu.isu.isuese.datamodel.Module;
import edu.isu.isuese.datamodel.Namespace;
import edu.isu.isuese.datamodel.Project;
import edu.isu.isuese.datamodel.Type;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.metrics.MetricEvaluator;
import edu.montana.gsoc.msusel.metrics.MetricsRegistrar;

import java.util.Collections;
import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
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
        evaluatorList = registrar.getPrimaryEvaluators();
        secondaryList = registrar.getSecondaryEvaluators();
        Collections.sort(evaluatorList);
    }

    public void exec() {
        Project proj = context.getProject();
        context.logger().atInfo().log("Measuring Primary Metrics");
        evaluatorList.forEach(metricEvaluator -> metricEvaluator.measure(proj));
        context.logger().atInfo().log("Measuring Secondary Metrics");
        secondaryList.forEach(metricEvaluator -> metricEvaluator.measure(proj));
    }

    private void streamAndMeasureMethods(Type type) {
        type.getAllMethods().forEach(method -> {
            evaluatorList.forEach(metricEvaluator -> metricEvaluator.measure(method));
        });
    }

    private void streamAndMeasureTypes(File file) {
        file.getAllTypes().forEach(type -> {
            streamAndMeasureMethods(type);
            evaluatorList.forEach(metricEvaluator -> metricEvaluator.measure(type));
        });
    }

    private void streamAndMeasureFiles(Namespace ns) {
        ns.getFiles().forEach(file -> {
            streamAndMeasureTypes(file);
            evaluatorList.forEach(metricEvaluator -> metricEvaluator.measure(file));
        });
    }

    private void streamAndMeasureNamespaces(Module module) {
        module.getNamespaces().forEach(ns -> {
            streamAndMeasureFiles(ns);
            evaluatorList.forEach(metricEvaluator -> metricEvaluator.measure(ns));
        });
    }

    private void streamAndMeasureModules(Project project) {
        project.getModules().forEach(mod -> {
            streamAndMeasureNamespaces(mod);
            evaluatorList.forEach(metricEvaluator -> metricEvaluator.measure(mod));
        });
    }
}
