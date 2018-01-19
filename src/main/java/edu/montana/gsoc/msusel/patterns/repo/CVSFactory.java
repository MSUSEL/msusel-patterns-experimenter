package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public class CVSFactory extends RepositoryFactory {

    /**
     * 
     */
    private static CVSFactory instance;

    /**
     * @return
     */
    public static CVSFactory getInstance()
    {
        if (CVSFactory.instance == null)
        {
            CVSFactory.instance = new CVSFactory();
        }

        return CVSFactory.instance;
    }

    /**
     * 
     */
    private CVSFactory()
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
        return null;
        // TODO add implementation and return statement
    }
}
