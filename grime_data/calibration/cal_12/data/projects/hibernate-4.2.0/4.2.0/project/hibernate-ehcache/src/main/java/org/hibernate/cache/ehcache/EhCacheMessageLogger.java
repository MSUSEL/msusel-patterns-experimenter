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

import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

import org.hibernate.internal.CoreMessageLogger;

import static org.jboss.logging.Logger.Level.WARN;

/**
 * The jboss-logging {@link MessageLogger} for the hibernate-ehcache module.  It reserves message ids ranging from
 * 20001 to 25000 inclusively.
 * <p/>
 * New messages must be added after the last message defined to ensure message codes are unique.
 */
@MessageLogger( projectCode = "HHH" )
public interface EhCacheMessageLogger extends CoreMessageLogger {

    @LogMessage( level = WARN )
    @Message( value = "Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() between repeated calls to "
                      + "buildSessionFactory. Using previously created EhCacheProvider. If this behaviour is required, consider "
                      + "using net.sf.ehcache.hibernate.SingletonEhCacheProvider.", id = 20001 )
    void attemptToRestartAlreadyStartedEhCacheProvider();

    @LogMessage( level = WARN )
    @Message( value = "Could not find configuration [%s]; using defaults.", id = 20002 )
    void unableToFindConfiguration( String name );

    @LogMessage( level = WARN )
    @Message( value = "Could not find a specific ehcache configuration for cache named [%s]; using defaults.", id = 20003 )
    void unableToFindEhCacheConfiguration( String name );

    @LogMessage( level = WARN )
    @Message( value = "A configurationResourceName was set to %s but the resource could not be loaded from the classpath. Ehcache will configure itself using defaults.", id = 20004 )
    void unableToLoadConfiguration( String configurationResourceName );

    @LogMessage( level = WARN )
    @Message( value = "The default cache value mode for this Ehcache configuration is \"identity\". This is incompatible with clustered "
								+ "Hibernate caching - the value mode has therefore been switched to \"serialization\"", id = 20005 )
    void incompatibleCacheValueMode( );

    @LogMessage( level = WARN )
    @Message( value = "The value mode for the cache[%s] is \"identity\". This is incompatible with clustered Hibernate caching - "
									+ "the value mode has therefore been switched to \"serialization\"", id = 20006 )
    void incompatibleCacheValueModePerCache( String cacheName );
    @LogMessage( level = WARN )
    @Message( value = "read-only cache configured for mutable entity [%s]", id = 20007 )
    void readOnlyCacheConfiguredForMutableEntity( String entityName );

    @LogMessage( level = WARN )
    @Message( value = "Cache[%s] Key[%s] Lockable[%s]\n"
                        + "A soft-locked cache entry was expired by the underlying Ehcache. "
                        + "If this happens regularly you should consider increasing the cache timeouts and/or capacity limits", id = 20008 )
    void softLockedCacheExpired( String regionName, Object key, String lock);


}
