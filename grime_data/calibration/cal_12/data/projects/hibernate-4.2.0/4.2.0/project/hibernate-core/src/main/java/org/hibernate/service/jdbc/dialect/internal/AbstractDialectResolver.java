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
package org.hibernate.service.jdbc.dialect.internal;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.jboss.logging.Logger;

import org.hibernate.JDBCException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.resolver.BasicSQLExceptionConverter;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.jdbc.dialect.spi.DialectResolver;

/**
 * A templated resolver impl which delegates to the {@link #resolveDialectInternal} method
 * and handles any thrown {@link SQLException SQL errors}.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractDialectResolver implements DialectResolver {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       AbstractDialectResolver.class.getName());

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Here we template the resolution, delegating to {@link #resolveDialectInternal} and handling
	 * {@link java.sql.SQLException}s properly.
	 */
	public final Dialect resolveDialect(DatabaseMetaData metaData) {
		try {
			return resolveDialectInternal( metaData );
		}
		catch ( SQLException sqlException ) {
			JDBCException jdbcException = BasicSQLExceptionConverter.INSTANCE.convert( sqlException );
            if (jdbcException instanceof JDBCConnectionException) throw jdbcException;
            LOG.warnf("%s : %s", BasicSQLExceptionConverter.MSG, sqlException.getMessage());
            return null;
		}
		catch ( Throwable t ) {
            LOG.unableToExecuteResolver(this, t.getMessage());
			return null;
		}
	}

	/**
	 * Perform the actual resolution without caring about handling {@link SQLException}s.
	 *
	 * @param metaData The database metadata
	 * @return The resolved dialect, or null if we could not resolve.
	 * @throws SQLException Indicates problems accessing the metadata.
	 */
	protected abstract Dialect resolveDialectInternal(DatabaseMetaData metaData) throws SQLException;
}
