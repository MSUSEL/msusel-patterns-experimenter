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
package org.hibernate.engine.transaction.spi;

import javax.transaction.Status;
import javax.transaction.Synchronization;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.TransactionException;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.jta.platform.spi.JtaPlatform;

/**
 * Abstract support for creating {@link TransactionImplementor transaction} implementations
 *
 * @author Steve Ebersole
 */
public abstract class AbstractTransactionImpl implements TransactionImplementor {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       AbstractTransactionImpl.class.getName());

	private final TransactionCoordinator transactionCoordinator;

	private boolean valid = true;

	private LocalStatus localStatus = LocalStatus.NOT_ACTIVE;
	private int timeout = -1;

	protected AbstractTransactionImpl(TransactionCoordinator transactionCoordinator) {
		this.transactionCoordinator = transactionCoordinator;
	}

	@Override
	public void invalidate() {
		valid = false;
	}

	/**
	 * Perform the actual steps of beginning a transaction according to the strategy.
	 *
	 * @throws org.hibernate.TransactionException Indicates a problem beginning the transaction
	 */
	protected abstract void doBegin();

	/**
	 * Perform the actual steps of committing a transaction according to the strategy.
	 *
	 * @throws org.hibernate.TransactionException Indicates a problem committing the transaction
	 */
	protected abstract void doCommit();

	/**
	 * Perform the actual steps of rolling back a transaction according to the strategy.
	 *
	 * @throws org.hibernate.TransactionException Indicates a problem rolling back the transaction
	 */
	protected abstract void doRollback();

	protected abstract void afterTransactionBegin();
	protected abstract void beforeTransactionCommit();
	protected abstract void beforeTransactionRollBack();
	protected abstract void afterTransactionCompletion(int status);
	protected abstract void afterAfterCompletion();

	/**
	 * Provide subclasses with access to the transaction coordinator.
	 *
	 * @return This transaction's context.
	 */
	protected TransactionCoordinator transactionCoordinator() {
		return transactionCoordinator;
	}

	/**
	 * Provide subclasses with convenient access to the configured {@link JtaPlatform}
	 *
	 * @return The {@link org.hibernate.service.jta.platform.spi.JtaPlatform}
	 */
	protected JtaPlatform jtaPlatform() {
		return transactionCoordinator().getTransactionContext().getTransactionEnvironment().getJtaPlatform();
	}

	@Override
	public void registerSynchronization(Synchronization synchronization) {
		transactionCoordinator().getSynchronizationRegistry().registerSynchronization( synchronization );
	}

	@Override
	public LocalStatus getLocalStatus() {
		return localStatus;
	}

	@Override
	public boolean isActive() {
		return localStatus == LocalStatus.ACTIVE && doExtendedActiveCheck();
	}

	@Override
	public boolean isParticipating() {
		return getJoinStatus() == JoinStatus.JOINED && isActive();
	}

	@Override
	public boolean wasCommitted() {
		return localStatus == LocalStatus.COMMITTED;
	}

	@Override
	public boolean wasRolledBack() throws HibernateException {
		return localStatus == LocalStatus.ROLLED_BACK;
	}

	/**
	 * Active has been checked against local state.  Perform any needed checks against resource transactions.
	 *
	 * @return {@code true} if the extended active check checks out as well; false otherwise.
	 */
	protected boolean doExtendedActiveCheck() {
		return true;
	}

	@Override
	public void begin() throws HibernateException {
		if ( ! valid ) {
			throw new TransactionException( "Transaction instance is no longer valid" );
		}
		if ( localStatus == LocalStatus.ACTIVE ) {
			throw new TransactionException( "nested transactions not supported" );
		}
		if ( localStatus != LocalStatus.NOT_ACTIVE ) {
			throw new TransactionException( "reuse of Transaction instances not supported" );
		}

		LOG.debug( "begin" );

		doBegin();

		localStatus = LocalStatus.ACTIVE;

		afterTransactionBegin();
	}

	@Override
	public void commit() throws HibernateException {
		if ( localStatus != LocalStatus.ACTIVE ) {
			throw new TransactionException( "Transaction not successfully started" );
		}

		LOG.debug( "committing" );

		beforeTransactionCommit();

		try {
			doCommit();
			localStatus = LocalStatus.COMMITTED;
			afterTransactionCompletion( Status.STATUS_COMMITTED );
		}
		catch ( Exception e ) {
			localStatus = LocalStatus.FAILED_COMMIT;
			afterTransactionCompletion( Status.STATUS_UNKNOWN );
			throw new TransactionException( "commit failed", e );
		}
		finally {
			invalidate();
			afterAfterCompletion();
		}
	}

	protected boolean allowFailedCommitToPhysicallyRollback() {
		return false;
	}

	@Override
	public void rollback() throws HibernateException {
		if ( localStatus != LocalStatus.ACTIVE && localStatus != LocalStatus.FAILED_COMMIT ) {
			throw new TransactionException( "Transaction not successfully started" );
		}

		LOG.debug( "rolling back" );

		beforeTransactionRollBack();

		if ( localStatus != LocalStatus.FAILED_COMMIT || allowFailedCommitToPhysicallyRollback() ) {
			try {
				doRollback();
				localStatus = LocalStatus.ROLLED_BACK;
				afterTransactionCompletion( Status.STATUS_ROLLEDBACK );
			}
			catch ( Exception e ) {
				afterTransactionCompletion( Status.STATUS_UNKNOWN );
				throw new TransactionException( "rollback failed", e );
			}
			finally {
				invalidate();
				afterAfterCompletion();
			}
		}

	}

	@Override
	public void setTimeout(int seconds) {
		timeout = seconds;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void markForJoin() {
		// generally speaking this is no-op
	}

	@Override
	public void join() {
		// generally speaking this is no-op
	}

	@Override
	public void resetJoinStatus() {
		// generally speaking this is no-op
	}
}
