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
package org.hibernate.testing.cache;

import java.util.Properties;

import org.jboss.logging.Logger;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.hibernate.internal.CoreMessageLogger;

/**
 * @author Strong Liu
 */
public class CachingRegionFactory implements RegionFactory {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class, CachingRegionFactory.class.getName()
	);
	public static String DEFAULT_ACCESSTYPE = "DefaultAccessType";
	private Settings settings;
	private Properties properties;
	public CachingRegionFactory() {
		LOG.warn( "CachingRegionFactory should be only used for testing." );
	}

	public CachingRegionFactory(Properties properties) {
		//add here to avoid run into catch
		LOG.warn( "CachingRegionFactory should be only used for testing." );
		this.properties=properties; 
	}

	@Override
	public void start(Settings settings, Properties properties) throws CacheException {
		this.settings=settings;
		this.properties=properties; 
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean isMinimalPutsEnabledByDefault() {
		return false;
	}

	@Override
	public AccessType getDefaultAccessType() {
		if (properties != null && properties.get(DEFAULT_ACCESSTYPE) != null) {
			return AccessType.fromExternalName(properties.getProperty(DEFAULT_ACCESSTYPE));
		}
		return AccessType.READ_WRITE;
	}

	@Override
	public long nextTimestamp() {
		return Timestamper.next();
	}

	@Override
	public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return new EntityRegionImpl( regionName, metadata, settings );
	}
	
	@Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata)
            throws CacheException {
        return new NaturalIdRegionImpl( regionName, metadata, settings );
    }

    @Override
	public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata)
			throws CacheException {
		return new CollectionRegionImpl( regionName, metadata, settings );
	}

	@Override
	public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
		return new QueryResultsRegionImpl( regionName );
	}

	@Override
	public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
		return new TimestampsRegionImpl( regionName );
	}

	private static class QueryResultsRegionImpl extends BaseGeneralDataRegion implements QueryResultsRegion {
		QueryResultsRegionImpl(String name) {
			super( name );
		}
	}

	private static class TimestampsRegionImpl extends BaseGeneralDataRegion implements TimestampsRegion {
		TimestampsRegionImpl(String name) {
			super( name );
		}
	}
}
