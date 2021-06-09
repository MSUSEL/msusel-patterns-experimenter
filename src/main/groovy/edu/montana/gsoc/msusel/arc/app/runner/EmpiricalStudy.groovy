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
package edu.montana.gsoc.msusel.arc.app.runner

import com.google.common.collect.Table
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.ReportingLevel
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
abstract class EmpiricalStudy {

    StudyConfigReader configReader
    ArcContext context
    ConfigObject runnerConfig
    Table<String, String, String> results
    ResultsExtractor resEx
    ResultsWriter resWrite
    ResultsReader resReader
    int status
    int num
    long start
    long end
    long studyStart
    long studyEnd
    String name
    String description
    List<WorkFlow> phases

    EmpiricalStudy(String name, String description, ArcContext context, StudyConfigReader configReader) {
        this.name = name
        this.description = description
        this.context = context
        this.configReader = configReader
        resEx = new ResultsExtractor()
        resWrite = new ResultsWriter()
        resReader = new ResultsReader()
        status = 0
        num = 0
    }

    void run() {
        studyStart = System.currentTimeMillis()
        initialize()
        executeProcess()
        if (status < 7) extractResults()
        studyEnd = System.currentTimeMillis()

        log.info(TimePrinter.print(studyEnd - studyStart, "Study"))
    }

    def initialize() {
        this.runnerConfig = loadConfiguration()
        context.setLanguage("java")
        readStatus()
        if (status > 0)
            readResults()
        if (status <= 0)
            resetDatabase()
        loadTools()
    }

    def executeProcess() {
        if (status < 1) loadStudyConfig()
        for (int i = status - 1; i < phases.size(); i++) {
            executePhase(phases[i])
        }
    }

    void extractResults() {
        log.info("Collecting Experimental Results")
        start = System.currentTimeMillis()
        resEx.initialize(ReportingLevel.PROJECT, runnerConfig.measures, context)
        resEx.extractResults(results)
        log.info("Finished Collecting Experimental Results")
        end = System.currentTimeMillis()
        updateStatus()
    }

    void updateStatus() {
        log.info(TimePrinter.print(end - start))
        resWrite.initialize(runnerConfig.measures, runnerConfig.results_file, context)
        resWrite.writeResults(results)
        writeStatus(status++)
    }

    private void writeStatus(int phase) {
        File f = new File(this.runnerConfig.status_file)
        if (f.exists() && f.isFile() && f.canWrite())
            f.text = phase
        else {
            f.createNewFile()
            f.text = phase
        }
    }

    private void readStatus() {
        File f = new File(this.runnerConfig.status_file)
        if (f.exists() && f.isFile() && f.canRead())
            status = Integer.parseInt(f.text.trim())
    }

    private void resetDatabase() {
        context.createDatabase()
    }

    private ConfigObject loadConfiguration() {
        ConfigSlurper slurper = new ConfigSlurper()
        File file = new File("config/runner.conf")
        slurper.parse(file.text)
    }

    private void loadTools() {
//        context.open()
        log.info("Loading and registering tools")
        ToolsLoader toolLoader = new ToolsLoader()
        toolLoader.loadTools(context)
        log.info("Tools loaded and registered")
//        context.close()
    }

    private void readResults() {
        resReader.initialize(runnerConfig.measures, runnerConfig.results_file)
        results = resReader.readResults()
    }

    def executePhase(WorkFlow phase) {
        if (!phase)
            return

        log.info("$name - Executing Phase ${phase.name}")
        this.start = System.currentTimeMillis()
        phase.setResults(results)
        phase.execute(runnerConfig, num)
        this.end = System.currentTimeMillis()
        log.info("$name - Finished Executing Phase ${phase.name}")
        updateStatus()
    }

    def loadStudyConfig() {
        log.info("Loading Study Config")
        this.start = System.currentTimeMillis()
        configReader.initialize()
        results = configReader.loadNext() as Table<String, String, String>
        num = results.rowKeySet().size()
        log.info("Finished Loading Study Config")
        this.end = System.currentTimeMillis()
        updateStatus()
    }
}
