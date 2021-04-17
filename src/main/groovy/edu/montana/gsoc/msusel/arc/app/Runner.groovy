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

    Runner(ArcContext context) {
        this.context = context
        patternGenerator = new PatternGeneratorExecutor()
        exGen = new ExperimentGenerator()
        resEx = new ResultsExtractor()
        resWrite = new ResultsWriter()
        injector = new SourceInjectorExecutor()
        sdl = new SystemDropLoader()
        status = 0
        num = 0
    }

    void run() {
        long start = System.currentTimeMillis()
        if (status < 1) initialize()
        if (status < 2) generateExperimentalConfig()
        if (status < 3) {
            generatePatternInstances()
            prepareDatabaseForExperiment()
        }
        if (status < 4) executeArcExperimenterPhaseOne()
        if (status < 5) executeSourceCodeInjector()
        if (status < 6) executeArcExperimenterPhaseTwo()
        if (status < 7) extractResults()
        long end = System.currentTimeMillis()

        println("Processing took a total of ${(double)(end - start) / 1000 / 60} minutes")
    }

    def initialize() {
        this.runnerConfig = loadConfiguration()
        readStatus()
        //if (status <= 0)
            //resetDatabase()
    }

    void generateExperimentalConfig() {
        context.logger().atInfo().log("Generating Experimental Config")
        exGen.initialize(runnerConfig.grime_types)
        results = exGen.generate()
        num = results.rowKeySet().size()
        context.logger().atInfo().log("Finished Generating Experimental Config")
        updateStatus()
    }

    void generatePatternInstances() {
        context.logger().atInfo().log("Generating Design Pattern Instances")
        patternGenerator.initialize(context, results, runnerConfig.pattern_types, runnerConfig.base, runnerConfig.lang, num)
        patternGenerator.execute()
        context.logger().atInfo().log("Finished Generating Design Pattern Instances")
        updateStatus()
    }

    void prepareDatabaseForExperiment() {
        context.logger().atInfo().log("Preparing Database for Experiment")
        sdl.dropOutSystemsAndProjects()
        resetDatabase()
        loadTools()
        sdl.loadSystemsAndProjects()
        context.logger().atInfo().log("Database Prepared")
    }

    void executeArcExperimenterPhaseOne() {
        context.logger().atInfo().log("Executing Experiment Phase One")
        phaseOne.execute()
        context.logger().atInfo().log("Finished Executing Experiment Phase One")
        updateStatus()
    }

    void executeSourceCodeInjector() {
        context.logger().atInfo().log("Executing Source Code Injector")
        injector.initialize(num, context)
        injector.execute()
        context.logger().atInfo().log("Finished Executing Source Code Injector")

        updateStatus()
    }

    void executeArcExperimenterPhaseTwo() {
        context.logger().atInfo().log("Executing Experiment Phase Two")
        phaseTwo.execute()
        context.logger().atInfo().log("Executing Experiment Phase Two")

        updateStatus()
    }

    void extractResults() {
        context.logger().atInfo().log("Collecting Experimental Results")
        resEx.initialize(ReportingLevel.PROJECT, runnerConfig.measures, num)
        resEx.extractResults(results)
        context.logger().atInfo().log("Finished Collecting Experimental Results")
        updateStatus()
    }

    void updateStatus() {
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
        context.open()
        context.logger().atInfo().log("Loading and registering tools")
        ToolsLoader toolLoader = new ToolsLoader()
        toolLoader.loadTools(context)
        context.logger().atInfo().log("Tools loaded and registered")
        context.close()
    }
}
