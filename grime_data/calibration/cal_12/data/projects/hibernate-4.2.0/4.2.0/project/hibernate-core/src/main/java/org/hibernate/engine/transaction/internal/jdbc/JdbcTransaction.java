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
import org.hibernate.TransactionException;
import org.hibernate.engine.transaction.spi.AbstractTransactionImpl;
import org.hibernate.engine.transaction.spi.IsolationDelegate;
import org.hibernate.engine.transaction.spi.JoinStatus;
import org.hibernate.engine.transaction.spi.LocalStatus;
import org.hibernate.engine.transaction.spi.TransactionCoordinator;
import org.hibernate.internal.CoreMessageLogger;

/**
 * {@link org.hibernate.Transaction} implementation based on transaction management through a JDBC {@link java.sql.Connection}.
 * <p/>
 * This the default transaction strategy.
 *
 * @author Anton van Straaten
 * @author Gavin King
 * @author Steve Ebersole
 */
public class JdbcTransaction extends AbstractTransactionImpl {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, JdbcTransaction.class.getName() );

	private Connection managedConnection;
	private boolean wasInitiallyAutoCommit;
	private boolean isDriver;

	protected JdbcTransaction(TransactionCoordinator transactionCoordinator) {
		super( transactionCoordinator );
	}

	@Override
	protected void doBegin() {
		try {
			if ( managedConnection != null ) {
				throw new TransactionException( "Already have an associated managed connection" );
			}
			managedConnection = transactionCoordinator().getJdbcCoordinator().getLogicalConnection().getConnection();
			wasInitiallyAutoCommit = managedConnection.getAutoCommit();
			LOG.debugv( "initial autocommit status: {0}", wasInitiallyAutoCommit );
			if ( wasInitiallyAutoCommit ) {
				LOG.debug( "disabling autocommit" );
				managedConnection.setAutoCommit( false );
			}
		}
		catch( SQLException e ) {
			throw new TransactionException( "JDBC begin transaction failed: ", e );
		}

		isDriver = transactionCoordinator().takeOwnership();
	}

	@Override
	protected void afterTransactionBegin() {
		if ( getTimeout() > 0 ) {
			transactionCoordinator().getJdbcCoordinator().setTransactionTimeOut( getTimeout() );
		}
		transactionCoordinator().sendAfterTransactionBeginNotifications( this );
		if ( isDriver ) {
			transactionCoordinator().getTransactionContext().afterTransactionBegin( this );
		}
	}

	@Override
	protected void beforeTransactionCommit() {
		transactionCoordinator().sendBeforeTransactionCompletionNotifications( this );

		// basically, if we are the driver of the transaction perform a managed flush prior to
		// physically committing the transaction
		if ( isDriver && !transactionCoordinator().getTransactionContext().isFlushModeNever() ) {
			// if an exception occurs during flush, user must call rollback()
			transactionCoordinator().getTransactionContext().managedFlush();
		}

		if ( isDriver ) {
			transactionCoordinator().getTransactionContext().beforeTransactionCompletion( this );
		}
	}

	@Override
	protected void doCommit() throws TransactionException {
		try {
			managedConnection.commit();
			LOG.debug( "committed JDBC Connection" );
		}
		catch( SQLException e ) {
			throw new TransactionException( "unable to commit against JDBC connection", e );
		}
		finally {
			releaseManagedConnection();
		}
	}

	private void releaseManagedConnection() {
		try {
			if ( wasInitiallyAutoCommit ) {
				LOG.debug( "re-enabling autocommit" );
				managedConnection.setAutoCommit( true );
			}
			managedConnection = null;
		}
		catch ( Exception e ) {
			LOG.debug( "Could not toggle autocommit", e );
		}
	}

	@Override
	protected void afterTransactionCompletion(int status) {
		transactionCoordinator().afterTransaction( this, status );
	}

	@Override
	protected void afterAfterCompletion() {
		if ( isDriver
				&& transactionCoordinator().getTransactionContext().shouldAutoClose()
				&& !transactionCoordinator().getTransactionContext().isClosed() ) {
			try {
				transactionCoordinator().getTransactionContext().managedClose();
			}
			catch (HibernateException e) {
				LOG.unableToCloseSessionButSwallowingError( e );
			}
		}
	}

	@Override
	protected void beforeTransactionRollBack() {
		// nothing to do here
	}

	@Override
	protected void doRollback() throws TransactionException {
		try {
			managedConnection.rollback();
			LOG.debug( "rolled JDBC Connection" );
		}
		catch( SQLException e ) {
			throw new TransactionException( "unable to rollback against JDBC connection", e );
		}
		finally {
			releaseManagedConnection();
		}
	}

	@Override
	public boolean isInitiator() {
		return isActive();
	}

	@Override
	public IsolationDelegate createIsolationDelegate() {
		return new JdbcIsolationDelegate( transactionCoordinator() );
	}

	@Override
	public JoinStatus getJoinStatus() {
		return isActive() ? JoinStatus.JOINED : JoinStatus.NOT_JOINED;
	}

	@Override
	public void markRollbackOnly() {
		// nothing to do here
	}

	@Override
	public void join() {
		// nothing to do
	}

	@Override
	public void resetJoinStatus() {
		// nothing to do
	}

	@Override
	public boolean isActive() throws HibernateException {
		return getLocalStatus() == LocalStatus.ACTIVE;
	}
}