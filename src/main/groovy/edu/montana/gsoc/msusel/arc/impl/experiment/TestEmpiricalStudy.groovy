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
package edu.montana.gsoc.msusel.arc.impl.experiment

import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Collector
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.impl.findbugs.FindBugsConstants
import edu.montana.gsoc.msusel.arc.impl.ghsearch.GitHubSearchConstants
import edu.montana.gsoc.msusel.arc.impl.git.GitConstants
import edu.montana.gsoc.msusel.arc.impl.java.GradleConstants
import edu.montana.gsoc.msusel.arc.impl.grime.GrimeConstants
import edu.montana.gsoc.msusel.arc.impl.java.JavaConstants
import edu.montana.gsoc.msusel.arc.impl.java.MavenConstants
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Constants
import edu.montana.gsoc.msusel.arc.impl.patterns.ArcPatternConstants
import edu.montana.gsoc.msusel.arc.impl.pmd.PMDConstants
import edu.montana.gsoc.msusel.arc.impl.qmood.QMoodConstants
import edu.montana.gsoc.msusel.arc.impl.quamoco.QuamocoConstants
import edu.montana.gsoc.msusel.arc.impl.reporting.CSVReportWriter
import edu.montana.gsoc.msusel.arc.impl.reporting.Report
import edu.montana.gsoc.msusel.arc.impl.td.TechDebtConstants

class TestEmpiricalStudy extends EmpiricalStudy {

    TestEmpiricalStudy(ArcContext context) {
        super("Test", "A Test Empirical Study", context)
    }

    @Override
    void initWorkflow() {
//        workflow = Workflow.builder()
//                .context(context)
//                .name("Test Workflow")
//                .phase(Phase.builder()
//                        .name("Project Discovery")
//                        .context(context)
//                        .command(GitHubSearchConstants.GHSEARCH_CMD_NAME)
//                        .command(GitConstants.GIT_CMD_NAME)
//                        .command(JavaConstants.JAVA_TOOL_CMD_NAME)
//                        .command(GradleConstants.GRADLE_CMD_NAME)
//                        .create())
//                .phase(Phase.builder()
//                        .name("Primary Analyses")
//                        .context(context)
//                        .command(FindBugsConstants.FB_CMD_NAME)
//                        .command(PMDConstants.PMD_CMD_NAME)
//                        .command(Pattern4Constants.PATTERN4_CMD_NAME)
//                        .command(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME)
//                        .command(ArcPatternConstants.PATTERN_SIZE_CMD_NAME)
//                        .command(GrimeConstants.GRIME_DETECT_CMD_NAME)
//                        .command(MetricsConstants.METRICS_CMD_NAME)
//                        .create())
//                .phase(Phase.builder()
//                        .name("Quality Analyses")
//                        .context(context)
//                        .command(TechDebtConstants.TD_CMD_NAME)
//                        .command(QMoodConstants.QMOOD_CMD_NAME)
//                        .command(QuamocoConstants.QUAMOCO_CMD_NAME)
//                        .create())
//                .create()
    }


    @Override
    void initReport() {
        report = new Report().writeWith(new CSVReportWriter())
//        report = Report.on()
//                .column()
//                .column()
//                .column()
//                .measures()
//                .where()
//                .where()
//                .writeWith(new CSVReportWriter())
    }

    @Override
    void execute() {
        context.logger().atInfo().log("Running Empirical Study: $name")
        context.logger().atInfo().log("Initializing study workflow")
        initWorkflow()

        context.logger().atInfo().log("Initializing study report")
        initReport()

        context.logger().atInfo().log("Starting empirical study workflow")
        executeStudy()
        context.logger().atInfo().log("Empirical study workflow complete")

        context.logger().atInfo().log("Generating report for Empirical Study: $name")
        //report.generate()
        context.logger().atInfo().log("Report generated in ${report.getReportFileName()}")
    }

    void executeStudy() {
        Command ghSearch  = context.getRegisteredCommand(GitHubSearchConstants.GHSEARCH_CMD_NAME)
        Command git       = context.getRegisteredCommand(GitConstants.GIT_CMD_NAME)
        Command java      = context.getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME)
        Command jdi       = context.getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME)
        Command build     = context.getRegisteredCommand(JavaConstants.JAVA_BUILD_CMD_NAME)
        Command findbugs  = context.getRegisteredCommand(FindBugsConstants.FB_CMD_NAME)
        Command pmd       = context.getRegisteredCommand(PMDConstants.PMD_CMD_NAME)
        Command pattern4  = context.getRegisteredCommand(Pattern4Constants.PATTERN4_CMD_NAME)
        Command coalesce  = context.getRegisteredCommand(ArcPatternConstants.PATTERN_COALESCE_CMD_NAME)
        Command pSize     = context.getRegisteredCommand(ArcPatternConstants.PATTERN_SIZE_CMD_NAME)
        Command grime     = context.getRegisteredCommand(GrimeConstants.GRIME_DETECT_CMD_NAME)
        Command metrics   = context.getRegisteredCommand(MetricsConstants.METRICS_CMD_NAME)
        Command techdebt  = context.getRegisteredCommand(TechDebtConstants.TD_CMD_NAME)
        Command qmood     = context.getRegisteredCommand(QMoodConstants.QMOOD_CMD_NAME)
        Command quamoco   = context.getRegisteredCommand(QuamocoConstants.QUAMOCO_CMD_NAME)

        Collector fbColl  = context.getRegisteredCollector(FindBugsConstants.FB_COLL_NAME)
        Collector pmdColl = context.getRegisteredCollector(PMDConstants.PMD_COLL_NAME)

        ghSearch.execute(context)
        System.findAll().each { sys ->
            context.project = (sys as System).getProjects().first()
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.GIT))) git.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.BUILD))) build.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.JAI))) java.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.JDI))) jdi.execute(context)

            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.FB))) findbugs.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.FB))) fbColl.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PMD))) pmd.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PMD))) pmdColl.execute(context)

            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.P4))) pattern4.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PC))) coalesce.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.PS))) pSize.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.GRIME))) grime.execute(context)

            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.METRICS))) metrics.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.TD))) techdebt.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.QMOOD))) qmood.execute(context)
            if (Boolean.parseBoolean(context.getArcProperty(TestStudyProperties.QUAMOCO))) quamoco.execute(context)
        }
    }
}
