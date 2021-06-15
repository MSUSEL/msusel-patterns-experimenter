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
package org.hibernate.engine.transaction.internal.jta;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
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
 * An isolation delegate for JTA environments.
 *
 * @author Steve Ebersole
 */
public class JtaIsolationDelegate implements IsolationDelegate {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, JtaIsolationDelegate.class.getName());

	private final TransactionCoordinator transactionCoordinator;

	public JtaIsolationDelegate(TransactionCoordinator transactionCoordinator) {
		this.transactionCoordinator = transactionCoordinator;
	}

	protected TransactionManager transactionManager() {
		return transactionCoordinator.getTransactionContext()
				.getTransactionEnvironment()
				.getJtaPlatform()
				.retrieveTransactionManager();
	}

	protected JdbcConnectionAccess jdbcConnectionAccess() {
		return transactionCoordinator.getTransactionContext().getJdbcConnectionAccess();
	}

	protected SqlExceptionHelper sqlExceptionHelper() {
		return transactionCoordinator.getTransactionContext()
				.getTransactionEnvironment()
				.getJdbcServices()
				.getSqlExceptionHelper();
	}

	@Override
	public <T> T delegateWork(WorkExecutorVisitable<T> work, boolean transacted) throws HibernateException {
		TransactionManager transactionManager = transactionManager();

		try {
			// First we suspend any current JTA transaction
			Transaction surroundingTransaction = transactionManager.suspend();
			LOG.debugf( "Surrounding JTA transaction suspended [%s]", surroundingTransaction );

			boolean hadProblems = false;
			try {
				// then perform the requested work
				if ( transacted ) {
					return doTheWorkInNewTransaction( work, transactionManager );
				}
				else {
					return doTheWorkInNoTransaction( work );
				}
			}
			catch ( HibernateException e ) {
				hadProblems = true;
				throw e;
			}
			finally {
				try {
					transactionManager.resume( surroundingTransaction );
					LOG.debugf( "Surrounding JTA transaction resumed [%s]", surroundingTransaction );
				}
				catch( Throwable t ) {
					// if the actually work had an error use that, otherwise error based on t
					if ( !hadProblems ) {
						//noinspection ThrowFromFinallyBlock
						throw new HibernateException( "Unable to resume previously suspended transaction", t );
					}
				}
			}
		}
		catch ( SystemException e ) {
			throw new HibernateException( "Unable to suspend current JTA transaction", e );
		}
	}

	private <T> T doTheWorkInNewTransaction(WorkExecutorVisitable<T> work, TransactionManager transactionManager) {
		try {
			// start the new isolated transaction
			transactionManager.begin();

			try {
				T result = doTheWork( work );
				// if everything went ok, commit the isolated transaction
				transactionManager.commit();
				return result;
			}
			catch ( Exception e ) {
				try {
					transactionManager.rollback();
				}
				catch ( Exception ignore ) {
					LOG.unableToRollbackIsolatedTransaction( e, ignore );
				}
				throw new HibernateException( "Could not apply work", e );
			}
		}
		catch ( SystemException e ) {
			throw new HibernateException( "Unable to start isolated transaction", e );
		}
		catch ( NotSupportedException e ) {
			throw new HibernateException( "Unable to start isolated transaction", e );
		}
	}

	private <T> T doTheWorkInNoTransaction(WorkExecutorVisitable<T> work) {
		return doTheWork( work );
	}

	private <T> T doTheWork(WorkExecutorVisitable<T> work) {
		try {
			// obtain our isolated connection
			Connection connection = jdbcConnectionAccess().obtainConnection();
			try {
				// do the actual work
				return work.accept( new WorkExecutor<T>(), connection );
			}
			catch ( HibernateException e ) {
				throw e;
			}
			catch ( Exception e ) {
				throw new HibernateException( "Unable to perform isolated work", e );
			}
			finally {
				try {
					// no matter what, release the connection (handle)
					jdbcConnectionAccess().releaseConnection( connection );
				}
				catch ( Throwable ignore ) {
					LOG.unableToReleaseIsolatedConnection( ignore );
				}
			}
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "unable to obtain isolated JDBC connection" );
		}
	}
}

