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
package org.hibernate.cache.ehcache.internal.strategy;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.internal.regions.EhcacheEntityRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

/**
 * JTA EntityRegionAccessStrategy.
 *
 * @author Chris Dennis
 * @author Ludovic Orban
 * @author Alex Snaps
 */
public class TransactionalEhcacheEntityRegionAccessStrategy extends AbstractEhcacheAccessStrategy<EhcacheEntityRegion>
		implements EntityRegionAccessStrategy {

	private final Ehcache ehcache;

	/**
	 * Construct a new entity region access strategy.
	 *
	 * @param region the Hibernate region.
	 * @param ehcache the cache.
	 * @param settings the Hibernate settings.
	 */
	public TransactionalEhcacheEntityRegionAccessStrategy(EhcacheEntityRegion region, Ehcache ehcache, Settings settings) {
		super( region, settings );
		this.ehcache = ehcache;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean afterInsert(Object key, Object value, Object version) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(Object key, long txTimestamp) throws CacheException {
		try {
			Element element = ehcache.get( key );
			return element == null ? null : element.getObjectValue();
		}
		catch ( net.sf.ehcache.CacheException e ) {
			throw new CacheException( e );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public EntityRegion getRegion() {
		return region;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean insert(Object key, Object value, Object version)
			throws CacheException {
		//OptimisticCache? versioning?
		try {
			ehcache.put( new Element( key, value ) );
			return true;
		}
		catch ( net.sf.ehcache.CacheException e ) {
			throw new CacheException( e );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SoftLock lockItem(Object key, Object version) throws CacheException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean putFromLoad(Object key, Object value, long txTimestamp,
							   Object version, boolean minimalPutOverride) throws CacheException {
		try {
			if ( minimalPutOverride && ehcache.get( key ) != null ) {
				return false;
			}
			//OptimisticCache? versioning?
			ehcache.put( new Element( key, value ) );
			return true;
		}
		catch ( net.sf.ehcache.CacheException e ) {
			throw new CacheException( e );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Object key) throws CacheException {
		try {
			ehcache.remove( key );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			throw new CacheException( e );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
		// no-op
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean update(Object key, Object value, Object currentVersion,
						  Object previousVersion) throws CacheException {
		try {
			ehcache.put( new Element( key, value ) );
			return true;
		}
		catch ( net.sf.ehcache.CacheException e ) {
			throw new CacheException( e );
		}
	}
}
