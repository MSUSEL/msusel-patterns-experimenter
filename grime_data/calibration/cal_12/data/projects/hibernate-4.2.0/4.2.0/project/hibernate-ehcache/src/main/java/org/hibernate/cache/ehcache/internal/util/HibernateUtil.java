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
package org.hibernate.cache.ehcache.internal.util;

import java.net.URL;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.NonstopConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration.ValueMode;
import net.sf.ehcache.config.TimeoutBehaviorConfiguration.TimeoutBehaviorType;
import org.jboss.logging.Logger;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheMessageLogger;


/**
 * @author Chris Dennis
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public final class HibernateUtil {

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            HibernateUtil.class.getName()
    );

    private HibernateUtil() {
    }

    /**
     * Create a cache manager configuration from the supplied url, correcting it for Hibernate compatibility.
     * <p/>
     * Currently correcting for Hibernate compatibility means simply switching any identity based value modes to serialization.
     */
    public static Configuration loadAndCorrectConfiguration(URL url) {
        Configuration config = ConfigurationFactory.parseConfiguration( url );
        if ( config.getDefaultCacheConfiguration().isTerracottaClustered() ) {
            if ( ValueMode.IDENTITY
                    .equals( config.getDefaultCacheConfiguration().getTerracottaConfiguration().getValueMode() ) ) {
                LOG.incompatibleCacheValueMode();
                config.getDefaultCacheConfiguration()
                        .getTerracottaConfiguration()
                        .setValueMode( ValueMode.SERIALIZATION.name() );
            }
            setupHibernateTimeoutBehavior(
                    config.getDefaultCacheConfiguration()
                            .getTerracottaConfiguration()
                            .getNonstopConfiguration()
            );
        }

        for ( CacheConfiguration cacheConfig : config.getCacheConfigurations().values() ) {
            if ( cacheConfig.isTerracottaClustered() ) {
                if ( ValueMode.IDENTITY.equals( cacheConfig.getTerracottaConfiguration().getValueMode() ) ) {
                    LOG.incompatibleCacheValueModePerCache( cacheConfig.getName() );
                    cacheConfig.getTerracottaConfiguration().setValueMode( ValueMode.SERIALIZATION.name() );
                }
                setupHibernateTimeoutBehavior( cacheConfig.getTerracottaConfiguration().getNonstopConfiguration() );
            }
        }
        return config;
    }

    private static void setupHibernateTimeoutBehavior(NonstopConfiguration nonstopConfig) {
        nonstopConfig.getTimeoutBehavior().setType( TimeoutBehaviorType.EXCEPTION.getTypeName() );
    }

    /**
     * Validates that the supplied Ehcache instance is valid for use as a Hibernate cache.
     */
    public static void validateEhcache(Ehcache cache) throws CacheException {
        CacheConfiguration cacheConfig = cache.getCacheConfiguration();

        if ( cacheConfig.isTerracottaClustered() ) {
            TerracottaConfiguration tcConfig = cacheConfig.getTerracottaConfiguration();
            switch ( tcConfig.getValueMode() ) {
                case IDENTITY:
                    throw new CacheException(
                            "The clustered Hibernate cache " + cache.getName() + " is using IDENTITY value mode.\n"
                                    + "Identity value mode cannot be used with Hibernate cache regions."
                    );
                case SERIALIZATION:
                default:
                    // this is the recommended valueMode
                    break;
            }
        }
    }
}