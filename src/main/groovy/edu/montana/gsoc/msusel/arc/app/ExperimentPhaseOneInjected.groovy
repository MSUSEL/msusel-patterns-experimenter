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

import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Collector
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.impl.experiment.EmpiricalStudy
import edu.montana.gsoc.msusel.arc.impl.java.JavaConstants
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Constants
import edu.montana.gsoc.msusel.arc.impl.patterns.ArcPatternConstants

class ExperimentPhaseOneInjected extends EmpiricalStudy {

    Table<String, String, String> results
    Command java
    Command jdi
    Command build
    Command pattern4
    Command coalesce
    Command pSize
    Collector p4Coll

    ExperimentPhaseOneInjected(ArcContext context) {
        super("Experiment Phase One Injected", "A Test Empirical Study", context)
    }

    @Override
    void execute() {
        initWorkflow()
        initReport()
        executeStudy()
    }

    @Override
    void initWorkflow() {
        java     = getContext().getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME)
        jdi      = getContext().getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME)
        build    = getContext().getRegisteredCommand(JavaConstants.JAVA_BUILD_CMD_NAME)
        pattern4 = getContext().getRegisteredCommand(Pattern4Constants.PATTERN4_CMD_NAME)
        p4Coll   = getContext().getRegisteredCollector(Pattern4Constants.PATTERN4_COLL_NAME);
        coalesce = getContext().getRegisteredCommand(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME)
        pSize    = getContext().getRegisteredCommand(ArcPatternConstants.PATTERN_SIZE_CMD_NAME)
    }

    @Override
    void initReport() {}

    void executeStudy() {
        results.rowKeySet().each {id ->
            String projKey = results.get(id, Constants.Key2)
            getContext().open()
            getContext().setProject(Project.findFirst("projKey = ?", projKey))
            getContext().close()

            // Java
            java.execute(getContext())
            build.execute(getContext())
            java.execute(getContext())
            jdi.execute(getContext())

            // Pattern 4
//            pattern4.execute(getContext())
//            p4Coll.execute(getContext())

            // Patterns
//            coalesce.execute(getContext())
//            pSize.execute(getContext())
        }
    }
}
