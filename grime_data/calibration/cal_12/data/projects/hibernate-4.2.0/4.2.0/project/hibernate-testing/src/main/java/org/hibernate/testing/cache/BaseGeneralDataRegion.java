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
import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.internal.CoreMessageLogger;

/**
 * @author Strong Liu
 */
class BaseGeneralDataRegion extends BaseRegion implements GeneralDataRegion {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class, BaseGeneralDataRegion.class.getName()
	);

	BaseGeneralDataRegion(String name) {
		super( name );
	}

	@Override
	public Object get(Object key) throws CacheException {
		LOG.debugf( "Cache[%s] lookup : key[%s]",getName(), key );
		if ( key == null ) {
			return null;
		}
		Object result = cache.get( key );
		if ( result != null ) {
			LOG.debugf( "Cache[%s] hit: %s",getName(), key );
		}
		return result;
	}

	@Override
	public void put(Object key, Object value) throws CacheException {
		LOG.debugf( "Caching[%s] : [%s] -> [%s]",getName(), key, value );
		if ( key == null || value == null ) {
			LOG.debug( "Key or Value is null" );
			return;
		}
		cache.put( key, value );
	}

	@Override
	public void evict(Object key) throws CacheException {
		LOG.debugf( "Evicting[%s]: %s",getName(), key );
		if ( key == null ) {
			LOG.debug( "Key is null" );
			return;
		}
		cache.remove( key );
	}

	@Override
	public void evictAll() throws CacheException {
		LOG.debugf( "evict cache[%s]", getName() );
		cache.clear();
	}
}
