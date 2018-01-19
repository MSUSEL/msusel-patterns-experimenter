package edu.montana.gsoc.msusel.patterns;

import edu.montana.gsoc.msusel.patterns.datamodel.Project;
import edu.montana.gsoc.msusel.patterns.command.Phase;
import edu.montana.gsoc.msusel.patterns.command.Workflow;
import edu.montana.gsoc.msusel.patterns.command.impl.*;

import java.util.Properties;

public class Experimenter {

    public static void main(String args[]) {
        Properties p = ExperimenterPropLoader.loadProperties("msusel-patterns/settings/");
        p.forEach((key, value) -> System.out.printf("%s = %s\n", key, value));

        Project project = Project.builder()
                .repoName("msusel-codetree")
                .repoURL("https://github.com/MSUSEL/msusel-codetree.git")
                .tag("v1.1.1")
                .create();

        Workflow w = Workflow.builder()
                .phase(Phase.builder()
                    .name("Retrieve Project")
                    .command(GitCommand.builder()
                        .repoDir("/home/git/test")
                        .project(project)
                        .username(p.getProperty("github.username"))
                        .password(p.getProperty("github.password"))
                        .create())
                    .create())
                .phase(Phase.builder()
                    .name("Build Project")
                    .command(MavenCommand.builder()
                        .create())
                    .create())
                .phase(Phase.builder()
                    .name("Pattern Detection")
                    .command(Pattern4Command.builder()
                        .projectName(project.getRepoName())
                        .reportFile("msusel-patterns/data/pattern4_results.xml")
                        .toolHome(p.getProperty("pattern4.toolhome"))
                        .binDir("target")
                        .create())
//                    .command(Pattern4Collector.builder().create())
                    .create())
                .phase(Phase.builder()
                    .name("Detect Issues")
                    .command(FindBugsCommand.builder()
                        .projectName(project.getRepoName())
                        .reportFile("findbugs.xml")
                        .toolHome(p.getProperty("findbugs.toolhome"))
                        .binDir("target")
                        .create())
                    .command(PMDCommand.builder()
                        .projectName(project.getRepoName())
                        .reportFile("pmd.xml")
                        .toolHome(p.getProperty("pmd.toolhome"))
                        .binDir("target")
                        .create())
//                    .command(FindBugsIssueCollector.builder().create())
//                    .command(PMDIssuesCollector.builder().create())
                    .create())
//                .phase(Phase.builder()
//                    .name("Quality Analysis")
//                    .command(MetricsCollector.builder().create())
//                    .command(QuamocoAnalyzer.builder().create())
//                    .create())
                .create();

        w.execute();
    }

}
