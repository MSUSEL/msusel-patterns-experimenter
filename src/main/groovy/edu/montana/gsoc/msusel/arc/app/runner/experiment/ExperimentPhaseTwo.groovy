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
package edu.montana.gsoc.msusel.arc.app.runner.experiment

import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.util.MeasureTable
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Collector
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.arc.impl.issues.findbugs.FindBugsConstants
import edu.montana.gsoc.msusel.arc.impl.issues.pmd.PMDConstants
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.arc.impl.quality.qmood.QMoodConstants
import edu.montana.gsoc.msusel.arc.impl.quality.quamoco.QuamocoConstants
import edu.montana.gsoc.msusel.arc.impl.quality.sigmain.SigMainConstants
import edu.montana.gsoc.msusel.arc.impl.quality.td.TechDebtConstants

class ExperimentPhaseTwo extends WorkFlow {

    Table<String, String, String> results
//    Command findbugs
//    Command pmd
    Command metrics
//    Command qmood
//    Command quamoco
//    Command castTD
    Command nugrohoTD
    Command sigmain
    Command sigrating
//    Collector fbColl
//    Collector pmdColl

    ExperimentPhaseTwo(ArcContext context) {
        super("Experiment Phase Two", "A Test Empirical Study", context)
    }

    @Override
    void initWorkflow(ConfigObject runnerConfig, int num) {
        getContext().addArcProperty("quamoco.models.dir", "config/quamoco/models")

//        findbugs  = getContext().getRegisteredCommand(FindBugsConstants.FB_CMD_NAME)
//        pmd       = getContext().getRegisteredCommand(PMDConstants.PMD_CMD_NAME)
        metrics   = getContext().getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME)
//        castTD    = getContext().getRegisteredCommand(TechDebtConstants.CAST_CMD_NAME)
        nugrohoTD = getContext().getRegisteredCommand(TechDebtConstants.NUGROHO_CMD_NAME)
        sigmain   = getContext().getRegisteredCommand(SigMainConstants.SIGMAIN_CMD_NAME)
        sigrating = getContext().getRegisteredCommand(SigMainConstants.SIGRATE_CMD_NAME)
//        qmood     = getContext().getRegisteredCommand(QMoodConstants.QMOOD_CMD_NAME)
//        quamoco   = getContext().getRegisteredCommand(QuamocoConstants.QUAMOCO_CMD_NAME)
//        fbColl    = getContext().getRegisteredCollector(FindBugsConstants.FB_COLL_NAME)
//        pmdColl   = getContext().getRegisteredCollector(PMDConstants.PMD_COLL_NAME)
    }

    void executeStudy() {
        results.rowKeySet().each { id ->
            runTools(results.get(id, ExperimentConstants.Key1))
            runTools(results.get(id, ExperimentConstants.Key2))
        }
    }

    void runTools(String projKey) {
        MeasureTable.getInstance().reset()
        getContext().open()
        getContext().setProject(Project.findFirst("projKey = ?", projKey))
        getContext().close()

        // SpotBugs
//        findbugs.execute(getContext())
//        fbColl.execute(getContext())

        // PMD
//        pmd.execute(getContext())
//        pmdColl.execute(getContext())

        // Metrics
        metrics.execute(getContext())

        // Cast TechDebt
//        castTD.execute(getContext())

        // QMood
//        qmood.execute(getContext())

        // Quamoco
//        quamoco.execute(getContext())

        // Sig Maintainability
        sigmain.execute(getContext())
        sigrating.execute(getContext())

        // Nugroho TechDebt
        nugrohoTD.execute(getContext())
    }
}
