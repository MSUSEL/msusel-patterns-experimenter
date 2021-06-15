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
package org.hibernate.engine.jdbc.internal;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.jdbc.spi.ResultSetReturn;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

/**
 * @author Brett Meyer
 */
public class ResultSetReturnImpl implements ResultSetReturn {

	private final JdbcCoordinator jdbcCoordinator;

	public ResultSetReturnImpl(JdbcCoordinator jdbcCoordinator) {
		this.jdbcCoordinator = jdbcCoordinator;
	}

	@Override
	public ResultSet extract(PreparedStatement statement) {
		// sql logged by StatementPreparerImpl
		if ( statement instanceof CallableStatement ) {
			// We actually need to extract from Callable statement.  Although
			// this seems needless, Oracle can return an
			// OracleCallableStatementWrapper that finds its way to this method,
			// rather than extract(CallableStatement).  See HHH-8022.
			CallableStatement callableStatement = (CallableStatement) statement;
			return extract( callableStatement );
		}
		try {
			ResultSet rs = statement.executeQuery();
			postExtract( rs );
			return rs;
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not extract ResultSet" );
		}
	}

	@Override
	public ResultSet extract(CallableStatement statement) {
		try {
			// sql logged by StatementPreparerImpl
			ResultSet rs = jdbcCoordinator.getLogicalConnection().getJdbcServices()
					.getDialect().getResultSet( statement );
			postExtract( rs );
			return rs;
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not extract ResultSet" );
		}
	}

	@Override
	public ResultSet extract(Statement statement, String sql) {
		jdbcCoordinator.getLogicalConnection().getJdbcServices()
				.getSqlStatementLogger().logStatement( sql );
		try {
			ResultSet rs = statement.executeQuery( sql );
			postExtract( rs );
			return rs;
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not extract ResultSet" );
		}
	}

	@Override
	public ResultSet execute(PreparedStatement statement) {
		// sql logged by StatementPreparerImpl
		try {
			if ( !statement.execute() ) {
				while ( !statement.getMoreResults() && statement.getUpdateCount() != -1 ) {
					// do nothing until we hit the resultset
				}
			}
			ResultSet rs = statement.getResultSet();
			postExtract( rs );
			return rs;
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not execute statement" );
		}
	}

	@Override
	public ResultSet execute(Statement statement, String sql) {
		jdbcCoordinator.getLogicalConnection().getJdbcServices()
				.getSqlStatementLogger().logStatement( sql );
		try {
			if ( !statement.execute( sql ) ) {
				while ( !statement.getMoreResults() && statement.getUpdateCount() != -1 ) {
					// do nothing until we hit the resultset
				}
			}
			ResultSet rs = statement.getResultSet();
			postExtract( rs );
			return rs;
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not execute statement" );
		}
	}
	
	@Override
	public int executeUpdate( PreparedStatement statement ) {
		try {
			return statement.executeUpdate();
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not execute statement" );
		}
	}
	
	@Override
	public int executeUpdate( Statement statement, String sql ) {
		jdbcCoordinator.getLogicalConnection().getJdbcServices()
				.getSqlStatementLogger().logStatement( sql );
		try {
			return statement.executeUpdate( sql );
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not execute statement" );
		}
	}

	private final SqlExceptionHelper sqlExceptionHelper() {
		return jdbcCoordinator.getTransactionCoordinator()
				.getTransactionContext()
				.getTransactionEnvironment()
				.getJdbcServices()
				.getSqlExceptionHelper();
	}

	private void postExtract(ResultSet rs) {
		if ( rs != null ) jdbcCoordinator.register( rs );
	}

}
