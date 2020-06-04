package edu.montana.gsoc.msusel.arc.impl.patterns

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.PatternInstance
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.PrimaryAnalysisCommand
import edu.montana.gsoc.msusel.rbml.coalese.Coalescence

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternCoalescenceCommand extends PrimaryAnalysisCommand {

    PatternCoalescenceCommand() {
        super(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        context.logger().atInfo().log("Starting Pattern Coalescence")
        Coalescence coal = new Coalescence()
        List<PatternInstance> insts = Lists.newArrayList(context.getProject().getPatternInstances())

        coal.coalesce(insts)

        context.logger().atInfo().log("Patterns Coalesced")
    }
}
