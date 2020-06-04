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
        name = "QMOOD Reusability",
        primaryHandle = "QMREUSE",
        description = "Reflects the presence of object-oriented design characteristics that allow a design to be reapplied to a new problem without significant effort",
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
class ReusabilityEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        if (node instanceof Project) {
            Project proj = (Project) node
            List<Double> vals = Measure.getAllClassValues(proj, "${QMoodConstants.QMOOD_REPO_KEY}:QMREUSE")
            double value = 0
            if (proj.getAllTypes())
                value = vals.sum() / proj.getAllTypes().size()
            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMREUSE").on(node).withValue(value)
        }
        else if (node instanceof Type) {
            double coupling   = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DCC", node)
            double cohesion   = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "CAM", node)
            double messaging  = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "CIS", node)
            double designSize = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DSC", node)

            def factors = [
                    [-0.25, coupling],
                    [0.25, cohesion],
                    [0.50, messaging],
                    [0.50, designSize],
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMREUSE").on(node).withValue(value)
        }
    }
}
