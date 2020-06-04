package edu.montana.gsoc.msusel.arc.impl.qmood


import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.provider.AbstractMetricProvider
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.MetricsRegistrar
import edu.montana.gsoc.msusel.metrics.impl.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class QMoodMetricProvider extends AbstractMetricProvider {

    MetricsRegistrar registrar

    QMoodMetricProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        registerMetrics()
    }

    @Override
    void updateDatabase() {
        registrar.getPrimaryEvaluators().each {
            it.toMetric(repository)
        }
    }

    @Override
    void initRepository() {
        repository = MetricRepository.findFirst("repoKey = ?", QMoodConstants.QMOOD_REPO_KEY)
        registrar = new MetricsRegistrar(repository)
    }

    void registerMetrics() {
        // basic metrics
        registrar.registerPrimary(new DesignSize())
        registrar.registerPrimary(new NumberOfHierarchies())
        registrar.registerPrimary(new AverageNumberOfAncestors())
        registrar.registerPrimary(new DataAccessMetric())
        registrar.registerPrimary(new DirectClassCoupling())
        registrar.registerPrimary(new CohesionAmongMethodsOfClass()) // need to finish
        registrar.registerPrimary(new MeasureOfAggregation()) // need to finish
        registrar.registerPrimary(new MeasureOfFunctionalAbstraction()) // need to finish
        registrar.registerPrimary(new NumberOfPolymorphicMethods())
        registrar.registerPrimary(new ClassInterfaceSize())
        registrar.registerPrimary(new NumberOfMethods())

        // quality metrics
        registrar.registerSecondary(new ReusabilityEvaluator())
        registrar.registerSecondary(new FlexibilityEvaluator())
        registrar.registerSecondary(new UnderstandabilityEvaluator())
        registrar.registerSecondary(new FunctionalityEvaluator())
        registrar.registerSecondary(new ExtendibilityEvaluator())
        registrar.registerSecondary(new EffectivenessEvaluator())
    }

    MetricEvaluator getMetricEvaluator(String handle) {
        registrar.getEvaluator(handle)
    }
}
