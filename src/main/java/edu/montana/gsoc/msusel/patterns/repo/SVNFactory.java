package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public class SVNFactory extends RepositoryFactory {

    /**
     * 
     */
    private static SVNFactory instance;

    /**
     * @return
     */
    public static SVNFactory getInstance()
    {
        if (SVNFactory.instance == null)
        {
            SVNFactory.instance = new SVNFactory();
        }

        return SVNFactory.instance;
    }

    /**
     * 
     */
    private SVNFactory()
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
