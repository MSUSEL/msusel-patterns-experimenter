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
package org.hibernate.exception.spi;

import java.io.Serializable;
import java.sql.SQLException;

import org.hibernate.JDBCException;

/**
 * Defines a contract for implementations that know how to convert a SQLException
 * into Hibernate's JDBCException hierarchy.  Inspired by Spring's
 * SQLExceptionTranslator.
 * <p/>
 * Implementations <b>must</b> have a constructor which takes a
 * {@link ViolatedConstraintNameExtracter} parameter.
 * <p/>
 * Implementations may implement {@link org.hibernate.exception.spi.Configurable} if they need to perform
 * configuration steps prior to first use.
 *
 * @author Steve Ebersole
 * @see SQLExceptionConverterFactory
 */
public interface SQLExceptionConverter extends Serializable {
	/**
	 * Convert the given SQLException into the Hibernate {@link JDBCException} hierarchy.
	 *
	 * @param sqlException The SQLException to be converted.
	 * @param message      An optional error message.
	 * @return The resulting JDBCException.
	 * @see org.hibernate.exception.ConstraintViolationException , JDBCConnectionException, SQLGrammarException, LockAcquisitionException
	 */
	public JDBCException convert(SQLException sqlException, String message, String sql);
}
