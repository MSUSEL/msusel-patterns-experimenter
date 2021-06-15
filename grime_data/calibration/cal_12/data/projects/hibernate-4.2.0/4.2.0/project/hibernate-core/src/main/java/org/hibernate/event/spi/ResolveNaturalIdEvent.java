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
package org.hibernate.event.spi;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Defines an event class for the resolving of an entity id from the entity's natural-id
 * 
 * @author Eric Dalquist
 * @author Steve Ebersole
 */
public class ResolveNaturalIdEvent extends AbstractEvent {
	public static final LockMode DEFAULT_LOCK_MODE = LockMode.NONE;

	private final EntityPersister entityPersister;
	private final Map<String, Object> naturalIdValues;
	private final Object[] orderedNaturalIdValues;
	private final LockOptions lockOptions;

	private Serializable entityId;

	public ResolveNaturalIdEvent(Map<String, Object> naturalIdValues, EntityPersister entityPersister, EventSource source) {
		this( naturalIdValues, entityPersister, new LockOptions(), source );
	}

	public ResolveNaturalIdEvent(
			Map<String, Object> naturalIdValues,
			EntityPersister entityPersister,
			LockOptions lockOptions,
			EventSource source) {
		super( source );

		if ( entityPersister == null ) {
			throw new IllegalArgumentException( "EntityPersister is required for loading" );
		}

		if ( ! entityPersister.hasNaturalIdentifier() ) {
			throw new HibernateException( "Entity did not define a natural-id" );
		}

		if ( naturalIdValues == null || naturalIdValues.isEmpty() ) {
			throw new IllegalArgumentException( "natural-id to load is required" );
		}

		if ( entityPersister.getNaturalIdentifierProperties().length != naturalIdValues.size() ) {
			throw new HibernateException(
					String.format(
						"Entity [%s] defines its natural-id with %d properties but only %d were specified",
						entityPersister.getEntityName(),
						entityPersister.getNaturalIdentifierProperties().length,
						naturalIdValues.size()
					)
			);
		}

		if ( lockOptions.getLockMode() == LockMode.WRITE ) {
			throw new IllegalArgumentException( "Invalid lock mode for loading" );
		}
		else if ( lockOptions.getLockMode() == null ) {
			lockOptions.setLockMode( DEFAULT_LOCK_MODE );
		}

		this.entityPersister = entityPersister;
		this.naturalIdValues = naturalIdValues;
		this.lockOptions = lockOptions;

		int[] naturalIdPropertyPositions = entityPersister.getNaturalIdentifierProperties();
		orderedNaturalIdValues = new Object[naturalIdPropertyPositions.length];
		int i = 0;
		for ( int position : naturalIdPropertyPositions ) {
			final String propertyName = entityPersister.getPropertyNames()[position];
			if ( ! naturalIdValues.containsKey( propertyName ) ) {
				throw new HibernateException(
						String.format( "No value specified for natural-id property %s#%s", getEntityName(), propertyName )
				);
			}
			orderedNaturalIdValues[i++] = naturalIdValues.get( entityPersister.getPropertyNames()[position] );
		}
	}

	public Map<String, Object> getNaturalIdValues() {
		return Collections.unmodifiableMap( naturalIdValues );
	}

	public Object[] getOrderedNaturalIdValues() {
		return orderedNaturalIdValues;
	}

	public EntityPersister getEntityPersister() {
		return entityPersister;
	}

	public String getEntityName() {
		return getEntityPersister().getEntityName();
	}

	public LockOptions getLockOptions() {
		return lockOptions;
	}

	public Serializable getEntityId() {
		return entityId;
	}

	public void setEntityId(Serializable entityId) {
		this.entityId = entityId;
	}
}
