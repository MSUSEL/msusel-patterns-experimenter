/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.hibernate.cache.ehcache.internal.regions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.nonstop.NonStopCacheException;
import net.sf.ehcache.util.Timestamper;
import org.jboss.logging.Logger;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheMessageLogger;
import org.hibernate.cache.ehcache.internal.nonstop.HibernateNonstopCacheExceptionHandler;
import org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactory;
import org.hibernate.cache.spi.Region;

/**
 * An Ehcache specific data region implementation.
 * <p/>
 * This class is the ultimate superclass for all Ehcache Hibernate cache regions.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public abstract class EhcacheDataRegion implements Region {

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            EhcacheDataRegion.class.getName()
    );
    private static final String CACHE_LOCK_TIMEOUT_PROPERTY = "net.sf.ehcache.hibernate.cache_lock_timeout";
    private static final int DEFAULT_CACHE_LOCK_TIMEOUT = 60000;

    /**
     * Ehcache instance backing this Hibernate data region.
     */
    protected final Ehcache cache;

    /**
     * The {@link EhcacheAccessStrategyFactory} used for creating various access strategies
     */
    protected final EhcacheAccessStrategyFactory accessStrategyFactory;

    private final int cacheLockTimeout;


    /**
     * Create a Hibernate data region backed by the given Ehcache instance.
     */
    EhcacheDataRegion(EhcacheAccessStrategyFactory accessStrategyFactory, Ehcache cache, Properties properties) {
        this.accessStrategyFactory = accessStrategyFactory;
        this.cache = cache;
        String timeout = properties.getProperty(
                CACHE_LOCK_TIMEOUT_PROPERTY,
                Integer.toString( DEFAULT_CACHE_LOCK_TIMEOUT )
        );
        this.cacheLockTimeout = Timestamper.ONE_MS * Integer.decode( timeout );
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return cache.getName();
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() throws CacheException {
        try {
            cache.getCacheManager().removeCache( cache.getName() );
        }
        catch ( IllegalStateException e ) {
            //When Spring and Hibernate are both involved this will happen in normal shutdown operation.
            //Do not throw an exception, simply log this one.
            LOG.debug( "This can happen if multiple frameworks both try to shutdown ehcache", e );
        }
        catch ( net.sf.ehcache.CacheException e ) {
            if ( e instanceof NonStopCacheException ) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException( (NonStopCacheException) e );
            }
            else {
                throw new CacheException( e );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getSizeInMemory() {
        try {
            return cache.calculateInMemorySize();
        }
        catch ( Throwable t ) {
            if ( t instanceof NonStopCacheException ) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException( (NonStopCacheException) t );
            }
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getElementCountInMemory() {
        try {
            return cache.getMemoryStoreSize();
        }
        catch ( net.sf.ehcache.CacheException ce ) {
            if ( ce instanceof NonStopCacheException ) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException( (NonStopCacheException) ce );
                return -1;
            }
            else {
                throw new CacheException( ce );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getElementCountOnDisk() {
        try {
            return cache.getDiskStoreSize();
        }
        catch ( net.sf.ehcache.CacheException ce ) {
            if ( ce instanceof NonStopCacheException ) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException( (NonStopCacheException) ce );
                return -1;
            }
            else {
                throw new CacheException( ce );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map toMap() {
        try {
            Map<Object, Object> result = new HashMap<Object, Object>();
            for ( Object key : cache.getKeys() ) {
                result.put( key, cache.get( key ).getObjectValue() );
            }
            return result;
        }
        catch ( Exception e ) {
            if ( e instanceof NonStopCacheException ) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException( (NonStopCacheException) e );
                return Collections.emptyMap();
            }
            else {
                throw new CacheException( e );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public long nextTimestamp() {
        return Timestamper.next();
    }

    /**
     * {@inheritDoc}
     */
    public int getTimeout() {
        return cacheLockTimeout;
    }

    /**
     * Return the Ehcache instance backing this Hibernate data region.
     */
    public Ehcache getEhcache() {
        return cache;
    }

    /**
     * Returns <code>true</code> if this region contains data for the given key.
     * <p/>
     * This is a Hibernate 3.5 method.
     */
    public boolean contains(Object key) {
        return cache.isKeyInCache( key );
    }
}
