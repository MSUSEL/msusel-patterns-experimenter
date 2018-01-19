package edu.montana.gsoc.msusel.patterns.command.impl;

import edu.montana.gsoc.msusel.patterns.command.ToolCommand;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.exec.CommandLine;

public class PMDCommand extends ToolCommand {

    @Builder(buildMethodName = "create")
    public PMDCommand(String toolHome, String projectName, String reportFile, String binDir) {
        super("PMD", toolHome, projectName, reportFile, "", binDir, "");
    }

    @Override
    public boolean isRequirementsMet() {
        return CommandUtils.verifyFileExists(toolHome, "bin/run.sh");
    }

    @Override
    public CommandLine buildCommandLine() {
        return new CommandLine(CommandUtils.normalizePathString(toolHome) + "bin/run.sh")
            .addArgument("pmd")
            .addArgument("-d")
            .addArgument(sourceDirectory)
            .addArgument("-f")
            .addArgument("xml")
            .addArgument("-r")
            .addArgument(reportFile)
            .addArgument("-R")
            .addArgument("category/java/bestpractices.xml,category/java/codestyle.xml,category/java/design.xml,category/java/documentation.xml,category/java/errorprone.xml,category/java/multithreading.xml,category/java/performance.xml")
            .addArgument("-version")
            .addArgument("1.8")
            .addArgument("-language")
            .addArgument("java");
    }

    @Override
    protected int getExpectedExitValue() {
        return 0;
    }

    @Override
    public String getName() {
        return "PMD";
    }
}
