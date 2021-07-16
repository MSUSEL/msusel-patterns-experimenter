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
package edu.montana.gsoc.msusel.arc.app.runner.sigrating

import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.arc.app.runner.sigcalibrate.SigCalibrateConstants
import edu.montana.gsoc.msusel.arc.impl.quality.sigmain.SigMainConstants
import edu.montana.gsoc.msusel.arc.impl.quality.td.TechDebtConstants

class SigRatingPhaseTwo extends WorkFlow {

    Command sigrate
    Command nugrohoTD

    SigRatingPhaseTwo(ArcContext context) {
        super("Sig Rating Phase Two", "Sig Maintainability Model Rating Test - Phase Two", context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        sigrate = context.getRegisteredCommand(SigMainConstants.SIGRATE_CMD_NAME)
        nugrohoTD = context.getRegisteredCommand(TechDebtConstants.NUGROHO_CMD_NAME)
    }

    @Override
    void executeStudy() {
        context.open()
        List<Project> projects = []
        results.rowKeySet().each { row ->
            projects.add(Project.findFirst("projKey = ?", results.get(row, SigCalibrateConstants.KEY)))
        }
        context.close()

        projects.each {
            context.project = it
            runTools()
        }
    }

    void runTools() {
        sigrate.execute(context)
        nugrohoTD.execute(context)
    }
}
