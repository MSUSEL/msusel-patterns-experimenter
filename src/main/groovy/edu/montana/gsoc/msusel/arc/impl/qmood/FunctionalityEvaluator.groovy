package edu.montana.gsoc.msusel.arc.impl.qmood

import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.annotations.MetricCategory
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition
import edu.montana.gsoc.msusel.metrics.annotations.MetricProperties
import edu.montana.gsoc.msusel.metrics.annotations.MetricScale
import edu.montana.gsoc.msusel.metrics.annotations.MetricScope
import edu.montana.gsoc.msusel.metrics.annotations.MetricType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "QMOOD Functionality",
        primaryHandle = "QMFUNC",
        description = "The responsibilities assigned to the classes of a design, which are made available by the classes through their public interfaces.",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = [
                'Bansiya, Jagdish, and Carl G. Davis. "A hierarchical model for object-oriented design quality assessment." IEEE Transactions on software engineering 28.1 (2002): 4-17.'
        ]
)
class FunctionalityEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        if (node instanceof Project) {
            Project proj = (Project) node
            List<Double> vals = Measure.getAllClassValues(proj, "${QMoodConstants.QMOOD_REPO_KEY}:QMFUNC")
            double value = 0
            if (proj.getAllTypes())
                value = vals.sum() / proj.getAllTypes().size()
            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMFUNC").on(node).withValue(value)
        } else if (node instanceof Type) {
            double cohesion = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "CAM", node)
            double polymorphism = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "NOP", node)
            double messaging = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "CIS", node)
            double designSize = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DSC", node)
            double hierarchies = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "NOH", node)

            def factors = [
                    [0.12, cohesion],
                    [0.22, polymorphism],
                    [0.22, messaging],
                    [0.22, designSize],
                    [0.22, hierarchies]
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMFUNC").on(node).withValue(value)
        }
    }
}
