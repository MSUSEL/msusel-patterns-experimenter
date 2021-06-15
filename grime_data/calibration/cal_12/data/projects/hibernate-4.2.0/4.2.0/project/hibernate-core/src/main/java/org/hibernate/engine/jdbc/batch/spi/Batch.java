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
package org.hibernate.engine.jdbc.batch.spi;
import java.sql.PreparedStatement;

/**
 * Conceptually models a batch.
 * <p/>
 * Unlike directly in JDBC, here we add the ability to batch together multiple statements at a time.  In the underlying
 * JDBC this correlates to multiple {@link java.sql.PreparedStatement} objects (one for each DML string) maintained within the
 * batch.
 *
 * @author Steve Ebersole
 */
public interface Batch {
	/**
	 * Retrieves the object being used to key (uniquely identify) this batch.
	 *
	 * @return The batch key.
	 */
	public BatchKey getKey();

	/**
	 * Adds an observer to this batch.
	 *
	 * @param observer The batch observer.
	 */
	public void addObserver(BatchObserver observer);

	/**
	 * Get a statement which is part of the batch, creating if necessary (and storing for next time).
	 *
	 * @param sql The SQL statement.
	 * @param callable Is the SQL statement callable?
	 *
	 * @return The prepared statement instance, representing the SQL statement.
	 */
	public PreparedStatement getBatchStatement(String sql, boolean callable);

	/**
	 * Indicates completion of the current part of the batch.
	 */
	public void addToBatch();

	/**
	 * Execute this batch.
	 */
	public void execute();

	/**
	 * Used to indicate that the batch instance is no longer needed and that, therefore, it can release its
	 * resources.
	 */
	public void release();
}

