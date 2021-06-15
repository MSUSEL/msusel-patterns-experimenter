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
package org.hibernate.metamodel.source.binder;

import org.hibernate.EntityMode;
import org.hibernate.engine.OptimisticLockStyle;
import org.hibernate.metamodel.binding.Caching;

/**
 * Contract for the entity that is the root of an inheritance hierarchy.
 * <p/>
 * <b>NOTE</b> : I think most of this could be moved to {@link EntityHierarchy} much like was done with
 * {@link org.hibernate.metamodel.binding.HierarchyDetails}
 *
 * @author Steve Ebersole
 *
 * @todo Move these concepts to {@link EntityHierarchy} ?
 */
public interface RootEntitySource extends EntitySource {
	/**
	 * Obtain source information about this entity's identifier.
	 *
	 * @return Identifier source information.
	 */
	public IdentifierSource getIdentifierSource();

	/**
	 * Obtain the source information about the attribute used for versioning.
	 *
	 * @return the source information about the attribute used for versioning
	 */
	public SingularAttributeSource getVersioningAttributeSource();

	/**
	 * Obtain the source information about the discriminator attribute for single table inheritance
	 *
	 * @return the source information about the discriminator attribute for single table inheritance
	 */
	public DiscriminatorSource getDiscriminatorSource();

	/**
	 * Obtain the entity mode for this entity.
	 *
	 * @return The entity mode.
	 */
	public EntityMode getEntityMode();

	/**
	 * Is this root entity mutable?
	 *
	 * @return {@code true} indicates mutable; {@code false} non-mutable.
	 */
	public boolean isMutable();

	/**
	 * Should explicit polymorphism (querying) be applied to this entity?
	 *
	 * @return {@code true} indicates explicit polymorphism; {@code false} implicit.
	 */
	public boolean isExplicitPolymorphism();

	/**
	 * Obtain the specified extra where condition to be applied to this entity.
	 *
	 * @return The extra where condition
	 */
	public String getWhere();

	/**
	 * Obtain the row-id name for this entity
	 *
	 * @return The row-id name
	 */
	public String getRowId();

	/**
	 * Obtain the optimistic locking style for this entity.
	 *
	 * @return The optimistic locking style.
	 */
	public OptimisticLockStyle getOptimisticLockStyle();

	/**
	 * Obtain the caching configuration for this entity.
	 *
	 * @return The caching configuration.
	 */
	public Caching getCaching();
}
