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

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.hibernate.TransactionException;
import org.hibernate.engine.transaction.spi.AbstractTransactionImpl;
import org.hibernate.engine.transaction.spi.IsolationDelegate;
import org.hibernate.engine.transaction.spi.JoinStatus;
import org.hibernate.engine.transaction.spi.TransactionCoordinator;

/**
 * Implements a transaction strategy for Container Managed Transaction (CMT) scenarios.  All work is done in
 * the context of the container managed transaction.
 * <p/>
 * The term 'CMT' is potentially misleading; the pertinent point simply being that the transactions are being
 * managed by something other than the Hibernate transaction mechanism.
 * <p/>
 * Additionally, this strategy does *not* attempt to access or use the {@link javax.transaction.UserTransaction} since
 * in the actual case CMT access to the {@link javax.transaction.UserTransaction} is explicitly disallowed.  Instead
 * we use the JTA {@link javax.transaction.Transaction} object obtained from the {@link TransactionManager}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class CMTTransaction extends AbstractTransactionImpl {
	private JoinStatus joinStatus = JoinStatus.NOT_JOINED;

	protected CMTTransaction(TransactionCoordinator transactionCoordinator) {
		super( transactionCoordinator );
	}

	protected TransactionManager transactionManager() {
		return jtaPlatform().retrieveTransactionManager();
	}

	private TransactionManager getTransactionManager() {
		return transactionManager();
	}

	@Override
	protected void doBegin() {
		transactionCoordinator().pulse();
	}

	@Override
	protected void afterTransactionBegin() {
		if ( ! transactionCoordinator().isSynchronizationRegistered() ) {
			throw new TransactionException("Could not register synchronization for container transaction");
		}
		transactionCoordinator().sendAfterTransactionBeginNotifications( this );
		transactionCoordinator().getTransactionContext().afterTransactionBegin( this );
	}

	@Override
	protected void beforeTransactionCommit() {
		boolean flush = ! transactionCoordinator().getTransactionContext().isFlushModeNever() &&
				! transactionCoordinator().getTransactionContext().isFlushBeforeCompletionEnabled();
		if ( flush ) {
			// if an exception occurs during flush, user must call rollback()
			transactionCoordinator().getTransactionContext().managedFlush();
		}
	}

	@Override
	protected void doCommit() {
		// nothing to do
	}

	@Override
	protected void beforeTransactionRollBack() {
		// nothing to do
	}

	@Override
	protected void doRollback() {
		markRollbackOnly();
	}

	@Override
	protected void afterTransactionCompletion(int status) {
		// nothing to do
	}

	@Override
	protected void afterAfterCompletion() {
		// nothing to do
	}

	@Override
	public boolean isActive() throws TransactionException {
		return JtaStatusHelper.isActive( getTransactionManager() );
	}

	@Override
	public IsolationDelegate createIsolationDelegate() {
		return new JtaIsolationDelegate( transactionCoordinator() );
	}

	@Override
	public boolean isInitiator() {
		return false; // cannot be
	}

	@Override
	public void markRollbackOnly() {
		try {
			getTransactionManager().setRollbackOnly();
		}
		catch ( SystemException se ) {
			throw new TransactionException("Could not set transaction to rollback only", se);
		}
	}

	@Override
	public void markForJoin() {
		joinStatus = JoinStatus.MARKED_FOR_JOINED;
	}

	@Override
	public void join() {
		if ( joinStatus != JoinStatus.MARKED_FOR_JOINED ) {
			return;
		}

		if ( JtaStatusHelper.isActive( transactionManager() ) ) {
			// register synchronization if needed
			transactionCoordinator().pulse();
			joinStatus = JoinStatus.JOINED;
		}
		else {
			joinStatus = JoinStatus.NOT_JOINED;
		}
	}

	@Override
	public void resetJoinStatus() {
		joinStatus = JoinStatus.NOT_JOINED;
	}

	boolean isJoinable() {
		return ( joinStatus == JoinStatus.JOINED || joinStatus == JoinStatus.MARKED_FOR_JOINED ) &&
				JtaStatusHelper.isActive( transactionManager() );
	}

	@Override
	public JoinStatus getJoinStatus() {
		return joinStatus;
	}
}
