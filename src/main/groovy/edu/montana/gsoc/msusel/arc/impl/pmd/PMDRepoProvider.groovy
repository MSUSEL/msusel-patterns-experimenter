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
package edu.montana.gsoc.msusel.arc.impl.pmd

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
class PMDRepoProvider extends AbstractRepoProvider {

    Path configPath
    def config

    PMDRepoProvider(Path configPath) {
        this.configPath = configPath
    }

    @Override
    void loadData() {
        config = new XmlSlurper().parse(configPath.toFile())
    }

    @Override
    void updateDatabase() {
        RuleRepository repo = RuleRepository.builder().name("PMD").repoKey("pmd").create()
        mediator.addRuleRepository(repo)
        config.rule.each { rule ->
            String ruleKey = rule.@'key'
            String ruleName = rule.configKey
            Priority priority = Priority.forValue(rule.priority)
            String tag = rule.tag

            if (rule.status != "DEPRECATED") {
                Rule r = Rule.builder()
                        .name(ruleName)
                        .ruleKey("${repo.repoKey}:${ruleKey}")
                        .description()
                        .priority(priority)
                        .tags([Tag.builder().tag(tag).create()])
                        .create()
                repo << r
                mediator.addRule(r)
            }
        }
        mediator.updateRuleRepository(repo)
    }
}
