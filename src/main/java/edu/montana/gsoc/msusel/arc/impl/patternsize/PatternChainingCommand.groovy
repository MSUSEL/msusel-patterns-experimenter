package edu.montana.gsoc.msusel.arc.impl.patternsize

import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.PrimaryAnalysisCommand

class PatternChainingCommand extends PrimaryAnalysisCommand {

    PatternChainingCommand() {
        super(ArcPatternConstants.PATTERN_CHAIN_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {

    }
}
