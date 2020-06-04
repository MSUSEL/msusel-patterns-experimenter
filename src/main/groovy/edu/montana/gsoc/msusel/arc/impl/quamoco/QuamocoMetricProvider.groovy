package edu.montana.gsoc.msusel.arc.impl.quamoco

import edu.isu.isuese.datamodel.Metric
import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.provider.AbstractMetricProvider
import edu.montana.gsoc.msusel.arc.ArcContext
import groovy.yaml.YamlSlurper

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class QuamocoMetricProvider extends AbstractMetricProvider {

    def config

    QuamocoMetricProvider(ArcContext context) {
        super(context)
    }

    void loadData() {
        File f = new File(new File(QuamocoConstants.QUAMOCO_CONFIG_DIR), "quamoco_metrics.yml")
        config = new YamlSlurper().parseText(f.text)
    }

    void initRepository() {
        repository = MetricRepository.findFirst("repoKey = ?", QuamocoConstants.QUAMOCO_REPO_KEY)
    }

    void updateDatabase() {
        String root = config.metrics.root
        context.addArcProperty(QuamocoProperties.QUAMOCO_METRICS_ROOT, root)

        config.metrics.each { Map<String, String> map ->
            Metric metric = Metric.findFirst("metricKey = ?", "${repository.getRepoKey()}:${map.name}")
            if (!metric) {
                metric = Metric.builder()
                        .key("${repository.getRepoKey()}:$map.name")
                        .handle(map.handle)
                        .name(map.name)
                        .description(map.description)
                        .evaluator()
                        .create()
                repository.addMetric(metric)
            }
        }
    }
}
