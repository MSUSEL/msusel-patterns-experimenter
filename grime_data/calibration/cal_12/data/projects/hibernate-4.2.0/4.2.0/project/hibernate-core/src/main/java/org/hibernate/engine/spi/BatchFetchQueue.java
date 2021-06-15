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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.EntityMode;
import org.hibernate.cache.spi.CacheKey;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Tracks entity and collection keys that are available for batch
 * fetching, and the queries which were used to load entities, which
 * can be re-used as a subquery for loading owned collections.
 *
 * @author Gavin King
 * @author Steve Ebersole
 * @author Guenther Demetz
 */
public class BatchFetchQueue {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, BatchFetchQueue.class.getName() );

	private final PersistenceContext context;

	/**
	 * A map of {@link SubselectFetch subselect-fetch descriptors} keyed by the
	 * {@link EntityKey) against which the descriptor is registered.
	 */
	private final Map<EntityKey, SubselectFetch> subselectsByEntityKey = new HashMap<EntityKey, SubselectFetch>(8);

	/**
	 * Used to hold information about the entities that are currently eligible for batch-fetching.  Ultimately
	 * used by {@link #getEntityBatch} to build entity load batches.
	 * <p/>
	 * A Map structure is used to segment the keys by entity type since loading can only be done for a particular entity
	 * type at a time.
	 */
	private final Map <String,LinkedHashSet<EntityKey>> batchLoadableEntityKeys = new HashMap <String,LinkedHashSet<EntityKey>>(8);
	
	/**
	 * Used to hold information about the collections that are currently eligible for batch-fetching.  Ultimately
	 * used by {@link #getCollectionBatch} to build collection load batches.
	 */
	private final Map<String, LinkedHashMap<CollectionEntry, PersistentCollection>> batchLoadableCollections =
			new HashMap<String, LinkedHashMap <CollectionEntry, PersistentCollection>>(8);

	/**
	 * Constructs a queue for the given context.
	 *
	 * @param context The owning context.
	 */
	public BatchFetchQueue(PersistenceContext context) {
		this.context = context;
	}

	/**
	 * Clears all entries from this fetch queue.
	 * <p/>
	 * Called after flushing or clearing the session.
	 */
	public void clear() {
		batchLoadableEntityKeys.clear();
		batchLoadableCollections.clear();
		subselectsByEntityKey.clear();
	}


	// sub-select support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Retrieve the fetch descriptor associated with the given entity key.
	 *
	 * @param key The entity key for which to locate any defined subselect fetch.
	 * @return The fetch descriptor; may return null if no subselect fetch queued for
	 * this entity key.
	 */
	public SubselectFetch getSubselect(EntityKey key) {
		return subselectsByEntityKey.get( key );
	}

	/**
	 * Adds a subselect fetch decriptor for the given entity key.
	 *
	 * @param key The entity for which to register the subselect fetch.
	 * @param subquery The fetch descriptor.
	 */
	public void addSubselect(EntityKey key, SubselectFetch subquery) {
		subselectsByEntityKey.put( key, subquery );
	}

	/**
	 * After evicting or deleting an entity, we don't need to
	 * know the query that was used to load it anymore (don't
	 * call this after loading the entity, since we might still
	 * need to load its collections)
	 */
	public void removeSubselect(EntityKey key) {
		subselectsByEntityKey.remove( key );
	}

	// entity batch support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * If an EntityKey represents a batch loadable entity, add
	 * it to the queue.
	 * <p/>
	 * Note that the contract here is such that any key passed in should
	 * previously have been been checked for existence within the
	 * {@link PersistenceContext}; failure to do so may cause the
	 * referenced entity to be included in a batch even though it is
	 * already associated with the {@link PersistenceContext}.
	 */
	public void addBatchLoadableEntityKey(EntityKey key) {
		if ( key.isBatchLoadable() ) {
			LinkedHashSet<EntityKey> set =  batchLoadableEntityKeys.get( key.getEntityName());
			if (set == null) {
				set = new LinkedHashSet<EntityKey>(8);
				batchLoadableEntityKeys.put( key.getEntityName(), set);
			}
			set.add(key);
		}
	}

	/**
	 * After evicting or deleting or loading an entity, we don't
	 * need to batch fetch it anymore, remove it from the queue
	 * if necessary
	 */
	public void removeBatchLoadableEntityKey(EntityKey key) {
		if ( key.isBatchLoadable() ) {
			LinkedHashSet<EntityKey> set =  batchLoadableEntityKeys.get( key.getEntityName());
			if (set != null) {
				set.remove(key);
			}
		}
	}

	/**
	 * Get a batch of unloaded identifiers for this class, using a slightly
	 * complex algorithm that tries to grab keys registered immediately after
	 * the given key.
	 *
	 * @param persister The persister for the entities being loaded.
	 * @param id The identifier of the entity currently demanding load.
	 * @param batchSize The maximum number of keys to return
	 * @return an array of identifiers, of length batchSize (possibly padded with nulls)
	 */
	public Serializable[] getEntityBatch(
			final EntityPersister persister,
			final Serializable id,
			final int batchSize,
			final EntityMode entityMode) {
		Serializable[] ids = new Serializable[batchSize];
		ids[0] = id; //first element of array is reserved for the actual instance we are loading!
		int i = 1;
		int end = -1;
		boolean checkForEnd = false;

		// TODO: this needn't exclude subclasses...

		LinkedHashSet<EntityKey> set =  batchLoadableEntityKeys.get( persister.getEntityName() );
		if ( set != null ) {
			for ( EntityKey key : set ) {
				if ( checkForEnd && i == end ) {
					//the first id found after the given id
					return ids;
				}
				if ( persister.getIdentifierType().isEqual( id, key.getIdentifier() ) ) {
					end = i;
				}
				else {
					if ( !isCached( key, persister ) ) {
						ids[i++] = key.getIdentifier();
					}
				}
				if ( i == batchSize ) {
					i = 1; // end of array, start filling again from start
					if ( end != -1 ) {
						checkForEnd = true;
					}
				}
			}
		}
		return ids; //we ran out of ids to try
	}

	private boolean isCached(EntityKey entityKey, EntityPersister persister) {
		if ( persister.hasCache() ) {
			CacheKey key = context.getSession().generateCacheKey(
					entityKey.getIdentifier(),
					persister.getIdentifierType(),
					entityKey.getEntityName()
			);
			return persister.getCacheAccessStrategy().get( key, context.getSession().getTimestamp() ) != null;
		}
		return false;
	}
	

	// collection batch support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * If an CollectionEntry represents a batch loadable collection, add
	 * it to the queue.
	 */
	public void addBatchLoadableCollection(PersistentCollection collection, CollectionEntry ce) {
		final CollectionPersister persister = ce.getLoadedPersister();

		LinkedHashMap<CollectionEntry, PersistentCollection> map =  batchLoadableCollections.get( persister.getRole() );
		if ( map == null ) {
			map = new LinkedHashMap<CollectionEntry, PersistentCollection>( 16 );
			batchLoadableCollections.put( persister.getRole(), map );
		}
		map.put( ce, collection );
	}
	
	/**
	 * After a collection was initialized or evicted, we don't
	 * need to batch fetch it anymore, remove it from the queue
	 * if necessary
	 */
	public void removeBatchLoadableCollection(CollectionEntry ce) {
		LinkedHashMap<CollectionEntry, PersistentCollection> map =  batchLoadableCollections.get( ce.getLoadedPersister().getRole() );
		if ( map != null ) {
			map.remove( ce );
		}
	}

	/**
	 * Get a batch of uninitialized collection keys for a given role
	 *
	 * @param collectionPersister The persister for the collection role.
	 * @param id A key that must be included in the batch fetch
	 * @param batchSize the maximum number of keys to return
	 * @return an array of collection keys, of length batchSize (padded with nulls)
	 */
	public Serializable[] getCollectionBatch(
			final CollectionPersister collectionPersister,
			final Serializable id,
			final int batchSize) {

		Serializable[] keys = new Serializable[batchSize];
		keys[0] = id;

		int i = 1;
		int end = -1;
		boolean checkForEnd = false;

		final LinkedHashMap<CollectionEntry, PersistentCollection> map =  batchLoadableCollections.get( collectionPersister.getRole() );
		if ( map != null ) {
			for ( Map.Entry<CollectionEntry, PersistentCollection> me : map.entrySet() ) {
				final CollectionEntry ce = me.getKey();
				final PersistentCollection collection = me.getValue();
				
				if ( ce.getLoadedKey() == null ) {
					// the loadedKey of the collectionEntry might be null as it might have been reset to null
					// (see for example Collections.processDereferencedCollection()
					// and CollectionEntry.afterAction())
					// though we clear the queue on flush, it seems like a good idea to guard
					// against potentially null loadedKeys (which leads to various NPEs as demonstrated in HHH-7821).
					continue;
				}

				if ( collection.wasInitialized() ) {
					// should never happen
					LOG.warn( "Encountered initialized collection in BatchFetchQueue, this should not happen." );
					continue;
				}

				if ( checkForEnd && i == end ) {
					return keys; //the first key found after the given key
				}

				final boolean isEqual = collectionPersister.getKeyType().isEqual(
						id,
						ce.getLoadedKey(),
						collectionPersister.getFactory()
				);

				if ( isEqual ) {
					end = i;
					//checkForEnd = false;
				}
				else if ( !isCached( ce.getLoadedKey(), collectionPersister ) ) {
					keys[i++] = ce.getLoadedKey();
					//count++;
				}

				if ( i == batchSize ) {
					i = 1; //end of array, start filling again from start
					if ( end != -1 ) {
						checkForEnd = true;
					}
				}
			}
		}
		return keys; //we ran out of keys to try
	}

	private boolean isCached(Serializable collectionKey, CollectionPersister persister) {
		if ( persister.hasCache() ) {
			CacheKey cacheKey = context.getSession().generateCacheKey(
					collectionKey,
			        persister.getKeyType(),
			        persister.getRole()
			);
			return persister.getCacheAccessStrategy().get( cacheKey, context.getSession().getTimestamp() ) != null;
		}
		return false;
	}
}
