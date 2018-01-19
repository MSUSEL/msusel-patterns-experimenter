package edu.montana.gsoc.msusel.patterns.command.impl;

import edu.montana.gsoc.msusel.patterns.command.BuildCommand;
import edu.montana.gsoc.msusel.patterns.command.ToolCommand;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GradleCommand extends ToolCommand {

    public GradleCommand(String toolName, String toolHome, String projectName, String reportFile, String sourceDirectory, String binaryDirectory, String projectBaseDirectory) {
        super(toolName, toolHome, projectName, reportFile, sourceDirectory, binaryDirectory, projectBaseDirectory);
    }

    @Override
    public boolean isRequirementsMet() {
        Path bg = Paths.get(projectBaseDirectory, "build.gradle");
        Path gw = Paths.get(projectBaseDirectory, "gradlew");
        return Files.exists(bg) && Files.exists(gw);
    }

    @Override
    public CommandLine buildCommandLine() {
        CommandLine cmdLine = new CommandLine("./gradlew")
                .addArgument("clean")
                .addArgument("compile")
                .addArgument("package");

        return cmdLine;
    }

    @Override
    protected int getExpectedExitValue() {
        return 0;
    }

    @Override
    public String getName() {
        return "Gradle";
    }
}
