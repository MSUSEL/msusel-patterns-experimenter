/*
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
package edu.montana.gsoc.msusel.arc.impl.pattern4;

import com.google.common.collect.ImmutableList;
import edu.montana.gsoc.msusel.arc.*;
import edu.montana.gsoc.msusel.arc.provider.RepoProvider;
import edu.montana.gsoc.msusel.arc.tool.PatternOnlyTool;
import lombok.Getter;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Pattern4Tool extends PatternOnlyTool {

    Pattern4Command command;
    Pattern4Collector collector;
    @Getter
    Pattern4PatternProvider provider;

    public Pattern4Tool(ArcContext context) {
        super(context);
        provider = new Pattern4PatternProvider(context);
    }

    @Override
    public List<Provider> getOtherProviders() {
        return ImmutableList.of(provider);
    }

    @Override
    public RepoProvider getRepoProvider() {
        return new Pattern4RepoProvider(context);
    }

    @Override
    public void init() {
        String resultsFile = context.getArcProperty(ArcProperties.TOOL_OUTPUT_DIR) + Pattern4Constants.REPORT_FILE_NAME;

        command = Pattern4Command.builder()
                .owner(this)
                .toolHome(context.getArcProperty(Pattern4Properties.P4_TOOL_HOME))
                .reportFile(resultsFile)
                .create();

        collector = new Pattern4Collector(this, resultsFile);

        context.registerCommand(command);
        context.registerCollector(collector);
    }
}
