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
package org.hibernate.exception.internal;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.JDBCException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.spi.AbstractSQLExceptionConversionDelegate;
import org.hibernate.exception.spi.ConversionContext;
import org.hibernate.internal.util.JdbcExceptionHelper;

/**
 * A {@link org.hibernate.exception.spi.SQLExceptionConversionDelegate}
 * implementation specific to Cach&eacute; SQL, accounting for its custom
 * integrity constraint violation error codes.
 *
 * @author Jonathan Levinson
 */
public class CacheSQLExceptionConversionDelegate extends AbstractSQLExceptionConversionDelegate {

	private static final Set<String> DATA_CATEGORIES = new HashSet<String>();
	private static final Set<Integer> INTEGRITY_VIOLATION_CATEGORIES = new HashSet<Integer>();

	static {
		DATA_CATEGORIES.add( "22" );
		DATA_CATEGORIES.add( "21" );
		DATA_CATEGORIES.add( "02" );

		INTEGRITY_VIOLATION_CATEGORIES.add( 119 );
		INTEGRITY_VIOLATION_CATEGORIES.add( 120 );
		INTEGRITY_VIOLATION_CATEGORIES.add( 121 );
		INTEGRITY_VIOLATION_CATEGORIES.add( 122 );
		INTEGRITY_VIOLATION_CATEGORIES.add( 123 );
		INTEGRITY_VIOLATION_CATEGORIES.add( 124 );
		INTEGRITY_VIOLATION_CATEGORIES.add( 125 );
		INTEGRITY_VIOLATION_CATEGORIES.add( 127 );
	}

	public CacheSQLExceptionConversionDelegate(ConversionContext conversionContext) {
		super( conversionContext );
	}

	/**
	 * Convert the given SQLException into Hibernate's JDBCException hierarchy.
	 *
	 * @param sqlException The SQLException to be converted.
	 * @param message	  An optional error message.
	 * @param sql		  Optionally, the sql being performed when the exception occurred.
	 * @return The resulting JDBCException; returns null if it could not be converted.
	 */
	@Override
	public JDBCException convert(SQLException sqlException, String message, String sql) {
		String sqlStateClassCode = JdbcExceptionHelper.extractSqlStateClassCode( sqlException );
		if ( sqlStateClassCode != null ) {
			Integer errorCode = JdbcExceptionHelper.extractErrorCode( sqlException );
			if ( INTEGRITY_VIOLATION_CATEGORIES.contains( errorCode ) ) {
				String constraintName =
						getConversionContext()
								.getViolatedConstraintNameExtracter()
								.extractConstraintName( sqlException );
				return new ConstraintViolationException( message, sqlException, sql, constraintName );
			}
			else if ( DATA_CATEGORIES.contains( sqlStateClassCode ) ) {
				return new DataException( message, sqlException, sql );
			}
		}
		return null; // allow other delegates the chance to look
	}
}
