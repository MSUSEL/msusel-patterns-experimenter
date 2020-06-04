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
        name = "QMOOD Effectiveness",
        primaryHandle = "QMEFFECT",
        description = "This refers to a design's ability to achieve the desired functionality and behavior using object-oriented design concepts and techniques.",
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
class EffectivenessEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        if (node instanceof Project) {
            Project proj = (Project) node
            List<Double> vals = Measure.getAllClassValues(proj, "${QMoodConstants.QMOOD_REPO_KEY}:QMEFFECT")
            double value = 0
            if (proj.getAllTypes())
                value = vals.sum() / proj.getAllTypes().size()
            Measure.of("${QMoodConstants.QMOOD_REPO_KEY}:QMEFFECT").on(node).withValue(value)
        } else if (node instanceof Type) {
            double abstraction = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "ANA", node)
            double encapsulation = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "DAM", node)
            double composition = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "MOA", node)
            double inheritance = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "MFA", node)
            double polymorphism = Measure.valueFor(QMoodConstants.QMOOD_REPO_KEY, "NOP", node)

            def factors = [
                    [0.20, abstraction],
                    [0.20, encapsulation],
                    [0.20, composition],
                    [0.20, inheritance],
                    [0.20, polymorphism]
            ]

            double value = 0
            factors.each {
                value += it[0] * it[1]
            }

            Measure.of("${repo.getRepoKey()}:QMEFFECT").on(node).withValue(value)
        }
    }
}
