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

import java.sql.DataTruncation;
import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientConnectionException;

import org.hibernate.JDBCException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.exception.spi.AbstractSQLExceptionConversionDelegate;
import org.hibernate.exception.spi.ConversionContext;
import org.hibernate.exception.spi.SQLExceptionConverter;

/**
 * {@link SQLExceptionConverter} implementation that does conversion based on the
 * JDBC 4 defined {@link SQLException} sub-type hierarchy.
 *
 * @author Steve Ebersole
 */
public class SQLExceptionTypeDelegate extends AbstractSQLExceptionConversionDelegate {
	public SQLExceptionTypeDelegate(ConversionContext conversionContext) {
		super( conversionContext );
	}


	@Override
	public JDBCException convert(SQLException sqlException, String message, String sql) {
		if ( SQLClientInfoException.class.isInstance( sqlException )
				|| SQLInvalidAuthorizationSpecException.class.isInstance( sqlException )
				|| SQLNonTransientConnectionException.class.isInstance( sqlException )
				|| SQLTransientConnectionException.class.isInstance( sqlException ) ) {
			return new JDBCConnectionException( message, sqlException, sql );
		}
		else if ( DataTruncation.class.isInstance( sqlException ) ||
				SQLDataException.class.isInstance( sqlException ) ) {
			throw new DataException( message, sqlException, sql );
		}
		else if ( SQLIntegrityConstraintViolationException.class.isInstance( sqlException ) ) {
			return new ConstraintViolationException(
					message,
					sqlException,
					sql,
					getConversionContext().getViolatedConstraintNameExtracter().extractConstraintName( sqlException )
			);
		}
		else if ( SQLSyntaxErrorException.class.isInstance( sqlException ) ) {
			return new SQLGrammarException( message, sqlException, sql );
		}
		else if ( SQLTimeoutException.class.isInstance( sqlException ) ) {
			return new QueryTimeoutException( message, sqlException, sql );
		}
		else if ( SQLTransactionRollbackException.class.isInstance( sqlException ) ) {
			// Not 100% sure this is completely accurate.  The JavaDocs for SQLTransactionRollbackException state that
			// it indicates sql states starting with '40' and that those usually indicate that:
			//		<quote>
			//			the current statement was automatically rolled back by the database because of deadlock or
			// 			other transaction serialization failures.
			//		</quote>
			return new LockAcquisitionException( message, sqlException, sql );
		}

		return null; // allow other delegates the chance to look
	}
}
