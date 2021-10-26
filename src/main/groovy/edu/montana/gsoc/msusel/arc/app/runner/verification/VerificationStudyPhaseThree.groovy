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
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Collector
import edu.montana.gsoc.msusel.arc.Command
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.arc.app.runner.experiment.ExperimentConstants
import edu.montana.gsoc.msusel.arc.impl.java.JavaConstants
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Constants
import groovy.util.logging.Log4j2

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class VerificationStudyPhaseThree  extends WorkFlow {

    Table<String, String, String> results
    Command java
    Command jdi
    Command build
    Command parser

    VerificationStudyPhaseThree(ArcContext context) {
        super("Verification Study Phase Three", "Phase Three", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
        java     = getContext().getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME)
        parser   = getContext().getRegisteredCommand(JavaConstants.JAVA_PARSE_CMD_NAME)
        jdi      = getContext().getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME)
        build    = getContext().getRegisteredCommand(JavaConstants.JAVA_BUILD_CMD_NAME)
    }

    void executeStudy() {
        ConfigSlurper slurper = new ConfigSlurper()

        results.rowKeySet().each {id ->
            ConfigObject config = createConfig(slurper, results.row(id))

            context.open()
            def vals = VerificationInjectorDirector.instance.inject(config)
            context.close()

            vals.each { col, value ->
                results.put(id, col, value)
            }

            context.open()
            context.project = Project.findFirst("projKey = ?", results.row(id).get(VerificationStudyConstants.INJECTED_KEY))
            runTools()
            context.close()
        }
    }

    void runTools() {
        java.execute(context)
        build.execute(context)
        java.execute(context)
        parser.execute(context)
        jdi.execute(context)
    }

    private ConfigObject createConfig(ConfigSlurper slurper, Map<String, String> map) {
        context.open()
        log.info("Looking up project with key: ${map[VerificationStudyConstants.BASE_KEY]}")
        Project proj = Project.findFirst("projKey = ?", map[VerificationStudyConstants.BASE_KEY])
        PatternInstance inst

        if (proj) inst = proj.getPatternInstances().first()

        if (proj && inst) {
            String confText = """
            where {
                systemKey = '${proj.getParentSystem().getKey()}'
                baseKey = '${proj.getProjectKey()}'
                injectedKey = '${map[VerificationStudyConstants.INJECTED_KEY]}'
                injectedLoc = '${map[VerificationStudyConstants.INJECTED_LOCATION]}'
                patternInst = '${inst.getInstKey()}'
            }
            control {
                fileName = '${map[VerificationStudyConstants.CONTROL_FILE]}'
            }
            """
            context.close()
            return slurper.parse(confText)
        }
        else {
            context.close()
            return null
        }
    }
}
