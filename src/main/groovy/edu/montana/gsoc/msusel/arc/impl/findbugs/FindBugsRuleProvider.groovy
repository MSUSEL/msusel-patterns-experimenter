package edu.montana.gsoc.msusel.arc.impl.findbugs

import edu.isu.isuese.datamodel.Priority
import edu.isu.isuese.datamodel.Rule
import edu.isu.isuese.datamodel.RuleRepository
import edu.isu.isuese.datamodel.Tag
import edu.montana.gsoc.msusel.arc.provider.AbstractRuleProvider
import edu.montana.gsoc.msusel.arc.ArcContext
import groovy.xml.XmlSlurper

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
        RuleRepository repo = RuleRepository.findFirst("repoKey = ?", repoKey)

        config.rule.each { rule ->
            String ruleKey = rule.@'key'
            String ruleName = rule.getToolName
            String description = rule.description
            String priorityName = rule.@'priority'
            Priority priority = Priority.fromValue(priorityName)
            List<Tag> tags = []
            rule.tag.each { String tag ->
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
    }
}
