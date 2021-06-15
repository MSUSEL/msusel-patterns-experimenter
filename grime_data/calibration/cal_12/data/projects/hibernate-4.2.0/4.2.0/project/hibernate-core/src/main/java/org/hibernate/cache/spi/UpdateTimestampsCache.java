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
package org.hibernate.cache.spi;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.cache.CacheException;
import org.hibernate.cfg.Settings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CoreMessageLogger;

/**
 * Tracks the timestamps of the most recent updates to particular tables. It is
 * important that the cache timeout of the underlying cache implementation be set
 * to a higher value than the timeouts of any of the query caches. In fact, we
 * recommend that the the underlying cache not be configured for expiry at all.
 * Note, in particular, that an LRU cache expiry policy is never appropriate.
 *
 * @author Gavin King
 * @author Mikheil Kapanadze
 */
public class UpdateTimestampsCache {

	public static final String REGION_NAME = UpdateTimestampsCache.class.getName();
	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, UpdateTimestampsCache.class.getName() );

	private final SessionFactoryImplementor factory;
	private final TimestampsRegion region;

	public UpdateTimestampsCache(Settings settings, Properties props, final SessionFactoryImplementor factory) throws HibernateException {
		this.factory = factory;
		final String prefix = settings.getCacheRegionPrefix();
		final String regionName = prefix == null ? REGION_NAME : prefix + '.' + REGION_NAME;

		LOG.startingUpdateTimestampsCache( regionName );
		this.region = settings.getRegionFactory().buildTimestampsRegion( regionName, props );
	}

    @SuppressWarnings({"UnusedDeclaration"})
    public UpdateTimestampsCache(Settings settings, Properties props) throws HibernateException {
        this( settings, props, null );
    }

	@SuppressWarnings({"UnnecessaryBoxing"})
	public void preinvalidate(Serializable[] spaces) throws CacheException {
		final boolean debug = LOG.isDebugEnabled();
		final boolean stats = factory != null && factory.getStatistics().isStatisticsEnabled();

		final Long ts = region.nextTimestamp() + region.getTimeout();

		for ( Serializable space : spaces ) {
			if ( debug ) {
				LOG.debugf( "Pre-invalidating space [%s], timestamp: %s", space, ts );
			}
			//put() has nowait semantics, is this really appropriate?
			//note that it needs to be async replication, never local or sync
			region.put( space, ts );
			if ( stats ) {
				factory.getStatisticsImplementor().updateTimestampsCachePut();
			}
		}
	}

	@SuppressWarnings({"UnnecessaryBoxing"})
	public void invalidate(Serializable[] spaces) throws CacheException {
		 final boolean debug = LOG.isDebugEnabled();
		 final boolean stats = factory != null && factory.getStatistics().isStatisticsEnabled();

		 final Long ts = region.nextTimestamp();

		for (Serializable space : spaces) {
			if ( debug ) {
				LOG.debugf( "Invalidating space [%s], timestamp: %s", space, ts );
			}
			//put() has nowait semantics, is this really appropriate?
			//note that it needs to be async replication, never local or sync
			region.put( space, ts );
			if ( stats ) {
				factory.getStatisticsImplementor().updateTimestampsCachePut();
			}
		}
	}

	@SuppressWarnings({"unchecked", "UnnecessaryUnboxing"})
	public boolean isUpToDate(Set spaces, Long timestamp) throws HibernateException {
		final boolean debug = LOG.isDebugEnabled();
		final boolean stats = factory != null && factory.getStatistics().isStatisticsEnabled();

		for ( Serializable space : (Set<Serializable>) spaces ) {
			Long lastUpdate = (Long) region.get( space );
			if ( lastUpdate == null ) {
				if ( stats ) {
					factory.getStatisticsImplementor().updateTimestampsCacheMiss();
				}
				//the last update timestamp was lost from the cache
				//(or there were no updates since startup!)
				//updateTimestamps.put( space, new Long( updateTimestamps.nextTimestamp() ) );
				//result = false; // safer
			}
			else {
				if ( debug ) {
					LOG.debugf(
							"[%s] last update timestamp: %s",
							space,
							lastUpdate + ", result set timestamp: " + timestamp
					);
				}
				if ( stats ) {
					factory.getStatisticsImplementor().updateTimestampsCacheHit();
				}
				if ( lastUpdate >= timestamp ) {
					return false;
				}
			}
		}
		return true;
	}

	public void clear() throws CacheException {
		region.evictAll();
	}

	public void destroy() {
		try {
			region.destroy();
		}
		catch (Exception e) {
			LOG.unableToDestroyUpdateTimestampsCache( region.getName(), e.getMessage() );
		}
	}

	public TimestampsRegion getRegion() {
		return region;
	}

	@Override
    public String toString() {
		return "UpdateTimestampsCache";
	}

}
