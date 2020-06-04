package edu.montana.gsoc.msusel.arc.impl.patternsize;

import edu.isu.isuese.datamodel.Project;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PatternSizeCommand extends SecondaryAnalysisCommand {

    public PatternSizeCommand() {
        super(ArcPatternConstants.PATTERN_SIZE_CMD_NAME);
    }

    @Override
    public void execute(ArcContext context) {
        Project project = context.getProject();
        PatternSizeEvaluator eval = new PatternSizeEvaluator();

        project.getPatternInstances().forEach(eval::measure);
    }
}
