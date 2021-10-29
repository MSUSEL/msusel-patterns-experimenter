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

import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.util.MeasureTable
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.arc.impl.quality.sigmain.SigMainConstants
import edu.montana.gsoc.msusel.arc.impl.quality.td.TechDebtConstants

class VerificationStudyPhaseFour extends WorkFlow {

    private static final String STUDY_NAME = "Verification Study Phase Four"
    private static final String STUDY_DESC = "Phase Four"

    Table<String, String, String> results
    Command metrics
    Command nugrohoTD
    Command sigmain
    Command sigrating

    VerificationStudyPhaseFour(ArcContext context) {
        super(STUDY_NAME, STUDY_DESC, context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        metrics   = getContext().getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME)
        nugrohoTD = getContext().getRegisteredCommand(TechDebtConstants.NUGROHO_CMD_NAME)
        sigmain   = getContext().getRegisteredCommand(SigMainConstants.SIGMAIN_CMD_NAME)
        sigrating = getContext().getRegisteredCommand(SigMainConstants.SIGRATE_CMD_NAME)
    }

    @Override
    void executeStudy() {
        results.rowKeySet().each {id ->
            runTools(results.row(id).get(VerificationStudyConstants.BASE_KEY))
            runTools(results.row(id).get(VerificationStudyConstants.INFECTED_KEY))
            runTools(results.row(id).get(VerificationStudyConstants.INJECTED_KEY))
        }
    }

    void runTools(String projKey) {
        MeasureTable.getInstance().reset()
        context.open()
        context.setProject(Project.findFirst("projKey = ?", projKey))
        context.close()

        metrics.execute(context)
        sigmain.execute(context)
        sigrating.execute(context)
        nugrohoTD.execute(context)
    }
}
