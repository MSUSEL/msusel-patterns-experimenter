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
package org.hibernate.action.internal;

import java.io.Serializable;

import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostCollectionRemoveEvent;
import org.hibernate.event.spi.PostCollectionRemoveEventListener;
import org.hibernate.event.spi.PreCollectionRemoveEvent;
import org.hibernate.event.spi.PreCollectionRemoveEventListener;
import org.hibernate.persister.collection.CollectionPersister;

public final class CollectionRemoveAction extends CollectionAction {

	private boolean emptySnapshot;
	private final Object affectedOwner;
	
	/**
	 * Removes a persistent collection from its loaded owner.
	 *
	 * Use this constructor when the collection is non-null.
	 *
	 * @param collection The collection to to remove; must be non-null
	 * @param persister  The collection's persister
	 * @param id The collection key
	 * @param emptySnapshot Indicates if the snapshot is empty
	 * @param session The session
	 *
	 * @throws AssertionFailure if collection is null.
	 */
	public CollectionRemoveAction(
				final PersistentCollection collection,
				final CollectionPersister persister,
				final Serializable id,
				final boolean emptySnapshot,
				final SessionImplementor session) {
		super( persister, collection, id, session );
		if (collection == null) {
			throw new AssertionFailure("collection == null");
		}
		this.emptySnapshot = emptySnapshot;
		// the loaded owner will be set to null after the collection is removed,
		// so capture its value as the affected owner so it is accessible to
		// both pre- and post- events
		this.affectedOwner = session.getPersistenceContext().getLoadedCollectionOwnerOrNull( collection );
	}

	/**
	 * Removes a persistent collection from a specified owner.
	 *
	 * Use this constructor when the collection to be removed has not been loaded.
	 *
	 * @param affectedOwner The collection's owner; must be non-null
	 * @param persister  The collection's persister
	 * @param id The collection key
	 * @param emptySnapshot Indicates if the snapshot is empty
	 * @param session The session
	 *
	 * @throws AssertionFailure if affectedOwner is null.
	 */
	public CollectionRemoveAction(
				final Object affectedOwner,
				final CollectionPersister persister,
				final Serializable id,
				final boolean emptySnapshot,
				final SessionImplementor session) {
		super( persister, null, id, session );
		if (affectedOwner == null) {
			throw new AssertionFailure("affectedOwner == null");
		}
		this.emptySnapshot = emptySnapshot;
		this.affectedOwner = affectedOwner;
	}

	@Override
	public void execute() throws HibernateException {
		preRemove();

		if ( !emptySnapshot ) {
			// an existing collection that was either non-empty or uninitialized
			// is replaced by null or a different collection
			// (if the collection is uninitialized, hibernate has no way of
			// knowing if the collection is actually empty without querying the db)
			getPersister().remove( getKey(), getSession() );
		}
		
		final PersistentCollection collection = getCollection();
		if (collection!=null) {
			getSession().getPersistenceContext()
				.getCollectionEntry(collection)
				.afterAction(collection);
		}
		
		evict();

		postRemove();		

		if ( getSession().getFactory().getStatistics().isStatisticsEnabled() ) {
			getSession().getFactory().getStatisticsImplementor()
					.removeCollection( getPersister().getRole() );
		}
	}

	private void preRemove() {
		EventListenerGroup<PreCollectionRemoveEventListener> listenerGroup = listenerGroup( EventType.PRE_COLLECTION_REMOVE );
		if ( listenerGroup.isEmpty() ) {
			return;
		}
		final PreCollectionRemoveEvent event = new PreCollectionRemoveEvent(
				getPersister(),
				getCollection(),
				eventSource(),
				affectedOwner
		);
		for ( PreCollectionRemoveEventListener listener : listenerGroup.listeners() ) {
			listener.onPreRemoveCollection( event );
		}
	}

	private void postRemove() {
		EventListenerGroup<PostCollectionRemoveEventListener> listenerGroup = listenerGroup( EventType.POST_COLLECTION_REMOVE );
		if ( listenerGroup.isEmpty() ) {
			return;
		}
		final PostCollectionRemoveEvent event = new PostCollectionRemoveEvent(
				getPersister(),
				getCollection(),
				eventSource(),
				affectedOwner
		);
		for ( PostCollectionRemoveEventListener listener : listenerGroup.listeners() ) {
			listener.onPostRemoveCollection( event );
		}
	}
}
