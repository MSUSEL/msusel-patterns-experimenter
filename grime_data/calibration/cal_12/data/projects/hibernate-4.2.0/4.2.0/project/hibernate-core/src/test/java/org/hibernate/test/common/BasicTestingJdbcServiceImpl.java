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
package org.hibernate.test.common;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.LobCreationContext;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.engine.jdbc.internal.ResultSetWrapperImpl;
import org.hibernate.engine.jdbc.internal.TypeInfo;
import org.hibernate.engine.jdbc.spi.ExtractedDatabaseMetaData;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.ResultSetWrapper;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Stoppable;
import org.hibernate.testing.env.ConnectionProviderBuilder;


/**
 * Implementation of the {@link JdbcServices} contract for use by these
 * tests.
 *
 * @author Steve Ebersole
 */
public class BasicTestingJdbcServiceImpl implements JdbcServices {
	private ConnectionProvider connectionProvider;
	private Dialect dialect;
	private SqlStatementLogger sqlStatementLogger;
	private SqlExceptionHelper exceptionHelper;
	private final ExtractedDatabaseMetaData metaDataSupport = new MetaDataSupportImpl();
	private final ResultSetWrapper resultSetWrapper = ResultSetWrapperImpl.INSTANCE;

	public void start() {
	}

	public void stop() {
		release();
	}

	public void prepare(boolean allowAggressiveRelease) {
		connectionProvider = ConnectionProviderBuilder.buildConnectionProvider( allowAggressiveRelease );
		dialect = ConnectionProviderBuilder.getCorrespondingDialect();
		sqlStatementLogger = new SqlStatementLogger( true, false );
		exceptionHelper = new SqlExceptionHelper();

	}

	public void release() {
		if ( connectionProvider instanceof Stoppable ) {
			( (Stoppable) connectionProvider ).stop();
		}
	}

	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public LobCreator getLobCreator(LobCreationContext lobCreationContext) {
		return null;
	}

	public ResultSetWrapper getResultSetWrapper() {
		return null;
	}

	public SqlStatementLogger getSqlStatementLogger() {
		return sqlStatementLogger;
	}

	public SqlExceptionHelper getSqlExceptionHelper() {
		return exceptionHelper;
	}

	public ExtractedDatabaseMetaData getExtractedMetaDataSupport() {
		return metaDataSupport;
	}

	private static class MetaDataSupportImpl implements ExtractedDatabaseMetaData {
		public boolean supportsScrollableResults() {
			return false;
		}

		public boolean supportsGetGeneratedKeys() {
			return false;
		}

		public boolean supportsBatchUpdates() {
			return false;
		}

		public boolean supportsDataDefinitionInTransaction() {
			return false;
		}

		public boolean doesDataDefinitionCauseTransactionCommit() {
			return false;
		}

		public Set<String> getExtraKeywords() {
			return Collections.emptySet();
		}

		public SQLStateType getSqlStateType() {
			return SQLStateType.UNKOWN;
		}

		public boolean doesLobLocatorUpdateCopy() {
			return false;
		}

		public String getConnectionSchemaName() {
			return null;
		}

		public String getConnectionCatalogName() {
			return null;
		}

		public LinkedHashSet<TypeInfo> getTypeInfoSet() {
			return null;
		}
	}
}
