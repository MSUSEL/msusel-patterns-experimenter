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
package org.hibernate.dialect.lock;

import java.io.Serializable;

import org.hibernate.StaleObjectStateException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * A strategy abstraction for how locks are obtained in the underlying database.
 * <p/>
 * All locking provided implementations assume the underlying database supports
 * (and that the connection is in) at least read-committed transaction isolation.
 * The most glaring exclusion to this is HSQLDB which only offers support for
 * READ_UNCOMMITTED isolation.
 *
 * @see org.hibernate.dialect.Dialect#getLockingStrategy
 * @since 3.2
 *
 * @author Steve Ebersole
 */
public interface LockingStrategy {
	/**
	 * Acquire an appropriate type of lock on the underlying data that will
	 * endure until the end of the current transaction.
	 *
	 * @param id The id of the row to be locked
	 * @param version The current version (or null if not versioned)
	 * @param object The object logically being locked (currently not used)
	 * @param timeout timeout in milliseconds, 0 = no wait, -1 = wait indefinitely
	 * @param session The session from which the lock request originated
	 * @throws StaleObjectStateException Indicates an inability to locate the database row as part of acquiring
	 * the requested lock.
	 * @throws LockingStrategyException Indicates a failure in the lock attempt
	 */
	public void lock(Serializable id, Object version, Object object, int timeout, SessionImplementor session)
	throws StaleObjectStateException, LockingStrategyException;
}
