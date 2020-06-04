/**
 * The MIT License (MIT)
 *
 * ISUESE Repo Research Tools
 * Copyright (c) 2015-2019 Idaho State University, Informatics and
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
/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package edu.montana.gsoc.msusel.arc.impl.ghsearch

import com.google.common.collect.Lists
import com.google.common.flogger.StackSize
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.SCM
import edu.isu.isuese.datamodel.SCMType
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.RepositoryCommand
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHTag
import org.kohsuke.github.GitHub
import org.kohsuke.github.PagedSearchIterable

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class GitHubSearchCommand extends RepositoryCommand {

    private GitHub github
    private String user
    private String token
    private ArcContext context

    GitHubSearchCommand() {
        super(GitHubSearchConstants.GHSEARCH_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        context.logger().atInfo().log("Searching GitHub for projects")

        authenticate()

        findProjects()

        context.logger().atInfo().log("GitHub Search Complete")
    }

    @Override
    String getToolName() {
        return GitHubSearchConstants.GHSEARCH_CMD_NAME
    }

    void authenticate() {
        context.logger().atInfo().log("Authenticating with GitHub")
        try {
            github = GitHub.connect(context.getArcProperty(GitHubSearchProperties.GIT_HUB_USER),
                    context.getArcProperty(GitHubSearchProperties.GIT_HUB_TOKEN))
        } catch (IOException e) {
            context.logger().atSevere().withCause(e).withStackTrace(StackSize.MEDIUM).log(e.getMessage())
        }
        context.logger().atInfo().log("Authenticated to GitHub")
    }

    List<System> findProjects() {
        context.logger().atInfo().log("Searching...")
        int maxProj = Integer.parseInt(context.getArcProperty(GitHubSearchProperties.SEARCH_MAX_PROJ))
        int minSize = Integer.parseInt(context.getArcProperty(GitHubSearchProperties.SEARCH_MIN_SIZE))
        int maxSize = Integer.parseInt(context.getArcProperty(GitHubSearchProperties.SEARCH_MAX_SIZE))
        int minStars = Integer.parseInt(context.getArcProperty(GitHubSearchProperties.SEARCH_MIN_STARS))
        int minTags = Integer.parseInt(context.getArcProperty(GitHubSearchProperties.SEARCH_MIN_TAGS))

        final int[] numProj = {0}

        List<System> systems = Lists.newCopyOnWriteArrayList()

        if (github != null) {
            PagedSearchIterable<GHRepository> repos = github.searchRepositories()
                    .size(">" + minSize)// + ", <" + maxSize)
                    .language("java")
                    .stars(">" + minStars)
                    .list()
                    .withPageSize(100)

            for (GHRepository repo : repos) {
                if (repo.getSize() > minSize && repo.getSize() < maxSize) {
                    try {
                        List<GHTag> tags = repo.listTags().withPageSize(10).asList()
                        if (tags.size() >= minTags) {
                            System sys = System.builder()
                                    .name(repo.getName())
                                    .key(repo.getName())
                                    .create()
                            sys.saveIt()
                            systems.add(sys)

                            tags.forEach(tag -> {
                                Project p = Project.builder()
                                        .name(repo.getName())
                                        .version(tag.getName())
                                        .projKey(repo.getName() + ":" + tag.getName())
                                        .create()
                                p.saveIt()
                                sys.addProject(p)
                                numProj[0] += 1

                                SCM scm = SCM.builder()
                                        .name(repo.getName())
                                        .url(repo.getHtmlUrl().toString())
                                        .type(SCMType.GIT)
                                        .tag(tag.getName())
                                        .create()
                                scm.saveIt()
                                p.addSCM(scm)
                            })
                        }
                        if (numProj[0] > maxProj)
                            break
                    } catch (IOException e) {
                        e.printStackTrace()
                    }
                }
            }

            context.logger().atInfo().log("Repos Found: %d", numProj[0])
        }

        return systems
    }
}
