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

import org.hibernate.cache.CacheException;

/**
 * Contract for general-purpose cache regions.
 *
 * @author Steve Ebersole
 */
public interface GeneralDataRegion extends Region {

	/**
	 * Get an item from the cache.
	 *
	 * @param key The key of the item to be retrieved.
	 * @return the cached object or <tt>null</tt>
	 * @throws org.hibernate.cache.CacheException Indicates a problem accessing the item or region.
	 */
	public Object get(Object key) throws CacheException;

	/**
	 * Put an item into the cache.
	 *
	 * @param key The key under which to cache the item.
	 * @param value The item to cache.
	 * @throws CacheException Indicates a problem accessing the region.
	 */
	public void put(Object key, Object value) throws CacheException;

	/**
	 * Evict an item from the cache immediately (without regard for transaction
	 * isolation).
	 *
	 * @param key The key of the item to remove
	 * @throws CacheException Indicates a problem accessing the item or region.
	 */
	public void evict(Object key) throws CacheException;

	/**
	 * Evict all contents of this particular cache region (without regard for transaction
	 * isolation).
	 *
	 * @throws CacheException Indicates problem accessing the region.
	 */
	public void evictAll() throws CacheException;
}
