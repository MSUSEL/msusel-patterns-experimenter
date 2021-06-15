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
package org.hibernate.cache.ehcache.internal.nonstop;

import net.sf.ehcache.constructs.nonstop.NonStopCacheException;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

/**
 * Implementation of {@link NaturalIdRegionAccessStrategy} that handles {@link NonStopCacheException} using
 * {@link HibernateNonstopCacheExceptionHandler}
 *
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class NonstopAwareNaturalIdRegionAccessStrategy implements NaturalIdRegionAccessStrategy {

	private final NaturalIdRegionAccessStrategy actualStrategy;
	private final HibernateNonstopCacheExceptionHandler hibernateNonstopExceptionHandler;

	/**
	 * Constructor accepting the actual {@link NaturalIdRegionAccessStrategy} and the {@link HibernateNonstopCacheExceptionHandler}
	 *
	 * @param actualStrategy
	 * @param hibernateNonstopExceptionHandler
	 */
	public NonstopAwareNaturalIdRegionAccessStrategy(NaturalIdRegionAccessStrategy actualStrategy,
													  HibernateNonstopCacheExceptionHandler hibernateNonstopExceptionHandler) {
		this.actualStrategy = actualStrategy;
		this.hibernateNonstopExceptionHandler = hibernateNonstopExceptionHandler;
	}
	
	@Override
	public boolean insert(Object key, Object value) throws CacheException {
		try {
			return actualStrategy.insert( key, value );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return false;
		}
	}

	@Override
	public boolean afterInsert(Object key, Object value) throws CacheException {
		try {
			return actualStrategy.afterInsert( key, value );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return false;
		}
	}

	@Override
	public boolean update(Object key, Object value) throws CacheException {
		try {
			return actualStrategy.update( key, value );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return false;
		}
	}

	@Override
	public boolean afterUpdate(Object key, Object value, SoftLock lock) throws CacheException {
		try {
			return actualStrategy.afterUpdate( key, value, lock );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#getRegion()
	 */
	public NaturalIdRegion getRegion() {
		return actualStrategy.getRegion();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#evict(java.lang.Object)
	 */
	public void evict(Object key) throws CacheException {
		try {
			actualStrategy.evict( key );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#evictAll()
	 */
	public void evictAll() throws CacheException {
		try {
			actualStrategy.evictAll();
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#get(java.lang.Object, long)
	 */
	public Object get(Object key, long txTimestamp) throws CacheException {
		try {
			return actualStrategy.get( key, txTimestamp );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#lockItem(java.lang.Object, java.lang.Object)
	 */
	public SoftLock lockItem(Object key, Object version) throws CacheException {
		try {
			return actualStrategy.lockItem( key, version );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#lockRegion()
	 */
	public SoftLock lockRegion() throws CacheException {
		try {
			return actualStrategy.lockRegion();
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#putFromLoad(java.lang.Object, java.lang.Object, long, java.lang.Object,
	 *	  boolean)
	 */
	public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride)
			throws CacheException {
		try {
			return actualStrategy.putFromLoad( key, value, txTimestamp, version, minimalPutOverride );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#putFromLoad(java.lang.Object, java.lang.Object, long, java.lang.Object)
	 */
	public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
		try {
			return actualStrategy.putFromLoad( key, value, txTimestamp, version );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#remove(java.lang.Object)
	 */
	public void remove(Object key) throws CacheException {
		try {
			actualStrategy.remove( key );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#removeAll()
	 */
	public void removeAll() throws CacheException {
		try {
			actualStrategy.removeAll();
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#unlockItem(java.lang.Object, org.hibernate.cache.spi.access.SoftLock)
	 */
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
		try {
			actualStrategy.unlockItem( key, lock );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.cache.spi.access.EntityRegionAccessStrategy#unlockRegion(org.hibernate.cache.spi.access.SoftLock)
	 */
	public void unlockRegion(SoftLock lock) throws CacheException {
		try {
			actualStrategy.unlockRegion( lock );
		}
		catch ( NonStopCacheException nonStopCacheException ) {
			hibernateNonstopExceptionHandler.handleNonstopCacheException( nonStopCacheException );
		}
	}

}
