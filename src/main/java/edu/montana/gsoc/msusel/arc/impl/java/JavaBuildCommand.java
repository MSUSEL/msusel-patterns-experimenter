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
package edu.montana.gsoc.msusel.arc.impl.java;

import edu.montana.gsoc.msusel.arc.command.ToolCommand;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

import java.io.File;

public class JavaBuildCommand extends ToolCommand {

    ToolCommand command;
    ToolCommand gradleCmd;
    ToolCommand mavenCmd;
    ToolCommand basicJavaCmd;

    @Builder(buildMethodName = "create")
    public JavaBuildCommand() {
        super(JavaConstants.JAVA_BUILD_CMD_NAME, null, null);

        gradleCmd = GradleCommand.builder().create();
        mavenCmd = MavenCommand.builder().create();
        basicJavaCmd = BasicJavaCommand.builder().create();
    }

    private void selectCommand() {
        String projDir = context.getProjectDirectory();
        File dir = new File(projDir);
        File gradle = new File(dir, "build.gradle");
        File maven = new File(dir, "pom.xml");
        if (gradle.exists()) {
            command = gradleCmd;
        } else if (maven.exists()) {
            command = mavenCmd;
        } else {
            command = basicJavaCmd;
        }
    }

    @Override
    public boolean isRequirementsMet() {
        return true;
    }

    @Override
    public CommandLine buildCommandLine() {
        selectCommand();

        command.setSourceDirectory(context.getProject().getModules().get(0).getSrcPath());
        command.setBinaryDirectory(context.getProject().getModules().get(0).getBinaryPath());
        command.setProjectBaseDirectory(context.getProjectDirectory());
        command.setProjectName(context.getProject().getName());

        return command.buildCommandLine();
    }

    @Override
    public void updateCollector() {
        command.updateCollector();
    }

    @Override
    public int getExpectedExitValue() {
        return command.getExpectedExitValue();
    }
}
