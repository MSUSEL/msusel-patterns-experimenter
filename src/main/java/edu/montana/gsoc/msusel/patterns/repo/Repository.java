package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac
 */
public abstract class Repository {

    /**
     * 
     */
    protected Credentials credentials;
    /**
     * 
     */
    protected RepositoryConfiguration config;

    /**
     * @param credentials
     * @param config
     */
    public Repository(final Credentials credentials, final RepositoryConfiguration config)
    {
        this.credentials = credentials;
        this.config = config;
    }

    /**
     * 
     */
    public abstract void add();

    /**
     * 
     */
    public abstract void branch();

    /**
     * 
     */
    public abstract void cat();

    /**
     * 
     */
    public abstract void checkout();

    /**
     * 
     */
    public abstract void cloneRepo();

    /**
     * 
     */
    public abstract void commit();

    /**
     * 
     */
    public abstract void diff();

    /**
     * 
     */
    public abstract void init();

    /**
     * 
     */
    public abstract void log();

    /**
     * 
     */
    public abstract void merge();

    /**
     * 
     */
    public abstract void pull();

    /**
     * 
     */
    public abstract void push();

    /**
     * 
     */
    public abstract void remove();

    /**
     * 
     */
    public abstract void revert();

    /**
     * @param config
     */
    public void setConfiguration(final RepositoryConfiguration config)
    {
        this.config = config;
    }

    /**
     * @param credentials
     */
    public void setCredentials(final Credentials credentials)
    {
        this.credentials = credentials;
    }

    /**
     * 
     */
    public abstract void status();

    /**
     * 
     */
    public abstract void tag();

    /**
     * 
     */
    public abstract void update();
}
