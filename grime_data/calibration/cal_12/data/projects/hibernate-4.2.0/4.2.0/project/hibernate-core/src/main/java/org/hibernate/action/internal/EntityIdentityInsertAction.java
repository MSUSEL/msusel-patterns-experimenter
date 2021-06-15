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
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;

public final class EntityIdentityInsertAction extends AbstractEntityInsertAction  {

	private final boolean isDelayed;
	private final EntityKey delayedEntityKey;
	private EntityKey entityKey;
	//private CacheEntry cacheEntry;
	private Serializable generatedId;

	public EntityIdentityInsertAction(
			Object[] state,
			Object instance,
			EntityPersister persister,
			boolean isVersionIncrementDisabled,
			SessionImplementor session,
			boolean isDelayed) throws HibernateException {
		super(
				( isDelayed ? generateDelayedPostInsertIdentifier() : null ),
				state,
				instance,
				isVersionIncrementDisabled,
				persister,
				session
		);
		this.isDelayed = isDelayed;
		this.delayedEntityKey = isDelayed ? generateDelayedEntityKey() : null;
	}

	@Override
	public void execute() throws HibernateException {
		nullifyTransientReferencesIfNotAlready();

		final EntityPersister persister = getPersister();
		final SessionImplementor session = getSession();
		final Object instance = getInstance();

		boolean veto = preInsert();

		// Don't need to lock the cache here, since if someone
		// else inserted the same pk first, the insert would fail

		if ( !veto ) {
			generatedId = persister.insert( getState(), instance, session );
			if ( persister.hasInsertGeneratedProperties() ) {
				persister.processInsertGeneratedProperties( generatedId, instance, getState(), session );
			}
			//need to do that here rather than in the save event listener to let
			//the post insert events to have a id-filled entity when IDENTITY is used (EJB3)
			persister.setIdentifier( instance, generatedId, session );
			session.getPersistenceContext().registerInsertedKey( getPersister(), generatedId );
			entityKey = session.generateEntityKey( generatedId, persister );
			session.getPersistenceContext().checkUniqueness( entityKey, getInstance() );
		}


		//TODO: this bit actually has to be called after all cascades!
		//      but since identity insert is called *synchronously*,
		//      instead of asynchronously as other actions, it isn't
		/*if ( persister.hasCache() && !persister.isCacheInvalidationRequired() ) {
			cacheEntry = new CacheEntry(object, persister, session);
			persister.getCache().insert(generatedId, cacheEntry);
		}*/

		postInsert();

		if ( session.getFactory().getStatistics().isStatisticsEnabled() && !veto ) {
			session.getFactory().getStatisticsImplementor().insertEntity( getPersister().getEntityName() );
		}

		markExecuted();
	}

	@Override
    public boolean needsAfterTransactionCompletion() {
		//TODO: simply remove this override if we fix the above todos
		return hasPostCommitEventListeners();
	}

	@Override
    protected boolean hasPostCommitEventListeners() {
		return ! listenerGroup( EventType.POST_COMMIT_INSERT ).isEmpty();
	}

	@Override
	public void doAfterTransactionCompletion(boolean success, SessionImplementor session) {
		//TODO: reenable if we also fix the above todo
		/*EntityPersister persister = getEntityPersister();
		if ( success && persister.hasCache() && !persister.isCacheInvalidationRequired() ) {
			persister.getCache().afterInsert( getGeneratedId(), cacheEntry );
		}*/
		postCommitInsert();
	}

	private void postInsert() {
		if ( isDelayed ) {
			getSession().getPersistenceContext().replaceDelayedEntityIdentityInsertKeys( delayedEntityKey, generatedId );
		}

		EventListenerGroup<PostInsertEventListener> listenerGroup = listenerGroup( EventType.POST_INSERT );
		if ( listenerGroup.isEmpty() ) {
			return;
		}
		final PostInsertEvent event = new PostInsertEvent(
				getInstance(),
				generatedId,
				getState(),
				getPersister(),
				eventSource()
		);
		for ( PostInsertEventListener listener : listenerGroup.listeners() ) {
			listener.onPostInsert( event );
		}
	}

	private void postCommitInsert() {
		EventListenerGroup<PostInsertEventListener> listenerGroup = listenerGroup( EventType.POST_COMMIT_INSERT );
		if ( listenerGroup.isEmpty() ) {
			return;
		}
		final PostInsertEvent event = new PostInsertEvent(
				getInstance(),
				generatedId,
				getState(),
				getPersister(),
				eventSource()
		);
		for ( PostInsertEventListener listener : listenerGroup.listeners() ) {
			listener.onPostInsert( event );
		}
	}

	private boolean preInsert() {
		EventListenerGroup<PreInsertEventListener> listenerGroup = listenerGroup( EventType.PRE_INSERT );
		if ( listenerGroup.isEmpty() ) {
			return false; // NO_VETO
		}
		boolean veto = false;
		final PreInsertEvent event = new PreInsertEvent( getInstance(), null, getState(), getPersister(), eventSource() );
		for ( PreInsertEventListener listener : listenerGroup.listeners() ) {
			veto |= listener.onPreInsert( event );
		}
		return veto;
	}

	public final Serializable getGeneratedId() {
		return generatedId;
	}

	// TODO: nothing seems to use this method; can it be renmoved?
	public EntityKey getDelayedEntityKey() {
		return delayedEntityKey;
	}

	@Override
	public boolean isEarlyInsert() {
		return !isDelayed;
	}

	@Override
	protected EntityKey getEntityKey() {
		return entityKey != null ? entityKey : delayedEntityKey;
	}

	private synchronized static DelayedPostInsertIdentifier generateDelayedPostInsertIdentifier() {
		return new DelayedPostInsertIdentifier();
	}

	private EntityKey generateDelayedEntityKey() {
		if ( !isDelayed ) {
			throw new AssertionFailure( "cannot request delayed entity-key for early-insert post-insert-id generation" );
		}
		return getSession().generateEntityKey( getDelayedId(), getPersister() );
	}
}
