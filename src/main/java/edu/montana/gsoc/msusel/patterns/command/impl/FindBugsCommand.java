package edu.montana.gsoc.msusel.patterns.command.impl;

import edu.montana.gsoc.msusel.patterns.command.ToolCommand;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

public class FindBugsCommand extends ToolCommand {

    @Builder(buildMethodName = "create")
    public FindBugsCommand(String toolHome, String projectName, String reportFile, String binDir) {
        super("FindBugs", toolHome, projectName, reportFile, "", binDir, "");
    }

    @Override
    public boolean isRequirementsMet() {
        return CommandUtils.verifyFileExists(toolHome, "lib/findbugs.jar");
    }

    @Override
    public CommandLine buildCommandLine() {
        return new CommandLine("java")
            .addArgument("-jar")
            .addArgument(toolHome + "lib/findbugs.jar")
            .addArgument("-projectName")
            .addArgument(projectName)
            .addArgument("-low")
            .addArgument("-output")
            .addArgument(reportFile)
            .addArgument(binaryDirectory);
    }

    @Override
    protected int getExpectedExitValue() {
        return 0;
    }

    @Override
    public String getName() {
        return "FindBugs";
    }
}
