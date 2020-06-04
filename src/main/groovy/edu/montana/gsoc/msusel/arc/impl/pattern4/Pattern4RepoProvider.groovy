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
package edu.montana.gsoc.msusel.arc.impl.pattern4


import edu.isu.isuese.datamodel.PatternRepository
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.provider.AbstractRepoProvider
import edu.montana.gsoc.msusel.arc.impl.pmd.PMDRepoProvider
import groovy.xml.XmlSlurper

class Pattern4RepoProvider extends AbstractRepoProvider {

    def config

    Pattern4RepoProvider(ArcContext context) {
        super(context)
    }

    @Override
    def loadData() {
        config = new XmlSlurper()
                .parseText(PMDRepoProvider.class.getResourceAsStream(Pattern4Constants.PATTERN4_CONFIG_PATH).getText('UTF-8'))
    }

    @Override
    def updateDatabase() {
        config.patterns.each {
            createPatternRepo(it)
        }
    }

    private void createPatternRepo(patterns) {
        String repoName = patterns.@repo
        String toolName = patterns.@tool

        PatternRepository repo = PatternRepository.findFirst("repoKey = ?", repoName)
        if (!repo)
            PatternRepository.builder()
                    .name(repoName)
                    .key(repoName)
                    .toolName(toolName)
                    .create()
    }
}
