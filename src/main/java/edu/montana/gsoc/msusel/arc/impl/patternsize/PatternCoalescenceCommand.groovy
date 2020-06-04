package edu.montana.gsoc.msusel.arc.impl.patternsize

import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.PrimaryAnalysisCommand

class PatternCoalescenceCommand extends PrimaryAnalysisCommand {

    PatternCoalescenceCommand() {
        super(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {

    }
}
