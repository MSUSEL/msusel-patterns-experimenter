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
package org.hibernate.cache.ehcache.internal.regions;

import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.concurrent.CacheLockProvider;
import net.sf.ehcache.concurrent.LockType;
import net.sf.ehcache.concurrent.StripedReadWriteLockSync;
import net.sf.ehcache.constructs.nonstop.NonStopCacheException;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.internal.nonstop.HibernateNonstopCacheExceptionHandler;
import org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactory;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.hibernate.cfg.Settings;

/**
 * An Ehcache specific TransactionalDataRegion.
 * <p/>
 * This is the common superclass entity and collection regions.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class EhcacheTransactionalDataRegion extends EhcacheDataRegion implements TransactionalDataRegion {

	private static final int LOCAL_LOCK_PROVIDER_CONCURRENCY = 128;

	/**
	 * Hibernate settings associated with the persistence unit.
	 */
	protected final Settings settings;

	/**
	 * Metadata associated with the objects stored in the region.
	 */
	protected final CacheDataDescription metadata;

	private final CacheLockProvider lockProvider;

	/**
	 * Construct an transactional Hibernate cache region around the given Ehcache instance.
	 */
	EhcacheTransactionalDataRegion(EhcacheAccessStrategyFactory accessStrategyFactory, Ehcache cache, Settings settings,
								   CacheDataDescription metadata, Properties properties) {
		super( accessStrategyFactory, cache, properties );
		this.settings = settings;
		this.metadata = metadata;

		Object context = cache.getInternalContext();
		if ( context instanceof CacheLockProvider ) {
			this.lockProvider = (CacheLockProvider) context;
		}
		else {
			this.lockProvider = new StripedReadWriteLockSync( LOCAL_LOCK_PROVIDER_CONCURRENCY );
		}
	}

	/**
	 * Return the hibernate settings
	 *
	 * @return settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTransactionAware() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public CacheDataDescription getCacheDataDescription() {
		return metadata;
	}

	/**
	 * Get the value mapped to this key, or null if no value is mapped to this key.
	 */
	public final Object get(Object key) {
		try {
			Element element = cache.get( key );
			if ( element == null ) {
				return null;
			}
			else {
				return element.getObjectValue();
			}
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
				return null;
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Map the given value to the given key, replacing any existing mapping for this key
	 */
	public final void put(Object key, Object value) throws CacheException {
		try {
			Element element = new Element( key, value );
			cache.put( element );
		}
		catch ( IllegalArgumentException e ) {
			throw new CacheException( e );
		}
		catch ( IllegalStateException e ) {
			throw new CacheException( e );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Remove the mapping for this key (if any exists).
	 */
	public final void remove(Object key) throws CacheException {
		try {
			cache.remove( key );
		}
		catch ( ClassCastException e ) {
			throw new CacheException( e );
		}
		catch ( IllegalStateException e ) {
			throw new CacheException( e );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Remove all mapping from this cache region.
	 */
	public final void clear() throws CacheException {
		try {
			cache.removeAll();
		}
		catch ( IllegalStateException e ) {
			throw new CacheException( e );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Attempts to write lock the mapping for the given key.
	 */
	public final void writeLock(Object key) {
		try {
			lockProvider.getSyncForKey( key ).lock( LockType.WRITE );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Attempts to write unlock the mapping for the given key.
	 */
	public final void writeUnlock(Object key) {
		try {
			lockProvider.getSyncForKey( key ).unlock( LockType.WRITE );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Attempts to read lock the mapping for the given key.
	 */
	public final void readLock(Object key) {
		try {
			lockProvider.getSyncForKey( key ).lock( LockType.WRITE );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Attempts to read unlock the mapping for the given key.
	 */
	public final void readUnlock(Object key) {
		try {
			lockProvider.getSyncForKey( key ).unlock( LockType.WRITE );
		}
		catch ( net.sf.ehcache.CacheException e ) {
			if ( e instanceof NonStopCacheException ) {
				HibernateNonstopCacheExceptionHandler.getInstance()
						.handleNonstopCacheException( (NonStopCacheException) e );
			}
			else {
				throw new CacheException( e );
			}
		}
	}

	/**
	 * Returns <code>true</code> if the locks used by the locking methods of this region are the independent of the cache.
	 * <p/>
	 * Independent locks are not locked by the cache when the cache is accessed directly.  This means that for an independent lock
	 * lock holds taken through a region method will not block direct access to the cache via other means.
	 */
	public final boolean locksAreIndependentOfCache() {
		return lockProvider instanceof StripedReadWriteLockSync;
	}
}
