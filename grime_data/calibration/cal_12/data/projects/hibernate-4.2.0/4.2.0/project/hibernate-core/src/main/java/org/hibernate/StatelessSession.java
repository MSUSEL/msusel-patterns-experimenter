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
import java.sql.Connection;

/**
 * A command-oriented API for performing bulk operations against a database.
 * <p/>
 * A stateless session does not implement a first-level cache nor interact
 * with any second-level cache, nor does it implement transactional
 * write-behind or automatic dirty checking, nor do operations cascade to
 * associated instances. Collections are ignored by a stateless session.
 * Operations performed via a stateless session bypass Hibernate's event model
 * and interceptors.  Stateless sessions are vulnerable to data aliasing
 * effects, due to the lack of a first-level cache.
 * <p/>
 * For certain kinds of transactions, a stateless session may perform slightly
 * faster than a stateful session.
 *
 * @author Gavin King
 */
public interface StatelessSession extends SharedSessionContract {
	/**
	 * Close the stateless session and release the JDBC connection.
	 */
	public void close();

	/**
	 * Insert a row.
	 *
	 * @param entity a new transient instance
	 */
	public Serializable insert(Object entity);

	/**
	 * Insert a row.
	 *
	 * @param entityName The entityName for the entity to be inserted
	 * @param entity a new transient instance
	 * @return the identifier of the instance
	 */
	public Serializable insert(String entityName, Object entity);

	/**
	 * Update a row.
	 *
	 * @param entity a detached entity instance
	 */
	public void update(Object entity);

	/**
	 * Update a row.
	 *
	 * @param entityName The entityName for the entity to be updated
	 * @param entity a detached entity instance
	 */
	public void update(String entityName, Object entity);

	/**
	 * Delete a row.
	 *
	 * @param entity a detached entity instance
	 */
	public void delete(Object entity);

	/**
	 * Delete a row.
	 *
	 * @param entityName The entityName for the entity to be deleted
	 * @param entity a detached entity instance
	 */
	public void delete(String entityName, Object entity);

	/**
	 * Retrieve a row.
	 *
	 * @return a detached entity instance
	 */
	public Object get(String entityName, Serializable id);

	/**
	 * Retrieve a row.
	 *
	 * @return a detached entity instance
	 */
	public Object get(Class entityClass, Serializable id);

	/**
	 * Retrieve a row, obtaining the specified lock mode.
	 *
	 * @return a detached entity instance
	 */
	public Object get(String entityName, Serializable id, LockMode lockMode);

	/**
	 * Retrieve a row, obtaining the specified lock mode.
	 *
	 * @return a detached entity instance
	 */
	public Object get(Class entityClass, Serializable id, LockMode lockMode);

	/**
	 * Refresh the entity instance state from the database.
	 *
	 * @param entity The entity to be refreshed.
	 */
	public void refresh(Object entity);

	/**
	 * Refresh the entity instance state from the database.
	 *
	 * @param entityName The entityName for the entity to be refreshed.
	 * @param entity The entity to be refreshed.
	 */
	public void refresh(String entityName, Object entity);

	/**
	 * Refresh the entity instance state from the database.
	 *
	 * @param entity The entity to be refreshed.
	 * @param lockMode The LockMode to be applied.
	 */
	public void refresh(Object entity, LockMode lockMode);

	/**
	 * Refresh the entity instance state from the database.
	 *
	 * @param entityName The entityName for the entity to be refreshed.
	 * @param entity The entity to be refreshed.
	 * @param lockMode The LockMode to be applied.
	 */
	public void refresh(String entityName, Object entity, LockMode lockMode);

	/**
	 * Returns the current JDBC connection associated with this
	 * instance.<br>
	 * <br>
	 * If the session is using aggressive connection release (as in a
	 * CMT environment), it is the application's responsibility to
	 * close the connection returned by this call. Otherwise, the
	 * application should not close the connection.
	 *
	 * @deprecated just missed when deprecating same method from {@link Session}
	 */
	@Deprecated
	public Connection connection();
}
