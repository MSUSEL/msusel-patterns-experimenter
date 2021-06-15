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
import org.hibernate.LockMode;
import org.hibernate.TransientObjectException;
import org.hibernate.engine.internal.Cascade;
import org.hibernate.engine.internal.ForeignKeys;
import org.hibernate.engine.spi.CascadingAction;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.LockEvent;
import org.hibernate.event.spi.LockEventListener;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Defines the default lock event listeners used by hibernate to lock entities
 * in response to generated lock events.
 *
 * @author Steve Ebersole
 */
public class DefaultLockEventListener extends AbstractLockUpgradeEventListener implements LockEventListener {

	/** Handle the given lock event.
	 *
	 * @param event The lock event to be handled.
	 * @throws HibernateException
	 */
	public void onLock(LockEvent event) throws HibernateException {

		if ( event.getObject() == null ) {
			throw new NullPointerException( "attempted to lock null" );
		}

		if ( event.getLockMode() == LockMode.WRITE ) {
			throw new HibernateException( "Invalid lock mode for lock()" );
		}

		SessionImplementor source = event.getSession();
		
		Object entity = source.getPersistenceContext().unproxyAndReassociate( event.getObject() );
		//TODO: if object was an uninitialized proxy, this is inefficient,
		//      resulting in two SQL selects
		
		EntityEntry entry = source.getPersistenceContext().getEntry(entity);
		if (entry==null) {
			final EntityPersister persister = source.getEntityPersister( event.getEntityName(), entity );
			final Serializable id = persister.getIdentifier( entity, source );
			if ( !ForeignKeys.isNotTransient( event.getEntityName(), entity, Boolean.FALSE, source ) ) {
				throw new TransientObjectException(
						"cannot lock an unsaved transient instance: " +
						persister.getEntityName()
				);
			}

			entry = reassociate(event, entity, id, persister);
			cascadeOnLock(event, persister, entity);
		}

		upgradeLock( entity, entry, event.getLockOptions(), event.getSession() );
	}
	
	private void cascadeOnLock(LockEvent event, EntityPersister persister, Object entity) {
		EventSource source = event.getSession();
		source.getPersistenceContext().incrementCascadeLevel();
		try {
			new Cascade(CascadingAction.LOCK, Cascade.AFTER_LOCK, source)
					.cascade( persister, entity, event.getLockOptions() );
		}
		finally {
			source.getPersistenceContext().decrementCascadeLevel();
		}
	}

}
