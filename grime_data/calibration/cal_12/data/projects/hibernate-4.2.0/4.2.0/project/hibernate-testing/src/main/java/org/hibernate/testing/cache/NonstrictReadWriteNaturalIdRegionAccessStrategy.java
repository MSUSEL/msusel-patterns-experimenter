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

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;

/**
 * @author Eric Dalquist
 */
class NonstrictReadWriteNaturalIdRegionAccessStrategy extends BaseNaturalIdRegionAccessStrategy {
	NonstrictReadWriteNaturalIdRegionAccessStrategy(NaturalIdRegionImpl region) {
		super( region );
	}
	@Override
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
		evict( key );
	}

	@Override
	public void remove(Object key) throws CacheException {
		evict( key );
	}
	
	/**
	 * Returns <code>false</code> since this is an asynchronous cache access strategy.
	 * @see org.hibernate.cache.ehcache.internal.strategy.NonStrictReadWriteEhcacheNaturalIdRegionAccessStrategy 
	 */
	@Override
	public boolean insert(Object key, Object value ) throws CacheException {
		return false;
	}

	/**
	 * Returns <code>false</code> since this is a non-strict read/write cache access strategy
	 * @see org.hibernate.cache.ehcache.internal.strategy.NonStrictReadWriteEhcacheNaturalIdRegionAccessStrategy 
	 */
	@Override
	public boolean afterInsert(Object key, Object value ) throws CacheException {
		return false;
	}
	
	/**
	 * Removes the entry since this is a non-strict read/write cache strategy.
	 * @see org.hibernate.cache.ehcache.internal.strategy.NonStrictReadWriteEhcacheNaturalIdRegionAccessStrategy 
	 */
	public boolean update(Object key, Object value ) throws CacheException {
		remove( key );
		return false;
	}
}
