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
package edu.montana.gsoc.msusel.arc;

import edu.isu.isuese.datamodel.System;
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow;
import edu.montana.gsoc.msusel.arc.impl.issues.findbugs.FindBugsConstants;
import edu.montana.gsoc.msusel.arc.impl.java.JavaConstants;
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants;
import edu.montana.gsoc.msusel.arc.impl.issues.pmd.PMDConstants;
import edu.montana.gsoc.msusel.arc.impl.quality.qmood.QMoodConstants;
import edu.montana.gsoc.msusel.arc.impl.quality.quamoco.QuamocoConstants;
import edu.montana.gsoc.msusel.arc.impl.quality.td.TechDebtConstants;
import groovy.util.ConfigObject;

class TestStudy extends WorkFlow {

    public TestStudy(ArcContext context) {
        super("Test", "A Test Empirical Study", context);
    }

    @Override
    public void initWorkflow(ConfigObject runnerConfig, int num) {

    }

    public void executeStudy() {
        getContext().addArcProperty("quamoco.models.dir", "config/quamoco/models");
        Command java = getContext().getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME);
        Command jdi = getContext().getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME);
        Command build = getContext().getRegisteredCommand(JavaConstants.JAVA_BUILD_CMD_NAME);
        Command findbugs = getContext().getRegisteredCommand(FindBugsConstants.FB_CMD_NAME);
        Command pmd = getContext().getRegisteredCommand(PMDConstants.PMD_CMD_NAME);
//        Command pattern4 = getContext().getRegisteredCommand(Pattern4Constants.PATTERN4_CMD_NAME);
//        Command coalesce = getContext().getRegisteredCommand(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME);
//        Command pSize = getContext().getRegisteredCommand(ArcPatternConstants.PATTERN_SIZE_CMD_NAME);
//        Command grime = getContext().getRegisteredCommand(GrimeConstants.GRIME_DETECT_CMD_NAME);
        Command metrics = getContext().getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME);
        Command techdebt = getContext().getRegisteredCommand(TechDebtConstants.CAST_CMD_NAME);
        Command qmood = getContext().getRegisteredCommand(QMoodConstants.QMOOD_CMD_NAME);
        Command quamoco = getContext().getRegisteredCommand(QuamocoConstants.QUAMOCO_CMD_NAME);

        Collector fbColl = getContext().getRegisteredCollector(FindBugsConstants.FB_COLL_NAME);
        Collector pmdColl = getContext().getRegisteredCollector(PMDConstants.PMD_COLL_NAME);

        System sys = null;

        getContext().open();
        sys = System.findFirst("name = ?", "huston");
        getContext().close();
//        for (Model sys : System.findAll()) {
            getContext().open();
            getContext().setProject(((System) sys).getProjects().get(0));
            getContext().close();

//            build.execute(getContext());
            java.execute(getContext());
//            jdi.execute(getContext());

            findbugs.execute(getContext());
            fbColl.execute(getContext());
            pmd.execute(getContext());
            pmdColl.execute(getContext());

//            pattern4.execute(getContext());
//            coalesce.execute(getContext());
//            pSize.execute(getContext());
//            grime.execute(getContext());

            metrics.execute(getContext());
            techdebt.execute(getContext());
//            qmood.execute(getContext());
            quamoco.execute(getContext());
//        }
    }
}
