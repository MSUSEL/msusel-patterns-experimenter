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
            ConfigObject config = createConfig(slurper, results, id)

            context.open()
            Director.instance.inject(config)
            context.close()
        }
    }

    private ConfigObject createConfig(ConfigSlurper slurper, Table<String, String, String> results, int id) {
        Map<String, String> map = results.row("$id")
        String sys = map[Constants.Key1]
        context.open()
        System s = System.findFirst("key = ?", sys)
        Project proj
        PatternInstance inst
        String pattern = map.get(Constants.PatternType)
        String grimeType = map.get(Constants.GrimeType)
        int min = 0, max = 0

        if (s) {
            proj = s.getProjects().first()
            inst = proj.getPatternInstances().first()
            (min, max) = calculateGrimeSeverity(inst, Integer.parseInt(map.get(Constants.GrimeSeverity)))
        }

        if (proj && inst) {
            String confText = """
            where {
                systemKey = '$sys'
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
            return slurper.parse(confText)
        }
        else return null
    }

    private def calculateGrimeSeverity(PatternInstance inst, int severity) {
        if (severity == 0)
            return [0, 0]

        int size = inst.getRoleBindings().size()

        int min = (int) Math.ceil((double) (severityMap[severity].v1 * size) / 100)
        int max = (int) Math.ceil((double) (severityMap[severity].v2 * size) / 100)

        return [min, max]
    }
}
