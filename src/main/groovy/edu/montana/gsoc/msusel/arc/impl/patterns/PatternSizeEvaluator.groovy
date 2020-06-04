package edu.montana.gsoc.msusel.arc.impl.patterns;

import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.RoleType
import edu.isu.isuese.datamodel.Type;
import edu.montana.gsoc.msusel.metrics.MetricEvaluator
import edu.montana.gsoc.msusel.metrics.annotations.MetricCategory
import edu.montana.gsoc.msusel.metrics.annotations.MetricDefinition
import edu.montana.gsoc.msusel.metrics.annotations.MetricProperties
import edu.montana.gsoc.msusel.metrics.annotations.MetricScale
import edu.montana.gsoc.msusel.metrics.annotations.MetricScope
import edu.montana.gsoc.msusel.metrics.annotations.MetricType;

/**
 * Pattern Size
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "Pattern Size",
        primaryHandle = "PS",
        description = "",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PATTERN,
                type = MetricType.Model,
                scale = MetricScale.Ordinal,
                category = MetricCategory.Size
        ),
        references = [
                'Griffith, I. "Design Pattern Decay -- A Study of Design Pattern Grime and Its Impact on Quality and Technical Debt." Doctoral Dissertation.'
        ]
)
class PatternSizeEvaluator extends MetricEvaluator {

    @Override
    def measure(Measurable node) {
        int size = 0

        if (node instanceof PatternInstance) {
            PatternInstance inst = (PatternInstance) node
            inst.getRoleBindings().each {
                if (it.getRole().getType() == RoleType.CLASSIFIER) {
                    Type type = (Type) it.getReference().getReferencedComponent(inst.getParentProject())
                    size += type.getAllMethods().size() + type.getFields().size()
                }
            }

            Measure.of(null).on(inst).withValue(size)
        }

        size
    }
}
