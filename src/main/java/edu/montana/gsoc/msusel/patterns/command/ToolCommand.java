package edu.montana.gsoc.msusel.patterns.command;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
public abstract class ToolCommand implements Command {

    @Setter
    protected String toolName;
    @Setter
    protected String toolHome;
    @Setter
    protected String projectName;
    @Setter
    protected String reportFile;
    @Setter
    protected String sourceDirectory;
    @Setter
    protected String binaryDirectory;
    @Setter
    protected String projectBaseDirectory;

    public void execute() {
        if (isRequirementsMet()) {
            CommandLine cmdLine = buildCommandLine();
            executeCmdLine(cmdLine, getExpectedExitValue());
        }
    }

    public abstract boolean isRequirementsMet();

    protected abstract CommandLine buildCommandLine();

    protected abstract int getExpectedExitValue();

    private void executeCmdLine(CommandLine cmdLine, int expectedExitValue) {
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(projectBaseDirectory));
        executor.setExitValue(expectedExitValue);
        try {
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            int exitval = resultHandler.getExitValue();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
