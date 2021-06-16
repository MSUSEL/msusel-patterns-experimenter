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
package edu.montana.gsoc.msusel.arc.impl.issues.findbugs

import edu.isu.isuese.datamodel.Priority
import edu.isu.isuese.datamodel.Rule
import edu.isu.isuese.datamodel.RuleRepository
import edu.isu.isuese.datamodel.Tag
import edu.montana.gsoc.msusel.arc.provider.AbstractRuleProvider
import edu.montana.gsoc.msusel.arc.ArcContext
import groovy.xml.XmlSlurper

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FindBugsRuleProvider extends AbstractRuleProvider {

    def fbConfig
    def fbSecConfig
    def fbContribConfig

    FindBugsRuleProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        fbConfig = new XmlSlurper()
                .parseText(FindBugsRepoProvider.class.getResourceAsStream(FindBugsConstants.FB_CONFIG_PATH).getText('UTF-8'))
        fbSecConfig = new XmlSlurper()
                .parseText(FindBugsRepoProvider.class.getResourceAsStream(FindBugsConstants.FB_SEC_CONFIG_PATH).getText('UTF-8'))
        fbContribConfig = new XmlSlurper()
                .parseText(FindBugsRepoProvider.class.getResourceAsStream(FindBugsConstants.FB_CONTRIB_CONFIG_PATH).getText('UTF-8'))
    }

    @Override
    void updateDatabase() {
        process(FindBugsConstants.FB_REPO_NAME, FindBugsConstants.FB_REPO_KEY, fbConfig)
        process(FindBugsConstants.FB_CONTRIB_REPO_NAME, FindBugsConstants.FB_CONTRIB_REPO_KEY, fbContribConfig)
        process(FindBugsConstants.FB_SEC_REPO_NAME, FindBugsConstants.FB_SEC_REPO_KEY, fbSecConfig)
    }

    private process(String repoName, String repoKey, config) {
        context.open()
        RuleRepository repo = RuleRepository.findFirst("repoKey = ?", repoKey)
        context.close()

        config.rule.each { rule ->
            String ruleKey = rule.@'key'
            String ruleName = rule.@'key'
            String description = rule.description
            String priorityName = rule.@'priority'
            Priority priority = Priority.fromValue(priorityName)
            List<Tag> tags = []

            context.open()
            if (!Rule.findFirst("ruleKey = ?", (String) "${repo.repoKey}:${ruleKey}")) {
                rule.@tag?.each { String tag ->
                    tags << Tag.of(tag)
                }

                if (rule.status != "DEPRECATED") {
                    Rule r = Rule.builder()
                            .name(ruleName)
                            .key("${repo.repoKey}:${ruleKey}")
                            .description(description)
                            .priority(priority)
                            .create()
                    tags.each {
                        r.addTag(it)
                    }
                    repo.addRule(r)
                }
            }
            context.close()
        }
    }
}
