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

    Runner(ArcContext context) {
        this.context = context
        exGen = new ExperimentGenerator()
        resEx = new ResultsExtractor()
        resWrite = new ResultsWriter()
        injector = new SourceInjectorExecutor()
        status = 1
        num = 0
    }

    void run() {
        initialize()
        generateExperimentalConfig()
        generatePatternInstances()
        executeArcExperimenterPhaseOne()
        executeSourceCodeInjector()
        executeArcExperimenterPhaseTwo()
        extractResults()
    }

    def initialize() {
        this.runnerConfig = loadConfiguration()
    }

    void generateExperimentalConfig() {
        context.logger().atInfo().log("Generating Experimental Config")
        exGen.initialize(runnerConfig.grime_types)
        results = exGen.generate()
        num = results.rowKeySet().size()
        context.logger().atInfo().log("Finished Generating Experimental Config")

        resWrite.writeResults(results)
        updateStatus()
    }

    void generatePatternInstances() {
        context.logger().atInfo().log("Generating Design Pattern Instances")
        patternGenerator.initialize(runnerConfig.pattern_types, runnerConfig.base, runnerConfig.lang, num)
        patternGenerator.execute()
        context.logger().atInfo().log("Finished Generating Design Pattern Instances")
        updateStatus()
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
        context.logger().atInfo().log("Executing Expreiment Phase Two")

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
        resWrite.initialize(num, runnerConfig.measures)
        resWrite.writeResults(results)
        writeStatus(status++)
    }

    private void writeStatus(int phase) {

    }

    private ConfigObject loadConfiguration() {
        ConfigSlurper slurper = new ConfigSlurper()
        File file = new File("config/runner.conf")
        slurper.parse(file.text)
    }
}
