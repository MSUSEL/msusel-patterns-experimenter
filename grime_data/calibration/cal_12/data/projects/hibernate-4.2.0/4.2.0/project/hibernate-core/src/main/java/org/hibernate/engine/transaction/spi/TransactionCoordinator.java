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

import java.io.Serializable;
import java.sql.Connection;

import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.transaction.synchronization.spi.SynchronizationCallbackCoordinator;

/**
 * Acts as the coordinator between the Hibernate engine and physical transactions.
 *
 * @author Steve Ebersole
 */
public interface TransactionCoordinator extends Serializable {
	/**
	 * Retrieves the context in which this coordinator operates.
	 *
	 * @return The context of the coordinator
	 */
	public TransactionContext getTransactionContext();

	/**
	 * Retrieves the JDBC coordinator currently operating within this transaction coordinator.
	 *
	 * @return The JDBC coordinator.
	 */
	public JdbcCoordinator getJdbcCoordinator();

	/**
	 * Get the Hibernate transaction facade object currently associated with this coordinator.
	 *
	 * @return The current Hibernate transaction.
	 */
	public TransactionImplementor getTransaction();

	/**
	 * Obtain the {@link javax.transaction.Synchronization} registry associated with this coordinator.
	 *
	 * @return The registry
	 */
	public SynchronizationRegistry getSynchronizationRegistry();

	/**
	 * Adds an observer to the coordinator.
	 * <p/>
	 * Unlike synchronizations added to the {@link #getSynchronizationRegistry() registry}, observers are not to be
	 * cleared on transaction completion.
	 *
	 * @param observer The observer to add.
	 */
	public void addObserver(TransactionObserver observer);

	/**
	 * Removed an observer from the coordinator.
	 *
	 * @param observer The observer to remove.
	 */
	public void removeObserver(TransactionObserver observer);
	
	/**
	 * Can we join to the underlying transaction?
	 *
	 * @return {@literal true} if the underlying transaction can be joined or is already joined; {@literal false}
	 * otherwise.
	 *
	 * @see TransactionFactory#isJoinableJtaTransaction(TransactionCoordinator, TransactionImplementor)
	 */
	public boolean isTransactionJoinable();

	/**
	 * Is the underlying transaction already joined?
	 *
	 * @return {@literal true} if the underlying transaction is already joined; {@literal false} otherwise.
	 */
	public boolean isTransactionJoined();

	/**
	 * Reset the transaction's join status.
	 */
	public void resetJoinStatus();

	/**
	 * Are we "in" an active and joined transaction
	 *
	 * @return {@literal true} if there is currently a transaction in progress; {@literal false} otherwise.
	 */
	public boolean isTransactionInProgress();

	/**
	 * Attempts to register JTA synchronization if possible and needed.
	 */
	public void pulse();

	/**
	 * Close the transaction context, returning any user supplied connection from the underlying JDBC coordinator.
	 *
	 * @return The user supplied connection (if one).
	 */
	public Connection close();

	/**
	 * Performs actions needed after execution of a non-transactional query.
	 *
	 * @param success Was the query successfully performed
	 */
	public void afterNonTransactionalQuery(boolean success);

	public void setRollbackOnly();

	public SynchronizationCallbackCoordinator getSynchronizationCallbackCoordinator();

	public boolean isSynchronizationRegistered();
	public boolean takeOwnership();

	public void afterTransaction(TransactionImplementor hibernateTransaction, int status);

	public void sendAfterTransactionBeginNotifications(TransactionImplementor hibernateTransaction);
	public void sendBeforeTransactionCompletionNotifications(TransactionImplementor hibernateTransaction);
	public void sendAfterTransactionCompletionNotifications(TransactionImplementor hibernateTransaction, int status);

}