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
package edu.montana.gsoc.msusel.arc.app;

import com.google.common.collect.Sets;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.Tool;
import edu.montana.gsoc.msusel.arc.command.Workflow;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Set;

public class Experimenter {

    ArcContext context;

    public Experimenter() {
        this.context = new ArcContext();
    }

    public void initialize() {
        Properties p = ExperimenterPropLoader.loadProperties("msusel-patterns/settings/");
        context.setArcProperties(p);
    }

    public Workflow selectWorkflow() {
        return null;
    }

    public void initTools() {
        ArcContext context = new ArcContext();
        String toolsPkg = "edu.montana.gsoc.msusel.arc.impl";

        Reflections ref = new Reflections(toolsPkg);

        Set<Class<? extends Tool>> tools = Sets.newHashSet(ref.getSubTypesOf(Tool.class));
        Set<Class<? extends Tool>> remove = Sets.newHashSet();
        for (Class x : ref.getSubTypesOf(Tool.class)) {
            if (Modifier.isAbstract(x.getModifiers())) {
                remove.add(x);
            }
        }

        tools.removeAll(remove);

        for (Class<? extends Tool> t : tools) {
            try {
                Tool tool = t.getDeclaredConstructor().newInstance();
                tool.init(context);
                tool.getRepoProvider().registerRepos(context);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void execute() {

    }

    public static void main(String args[]) {
//        p.forEach((key, value) -> Project.out.printf("%s = %s\n", key, value));
//
//        Project project = Project.builder()
//                .repoName("msusel-codetree")
//                .repoURL("https://github.com/MSUSEL/msusel-codetree.git")
//                .tag("v1.1.1")
//                .create();
//
//        Workflow w = Workflow.builder()
//                .phase(Phase.builder()
//                    .name("Retrieve Project")
//                    .command(GitCommand.builder()
//                        .repoDir("/home/git/test")
//                        .project(project)
//                        .username(p.getProperty("github.username"))
//                        .password(p.getProperty("github.password"))
//                        .create())
//                    .create())
//                .phase(Phase.builder()
//                    .name("Build Project")
//                    .command(MavenCommand.builder()
//                        .create())
//                    .create())
//                .phase(Phase.builder()
//                    .name("Pattern Detection")
//                    .command(Pattern4Command.builder()
//                        .projectName(project.getRepoName())
//                        .reportFile("msusel-patterns/data/pattern4_results.xml")
//                        .toolHome(p.getProperty("pattern4.toolhome"))
//                        .binDir("target")
//                        .create())
////                    .command(Pattern4Collector.builder().create())
//                    .create())
//                .phase(Phase.builder()
//                    .name("Detect Issues")
//                    .command(FindBugsCommand.builder()
//                        .projectName(project.getRepoName())
//                        .reportFile("findbugs.xml")
//                        .toolHome(p.getProperty("findbugs.toolhome"))
//                        .binDir("target")
//                        .create())
//                    .command(PMDCommand.builder()
//                        .projectName(project.getRepoName())
//                        .reportFile("pmd.xml")
//                        .toolHome(p.getProperty("pmd.toolhome"))
//                        .binDir("target")
//                        .create())
////                    .command(FindBugsIssueCollector.builder().create())
////                    .command(PMDIssuesCollector.builder().create())
//                    .create())
////                .phase(Phase.builder()
////                    .name("Quality Analysis")
////                    .command(MetricsCollector.builder().create())
////                    .command(QuamocoAnalyzer.builder().create())
////                    .create())
//                .create();
//
//        w.execute();
    }
}