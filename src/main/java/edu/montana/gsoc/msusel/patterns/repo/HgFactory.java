package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public class HgFactory extends RepositoryFactory {

    /**
     * 
     */
    private static HgFactory instance;

    /**
     * @return
     */
    public static HgFactory getInstance()
    {
        if (HgFactory.instance == null)
        {
            HgFactory.instance = new HgFactory();
        }

        return HgFactory.instance;
    }

    /**
     * 
     */
    private HgFactory()
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
