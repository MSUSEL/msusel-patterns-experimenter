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
package edu.montana.gsoc.msusel.arc.app.runner.verification

import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Collector
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.arc.impl.java.JavaConstants
import edu.montana.gsoc.msusel.arc.impl.patextract.PatternExtractorConstants
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Constants

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
class VerificationStudyPhaseTwo extends WorkFlow {

    Command java
    Command parser
    Command jdi
    Command build
    Command pattern4
    Collector p4coll

    VerificationStudyPhaseTwo(ArcContext context) {
        super("Verification Study Phase Two", "Phase Two", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
        java = context.getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME)
        parser = context.getRegisteredCommand(JavaConstants.JAVA_PARSE_CMD_NAME)
        jdi = context.getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME)
        build    = getContext().getRegisteredCommand(JavaConstants.JAVA_BUILD_CMD_NAME)
        pattern4 = getContext().getRegisteredCommand(Pattern4Constants.PATTERN4_CMD_NAME)
        p4coll = context.getRegisteredCollector(Pattern4Constants.PATTERN4_COLL_NAME)
    }

    void executeStudy() {
        context.open()
        List<Project> projects = []
        results.rowKeySet().each { row ->
            projects.add(Project.findFirst("projKey = ?", results.get(row, VerificationStudyConstants.BASE_KEY)))
            projects.add(Project.findFirst("projKey = ?", results.get(row, VerificationStudyConstants.BASE_KEY)))
        }
        context.close()

        projects.each { project ->
            context.project = project
            runTools()
        }
    }

    void runTools() {
        java.execute(context)
        build.execute(context)
        java.execute(context)
        parser.execute(context)
        jdi.execute(context)
        pattern4.execute(context)
        p4coll.execute(context)
    }
}
