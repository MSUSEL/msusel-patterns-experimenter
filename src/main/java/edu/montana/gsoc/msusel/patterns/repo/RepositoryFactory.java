package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public abstract class RepositoryFactory {

    /**
     * @param cred
     * @param config
     * @return
     */
    public abstract Repository connect(Credentials cred, RepositoryConfiguration config);

}
