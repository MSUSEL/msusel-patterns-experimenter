package edu.montana.gsoc.msusel.arc.impl.qmood

import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.annotations.*

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "QMOOD Understandability",
        primaryHandle = "QMUNDER",
        description = "The properties of the design that enable it to be easily learned and comprehended. This directly relates to the complexity of the design structure.",
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
class UnderstandabilityEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        if (node instanceof Project) {
            Project proj = (Project) node
            List<Double> vals = Measure.getAllClassValues(proj, "${QMoodConstants.QMOOD_REPO_KEY}:QMUNDER")
            double value = 0
            if (proj.getAllTypes())
                value = vals.sum() / proj.getAllTypes().size()
            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMUNDER").on(node).withValue(value)
        }
        else if (node instanceof Type) {
            double abstraction   = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "ANA", node)
            double encapsulation = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DAM", node)
            double coupling      = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DCC", node)
            double cohesion      = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "CAM", node)
            double polymorphism  = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "NOP", node)
            double complexity    = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "NOM", node)
            double designSize    = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DSC", node)

            def factors = [
                    [-0.33, abstraction],
                    [0.33, encapsulation],
                    [-0.33, coupling],
                    [0.33, cohesion],
                    [-0.33, polymorphism],
                    [-0.33, complexity],
                    [-0.33, designSize]
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMUNDER").on(node).withValue(value)
        }
    }
}
