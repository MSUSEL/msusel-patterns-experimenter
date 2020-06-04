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
        name = "QMOOD Flexibility",
        primaryHandle = "QMFLEX",
        description = "Characteristics that allow the incorporation of changes in a design. The ability of a design to be adapted to provide functionally related capabilities",
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
class FlexibilityEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        if (node instanceof Project) {
            Project proj = (Project) node
            List<Double> vals = Measure.getAllClassValues(proj, "${QMoodConstants.QMOOD_REPO_KEY}:QMFLEX")
            double value = 0
            if (proj.getAllTypes())
                value = vals.sum() / proj.getAllTypes().size()
            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMFLEX").on(node).withValue(value)
        }
        else if (node instanceof Type) {
            double encapsulation = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DAM", node)
            double coupling      = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DCC", node)
            double composition   = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "MOA", node)
            double polymorphism  = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "NOP", node)

            def factors = [
                    [0.25, encapsulation],
                    [-0.25, coupling],
                    [0.50, composition],
                    [0.50, polymorphism],
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMFLEX").on(node).withValue(value)
        }
    }
}
