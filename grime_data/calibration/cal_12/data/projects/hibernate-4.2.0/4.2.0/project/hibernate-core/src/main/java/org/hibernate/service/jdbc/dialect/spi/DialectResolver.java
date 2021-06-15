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
package org.hibernate.service.jdbc.dialect.spi;

import java.sql.DatabaseMetaData;

import org.hibernate.dialect.Dialect;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.Service;

/**
 * Contract for determining the {@link Dialect} to use based on a JDBC {@link java.sql.Connection}.
 *
 * @author Tomoto Shimizu Washio
 * @author Steve Ebersole
 */
public interface DialectResolver extends Service {
	/**
	 * Determine the {@link Dialect} to use based on the given JDBC {@link DatabaseMetaData}.  Implementations are
	 * expected to return the {@link Dialect} instance to use, or null if the {@link DatabaseMetaData} does not match
	 * the criteria handled by this impl.
	 * 
	 * @param metaData The JDBC metadata.
	 *
	 * @return The dialect to use, or null.
	 *
	 * @throws JDBCConnectionException Indicates a 'non transient connection problem', which indicates that
	 * we should stop resolution attempts.
	 */
	public Dialect resolveDialect(DatabaseMetaData metaData) throws JDBCConnectionException;
}
