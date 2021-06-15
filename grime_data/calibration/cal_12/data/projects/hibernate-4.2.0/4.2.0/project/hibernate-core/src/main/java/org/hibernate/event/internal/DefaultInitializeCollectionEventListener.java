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
package org.hibernate.event.internal;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.cache.spi.CacheKey;
import org.hibernate.cache.spi.entry.CollectionCacheEntry;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.CollectionEntry;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.spi.InitializeCollectionEvent;
import org.hibernate.event.spi.InitializeCollectionEventListener;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.pretty.MessageHelper;
import org.jboss.logging.Logger;

/**
 * @author Gavin King
 */
public class DefaultInitializeCollectionEventListener implements InitializeCollectionEventListener {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, DefaultInitializeCollectionEventListener.class.getName() );

	/**
	 * called by a collection that wants to initialize itself
	 */
	public void onInitializeCollection(InitializeCollectionEvent event)
	throws HibernateException {

		PersistentCollection collection = event.getCollection();
		SessionImplementor source = event.getSession();

		CollectionEntry ce = source.getPersistenceContext().getCollectionEntry(collection);
		if (ce==null) throw new HibernateException("collection was evicted");
		if ( !collection.wasInitialized() ) {
			if ( LOG.isTraceEnabled() ) {
				LOG.tracev( "Initializing collection {0}",
						MessageHelper.collectionInfoString( ce.getLoadedPersister(), collection, ce.getLoadedKey(), source ) );
			}

			LOG.trace( "Checking second-level cache" );
			final boolean foundInCache = initializeCollectionFromCache(
					ce.getLoadedKey(),
					ce.getLoadedPersister(),
					collection,
					source
				);

			if ( foundInCache ) {
				LOG.trace( "Collection initialized from cache" );
			}
			else {
				LOG.trace( "Collection not cached" );
				ce.getLoadedPersister().initialize( ce.getLoadedKey(), source );
				LOG.trace( "Collection initialized" );

				if ( source.getFactory().getStatistics().isStatisticsEnabled() ) {
					source.getFactory().getStatisticsImplementor().fetchCollection(
							ce.getLoadedPersister().getRole()
						);
				}
			}
		}
	}

	/**
	 * Try to initialize a collection from the cache
	 *
	 * @param id The id of the collection of initialize
	 * @param persister The collection persister
	 * @param collection The collection to initialize
	 * @param source The originating session
	 * @return true if we were able to initialize the collection from the cache;
	 * false otherwise.
	 */
	private boolean initializeCollectionFromCache(
			Serializable id,
			CollectionPersister persister,
			PersistentCollection collection,
			SessionImplementor source) {

		if ( !source.getLoadQueryInfluencers().getEnabledFilters().isEmpty() && persister.isAffectedByEnabledFilters( source ) ) {
			LOG.trace( "Disregarding cached version (if any) of collection due to enabled filters" );
			return false;
		}

		final boolean useCache = persister.hasCache() &&
				source.getCacheMode().isGetEnabled();

        if (!useCache) return false;

        final SessionFactoryImplementor factory = source.getFactory();

        final CacheKey ck = source.generateCacheKey( id, persister.getKeyType(), persister.getRole() );
        Object ce = persister.getCacheAccessStrategy().get(ck, source.getTimestamp());

		if ( factory.getStatistics().isStatisticsEnabled() ) {
            if (ce == null) {
                factory.getStatisticsImplementor()
						.secondLevelCacheMiss( persister.getCacheAccessStrategy().getRegion().getName() );
            }
			else {
                factory.getStatisticsImplementor()
						.secondLevelCacheHit( persister.getCacheAccessStrategy().getRegion().getName() );
            }
		}

        if ( ce == null ) {
			return false;
		}

		CollectionCacheEntry cacheEntry = (CollectionCacheEntry)persister.getCacheEntryStructure().destructure(ce, factory);

		final PersistenceContext persistenceContext = source.getPersistenceContext();
        cacheEntry.assemble(collection, persister, persistenceContext.getCollectionOwner(id, persister));
        persistenceContext.getCollectionEntry(collection).postInitialize(collection);
        // addInitializedCollection(collection, persister, id);
        return true;
	}
}
