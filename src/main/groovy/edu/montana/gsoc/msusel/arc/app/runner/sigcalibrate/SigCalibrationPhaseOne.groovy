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
package edu.montana.gsoc.msusel.arc.app.runner.sigcalibrate

import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.arc.impl.ghsearch.GitHubSearchConstants
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class SigCalibrationPhaseOne extends WorkFlow {

//    Command ghSearch

    SigCalibrationPhaseOne(ArcContext context) {
        super("Sig Calibration Phase One", "Sig Maintainability Model Calibration - Phase One", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
//        ghSearch  = context.getRegisteredCommand(GitHubSearchConstants.GHSEARCH_CMD_NAME)
    }

    void executeStudy() {
        context.open()
        results.rowKeySet().each {id ->
            println("ID: $id")
            def map = results.row(id)
            System sys = System.builder()
                    .name(map[SigCalibrateConstants.Project])
                    .key(map[SigCalibrateConstants.Project])
                    .basePath(map[SigCalibrateConstants.Location])
                    .create()
            Project proj = Project.builder()
                    .name("${map[SigCalibrateConstants.Project]}-${map[SigCalibrateConstants.Version]}")
                    .projKey("${map[SigCalibrateConstants.Project]}:${map[SigCalibrateConstants.Project]}-${map[SigCalibrateConstants.Version]}")
                    .relPath(map[SigCalibrateConstants.Version])
                    .version(map[SigCalibrateConstants.Version])
                    .create()
            sys.addProject(proj)
        }
        context.close()
    }

    void runTools() {

    }
}
