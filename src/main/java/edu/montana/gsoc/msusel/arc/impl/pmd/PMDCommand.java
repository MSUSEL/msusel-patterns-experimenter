/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.arc.impl.pmd;

import edu.montana.gsoc.msusel.arc.command.ToolCommand;
import edu.montana.gsoc.msusel.arc.command.CommandUtils;
import lombok.Builder;
import org.apache.commons.exec.CommandLine;

public class PMDCommand extends ToolCommand {

    @Builder(buildMethodName = "create")
    public PMDCommand() {
        super("PMD");
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
