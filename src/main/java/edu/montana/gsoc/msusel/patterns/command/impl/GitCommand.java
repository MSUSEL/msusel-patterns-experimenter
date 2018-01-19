package edu.montana.gsoc.msusel.patterns.command.impl;

import edu.montana.gsoc.msusel.patterns.datamodel.Project;
import edu.montana.gsoc.msusel.patterns.command.RepositoryCommand;
import lombok.Builder;
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

@Builder(buildMethodName = "create")
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

    @Override
    public void execute() {
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
