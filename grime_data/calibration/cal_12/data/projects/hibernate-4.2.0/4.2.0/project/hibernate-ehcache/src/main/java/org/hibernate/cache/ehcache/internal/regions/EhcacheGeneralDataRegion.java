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

import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.nonstop.NonStopCacheException;
import org.jboss.logging.Logger;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheMessageLogger;
import org.hibernate.cache.ehcache.internal.nonstop.HibernateNonstopCacheExceptionHandler;
import org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactory;
import org.hibernate.cache.spi.GeneralDataRegion;

/**
 * An Ehcache specific GeneralDataRegion.
 * <p/>
 * GeneralDataRegion instances are used for both the timestamps and query caches.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
abstract class EhcacheGeneralDataRegion extends EhcacheDataRegion implements GeneralDataRegion {

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            EhcacheGeneralDataRegion.class.getName()
    );

    /**
     * Creates an EhcacheGeneralDataRegion using the given Ehcache instance as a backing.
     */
    public EhcacheGeneralDataRegion(EhcacheAccessStrategyFactory accessStrategyFactory, Ehcache cache, Properties properties) {
        super( accessStrategyFactory, cache, properties );
    }

    /**
     * {@inheritDoc}
     */
    public Object get(Object key) throws CacheException {
        try {
            LOG.debugf( "key: %s", key );
            if ( key == null ) {
                return null;
            }
            else {
                Element element = cache.get( key );
                if ( element == null ) {
                    LOG.debugf( "Element for key %s is null", key );
                    return null;
                }
                else {
                    return element.getObjectValue();
                }
            }
        }
        catch ( net.sf.ehcache.CacheException e ) {
            if ( e instanceof NonStopCacheException ) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException( (NonStopCacheException) e );
                return null;
            }
            else {
                throw new CacheException( e );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object key, Object value) throws CacheException {
        LOG.debugf( "key: %s value: %s", key, value );
        try {
            Element element = new Element( key, value );
            cache.put( element );
        }
        catch ( IllegalArgumentException e ) {
            throw new CacheException( e );
        }
        catch ( IllegalStateException e ) {
            throw new CacheException( e );
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
    public void evict(Object key) throws CacheException {
        try {
            cache.remove( key );
        }
        catch ( ClassCastException e ) {
            throw new CacheException( e );
        }
        catch ( IllegalStateException e ) {
            throw new CacheException( e );
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
    public void evictAll() throws CacheException {
        try {
            cache.removeAll();
        }
        catch ( IllegalStateException e ) {
            throw new CacheException( e );
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
}
