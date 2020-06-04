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
package edu.montana.gsoc.msusel.arc.impl.git;

import edu.isu.isuese.datamodel.Project;
import edu.isu.isuese.datamodel.SCMType;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.RepositoryCommand;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
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
    protected ArcContext context;

    public GitCommand() {
        super("Git");
    }

    @Override
    public void execute(ArcContext context) {
        this.context = context;
        if (context != null) {
            this.project = context.getProject();
            this.repoDir = context.getProjectDirectory();
            this.username = context.getArcProperty(GitProperties.GIT_USERNAME);
            this.password = context.getArcProperty(GitProperties.GIT_PASSWORD);
        }

        try {
            Path p = Paths.get(repoDir, ".git");
            Git git = null;
            if (!Files.exists(p))
                git = cloneRepository();
            else
                git = openRepository();

            assert git != null;

            if (tag != null && !tag.isEmpty()) {
                assert context != null;
                context.logger().atInfo().log("Checking out Tag");
                git.checkout().setName(tag).call();
            }

            closeRepository(git);
        } catch (GitAPIException | IOException e) {
            assert context != null;
            context.logger().atSevere().withCause(e).log(e.getMessage());
        }
    }

    @Override
    public String getToolName() {
        return GitConstants.GIT_CMD_NAME;
    }

    private Git cloneRepository() throws GitAPIException {
        context.logger().atInfo().log("Cloning Repo");
        Git git = Git.cloneRepository()
                .setURI(project.getSCM(SCMType.GIT).getURL())
                .setDirectory(new File(repoDir))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call();

        return git;
    }

    private Git openRepository() throws IOException {
        context.logger().atInfo().log("Opening Repo");
        return Git.open(new File(repoDir));
    }

    private void closeRepository(Git git) {
        context.logger().atInfo().log("Closing Repo");
        git.close();
    }
}
