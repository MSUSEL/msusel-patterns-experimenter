/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.montana.gsoc.msusel.arc.command;

import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.Command;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import java.io.File;
import java.io.IOException;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class ToolCommand implements Command {

    @Setter @Getter
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
    protected ArcContext context;

    public ToolCommand(String toolName, String toolHome, String reportFile) {
        this.toolName = toolName;
        this.toolHome = toolHome;
        this.reportFile = reportFile;
    }

    public void execute(ArcContext context) {
        this.context = context;
        context.logger().atInfo().log("Executing " + getToolName() + " Analysis");

        this.sourceDirectory = context.getProject().getSrcPath();
        this.binaryDirectory = context.getProject().getBinaryPath();
        this.projectBaseDirectory = context.getProjectDirectory();
        this.projectName = context.getProject().getName();

        if (isRequirementsMet()) {
            context.logger().atInfo().log("Constructing command line");
            CommandLine cmdLine = buildCommandLine();
            context.logger().atInfo().log("Executing command");
            context.logger().atInfo().log("Command Line: " + cmdLine.toString());
            executeCmdLine(cmdLine, getExpectedExitValue());
            context.logger().atInfo().log("Updating collector");
            updateCollector();
        }

        context.logger().atInfo().log("Finished Executing " + getToolName() + " Analysis");
    }

    public abstract boolean isRequirementsMet();

    public abstract CommandLine buildCommandLine();

    public abstract void updateCollector();

    public abstract int getExpectedExitValue();

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
