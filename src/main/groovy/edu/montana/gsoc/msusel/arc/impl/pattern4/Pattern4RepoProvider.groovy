/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.arc.impl.pattern4

import edu.montana.gsoc.msusel.arc.AbstractRepoProvider
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.datamodel.pattern.Pattern
import edu.montana.gsoc.msusel.datamodel.pattern.PatternRepository
import edu.montana.gsoc.msusel.datamodel.pattern.Role

import java.nio.file.Files

class Pattern4RepoProvider extends AbstractRepoProvider {

    @Override
    void registerRepos(ArcContext context) {

    }

    def loadData() {
        def repos = Files.readAllLines(configPath).join("\n")
        def configSluper = new ConfigSlurper()
        config = configSluper.parse(repos)
    }

    def updateDatabase() {
        config.repos.each { key, value ->
            String repoKey = value["key"]
            String repoName = value["name"]
            PatternRepository repo = PatternRepository.builder().repoKey(repoKey).name(repoName).create()
            mediator.addPatternRepository(repo)

            value.patterns.each { pKey, pValue ->
                def patternName = pKey
                def patternKey = "${repoKey}:${pKey.replaceAll(/\s/, "_")}"
                Pattern pattern = Pattern.builder().repository(null).name(patternName).patternKey(patternKey).create()
                repo << pattern
                mediator.addPattern(pattern)

                pValue.roles.each {
                    def roleKey = "${patternKey}#${it}"
                    Role r = Role.builder().roleKey(roleKey).name(it).create()
                    pattern << r
                    mediator.addRole(r)
                }
                mediator.updatePattern(pattern)
            }
            mediator.updatePatternRepository(repo)
        }
    }
}
