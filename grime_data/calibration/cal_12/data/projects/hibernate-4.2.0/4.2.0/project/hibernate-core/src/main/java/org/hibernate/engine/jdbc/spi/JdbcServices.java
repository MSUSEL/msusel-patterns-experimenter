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

import java.sql.ResultSet;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.LobCreationContext;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.service.Service;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

/**
 * Contract for services around JDBC operations.  These represent shared resources, aka not varied by session/use.
 *
 * @author Steve Ebersole
 */
public interface JdbcServices extends Service {
	/**
	 * Obtain service for providing JDBC connections.
	 *
	 * @return The connection provider.
	 *
	 * @deprecated See deprecation notice on {@link org.hibernate.engine.spi.SessionFactoryImplementor#getConnectionProvider()}
	 * for details
	 */
	@Deprecated
	public ConnectionProvider getConnectionProvider();

	/**
	 * Obtain the dialect of the database.
	 *
	 * @return The database dialect.
	 */
	public Dialect getDialect();

	/**
	 * Obtain service for logging SQL statements.
	 *
	 * @return The SQL statement logger.
	 */
	public SqlStatementLogger getSqlStatementLogger();

	/**
	 * Obtain service for dealing with exceptions.
	 *
	 * @return The exception helper service.
	 */
	public SqlExceptionHelper getSqlExceptionHelper();

	/**
	 * Obtain information about supported behavior reported by the JDBC driver.
	 * <p/>
	 * Yuck, yuck, yuck!  Much prefer this to be part of a "basic settings" type object.
	 * 
	 * @return The extracted database metadata, oddly enough :)
	 */
	public ExtractedDatabaseMetaData getExtractedMetaDataSupport();

	/**
	 * Create an instance of a {@link LobCreator} appropriate for the current environment, mainly meant to account for
	 * variance between JDBC 4 (<= JDK 1.6) and JDBC3 (>= JDK 1.5).
	 *
	 * @param lobCreationContext The context in which the LOB is being created
	 * @return The LOB creator.
	 */
	public LobCreator getLobCreator(LobCreationContext lobCreationContext);

	/**
	 * Obtain service for wrapping a {@link ResultSet} in a "column name cache" wrapper.
	 * @return The ResultSet wrapper.
	 */
	public ResultSetWrapper getResultSetWrapper();
}
