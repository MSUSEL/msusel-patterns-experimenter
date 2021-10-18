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
package edu.montana.gsoc.msusel.arc.app.runner.pattern4test

import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.arc.impl.issues.grime.GrimeConstants

class PatternsTestPhaseFive extends WorkFlow {

    private static final String STUDY_NAME = "Patterns Test - Phase 5"
    private static final String STUDY_DESC = "Grime Detection"

    Command grimeDetector

    PatternsTestPhaseFive(ArcContext context) {
        super(STUDY_NAME, STUDY_DESC, context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        grimeDetector = context.getRegisteredCommand(GrimeConstants.GRIME_DETECT_CMD_NAME)
    }

    @Override
    void executeStudy() {
        context.open()
        List<Project> projects = []
        results.rowKeySet().each {row ->
            projects.add(Project.findFirst("projKey = ?", results.get(row, PatternsTestConstants.KEY)))
        }
        context.close()

        projects.each { project ->
            context.project = project
            runTools()
        }
    }

    void runTools() {
        grimeDetector.execute(context)
    }
}
