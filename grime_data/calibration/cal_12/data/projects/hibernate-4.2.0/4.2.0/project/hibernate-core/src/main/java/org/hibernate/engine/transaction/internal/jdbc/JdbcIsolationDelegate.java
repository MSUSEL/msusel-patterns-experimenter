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
package org.hibernate.engine.transaction.internal.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.transaction.spi.IsolationDelegate;
import org.hibernate.engine.transaction.spi.TransactionCoordinator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.jdbc.WorkExecutor;
import org.hibernate.jdbc.WorkExecutorVisitable;

/**
 * The isolation delegate for JDBC {@link Connection} based transactions
 *
 * @author Steve Ebersole
 */
public class JdbcIsolationDelegate implements IsolationDelegate {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, JdbcIsolationDelegate.class.getName());

	private final TransactionCoordinator transactionCoordinator;

	public JdbcIsolationDelegate(TransactionCoordinator transactionCoordinator) {
		this.transactionCoordinator = transactionCoordinator;
	}

	protected JdbcConnectionAccess jdbcConnectionAccess() {
		return transactionCoordinator.getTransactionContext().getJdbcConnectionAccess();
	}

	protected SqlExceptionHelper sqlExceptionHelper() {
		return transactionCoordinator.getJdbcCoordinator().getLogicalConnection().getJdbcServices().getSqlExceptionHelper();
	}

	@Override
	public <T> T delegateWork(WorkExecutorVisitable<T> work, boolean transacted) throws HibernateException {
		boolean wasAutoCommit = false;
		try {
			Connection connection = jdbcConnectionAccess().obtainConnection();
			try {
				if ( transacted ) {
					if ( connection.getAutoCommit() ) {
						wasAutoCommit = true;
						connection.setAutoCommit( false );
					}
				}

				T result = work.accept( new WorkExecutor<T>(), connection );

				if ( transacted ) {
					connection.commit();
				}

				return result;
			}
			catch ( Exception e ) {
				try {
					if ( transacted && !connection.isClosed() ) {
						connection.rollback();
					}
				}
				catch ( Exception ignore ) {
					LOG.unableToRollbackConnection( ignore );
				}

				if ( e instanceof HibernateException ) {
					throw (HibernateException) e;
				}
				else if ( e instanceof SQLException ) {
					throw sqlExceptionHelper().convert( (SQLException) e, "error performing isolated work" );
				}
				else {
					throw new HibernateException( "error performing isolated work", e );
				}
			}
			finally {
				if ( transacted && wasAutoCommit ) {
					try {
						connection.setAutoCommit( true );
					}
					catch ( Exception ignore ) {
						LOG.trace( "was unable to reset connection back to auto-commit" );
					}
				}
				try {
					jdbcConnectionAccess().releaseConnection( connection );
				}
				catch ( Exception ignore ) {
					LOG.unableToReleaseIsolatedConnection( ignore );
				}
			}
		}
		catch ( SQLException sqle ) {
			throw sqlExceptionHelper().convert( sqle, "unable to obtain isolated JDBC connection" );
		}
	}
}
