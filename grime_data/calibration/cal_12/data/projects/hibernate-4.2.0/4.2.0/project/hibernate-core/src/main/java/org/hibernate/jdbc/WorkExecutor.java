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
package org.hibernate.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A visitor used for executing a discrete piece of work encapsulated in a
 * {@link Work} or {@link ReturningWork} instance..
 *
 * @author Gail Badner
 */
public class WorkExecutor<T> {

	/**
	 * Execute the discrete work encapsulated by a {@link Work} instance
	 * using the supplied connection.
	 *
	 * Because {@link Work} does not return a value when executed
	 * (via {@link Work#execute(java.sql.Connection)}, this method
	 * always returns null.
	 *
	 * @param work The @link ReturningWork} instance encapsulating the discrete work
	 * @param connection The connection on which to perform the work.
	 *
	 * @return null>.
	 * 
	 * @throws SQLException Thrown during execution of the underlying JDBC interaction.
	 * @throws org.hibernate.HibernateException Generally indicates a wrapped SQLException.
	 */
	public <T> T executeWork(Work work, Connection connection) throws SQLException {
		work.execute( connection );
		return null;
	}

	/**
	 * Execute the discrete work encapsulated by a {@link ReturningWork} instance
	 * using the supplied connection, returning the result of
	 * {@link ReturningWork#execute(java.sql.Connection)}
	 *
	 * @param work The @link ReturningWork} instance encapsulating the discrete work
	 * @param connection The connection on which to perform the work.
	 *
	 * @return the valued returned by <code>work.execute(connection)</code>.
	 *
	 * @throws SQLException Thrown during execution of the underlying JDBC interaction.
	 * @throws org.hibernate.HibernateException Generally indicates a wrapped SQLException.
	 */
	public <T> T executeReturningWork(ReturningWork<T> work, Connection connection) throws SQLException {
		return work.execute( connection );
	}
}
