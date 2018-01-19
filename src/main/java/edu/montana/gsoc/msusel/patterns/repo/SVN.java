package edu.montana.gsoc.msusel.patterns.repo;

/**
 * @author Isaac Griffith
 */
public class SVN extends Repository {

    /**
     * @param credentials
     * @param config
     */
    public SVN(final Credentials credentials, final RepositoryConfiguration config)
    {
        super(credentials, config);
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#add()
     */
    @Override
    public void add()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @param files
     */
    public void add(final String... files)
    {
        // TODO add implementation
    }

    /**
     * 
     */
    public void blame()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#branch()
     */
    @Override
    public void branch()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#cat()
     */
    @Override
    public void cat()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#checkout()
     */
    @Override
    public void checkout()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @param repository
     * @param location
     */
    public void checkout(final String repository, final String location)
    {
        // TODO add implementation
    }

    /**
     * 
     */
    public void cleanup()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#cloneRepo()
     */
    @Override
    public void cloneRepo()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#commit()
     */
    @Override
    public void commit()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @param message
     * @param location
     */
    public void commit(final String message, final String location)
    {
        // TODO add implementation
    }

    /**
     * @param source
     * @param dest
     * @param message
     */
    public void copy(final String source, final String dest, final String message)
    {
        // TODO add implementation
    }

    /**
     * @param files
     */
    public void delete(final String... files)
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#diff()
     */
    @Override
    public void diff()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#init()
     */
    @Override
    public void init()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @param location
     */
    public void list(final String location)
    {
        // TODO add implementation
    }

    /**
     * 
     */
    public void lock()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#log()
     */
    @Override
    public void log()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#merge()
     */
    @Override
    public void merge()
    {
        // TODO add implementation
    }

    /**
     * 
     */
    public void move()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#pull()
     */
    @Override
    public void pull()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#push()
     */
    @Override
    public void push()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#remove()
     */
    @Override
    public void remove()
    {
        // TODO Auto-generated method stub

    }

    /**
     * 
     */
    public void resolve()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#revert()
     */
    @Override
    public void revert()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#status()
     */
    @Override
    public void status()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#tag()
     */
    @Override
    public void tag()
    {
        // TODO Auto-generated method stub

    }

    /**
     * 
     */
    public void unlock()
    {
        // TODO add implementation
    }

    /*
     * (non-Javadoc)
     * @see net.siliconcode.truerefactor.repository.Repository#update()
     */
    @Override
    public void update()
    {
        // TODO add implementation
    }

}
