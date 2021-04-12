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

import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Collector
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.impl.experiment.EmpiricalStudy
import edu.montana.gsoc.msusel.arc.impl.findbugs.FindBugsConstants
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.arc.impl.pmd.PMDConstants
import edu.montana.gsoc.msusel.arc.impl.qmood.QMoodConstants
import edu.montana.gsoc.msusel.arc.impl.quamoco.QuamocoConstants
import edu.montana.gsoc.msusel.arc.impl.td.TechDebtConstants

class ExperimentPhaseTwo extends EmpiricalStudy {

    ExperimentPhaseTwo(ArcContext context) {
        super("Experiment Phase Two", "A Test Empirical Study", context)
    }

    @Override
    void execute() {
        initWorkflow()
        initReport()
        executeStudy()
    }

    @Override
    void initWorkflow() {

    }

    @Override
    void initReport() {

    }

    void executeStudy() {
        getContext().addArcProperty("quamoco.models.dir", "config/quamoco/models")

        Command findbugs = getContext().getRegisteredCommand(FindBugsConstants.FB_CMD_NAME)
        Command pmd = getContext().getRegisteredCommand(PMDConstants.PMD_CMD_NAME)
        Command metrics = getContext().getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME)
        Command techdebt = getContext().getRegisteredCommand(TechDebtConstants.TD_CMD_NAME)
        Command qmood = getContext().getRegisteredCommand(QMoodConstants.QMOOD_CMD_NAME)
        Command quamoco = getContext().getRegisteredCommand(QuamocoConstants.QUAMOCO_CMD_NAME)
        Collector fbColl = getContext().getRegisteredCollector(FindBugsConstants.FB_COLL_NAME)
        Collector pmdColl = getContext().getRegisteredCollector(PMDConstants.PMD_COLL_NAME)

        System sys = null

        getContext().open()
        sys = System.findFirst("name = ?", "huston")
        getContext().close()

        getContext().open()
        getContext().setProject(sys.getProjects().get(0))
        getContext().close()

        // SpotBugs
        findbugs.execute(getContext())
        fbColl.execute(getContext())

        // PMD
        pmd.execute(getContext())
        pmdColl.execute(getContext())

        // Metrics
        metrics.execute(getContext())

        // TechDebt
        techdebt.execute(getContext())

        // QMood
        qmood.execute(getContext())

        // Quamoco
        quamoco.execute(getContext())
    }
}
