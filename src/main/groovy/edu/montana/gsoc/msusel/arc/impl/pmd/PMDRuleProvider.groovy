package edu.montana.gsoc.msusel.arc.impl.pmd

import edu.isu.isuese.datamodel.Priority
import edu.isu.isuese.datamodel.Rule
import edu.isu.isuese.datamodel.RuleRepository
import edu.isu.isuese.datamodel.Tag
import edu.montana.gsoc.msusel.arc.provider.AbstractRuleProvider
import edu.montana.gsoc.msusel.arc.ArcContext
import groovy.xml.XmlSlurper

class PMDRuleProvider extends AbstractRuleProvider {

    def config

    PMDRuleProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        config = new XmlSlurper()
                .parseText(PMDRepoProvider.class.getResourceAsStream(PMDConstants.PMD_CONFIG_PATH).getText('UTF-8'))
    }

    @Override
    void updateDatabase() {
        RuleRepository repo = RuleRepository.findFirst("repoKey = ?", "pmd")

        config.rule.each { rule ->
            String ruleKey = rule.@key
            String ruleName = rule.configKey
            String priorityName = rule.priority
            Priority priority = Priority.fromValue(priorityName)
            String tag = rule.tag

            if (rule.status != "DEPRECATED" || ruleExists(repo, ruleKey)) {
                Rule r = Rule.builder()
                        .name(ruleName)
                        .key("${repo.repoKey}:${ruleKey}")
                        .description()
                        .priority(priority)
                        .create()
                r.addTag(Tag.of(tag))
                repo.addRule(r)
            }
        }
    }

    private boolean ruleExists(RuleRepository repo, String ruleKey) {
        repo.getRules().find { Rule r -> r.getKey() == ruleKey } != null
    }
}
