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
package org.hibernate.engine.jdbc.spi;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Contract for extracting ResultSets from Statements, executing Statements,
 * managing Statement/ResultSet resources, and logging statement calls.
 * 
 * TODO: This could eventually utilize the new Return interface.  It would be
 * great to have a common API shared.
 * 
 * @author Brett Meyer
 */
public interface ResultSetReturn {
	
	/**
	 * Extract the ResultSet from the statement. If user passes {@link CallableStatement}
	 * reference, method calls {@link #extract(CallableStatement)} internally.
	 *
	 * @param statement
	 *
	 * @return the ResultSet
	 */
	public ResultSet extract( PreparedStatement statement );
	
	/**
	 * Extract the ResultSet from the statement.
	 *
	 * @param statement
	 *
	 * @return the ResultSet
	 */
	public ResultSet extract( CallableStatement statement );
	
	/**
	 * Extract the ResultSet from the statement.
	 *
	 * @param statement
	 * @param sql
	 *
	 * @return the ResultSet
	 */
	public ResultSet extract( Statement statement, String sql );
	
	/**
	 * Execute the Statement query and, if results in a ResultSet, extract it.
	 *
	 * @param statement
	 *
	 * @return the ResultSet
	 */
	public ResultSet execute( PreparedStatement statement );
	
	/**
	 * Execute the Statement query and, if results in a ResultSet, extract it.
	 *
	 * @param statement
	 * @param sql
	 *
	 * @return the ResultSet
	 */
	public ResultSet execute( Statement statement, String sql );
	
	/**
	 * Execute the Statement queryUpdate.
	 *
	 * @param statement
	 *
	 * @return int
	 */
	public int executeUpdate( PreparedStatement statement );
	
	/**
	 * Execute the Statement query and, if results in a ResultSet, extract it.
	 *
	 * @param statement
	 * @param sql
	 *
	 * @return the ResultSet
	 */
	public int executeUpdate( Statement statement, String sql );
}
