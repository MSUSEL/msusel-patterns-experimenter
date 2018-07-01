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
package edu.montana.gsoc.msusel.arc.impl.git;

import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.RepositoryCommand;
import edu.montana.gsoc.msusel.arc.datamodel.Project;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class GitCommand extends RepositoryCommand {
    @Setter
    @Getter
    protected Project project;
    @Setter
    @Getter
    protected String repoDir;
    @Setter
    @Getter
    protected String username;
    @Setter
    @Getter
    protected String password;
    @Setter
    protected String tag;

    public GitCommand() {
        super("Git");
    }

    @Override
    public void execute(ArcContext context) {
        try {
            Path p = Paths.get(repoDir, ".git");
            Git git = null;
            if (!Files.exists(p))
                git = cloneRepository();
            else
                git = openRepository();

            if (tag != null && !tag.isEmpty()) {
                log.info("Checking out Tag");
                git.checkout().setName(tag).call();
            }

            closeRepository(git);
        } catch (GitAPIException | IOException e) {
            log.error(e.getMessage());
        }
    }

    private Git cloneRepository() throws GitAPIException {
        log.info("Cloning Repo");
        Git git = Git.cloneRepository()
                .setURI(project.getRepoURL())
                .setDirectory(new File(repoDir))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call();

        return git;
    }

    private Git openRepository() throws IOException {
        log.info("Opening Repo");
        return Git.open(new File(repoDir));
    }

    private void closeRepository(Git git) {
        log.info("Closing Repo");
        git.close();
    }
}
