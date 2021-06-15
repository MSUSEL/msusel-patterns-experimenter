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

/**
 * Specialized {@link Managed} contract for entity classes.  Essentially provides access to information
 * about an instance's association to a Session/EntityManager.  Specific information includes:<ul>
 *     <li>
 *        the association's {@link EntityEntry} (by way of {@link #$$_hibernate_getEntityEntry} and
 *        {@link #$$_hibernate_setEntityEntry}).  EntityEntry describes states, snapshots, etc.
 *     </li>
 *     <li>
 *         link information.  ManagedEntity instances are part of a "linked list", thus link information
 *         describes the next and previous entries/nodes in that ordering.  See
 *         {@link #$$_hibernate_getNextManagedEntity}, {@link #$$_hibernate_setNextManagedEntity},
 *         {@link #$$_hibernate_getPreviousManagedEntity}, {@link #$$_hibernate_setPreviousManagedEntity}
 *     </li>
 * </ul>
 *
 * @author Steve Ebersole
 */
public interface ManagedEntity extends Managed {
	/**
	 * Obtain a reference to the entity instance.
	 *
	 * @return The entity instance.
	 */
	public Object $$_hibernate_getEntityInstance();

	/**
	 * Provides access to the associated EntityEntry.
	 *
	 * @return The EntityEntry associated with this entity instance.
	 *
	 * @see #$$_hibernate_setEntityEntry
	 */
	public EntityEntry $$_hibernate_getEntityEntry();

	/**
	 * Injects the EntityEntry associated with this entity instance.  The EntityEntry represents state associated
	 * with the entity in regards to its association with a Hibernate Session.
	 *
	 * @param entityEntry The EntityEntry associated with this entity instance.
	 */
	public void $$_hibernate_setEntityEntry(EntityEntry entityEntry);

	/**
	 * Part of entry linking; obtain reference to the previous entry.  Can be {@code null}, which should indicate
	 * this is the head node.
	 *
	 * @return The previous entry
	 */
	public ManagedEntity $$_hibernate_getPreviousManagedEntity();

	/**
	 * Part of entry linking; sets the previous entry.  Again, can be {@code null}, which should indicate
	 * this is (now) the head node.
	 *
	 * @param previous The previous entry
	 */
	public void $$_hibernate_setPreviousManagedEntity(ManagedEntity previous);

	/**
	 * Part of entry linking; obtain reference to the next entry.  Can be {@code null}, which should indicate
	 * this is the tail node.
	 *
	 * @return The next entry
	 */
	public ManagedEntity $$_hibernate_getNextManagedEntity();

	/**
	 * Part of entry linking; sets the next entry.  Again, can be {@code null}, which should indicate
	 * this is (now) the tail node.
	 *
	 * @param next The next entry
	 */
	public void $$_hibernate_setNextManagedEntity(ManagedEntity next);
}
