package edu.montana.gsoc.msusel.arc.impl.patterns

import edu.isu.isuese.datamodel.System
import edu.isu.isuese.datamodel.pattern.ChainIdentifier
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.PrimaryAnalysisCommand

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternChainingCommand extends PrimaryAnalysisCommand {

    PatternChainingCommand() {
        super(ArcPatternConstants.PATTERN_CHAIN_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        context.logger().atInfo().log("Starting Pattern Chain Identification")
        ChainIdentifier chainId = new ChainIdentifier()

        System sys = context.getProject().getParentSystem()
        chainId.findChains(sys)
        context.logger().atInfo().log("Pattern Chain Identification Complete")
    }
}
