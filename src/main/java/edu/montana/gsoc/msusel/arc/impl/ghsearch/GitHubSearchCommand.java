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
package edu.montana.gsoc.msusel.arc.impl.ghsearch;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.RepositoryCommand;
import edu.montana.gsoc.msusel.arc.datamodel.Project;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTag;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedSearchIterable;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GitHubSearchCommand extends RepositoryCommand {

    private static final int MIN_SIZE = 5000;
    private static final int MAX_SIZE = 1000000;
    private static final int MIN_TAGS = 10;
    private static final int MIN_STARS = 1000;
    private GitHub github;

    public GitHubSearchCommand() {
        super("GitHub Search");
    }

    @Override
    public void execute(ArcContext context) {

    }

    public void getAuth() {
        try {
            github = GitHub.connect("isaacgriffith", "cf2eb9bbbe26fa08cc3b35be251f71c1a23d4817");
        } catch (IOException e) {

        }
    }

    public List<Project> findProjects() {
        List<Project> projects = Lists.newCopyOnWriteArrayList();

        if (github != null) {
            PagedSearchIterable<GHRepository> repos = github.searchRepositories()
                    .size(">" + MIN_SIZE + ", <" + MAX_SIZE)
                    .language("java")
                    .stars(">" + MIN_STARS)
                    .list()
                    .withPageSize(100);

            repos.forEach(repo -> {
                if (repo.getSize() > MIN_SIZE && repo.getSize() < MAX_SIZE) {
                    try {
                        List<GHTag> tags = repo.listTags().withPageSize(10).asList();
                        if (tags.size() >= MIN_TAGS) {
                            Project p = Project.builder()
                                    .repoName(repo.getName())
                                    .repoURL(repo.getHtmlUrl().toString())
                                    .size(repo.getSize())
                                    .create();
                            System.out.println(repo.getName() + ": " + tags.size() + " tags");

                            tags.forEach(tag -> {
                                //p.addTag(tag.getName());
                            });
                            projects.add(p);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("Repos Found: " + projects.size());
        }

        return projects;
    }

    public static void main(String args[]) {
        GitHubSearchCommand ghs = new GitHubSearchCommand();
        ghs.getAuth();

        List<Project> projects = ghs.findProjects();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(projects);

        Path p = Paths.get("java_projects.json");
        try {
            Files.deleteIfExists(p);
            Files.createFile(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(p, StandardCharsets.UTF_8))) {
            pw.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
