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
package org.hibernate.dialect.resolver;
import java.sql.SQLException;

import org.jboss.logging.Logger;

import org.hibernate.JDBCException;
import org.hibernate.exception.internal.SQLStateConverter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.internal.CoreMessageLogger;

/**
 * A helper to centralize conversion of {@link java.sql.SQLException}s to {@link org.hibernate.JDBCException}s.
 *
 * @author Steve Ebersole
 */
public class BasicSQLExceptionConverter {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, BasicSQLExceptionConverter.class.getName() );
	public static final BasicSQLExceptionConverter INSTANCE = new BasicSQLExceptionConverter();
	public static final String MSG = LOG.unableToQueryDatabaseMetadata();

	private static final SQLStateConverter CONVERTER = new SQLStateConverter( new ConstraintNameExtracter() );

	/**
	 * Perform a conversion.
	 *
	 * @param sqlException The exception to convert.
	 * @return The converted exception.
	 */
	public JDBCException convert(SQLException sqlException) {
		return CONVERTER.convert( sqlException, MSG, null );
	}

	private static class ConstraintNameExtracter implements ViolatedConstraintNameExtracter {
		/**
		 * {@inheritDoc}
		 */
		public String extractConstraintName(SQLException sqle) {
			return "???";
		}
	}
}
