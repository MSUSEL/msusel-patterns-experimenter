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
package org.hibernate.cache.ehcache;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import org.jboss.logging.Logger;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.internal.util.HibernateUtil;
import org.hibernate.cfg.Settings;

/**
 * A singleton EhCacheRegionFactory implementation.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Alex Snaps
 */
public class SingletonEhCacheRegionFactory extends AbstractEhcacheRegionFactory {

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            SingletonEhCacheRegionFactory.class.getName()
    );
    private static final AtomicInteger REFERENCE_COUNT = new AtomicInteger();

    /**
     * Returns a representation of the singleton EhCacheRegionFactory
     */
    public SingletonEhCacheRegionFactory(Properties prop) {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void start(Settings settings, Properties properties) throws CacheException {
        this.settings = settings;
        try {
            String configurationResourceName = null;
            if ( properties != null ) {
                configurationResourceName = (String) properties.get( NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME );
            }
            if ( configurationResourceName == null || configurationResourceName.length() == 0 ) {
                manager = CacheManager.create();
                REFERENCE_COUNT.incrementAndGet();
            }
            else {
                URL url;
                try {
                    url = new URL( configurationResourceName );
                }
                catch ( MalformedURLException e ) {
                    if ( !configurationResourceName.startsWith( "/" ) ) {
                        configurationResourceName = "/" + configurationResourceName;
                        LOG.debugf(
                                "prepending / to %s. It should be placed in the root of the classpath rather than in a package.",
                                configurationResourceName
                        );
                    }
                    url = loadResource( configurationResourceName );
                }
                Configuration configuration = HibernateUtil.loadAndCorrectConfiguration( url );
                manager = CacheManager.create( configuration );
                REFERENCE_COUNT.incrementAndGet();
            }
            mbeanRegistrationHelper.registerMBean( manager, properties );
        }
        catch ( net.sf.ehcache.CacheException e ) {
            throw new CacheException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        try {
            if ( manager != null ) {
                if ( REFERENCE_COUNT.decrementAndGet() == 0 ) {
                    manager.shutdown();
                }
                manager = null;
            }
        }
        catch ( net.sf.ehcache.CacheException e ) {
            throw new CacheException( e );
        }
    }

}
