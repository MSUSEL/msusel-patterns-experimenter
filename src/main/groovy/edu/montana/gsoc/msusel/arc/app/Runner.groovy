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
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.ReportingLevel
import groovy.util.logging.Log4j2

@Log4j2
class Runner {

    ArcContext context
    Table<String, String, String> results
    ExperimentGenerator exGen
    ResultsExtractor resEx
    ResultsWriter resWrite
    SourceInjectorExecutor injector
    PatternGeneratorExecutor patternGenerator
    ExperimentPhaseOne phaseOne
    ExperimentPhaseTwo phaseTwo
    int status
    int num
    ConfigObject runnerConfig
    SystemDropLoader sdl
    long start
    long end

    Runner(ArcContext context) {
        this.context = context
        patternGenerator = new PatternGeneratorExecutor()
        exGen = new ExperimentGenerator()
        resEx = new ResultsExtractor()
        resWrite = new ResultsWriter()
        injector = new SourceInjectorExecutor()
        phaseOne = new ExperimentPhaseOne(context)
        phaseTwo = new ExperimentPhaseTwo(context)
        sdl = new SystemDropLoader(context)
        status = 0
        num = 0
    }

    void run() {
        long start = System.currentTimeMillis()
        if (status < 1) initialize()
        if (status < 2) generateExperimentalConfig()
        if (status < 3) {
            generatePatternInstances()
            loadTools()
        }
        if (status < 4) executeArcExperimenterPhaseOne()
        if (status < 5) executeSourceCodeInjector()
        if (status < 6) executeArcExperimenterPhaseTwo()
//        if (status < 7) extractResults()
        long end = System.currentTimeMillis()

        log.info(TimePrinter.print(end - start))
    }

    def initialize() {
        this.runnerConfig = loadConfiguration()
        readStatus()
        if (status <= 0)
            resetDatabase()
    }

    void generateExperimentalConfig() {
        log.info("Generating Experimental Config")
        start = System.currentTimeMillis()
        exGen.initialize()
        results = exGen.generate(runnerConfig.pattern_types, runnerConfig.grime_types, runnerConfig.grime_severity_levels)
        num = results.rowKeySet().size()
        log.info("Finished Generating Experimental Config")
        end = System.currentTimeMillis()
        updateStatus()
    }

    void generatePatternInstances() {
        log.info("Generating Design Pattern Instances")
        start = System.currentTimeMillis()
        patternGenerator.initialize(context, results, runnerConfig.pattern_types, runnerConfig.base, runnerConfig.lang, num)
        patternGenerator.execute()
        log.info("Finished Generating Design Pattern Instances")
        end = System.currentTimeMillis()
        updateStatus()
    }

    void executeArcExperimenterPhaseOne() {
        log.info("Executing Experiment Phase One")
        start = System.currentTimeMillis()
        phaseOne.setNUM(num)
        phaseOne.setResults(results)
        phaseOne.execute()
        log.info("Finished Executing Experiment Phase One")
        end = System.currentTimeMillis()
        updateStatus()
    }

    void executeSourceCodeInjector() {
        log.info("Executing Source Code Injector")
        start = System.currentTimeMillis()
        injector.initialize(num, context)
        injector.execute(results)
        log.info("Finished Executing Source Code Injector")
        end = System.currentTimeMillis()

        updateStatus()
    }

    void executeArcExperimenterPhaseTwo() {
        log.info("Executing Experiment Phase Two")
        start = System.currentTimeMillis()
        phaseTwo.setNUM(num)
        phaseTwo.setResults(results)
        phaseTwo.execute()
        log.info("Executing Experiment Phase Two")
        end = System.currentTimeMillis()

        updateStatus()
    }

    void extractResults() {
        log.info("Collecting Experimental Results")
        start = System.currentTimeMillis()
        resEx.initialize(ReportingLevel.PROJECT, runnerConfig.measures, num)
        resEx.extractResults(results)
        log.info("Finished Collecting Experimental Results")
        end = System.currentTimeMillis()
        updateStatus()
    }

    void updateStatus() {
        log.info(TimePrinter.print(end - start))
        resWrite.initialize(num, runnerConfig.measures, runnerConfig.results_file)
        resWrite.writeResults(results)
        writeStatus(status++)
    }

    private void writeStatus(int phase) {
        File f = new File("config/status")
        if (f.exists() && f.isFile() && f.canRead())
            f.text = phase
    }

    private void readStatus() {
        File f = new File("config/status")
        if (f.exists() && f.isFile() && f.canWrite())
            status = Integer.parseInt(f.text)
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
}
