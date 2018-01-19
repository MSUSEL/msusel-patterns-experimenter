package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public class Credentials {

    /**
     * 
     */
    protected String user;
    /**
     * 
     */
    protected String pass;
    /**
     * 
     */
    protected String key;

    /**
     * @param user
     * @param pass
     * @param key
     */
    public Credentials(final String user, final String pass, final String key)
    {
        this.user = user;
        this.pass = pass;
        this.key = key;
    }

    /**
     * @return
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @return
     */
    public String getPass()
    {
        return pass;
    }

    /**
     * @return
     */
    public String getUser()
    {
        return user;
    }

}
