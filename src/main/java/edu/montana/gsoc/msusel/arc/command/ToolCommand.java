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

    public ToolCommand(String toolName, String toolHome, String projectName, String reportFile, String sourceDirectory,
                       String binaryDirectory, String projectBaseDirectory) {
        this.toolName = toolName;
        this.toolHome = toolHome;
        this.projectName = projectName;
        this.reportFile = reportFile;
        this.sourceDirectory = sourceDirectory;
        this.binaryDirectory = binaryDirectory;
        this.projectBaseDirectory = projectBaseDirectory;
    }

    public void execute(ArcContext context) {
        if (isRequirementsMet()) {
            CommandLine cmdLine = buildCommandLine();
            executeCmdLine(cmdLine, getExpectedExitValue());
            updateCollector();
        }
    }

    public abstract boolean isRequirementsMet();

    protected abstract CommandLine buildCommandLine();

    protected abstract void updateCollector();

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
