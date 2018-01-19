package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public class RepositoryConfiguration {

    /**
     * 
     */
    private final String url;

    /**
     * @param url
     */
    public RepositoryConfiguration(final java.lang.String url)
    {
        this.url = url;
    }

    /**
     * @return
     */
    public String getURL()
    {
        return url;
    }
}
