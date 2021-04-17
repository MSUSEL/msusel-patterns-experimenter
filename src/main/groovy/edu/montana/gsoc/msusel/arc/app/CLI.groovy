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

import edu.isu.isuese.datamodel.util.DBManager
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.ArcProperties
import edu.montana.gsoc.msusel.arc.db.ConfigLoader
import edu.montana.gsoc.msusel.arc.impl.experiment.EmpiricalStudy
import edu.montana.gsoc.msusel.arc.impl.experiment.StudyManager
import groovy.cli.picocli.CliBuilder
import groovy.cli.picocli.OptionAccessor
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class CLI {

    static final String VERSION = "1.3.0"

    static void main(String[] args) {
        ArcContext context = new ArcContext(log)
        StudyManager studyManager = new StudyManager(context)
        Runner runner = new Runner(context)

        // Setup CLI
        CommandLineInterface cli = CommandLineInterface.instance
        cli.context = context
        cli.manager = studyManager

        String base = ""
        EmpiricalStudy empiricalStudy
        cli.initialize()
        ConfigLoader loader = ConfigLoader.instance

        // Load Configuration
        context.logger().atInfo().log("Loading Configuration")

        File fBase
        if (System.getenv("ARC_HOME") == null)
            fBase = new File(".")
        else fBase = new File((String) System.getenv("ARC_HOME"))
        File fConfig = new File(fBase, ArcConstants.PROPERTIES_FILE)
        Properties config = loader.loadConfiguration(fConfig)
        context.setArcProperties(config)
        if (System.getenv("ARC_HOME") == null)
            context.addArcProperty(ArcProperties.ARC_HOME_DIR, ".")
        else context.addArcProperty(ArcProperties.ARC_HOME_DIR, System.getenv("ARC_HOME"))

        context.logger().atInfo().log("Configuration loaded")

        // Process Command Line Args
        context.logger().atInfo().log("Processing Command Line Arguments")
//        (base, empiricalStudy) = cli.parse(args)
//        context.addArcProperty(ArcProperties.BASE_DIRECTORY, base)
        context.logger().atInfo().log("Command Line Arguments Processed")

        context.logger().atInfo().log("Verifying Database and Creating if missing")
        DBManager.instance.checkDatabaseAndCreateIfMissing(context.getDBCreds())

        runner.run()

        // Select experiment
//        context.logger().atInfo().log(String.format("Selecting experiment: %s", empiricalStudy.getName()))
//
//        context.logger().atInfo().log("Experiment loaded and ready to execute")
//
//        // Run the experiment
//        empiricalStudy.execute()
    }
}

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class CommandLineInterface {

    CliBuilder cli
    ArcContext context
    StudyManager manager

    void initialize() {
        context.logger().atInfo().log("Initializing CLI")

        cli = new CliBuilder(
                usage: "arc [options] <base>",
                header: "\nOptions:",
                footer: "\nCopyright (c) 2017-2020 Isaac Griffith and Montana State University",
        )

        // set the amount of columns the usage message will be in width
        cli.width = 80 // default is 74

        // add options
        cli.h(longOpt: 'help', 'Print this help text and exit')
        cli.v(longOpt: 'version', 'Print the version information')
        cli.D(args: 2, valueSeparator: '=', argName: 'property=value', 'use value for given property')
//        cli.l(longOpt: 'log', args: 1, argName: 'file', 'Name of the log file to log to')
        cli.e(longOpt: 'empirical-study', "Name of the study to execute, available studies: ${manager.studies.keySet().join(', ')}")

        context.logger().atInfo().log("Completed CLI Initialization")
    }

    def parse(args) {
        context.logger().atInfo().log("started parsing command line arguments")

        OptionAccessor options = cli.parse(args)

        if (!options) {
            System.err << 'Error while parsing command-line options.\n\n'
            cli.usage()
            System.exit 1
        }

        if (options.v) {
            println "arc experimenter version ${CLI.VERSION}"
            System.exit 0
        }

        if (options.h) {
            System.out.println()
            cli.usage()
            System.exit 0
        }

        String base
        if (options.arguments()) {
            base = options.arguments()[0]
        } else {
            base = "."
        }

        if (options.Ds) {
            List<String> values = options.Ds
            for (int i = 0; i < values.size(); i+=2)
                context.updateProperty(values[i], values[i + 1])
        }

//        if (options.l) {
//            String logfile = options.l
//
//            Appender console = ConsoleAppender.
//            Appender file = new FileAppender("", (String) System.getenv(ArcConstants.HOME_ENV_VAR) + "/logs/" + logfile)
//            Logger logger = LogManager.getRootLogger()
//            logger.addAppender(console)
//            logger.addAppender(file)
//        } else {
//            Appender console = new ConsoleAppender()
//            Appender file = new FileAppender("", (String) System.getenv(ArcConstants.HOME_ENV_VAR) + "/logs/arc.log")
//            Logger logger = LogManager.getRootLogger()
//            logger.addAppender(console)
//            logger.addAppender(file)
//        }

        EmpiricalStudy empiricalStudy
        if (options.e) {
            String studyName = options.e
            if (manager.studies.keySet().contains(studyName))
                empiricalStudy = manager.studies[studyName]
            else
            {
                context.logger().atError().log(String.format("Empirical Study, %s, is unknown. Failed to execute study runner.", studyName))
                System.exit(1)
            }
        }
        else {
            context.logger().atError().log("No empirical study specified. Failed to execute study runner.")
            System.exit(1)
        }

        context.logger().atInfo().log("Completed parsing command line arguments")

        return [base, empiricalStudy]
    }
}


