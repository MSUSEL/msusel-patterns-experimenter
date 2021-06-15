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
 * Contract for sources of information about a mapped discriminator.
 *
 * @author Hardy Ferentschik
 * @author Steve Ebersole
 */
public interface DiscriminatorSource {
	/**
	 * Obtain the column/formula information about the discriminator.
	 *
	 * @return The column/formula information
	 */
	public RelationalValueSource getDiscriminatorRelationalValueSource();

	/**
	 * Obtain the discriminator type.  Unlike the type of attributes, implementors here should generally specify the type
	 * in case of no user selection rather than return null because we cannot infer it from any physical java member.
	 *
	 * @return The discriminator type
	 */
	public String getExplicitHibernateTypeName();

	/**
	 * "Forces" Hibernate to specify the allowed discriminator values, even when retrieving all instances of the
	 * root class.
	 *
	 * @return {@code true} in case the discriminator value should be forces, {@code false} otherwise. Default
	 * is {@code false}.
	 */
	boolean isForced();

	/**
	 * Set this to {@code false}, if your discriminator column is also part of a mapped composite identifier.
	 * It tells Hibernate not to include the column in SQL INSERTs.
	 *
	 * @return {@code true} in case the discriminator value should be included in inserts, {@code false} otherwise.
	 * Default is {@code true}.
	 */
	boolean isInserted();
}
