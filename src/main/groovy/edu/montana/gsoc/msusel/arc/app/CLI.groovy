package edu.montana.gsoc.msusel.arc.app

import com.google.common.flogger.FluentLogger
import edu.isu.isuese.datamodel.util.DBManager
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.Tool
import edu.montana.gsoc.msusel.arc.db.ConfigLoader
import edu.montana.gsoc.msusel.arc.db.DbProperties
import edu.montana.gsoc.msusel.arc.impl.findbugs.FindBugsTool
import edu.montana.gsoc.msusel.arc.impl.ghsearch.GitHubSearchTool
import edu.montana.gsoc.msusel.arc.impl.git.GitTool
import edu.montana.gsoc.msusel.arc.impl.gradle.GradleTool
import edu.montana.gsoc.msusel.arc.impl.grime.GrimeTool
import edu.montana.gsoc.msusel.arc.impl.injector.SoftwareInjectorTool
import edu.montana.gsoc.msusel.arc.impl.java.JavaTool
import edu.montana.gsoc.msusel.arc.impl.maven.MavenTool
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsTool
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Tool
import edu.montana.gsoc.msusel.arc.impl.pmd.PMDTool
import edu.montana.gsoc.msusel.arc.impl.quamoco.QuamocoTool
import edu.montana.gsoc.msusel.arc.impl.td.TechDebtTool
import groovy.util.logging.Log

import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

class CLI {

    public static final FluentLogger logger = FluentLogger.forEnclosingClass()
    static final String VERSION = "1.3.0"

    static void main(String[] args) {
        ArcContext context = null

        // Setup CLI
        CommandLineInterface cli = CommandLineInterface.instance
        cli.context = context
        cli.initialize()
        ConfigLoader loader = ConfigLoader.instance

        // Load Configuration
        context.logger().atInfo().log("Loading Configuration")

        File fBase = new File(System.getenv("ARC_HOME"))
        File fConfig = new File(fBase, ArcConstants.PROPERTIES_FILE)
        Properties config = loader.loadConfiguration(fConfig)
        context.setArcProperties(config)

        context.logger().atInfo().log("Configuration loaded")

        // Process Command Line Args
        context.logger().atInfo().log("Processing Command Line Arguments")
        cli.parse(args)
        context.logger().atInfo().log("Command Line Arguments Processed")

        // Load and register tools
        context.logger().atInfo().log("Loading and registering tools")
        ToolsLoader toolLoader = new ToolsLoader()
        toolLoader.loadTools(context)
        context.logger().atInfo().log("Tools loaded and registered")

        // Select experiment
        context.logger().atInfo().log("Selecting experiment: %s", experiment)

        context.logger().atInfo().log("Experiment loaded and ready to execute")

        // Run the experiment
        DBManager.instance.open(context.getArcProperty(DbProperties.DB_DRIVER),
                context.getArcProperty(DbProperties.DB_URL),
                context.getArcProperty(DbProperties.DB_USER),
                context.getArcProperty(DbProperties.DB_PASS))
        // experiment.run
        DBManager.instance.close()
    }
}

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
@Log
class CommandLineInterface {

    CliBuilder cli
    ArcContext context

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
        cli.c(longOpt: 'config', args: 1, argName: 'file', 'Name of config file found in the base directory')
        cli.v(longOpt: 'version', 'Print the version information')
        cli.D(args: 2, valueSeparator: '=', argName: 'property=value', 'use value for given property')
        cli.l(longOpt: 'log', args: 1, argName: 'file', 'Name of the log file to log to')

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
            cli.usage()
            System.exit 0
        }

        String config
        if (options.c) {
            config = options.c
        } else {
            config = "InjectorConfig"
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

        if (options.l) {
            String logfile = options.l
            Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            FileHandler handler = new FileHandler(logfile)
            handler.setFormatter(new SimpleFormatter())
            log.addHandler(handler)
        } else {
            Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            FileHandler handler = new FileHandler("%t/arc.log")
            handler.setFormatter(new SimpleFormatter())
            log.addHandler(handler)
        }

        context.logger().atInfo().log("Completed parsing command line arguments")

        return [config, base]
    }
}

class ToolsLoader {

    void loadTools(ArcContext context) {
        context.logger().atInfo().log("Instantiating tools")

        Tool[] tools = [
                new FindBugsTool(context),
                new GitHubSearchTool(context),
                new GitTool(context),
                new GradleTool(context),
                new GrimeTool(context),
                new JavaTool(context),
                new MavenTool(context),
                new MetricsTool(context),
                new Pattern4Tool(context),
                new PMDTool(context),
                new Pattern4Tool(context),
                new QuamocoTool(context),
                new SoftwareInjectorTool(context),
                new TechDebtTool(context)
        ]

        context.logger().atInfo().log("Tools instantiated now loading repos and initializing commands")
        tools.each {
            it.getRepoProvider().load()
            it.getOtherProviders()*.load()
            it.init()
        }
        context.logger().atInfo().log("Finished loading tools")
    }
}