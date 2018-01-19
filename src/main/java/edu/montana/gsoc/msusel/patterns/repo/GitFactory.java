package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public class GitFactory extends RepositoryFactory {

    /**
     * 
     */
    private static GitFactory instance;

    /**
     * @return
     */
    public static GitFactory getInstance()
    {
        if (GitFactory.instance == null)
        {
            GitFactory.instance = new GitFactory();
        }

        return GitFactory.instance;
    }

    /**
     * 
     */
    private GitFactory()
    {
    }

    /*
     * (non-Javadoc)
     * @see
     * net.siliconcode.truerefactor.repository.RepositoryFactory#connect(net
     * .siliconcode.truerefactor.repository.Credentials,
     * net.siliconcode.truerefactor.repository.RepositoryConfiguration)
     */
    @Override
    public Repository connect(final Credentials cred, final RepositoryConfiguration config)
    {
        // TODO add implementation and return statement
        return null;
    }

}
