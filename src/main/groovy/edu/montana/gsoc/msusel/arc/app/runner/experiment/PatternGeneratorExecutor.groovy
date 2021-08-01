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
package edu.montana.gsoc.msusel.arc.app.runner.experiment

import com.google.common.collect.Table
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.app.runner.WorkFlow
import edu.montana.gsoc.msusel.pattern.gen.ConfigLoader
import edu.montana.gsoc.msusel.pattern.gen.Director
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.pattern.gen.PluginLoader

class PatternGeneratorExecutor extends WorkFlow {

    String base
    String lang
    int NUM
    List<String> patterns

    PatternGeneratorExecutor(ArcContext context) {
        super("Design Pattern Generator", "Generates Design Pattern Instances", context)
    }

    void initWorkflow(ConfigObject runnerConfig, int num) {
        patterns = (List<String>) runnerConfig.pattern_types
        base = (String) runnerConfig.base
        lang = (String) runnerConfig.lang
        NUM = num
    }

    void executeStudy() {
        PluginLoader plugins = PluginLoader.instance
        ConfigLoader loader = new ConfigLoader()

        File fBase = new File(base)
        fBase.mkdirs()

        plugins.loadBuiltInLanguageProviders()
        loader.buildContext(createConfig(), fBase)
        plugins.loadLanguage(lang)

        Director director = new Director()
        director.initialize()
        GeneratorContext context = GeneratorContext.instance
        context.resetDb = false
        context.dataOnly = false
        context.results = results
        director.execute()
    }

    ConfigObject createConfig() {
        String config = """\
        output = '$base'
        language = 'java'
        patterns = ['${patterns.join("','")}']
        numInstances = ${(int) Math.ceil((double)NUM / patterns.size())}
        maxBreadth = 3
        maxDepth = 2
        version = "1.0.0"
        arities = [1, 2, 3]
        srcPath = "src/main/java"
        testPath = "src/test/java"
        binPath = "build/classes/java/main"
        srcExt = ".java"
        java_binary = '${System.getenv('JAVA_HOME').replace("\\", File.separator) + File.separator + "bin" + File.separator + "java"}'
        
        license {
            name = 'MIT'
            year = 2021
            holder = 'Isaac D. Griffith'
            project = 'Design Pattern Generaotr'
            url = 'isu-ese.github.io/patterngen'
        }
        
        db {
            driver = '${context.getDBCreds().driver}'
            url = '${context.getDBCreds().url}'
            user = '${context.getDBCreds().user}'
            pass = '${context.getDBCreds().pass}'
            type = '${context.getDBCreds().type}'
        }
        
        build {
            project = 'griffith-experiment-one'
            artifact = 'piolot-experiment'
            description = 'Pilot Experiment Generated Pattern Instance'
        }
        """

        ConfigSlurper slurper = new ConfigSlurper()
        slurper.parse(config)
    }
}
