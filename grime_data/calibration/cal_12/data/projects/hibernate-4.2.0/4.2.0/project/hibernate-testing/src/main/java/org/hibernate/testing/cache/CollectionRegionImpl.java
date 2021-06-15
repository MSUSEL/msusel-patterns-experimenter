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

import org.jboss.logging.Logger;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.hibernate.internal.CoreMessageLogger;

/**
 * @author Strong Liu
 */
class CollectionRegionImpl extends BaseTransactionalDataRegion implements CollectionRegion {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class, CollectionRegionImpl.class.getName()
	);
	private final Settings settings;
	CollectionRegionImpl(String name, CacheDataDescription metadata, Settings settings) {
		super( name, metadata );
		this.settings=settings;
	}

	public Settings getSettings() {
		return settings;
	}

	@Override
	public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		switch ( accessType ) {
			case READ_ONLY:
				if ( getCacheDataDescription().isMutable() ) {
					LOG.warnf( "read-only cache configured for mutable collection [ %s ]", getName() );
				}
				return new ReadOnlyCollectionRegionAccessStrategy( this );
			case READ_WRITE:
				 return new ReadWriteCollectionRegionAccessStrategy( this );
			case NONSTRICT_READ_WRITE:
				return new NonstrictReadWriteCollectionRegionAccessStrategy( this );
			case TRANSACTIONAL:
				return new TransactionalCollectionRegionAccessStrategy( this );
//				throw new UnsupportedOperationException( "doesn't support this access strategy" );
			default:
				throw new IllegalArgumentException( "unrecognized access strategy type [" + accessType + "]" );
		}
	}


}
