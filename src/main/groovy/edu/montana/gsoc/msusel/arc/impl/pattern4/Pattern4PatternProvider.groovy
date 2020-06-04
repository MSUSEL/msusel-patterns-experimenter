package edu.montana.gsoc.msusel.arc.impl.pattern4

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternRepository
import edu.isu.isuese.datamodel.Role
import edu.isu.isuese.datamodel.RoleType
import edu.montana.gsoc.msusel.arc.provider.AbstractPatternProvider
import edu.montana.gsoc.msusel.arc.ArcContext
import groovy.xml.XmlSlurper

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Pattern4PatternProvider extends AbstractPatternProvider {

    def data
    Map<String, String> pattern4rbml = [:]
    Table<String, String, String> rolePattern4Rbml = HashBasedTable.create()

    Pattern4PatternProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        def slurper = new XmlSlurper()
        data = slurper.parseText(Pattern4PatternProvider.class.getResourceAsStream(Pattern4Constants.PATTERN4_CONFIG_PATH).text)
    }

    @Override
    void updateDatabase() {
        data.patterns.each {
            repository = findPatternRepo(data.patterns)
            createPatterns(data.patterns.pattern)
        }
    }

    String rbmlNameFor(String pattern4Name) {
        return pattern4rbml[pattern4Name]
    }

    String rbmlRoleNameFor(String patternName, String pattern4Name) {
        return rolePattern4Rbml.get(patternName, pattern4Name)
    }

    PatternRepository findPatternRepo(patterns) {
        String repoName = patterns.@repo

        PatternRepository.findFirst("repoKey = ?", repoName)
    }

    void createPatterns(patterns) {
        patterns.each {
            String name = it.@gofName
            String pattern4Name = it.@pattern4Name

            Pattern pattern = repository.getPatterns().find { p -> p.name == name }
            if (!pattern) {
                pattern = Pattern.builder().name(name).key("${repository.getRepoKey()}:$name").create()
                repository.addPattern(pattern)
            }

            if (name && pattern4Name)
                pattern4rbml[pattern4Name] = name

            createRoles(pattern, it.role)
        }
    }

    void createRoles(Pattern pattern, roles) {
        roles.each {
            String rbmlName = it.@rbmlName
            String pattern4Name = it.@pattern4Name
            String type = it.@elementType
            boolean mand = Boolean.parseBoolean(it.@mandatory)

            Role role = pattern.getRoles().find { r -> r.getName() == rbmlName }
            if (!role) {
                role = Role.builder()
                        .name(rbmlName)
                        .type(roleTypeFromElementType(type))
                        .roleKey("${pattern.getPatternKey()}:$rbmlName")
                        .mandatory(mand)
                        .create()
                pattern.addRole(role)
            }

            if (rbmlName && pattern4Name)
                rolePattern4Rbml.put(pattern.getName(), pattern4Name, rbmlName)
        }
    }

    RoleType roleTypeFromElementType(String elementType) {
        switch(elementType) {
            case "CLASS": return RoleType.CLASSIFIER
            case "FIELD": return RoleType.STRUCT_FEAT
            case "METHOD": return RoleType.BEHAVE_FEAT
        }

        null
    }
}
