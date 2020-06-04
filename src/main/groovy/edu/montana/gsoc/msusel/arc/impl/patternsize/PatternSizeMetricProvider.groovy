package edu.montana.gsoc.msusel.arc.impl.patternsize

import edu.isu.isuese.datamodel.Metric
import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.provider.AbstractMetricProvider
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternSizeMetricProvider extends AbstractMetricProvider {

    PatternSizeMetricProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
    }

    @Override
    void updateDatabase() {
        Class<? extends MetricEvaluator> eval = PatternSizeEvaluator.class
        MetricDefinition mdef = eval.getAnnotation(MetricDefinition.class)
        String handle = mdef.primaryHandle()
        String name = mdef.name()
        String desc = mdef.description()
        String key = ArcPatternConstants.PATTERN_SIZE_REPO_KEY + ":" + handle

        Metric metric = Metric.builder()
                .name(name)
                .handle(handle)
                .key(key)
                .description(desc)
                .evaluator(eval.getName())
                .create()
        repository.addMetric(metric);
    }

    @Override
    void initRepository() {
        this.repository = MetricRepository.findFirst("repoKey = ?", ArcPatternConstants.PATTERN_SIZE_REPO_KEY)
    }
}
