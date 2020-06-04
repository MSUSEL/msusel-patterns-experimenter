package edu.montana.gsoc.msusel.arc.impl.metrics


import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.provider.AbstractMetricProvider
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.MetricsRegistrar
import edu.montana.gsoc.msusel.metrics.impl.*

class MetricsToolMetricsProvider extends AbstractMetricProvider {

    static MetricsRegistrar registrar = new MetricsRegistrar()

    MetricsToolMetricsProvider(ArcContext context) {
        super(context)
    }

    @Override
    def loadData() {
        registerMetrics()
    }

    @Override
    def updateDatabase() {
        registrar.getPrimaryEvaluators().each {
            it.toMetric(repository)
        }
    }

    @Override
    void initRepository() {
        repository = MetricRepository.findFirst("repoKey = ?", MetricsConstants.METRICS_REPO_KEY)
    }

    void registerMetrics() {
        registrar.registerPrimary(new NumberOfFields())
        registrar.registerPrimary(new NumberOfMethods())
        registrar.registerPrimary(new NumberOfStatements())
        registrar.registerPrimary(new NumberOfTypes())
        registrar.registerPrimary(new LinesOfCode())
        registrar.registerPrimary(new LogicalLinesOfCode())
        registrar.registerPrimary(new SourceLinesOfCode())
        registrar.registerPrimary(new NumberOfPrivateAttributes())
        registrar.registerPrimary(new NumberOfProtectedAttributes())
        registrar.registerPrimary(new NumberOfPublicMethods())
        registrar.registerPrimary(new NumberOfAncestorClasses())
        registrar.registerPrimary(new NumberOfClasses())
        registrar.registerPrimary(new NumberOfInstanceVariables())
        registrar.registerPrimary(new NumberOfClassVariables())
        registrar.registerPrimary(new NumberOfLocalVariables())

        registrar.registerSecondary(new LinesOfCodePerClass()) // depends on LOC
        registrar.registerSecondary(new TotalNumberOfAttributes()) // depends on NOA
        registrar.registerSecondary(new TotalNumberOfClasses()) // depends on NC
        registrar.registerSecondary(new TotalNumberOfMethods()) // depends on NOM
        registrar.registerSecondary(new PolymorphicMethods()) // depends on NOC
    }

    MetricEvaluator getMetricEvaluator(String handle) {
        registrar.getEvaluator(handle)
    }
}
