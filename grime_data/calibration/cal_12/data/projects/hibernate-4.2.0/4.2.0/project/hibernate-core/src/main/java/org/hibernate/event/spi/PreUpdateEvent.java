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

import org.hibernate.persister.entity.EntityPersister;

/**
 * Represents a <tt>pre-update</tt> event, which occurs just prior to
 * performing the update of an entity in the database.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class PreUpdateEvent extends AbstractPreDatabaseOperationEvent {
	private Object[] state;
	private Object[] oldState;

	/**
	 * Constructs an event containing the pertinent information.
	 *
	 * @param entity The entity to be updated.
	 * @param id The id of the entity to use for updating.
	 * @param state The state to be updated.
	 * @param oldState The state of the entity at the time it was loaded from
	 * the database.
	 * @param persister The entity's persister.
	 * @param source The session from which the event originated.
	 */
	public PreUpdateEvent(
			Object entity,
			Serializable id,
			Object[] state,
			Object[] oldState,
			EntityPersister persister,
			EventSource source) {
		super( source, entity, id, persister );
		this.state = state;
		this.oldState = oldState;
	}

	/**
	 * Retrieves the state to be used in the update.
	 *
	 * @return The current state.
	 */
	public Object[] getState() {
		return state;
	}

	/**
	 * The old state of the entity at the time it was last loaded from the
	 * database; can be null in the case of detached entities.
	 *
	 * @return The loaded state, or null.
	 */
	public Object[] getOldState() {
		return oldState;
	}
}
