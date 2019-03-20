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
package edu.montana.gsoc.msusel.arc.impl.findbugs

import edu.montana.gsoc.msusel.arc.AbstractRepoProvider
import edu.montana.gsoc.msusel.datamodel.measures.Priority
import edu.montana.gsoc.msusel.datamodel.measures.Rule
import edu.montana.gsoc.msusel.datamodel.measures.RuleRepository
import edu.montana.gsoc.msusel.datamodel.measures.Tag

import java.nio.file.Path

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FindBugsRepoProvider extends AbstractRepoProvider {

    Path fbConfigPath
    Path fbSecConfigPath
    Path fbContribConfigPath
    def fbConfig
    def fbSecConfig
    def fbContribConfig

    FindBugsRepoProvider(Path fbConfigPath, Path fbSecConfigPath, Path fbContribConfigPath) {
        this.fbConfigPath = fbConfigPath
        this.fbSecConfigPath = fbSecConfigPath
        this.fbContribConfigPath = fbContribConfigPath
    }

    @Override
    void loadData() {
        fbConfig = new XmlSlurper().parse(fbConfigPath.toFile())
        fbSecConfig = new XmlSlurper().parse(fbSecConfigPath.toFile())
        fbContribConfig = new XmlSlurper().parse(fbContribConfigPath.toFile())
    }

    @Override
    void updateDatabase() {
        process("findbugs", "findbugs", fbConfig)
        process("fbcontrib", "fbcontrib", fbContribConfig)
        process("findsecbugs", "findsecbugs", fbSecConfig)
    }

    private process(String repoName, String repoKey, config) {
        RuleRepository repo = RuleRepository.builder().name(repoName).repoKey(repoKey).create()
        mediator.addRuleRepository(repo)
        config.rule.each { rule ->
            String ruleKey = rule.@'key'
            String ruleName = rule.name
            String description = rule.description
            Priority priority = Priority.forValue(rule.@'priority')
            List<Tag> tags = []
            rule.tag.each { tag ->
                tags << Tag.builder().tag(tag).create()
            }

            if (rule.status != "DEPRECATED") {
                Rule r = Rule.builder()
                        .name(ruleName)
                        .ruleKey("${repo.repoKey}:${ruleKey}")
                        .description(description)
                        .priority(priority)
                        .tags(tags)
                        .create()
                repo << r
                mediator.addRule(r)
            }
        }
        mediator.updateRuleRepository(repo)
    }
}
