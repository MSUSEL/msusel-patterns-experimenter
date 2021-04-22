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
import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.inject.Director
import groovy.util.logging.Log4j2

@Log4j2
class SourceInjectorExecutor {

    int NUM
    ArcContext context
    Map<Integer, Tuple2<Integer, Integer>> severityMap = [
            1 : new Tuple2(1, 5),
            2 : new Tuple2(6, 10),
            3 : new Tuple2(11, 15),
            4 : new Tuple2(16, 20),
            5 : new Tuple2(21, 25),
            6 : new Tuple2(26, 35)
    ]

    void initialize(int num, ArcContext context) {
        this.NUM = num
        this.context = context
    }

    void execute(Table<String, String, String> results) {
        ConfigSlurper slurper = new ConfigSlurper()
        for (int id = 0; id < NUM; id++) {
            ConfigObject config = createConfig(slurper, results.row("$id"))

            context.open()
            def vals = Director.instance.inject(config)
            context.close()

            vals.each { col, value ->
                results.put("$id", col, value)
            }
        }
    }

    private ConfigObject createConfig(ConfigSlurper slurper, Map<String, String> map) {
        context.open()
        Project.findAll().each {
            println(it.get("projKey"))
        }
        log.info("Looking up project with key: ${map[Constants.Key1]}")
        Project proj = Project.findFirst("projKey = ?", map[Constants.Key1])
        PatternInstance inst
        String pattern = map.get(Constants.PatternType)
        String grimeType = map.get(Constants.GrimeType)
        int min = 0, max = 0

        if (proj) {
            inst = proj.getPatternInstances().first()
            (min, max) = calculateGrimeSeverity(inst, Integer.parseInt(map.get(Constants.GrimeSeverity)))
        }

        println("Project: $proj")
        println("Instance: $inst")
        if (proj && inst) {
            String confText = """
            where {
                systemKey = '${proj.getParentSystem().getKey()}'
                projectKey = '${proj.getProjectKey()}'
                patternKey = 'gof:$pattern'
                patternInst = '${inst.getInstKey()}'
            }
            what {
                type = 'grime'
                form = '$grimeType'
                min = $min
                max = $max
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

    private def calculateGrimeSeverity(PatternInstance inst, int severity) {
        if (severity == 0)
            return [0, 0]

        int size = inst.getRoleBindings().size()

        int min = (int) Math.floor((double) (severityMap[severity].v1 / 100) * size) + 1
        int max = (int) Math.ceil((double) (severityMap[severity].v2 / 100) * size) + 1

        return [min, max]
    }
}
