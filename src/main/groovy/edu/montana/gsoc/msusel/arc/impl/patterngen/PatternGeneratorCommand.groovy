package edu.montana.gsoc.msusel.arc.impl.patterngen

import edu.isu.isuese.datamodel.SCMType
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.PrimaryAnalysisCommand
import edu.montana.gsoc.msusel.arc.db.DbProperties
import edu.montana.gsoc.msusel.pattern.gen.Director
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.pattern.gen.PatternManager
import edu.montana.gsoc.msusel.pattern.gen.PluginLoader

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternGeneratorCommand extends PrimaryAnalysisCommand {

    PatternGeneratorCommand() {
        super(GeneratorConstants.GENERATOR_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        PluginLoader plugins = PluginLoader.instance

        context.logger().atInfo().log("PatternGenerator: Creating the base directory")
        File fBase = new File(context.getArcProperty(GeneratorProperties.GEN_BASE_DIR))
        fBase.mkdirs()

        context.logger().atInfo().log("PatternGenerator: Setting up the GeneratorContext and loading generator plugins")
        createGeneratorContext(context)
        plugins.loadBuiltInLanguageProviders()
        plugins.loadLanguage(context.getArcProperty(GeneratorProperties.GEN_LANG_PROP))
        context.logger().atInfo().log("PatternGenerator: Completed GeneratorContext setups and loading generator plugins")

        // Run the program
        context.logger().atInfo().log("PatternGenerator: Initializing the director")
        Director director = new Director()
        director.initialize()
        context.logger().atInfo().log("PatternGenerator: Director initialized")

        context.logger().atInfo().log("PatternGenerator: Executing...")
        director.execute()
        context.logger().atInfo().log("PatternGenerator: Execution complete")
    }

    String createGeneratorContext(ArcContext context) {
        GeneratorContext generatorContext = GeneratorContext.getInstance()
        generatorContext.base         = new File(context.getArcProperty(GeneratorProperties.GEN_BASE_DIR))
        generatorContext.output       = context.getArcProperty(GeneratorProperties.GEN_OUTPUT_DIR)
        generatorContext.loader       = PatternManager.getInstance()
        generatorContext.patterns     = createPatternsList(context)
        generatorContext.numInstances = Integer.parseInt(context.getArcProperty(GeneratorProperties.GEN_NUM_INSTANCES))
        generatorContext.maxBreadth   = Integer.parseInt(context.getArcProperty(GeneratorProperties.GEN_MAX_BREADTH))
        generatorContext.maxDepth     = Integer.parseInt(context.getArcProperty(GeneratorProperties.GEN_MAX_DEPTH))
        generatorContext.version      = context.getArcProperty(GeneratorProperties.GEN_VERSION)
        generatorContext.license      = createLicenseMap(context)
        generatorContext.db           = createDbMap(context)
        generatorContext.build        = createBuildMap(context)
        generatorContext.arities      = createAritiesList(context)
        generatorContext.srcPath      = context.getArcProperty(GeneratorProperties.GEN_SOURCE_PATH)
        generatorContext.srcExt       = context.getArcProperty(GeneratorProperties.GEN_SOURCE_EXT)
        generatorContext.logger       = context.logger()
        generatorContext.resetDb      = false
        generatorContext.resetOnly    = false
        generatorContext.generateOnly = false
        generatorContext.dataOnly     = false

        context.logger().atInfo().log("PatternGenerator: GeneratorContext created with the following values:")
        context.logger().atInfo().log(generatorContext.toString())
    }

    private Map<String, String> createLicenseMap(ArcContext context) {
        [
                "name": context.getArcProperty(GeneratorProperties.GEN_LICENSE_NAME),
                "year": context.getArcProperty(GeneratorProperties.GEN_LICENSE_YEAR),
                "holder": context.getArcProperty(GeneratorProperties.GEN_LICENSE_HOLDER),
                "project": context.getProject().getName(),
                "url": context.getProject().getSCM(SCMType.GIT).getURL()
        ]
    }

    private Map<String, String> createDbMap(ArcContext context) {
        [
                "driver": context.getArcProperty(DbProperties.DB_DRIVER),
                "url": context.getArcProperty(DbProperties.DB_URL),
                "user": context.getArcProperty(DbProperties.DB_USER),
                "pass": context.getArcProperty(DbProperties.DB_PASS),
                "type": context.getArcProperty(DbProperties.DB_TYPE)
        ]
    }

    private Map<String, String> createBuildMap(ArcContext context) {
        [
                "project": context.getArcProperty(GeneratorProperties.GEN_BUILD_PROJECT),
                "artifact": context.getArcProperty(GeneratorProperties.GEN_BUILD_ARTIFACT),
                "description": context.getArcProperty(GeneratorProperties.GEN_BUILD_DESC)
        ]
    }

    private List<Integer> createAritiesList(ArcContext context) {
        String a = context.getArcProperty(GeneratorProperties.GEN_ARITIES)
        String[] vals = a.split(/,\w*/)
        List<Integer> arities = []
        vals.each {
            arities << Integer.parseInt(it.trim())
        }

        arities
    }

    private List<String> createPatternsList(ArcContext context) {
        String[] patterns = context.getArcProperty(GeneratorProperties.GEN_PATTERNS).split(/,\w*/)
        patterns.toList()
    }
}
