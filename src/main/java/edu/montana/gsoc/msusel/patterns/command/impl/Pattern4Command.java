package edu.montana.gsoc.msusel.patterns.command.impl;

import edu.montana.gsoc.msusel.patterns.command.ToolCommand;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

public class Pattern4Command extends ToolCommand {

    @Builder(buildMethodName = "create")
    public Pattern4Command(String projectName, String toolHome, String binDir, String reportFile) {
        super("Pattern4", toolHome, projectName, reportFile, "", binDir, "");
    }

    @Override
    public boolean isRequirementsMet() {
        return CommandUtils.verifyFileExists(toolHome, "pattern4.jar");
    }

    @Override
    public CommandLine buildCommandLine() {
        return new CommandLine("java")
            .addArgument("-Xms32m")
            .addArgument("-Xmx512m")
            .addArgument("-jar")
            .addArgument(CommandUtils.normalizePathString(toolHome) + "pattern4.jar")
            .addArgument("-target")
            .addArgument(binaryDirectory)
            .addArgument("-output")
            .addArgument(reportFile);
    }

    @Override
    protected int getExpectedExitValue() {
        return 0;
    }

    @Override
    public String getName() {
        return "Pattern4";
    }
}
