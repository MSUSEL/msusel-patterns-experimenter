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
import org.hibernate.action.spi.AfterTransactionCompletionProcess;
import org.hibernate.action.spi.BeforeTransactionCompletionProcess;
import org.hibernate.action.spi.Executable;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.pretty.MessageHelper;

/**
 * Base class for actions relating to insert/update/delete of an entity
 * instance.
 *
 * @author Gavin King
 */
public abstract class EntityAction
		implements Executable, Serializable, Comparable, AfterTransactionCompletionProcess {

	private final String entityName;
	private final Serializable id;

	private transient Object instance;
	private transient SessionImplementor session;
	private transient EntityPersister persister;

	/**
	 * Instantiate an action.
	 *
	 * @param session The session from which this action is coming.
	 * @param id The id of the entity
	 * @param instance The entity instance
	 * @param persister The entity persister
	 */
	protected EntityAction(SessionImplementor session, Serializable id, Object instance, EntityPersister persister) {
		this.entityName = persister.getEntityName();
		this.id = id;
		this.instance = instance;
		this.session = session;
		this.persister = persister;
	}

	@Override
	public BeforeTransactionCompletionProcess getBeforeTransactionCompletionProcess() {
		return null;
	}

	@Override
	public AfterTransactionCompletionProcess getAfterTransactionCompletionProcess() {
		return needsAfterTransactionCompletion()
				? this
				: null;
	}

	protected abstract boolean hasPostCommitEventListeners();

	public boolean needsAfterTransactionCompletion() {
		return persister.hasCache() || hasPostCommitEventListeners();
	}

	/**
	 * entity name accessor
	 *
	 * @return The entity name
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * entity id accessor
	 *
	 * @return The entity id
	 */
	public final Serializable getId() {
		if ( id instanceof DelayedPostInsertIdentifier ) {
			Serializable eeId = session.getPersistenceContext().getEntry( instance ).getId();
			return eeId instanceof DelayedPostInsertIdentifier ? null : eeId;
		}
		return id;
	}

	public final DelayedPostInsertIdentifier getDelayedId() {
		return DelayedPostInsertIdentifier.class.isInstance( id ) ?
				DelayedPostInsertIdentifier.class.cast( id ) :
				null;
	}

	/**
	 * entity instance accessor
	 *
	 * @return The entity instance
	 */
	public final Object getInstance() {
		return instance;
	}

	/**
	 * originating session accessor
	 *
	 * @return The session from which this action originated.
	 */
	public final SessionImplementor getSession() {
		return session;
	}

	/**
	 * entity persister accessor
	 *
	 * @return The entity persister
	 */
	public final EntityPersister getPersister() {
		return persister;
	}

	@Override
	public final Serializable[] getPropertySpaces() {
		return persister.getPropertySpaces();
	}

	@Override
	public void beforeExecutions() {
		throw new AssertionFailure( "beforeExecutions() called for non-collection action" );
	}

	@Override
	public String toString() {
		return StringHelper.unqualify( getClass().getName() ) + MessageHelper.infoString( entityName, id );
	}

	@Override
	public int compareTo(Object other) {
		EntityAction action = ( EntityAction ) other;
		//sort first by entity name
		int roleComparison = entityName.compareTo( action.entityName );
		if ( roleComparison != 0 ) {
			return roleComparison;
		}
		else {
			//then by id
			return persister.getIdentifierType().compare( id, action.id );
		}
	}

	/**
	 * Reconnect to session after deserialization...
	 *
	 * @param session The session being deserialized
	 */
	public void afterDeserialize(SessionImplementor session) {
		if ( this.session != null || this.persister != null ) {
			throw new IllegalStateException( "already attached to a session." );
		}
		// IMPL NOTE: non-flushed changes code calls this method with session == null...
		// guard against NullPointerException
		if ( session != null ) {
			this.session = session;
			this.persister = session.getFactory().getEntityPersister( entityName );
			this.instance = session.getPersistenceContext().getEntity( session.generateEntityKey( id, persister ) );
		}
	}

	protected <T> EventListenerGroup<T> listenerGroup(EventType<T> eventType) {
		return getSession()
				.getFactory()
				.getServiceRegistry()
				.getService( EventListenerRegistry.class )
				.getEventListenerGroup( eventType );
	}

	protected EventSource eventSource() {
		return (EventSource) getSession();
	}
}

