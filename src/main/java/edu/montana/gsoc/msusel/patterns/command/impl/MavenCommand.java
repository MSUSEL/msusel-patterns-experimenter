package edu.montana.gsoc.msusel.patterns.command.impl;

import edu.montana.gsoc.msusel.patterns.command.ToolCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class MavenCommand extends ToolCommand {

    @Builder(buildMethodName = "create")
    public MavenCommand(String toolName, String toolHome, String projectName, String reportFile, String sourceDirectory, String binaryDirectory, String projectBaseDirectory) {
        super(toolName, toolHome, projectName, reportFile, sourceDirectory, binaryDirectory, projectBaseDirectory);
    }

    @Override
    public boolean isRequirementsMet() {
        Path pom = Paths.get(projectBaseDirectory, "pom.xml");
        return Files.exists(pom);
    }

    @Override
    public CommandLine buildCommandLine() {
        return new CommandLine("mvn")
                .addArgument("clean")
                .addArgument("compile")
                .addArgument("package")
                .addArgument("-Dmaven.test.skip=true");
    }

    @Override
    protected int getExpectedExitValue() {
        return 0;
    }

    @Override
    public String getName() {
        return "Maven";
    }
}
