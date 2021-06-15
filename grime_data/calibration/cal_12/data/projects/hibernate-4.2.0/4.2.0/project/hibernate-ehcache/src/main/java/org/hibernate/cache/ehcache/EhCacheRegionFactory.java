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

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.jboss.logging.Logger;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.internal.util.HibernateUtil;
import org.hibernate.cfg.Settings;

/**
 * A non-singleton EhCacheRegionFactory implementation.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class EhCacheRegionFactory extends AbstractEhcacheRegionFactory {

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            EhCacheRegionFactory.class.getName()
    );


    public EhCacheRegionFactory() {
    }

    /**
     * Creates a non-singleton EhCacheRegionFactory
     */
    public EhCacheRegionFactory(Properties prop) {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void start(Settings settings, Properties properties) throws CacheException {
        this.settings = settings;
        if ( manager != null ) {
            LOG.attemptToRestartAlreadyStartedEhCacheProvider();
            return;
        }

        try {
            String configurationResourceName = null;
            if ( properties != null ) {
                configurationResourceName = (String) properties.get( NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME );
            }
            if ( configurationResourceName == null || configurationResourceName.length() == 0 ) {
                Configuration configuration = ConfigurationFactory.parseConfiguration();
                manager = new CacheManager( configuration );
            }
            else {
                URL url;
                try {
                    url = new URL( configurationResourceName );
                }
                catch ( MalformedURLException e ) {
                    url = loadResource( configurationResourceName );
                }
                Configuration configuration = HibernateUtil.loadAndCorrectConfiguration( url );
                manager = new CacheManager( configuration );
            }
            mbeanRegistrationHelper.registerMBean( manager, properties );
        }
        catch ( net.sf.ehcache.CacheException e ) {
            if ( e.getMessage().startsWith(
                    "Cannot parseConfiguration CacheManager. Attempt to create a new instance of " +
                            "CacheManager using the diskStorePath"
            ) ) {
                throw new CacheException(
                        "Attempt to restart an already started EhCacheRegionFactory. " +
                                "Use sessionFactory.close() between repeated calls to buildSessionFactory. " +
                                "Consider using SingletonEhCacheRegionFactory. Error from ehcache was: " + e.getMessage()
                );
            }
            else {
                throw new CacheException( e );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        try {
            if ( manager != null ) {
                mbeanRegistrationHelper.unregisterMBean();
                manager.shutdown();
                manager = null;
            }
        }
        catch ( net.sf.ehcache.CacheException e ) {
            throw new CacheException( e );
        }
    }

}
