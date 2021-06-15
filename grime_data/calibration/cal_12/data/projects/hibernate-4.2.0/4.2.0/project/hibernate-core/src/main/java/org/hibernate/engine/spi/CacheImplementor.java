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
package org.hibernate.engine.spi;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.cache.spi.QueryCache;
import org.hibernate.cache.spi.Region;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.UpdateTimestampsCache;
import org.hibernate.service.Service;

/**
 * Define internal contact of <tt>Cache API</tt>
 *
 * @author Strong Liu <stliu@hibernate.org>
 */
public interface CacheImplementor extends Service, Cache, Serializable {

	/**
	 * Close all cache regions.
	 */
	public void close();

	/**
	 * Get query cache by <tt>region name</tt> or create a new one if none exist.
	 * <p/>
	 * If the region name is null, then default query cache region will be returned.
	 *
	 * @param regionName Query cache region name.
	 * @return The {@code QueryCache} associated with the region name, or default query cache if the region name is <tt>null</tt>.
	 * @throws HibernateException {@code HibernateException} maybe thrown when the creation of new QueryCache instance.
	 */
	public QueryCache getQueryCache(String regionName) throws HibernateException;

	/**
	 * Get the default {@code QueryCache}.
	 */
	public QueryCache getQueryCache();

	/**
	 * Add {@code Region} to this Cache scope.
	 *
	 * @param name The region name.
	 * @param region The {@code Region} instance.
	 */
	public void addCacheRegion(String name, Region region);

	/**
	 * Get {@code UpdateTimestampsCache} instance managed by the {@code SessionFactory}.
	 */
	public UpdateTimestampsCache getUpdateTimestampsCache();

	/**
	 * Clean up the default {@code QueryCache}.
	 *
 	 * @throws HibernateException
	 */
	public void evictQueries() throws HibernateException;

	/**
	 * Get second level cache region by its name.
	 *
	 * @param regionName The region name.
	 * @return The second level cache region.
	 */
	public Region getSecondLevelCacheRegion(String regionName);

	/**
	 * Get natural id cache region by its name.
	 *
	 * @param regionName The region name.
	 * @return The natural id cache region.
	 */
	public Region getNaturalIdCacheRegion(String regionName);

	/**
	 * Get <tt>all</tt> cache regions, including query cache.
	 *
	 * @return The map contains all cache regions with region name as the key.
	 */
	public Map<String, Region> getAllSecondLevelCacheRegions();

	/**
	 *
	 * @return The {@code RegionFactory}
	 */
	public RegionFactory getRegionFactory();
}
