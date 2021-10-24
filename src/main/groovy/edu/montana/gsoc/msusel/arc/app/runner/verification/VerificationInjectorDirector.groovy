/*
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
package edu.montana.gsoc.msusel.arc.app.runner.verification

import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.inject.InjectorFactory
import edu.montana.gsoc.msusel.inject.ProjectCopier
import edu.montana.gsoc.msusel.inject.SourceInjector

@Singleton
class VerificationInjectorDirector {

    def inject(ConfigObject config) {
        Project proj = Project.findFirst("projKey = ?", (String) config.where.projectKey)

        String projKey = config.where.projectKey
        String[] comps = projKey.split(/:/)
        comps[1] = comps[1] + "_copy"
        projKey = comps.join(":")

        if (!Project.findFirst("projKey = ?", projKey)) {

            ProjectCopier copier = new ProjectCopier()
            proj = copier.execute(proj)

            String[] instKey = config.where.patternInst.split(/:/)
            instKey[1] = instKey[1] + "_copy"
            PatternInstance inst = PatternInstance.findFirst("instKey = ?", (String) instKey.join(":"))

            File file = new File(config.control.fileName)
            file.readLines().each { line ->
                String[] params = line.split(",")
                SourceInjector injector = InjectorFactory.instance.createInjector(inst, "grime", params[0])
                injector.inject(Arrays.copyOfRange(params, 1, params.length - 1))
            }

            return [
                    "InjectedKey" : proj.projectKey,
                    "InjectedLocation" : proj.getFullPath()
            ]
        } else {
            Project p = Project.findFirst("projKey = ?", projKey)
            return [
                    "InjectedKey" : p.projectKey,
                    "InjectedLocation" : p.getFullPath()]
        }
    }
}