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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;

/**
 * Defines an expected DML operation outcome.
 *
 * @author Steve Ebersole
 */
public interface Expectation {
	/**
	 * Perform verification of the outcome of the RDBMS operation based on
	 * the type of expectation defined.
	 *
	 * @param rowCount The RDBMS reported "number of rows affected".
	 * @param statement The statement representing the operation
	 * @param batchPosition The position in the batch (if batching)
	 * @throws SQLException Exception from the JDBC driver
	 * @throws HibernateException Problem processing the outcome.
	 */
	public void verifyOutcome(int rowCount, PreparedStatement statement, int batchPosition) throws SQLException, HibernateException;

	/**
	 * Perform any special statement preparation.
	 *
	 * @param statement The statement to be prepared
	 * @return The number of bind positions consumed (if any)
	 * @throws SQLException Exception from the JDBC driver
	 * @throws HibernateException Problem performing preparation.
	 */
	public int prepare(PreparedStatement statement) throws SQLException, HibernateException;

	/**
	 * Is it acceptable to combiner this expectation with statement batching?
	 *
	 * @return True if batching can be combined with this expectation; false otherwise.
	 */
	public boolean canBeBatched();
}
