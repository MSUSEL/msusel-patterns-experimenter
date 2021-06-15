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
package org.hibernate;

import java.io.Serializable;

/**
 * Contract methods shared between {@link Session} and {@link StatelessSession}
 * 
 * @author Steve Ebersole
 */
public interface SharedSessionContract extends Serializable {
	/**
	 * Obtain the tenant identifier associated with this session.
	 *
	 * @return The tenant identifier associated with this session, or {@code null}
	 */
	public String getTenantIdentifier();

	/**
	 * Begin a unit of work and return the associated {@link Transaction} object.  If a new underlying transaction is
	 * required, begin the transaction.  Otherwise continue the new work in the context of the existing underlying
	 * transaction.
	 *
	 * @return a Transaction instance
	 *
	 * @see #getTransaction
	 */
	public Transaction beginTransaction();

	/**
	 * Get the {@link Transaction} instance associated with this session.  The concrete type of the returned
	 * {@link Transaction} object is determined by the {@code hibernate.transaction_factory} property.
	 *
	 * @return a Transaction instance
	 */
	public Transaction getTransaction();

	/**
	 * Create a {@link Query} instance for the named query string defined in the metadata.
	 *
	 * @param queryName the name of a query defined externally
	 *
	 * @return The query instance for manipulation and execution
	 */
	public Query getNamedQuery(String queryName);

	/**
	 * Create a {@link Query} instance for the given HQL query string.
	 *
	 * @param queryString The HQL query
	 *
	 * @return The query instance for manipulation and execution
	 */
	public Query createQuery(String queryString);

	/**
	 * Create a {@link SQLQuery} instance for the given SQL query string.
	 *
	 * @param queryString The SQL query
	 * 
	 * @return The query instance for manipulation and execution
	 */
	public SQLQuery createSQLQuery(String queryString);

	/**
	 * Create {@link Criteria} instance for the given class (entity or subclasses/implementors)
	 *
	 * @param persistentClass The class, which is an entity, or has entity subclasses/implementors
	 *
	 * @return The criteria instance for manipulation and execution
	 */
	public Criteria createCriteria(Class persistentClass);

	/**
	 * Create {@link Criteria} instance for the given class (entity or subclasses/implementors), using a specific
	 * alias.
	 *
	 * @param persistentClass The class, which is an entity, or has entity subclasses/implementors
	 * @param alias The alias to use
	 *
	 * @return The criteria instance for manipulation and execution
	 */
	public Criteria createCriteria(Class persistentClass, String alias);

	/**
	 * Create {@link Criteria} instance for the given entity name.
	 *
	 * @param entityName The entity name

	 * @return The criteria instance for manipulation and execution
	 */
	public Criteria createCriteria(String entityName);

	/**
	 * Create {@link Criteria} instance for the given entity name, using a specific alias.
	 *
	 * @param entityName The entity name
	 * @param alias The alias to use
	 *
	 * @return The criteria instance for manipulation and execution
	 */
	public Criteria createCriteria(String entityName, String alias);
}
