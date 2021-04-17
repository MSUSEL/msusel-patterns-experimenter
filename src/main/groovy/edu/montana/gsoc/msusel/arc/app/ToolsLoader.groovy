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
package edu.montana.gsoc.msusel.arc.app

import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Tool
import edu.montana.gsoc.msusel.arc.impl.findbugs.FindBugsTool
import edu.montana.gsoc.msusel.arc.impl.ghsearch.GitHubSearchTool
import edu.montana.gsoc.msusel.arc.impl.git.GitTool
import edu.montana.gsoc.msusel.arc.impl.grime.GrimeTool
import edu.montana.gsoc.msusel.arc.impl.injector.SoftwareInjectorTool
import edu.montana.gsoc.msusel.arc.impl.java.JavaTool
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsTool
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Tool
import edu.montana.gsoc.msusel.arc.impl.patterngen.PatternGeneratorTool
import edu.montana.gsoc.msusel.arc.impl.patterns.ArcPatternTool
import edu.montana.gsoc.msusel.arc.impl.pmd.PMDTool
import edu.montana.gsoc.msusel.arc.impl.qmood.QMoodTool
import edu.montana.gsoc.msusel.arc.impl.quamoco.QuamocoTool
import edu.montana.gsoc.msusel.arc.impl.td.TechDebtTool

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ToolsLoader {

    void loadTools(ArcContext context) {
        context.logger().atInfo().log("Instantiating tools")

        Tool[] tools = [
                new FindBugsTool(context),
                new GitHubSearchTool(context),
                new GitTool(context),
                new GrimeTool(context),
                new JavaTool(context),
                new MetricsTool(context),
                new Pattern4Tool(context),
                new PMDTool(context),
                new QuamocoTool(context),
                new SoftwareInjectorTool(context),
                new TechDebtTool(context),
                new QMoodTool(context),
                new ArcPatternTool(context),
                new PatternGeneratorTool(context)
        ]

        context.logger().atInfo().log("Tools instantiated now loading repos and initializing commands")
        tools.each {
            it.getRepoProvider().load()
            it.getOtherProviders()*.load()
            it.init()
        }
        context.logger().atInfo().log("Finished loading tools")
    }
}