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

/**
 * Contract for sources of persistent attribute descriptions.
 *
 * @author Steve Ebersole
 */
public interface AttributeSource {
	/**
	 * Obtain the attribute name.
	 *
	 * @return The attribute name. {@code null} ais NOT allowed!
	 */
	public String getName();

	/**
	 * Is this a singular attribute?  Specifically, can it be cast to {@link SingularAttributeSource}?
	 *
	 * @return {@code true} indicates this is castable to {@link SingularAttributeSource}; {@code false} otherwise.
	 */
	public boolean isSingular();

	/**
	 * Obtain information about the Hibernate type ({@link org.hibernate.type.Type}) for this attribute.
	 *
	 * @return The Hibernate type information
	 */
	public ExplicitHibernateTypeSource getTypeInformation();

	/**
	 * Obtain the name of the property accessor style used to access this attribute.
	 *
	 * @return The property accessor style for this attribute.
	 *
	 * @see org.hibernate.property.PropertyAccessor
	 */
	public String getPropertyAccessorName();

	/**
	 * If the containing entity is using {@link org.hibernate.engine.OptimisticLockStyle#ALL} or
	 * {@link org.hibernate.engine.OptimisticLockStyle#DIRTY} style optimistic locking, should this attribute
	 * be used?
	 *
	 * @return {@code true} indicates it should be included; {@code false}, it should not.
	 */
	public boolean isIncludedInOptimisticLocking();

	/**
	 * Obtain the meta-attribute sources associated with this attribute.
	 *
	 * @return The meta-attribute sources.
	 */
	public Iterable<MetaAttributeSource> metaAttributes();
}
