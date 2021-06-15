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

import org.jboss.logging.Logger;

import org.hibernate.LockMode;
import org.hibernate.engine.internal.Versioning;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.Status;
import org.hibernate.event.spi.AbstractEvent;
import org.hibernate.event.spi.EventSource;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.type.TypeHelper;

/**
 * A convenience base class for listeners that respond to requests to reassociate an entity
 * to a session ( such as through lock() or update() ).
 *
 * @author Gavin King
 */
public class AbstractReassociateEventListener implements Serializable {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, AbstractReassociateEventListener.class.getName() );

	/**
	 * Associates a given entity (either transient or associated with another session) to
	 * the given session.
	 *
	 * @param event The event triggering the re-association
	 * @param object The entity to be associated
	 * @param id The id of the entity.
	 * @param persister The entity's persister instance.
	 *
	 * @return An EntityEntry representing the entity within this session.
	 */
	protected final EntityEntry reassociate(AbstractEvent event, Object object, Serializable id, EntityPersister persister) {

		if ( LOG.isTraceEnabled() ) {
			LOG.tracev( "Reassociating transient instance: {0}", MessageHelper.infoString( persister, id, event.getSession().getFactory() ) );
		}

		final EventSource source = event.getSession();
		final EntityKey key = source.generateEntityKey( id, persister );

		source.getPersistenceContext().checkUniqueness( key, object );

		//get a snapshot
		Object[] values = persister.getPropertyValues( object );
		TypeHelper.deepCopy(
				values,
				persister.getPropertyTypes(),
				persister.getPropertyUpdateability(),
				values,
				source
		);
		Object version = Versioning.getVersion( values, persister );

		EntityEntry newEntry = source.getPersistenceContext().addEntity(
				object,
				( persister.isMutable() ? Status.MANAGED : Status.READ_ONLY ),
				values,
				key,
				version,
				LockMode.NONE,
				true,
				persister,
				false,
				true //will be ignored, using the existing Entry instead
		);

		new OnLockVisitor( source, id, object ).process( object, persister );

		persister.afterReassociate( object, source );

		return newEntry;

	}
}
