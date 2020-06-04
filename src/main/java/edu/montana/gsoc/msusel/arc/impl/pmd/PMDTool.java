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
package edu.montana.gsoc.msusel.arc.impl.pmd;

import com.google.common.collect.ImmutableList;
import edu.montana.gsoc.msusel.arc.*;
import edu.montana.gsoc.msusel.arc.command.CommandUtils;
import edu.montana.gsoc.msusel.arc.provider.RepoProvider;
import edu.montana.gsoc.msusel.arc.tool.RuleOnlyTool;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PMDTool extends RuleOnlyTool {

    PMDCommand command;
    PMDCollector collector;

    public PMDTool(ArcContext context) {
        super(context);
    }

    @Override
    public RepoProvider getRepoProvider() {
        return new PMDRepoProvider();
    }

    @Override
    public List<Provider> getOtherProviders() {
        return ImmutableList.of(new PMDRuleProvider(context));
    }

    @Override
    public void init() {
        String resultsFile = CommandUtils.normalizePathString(context.getArcProperty(ArcProperties.TOOL_OUTPUT_DIR)) +
                PMDConstants.REPORT_FILE_NAME;

        command = PMDCommand.builder()
                .toolHome(context.getArcProperty(PMDProperties.PMD_TOOL_HOME))
                .projectName(context.getProject().getName())
                .reportFile(resultsFile)
                .sourceDirectory(context.getProject().getModules().get(0).getSrcPath())
                .binaryDirectory(context.getProject().getModules().get(0).getBinaryPath())
                .projectBaseDirectory(context.getProjectDirectory())
                .create();

        collector = PMDCollector.builder()
                .owner(this)
                .project(context.getProject())
                .resultsFile(resultsFile)
                .create();

        context.registerCommand(command);
        context.registerCollector(collector);
    }
}
