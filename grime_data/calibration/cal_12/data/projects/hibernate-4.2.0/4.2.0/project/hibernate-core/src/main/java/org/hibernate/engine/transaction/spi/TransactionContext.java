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

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;

/**
 * Access to services needed in the context of processing transaction requests.
 * <p/>
 * The context is roughly speaking equivalent to the Hibernate session, as opposed to the {@link TransactionEnvironment}
 * which is roughly equivalent to the Hibernate session factory
 * 
 * @author Steve Ebersole
 */
public interface TransactionContext extends Serializable {
	/**
	 * Obtain the {@link TransactionEnvironment} associated with this context.
	 *
	 * @return The transaction environment.
	 */
	public TransactionEnvironment getTransactionEnvironment();

	/**
	 * Get the mode for releasing JDBC connection in effect for ths context.
	 *
	 * @return The connection release mode.
	 */
	public ConnectionReleaseMode getConnectionReleaseMode();

	/**
	 * Should transactions be auto joined?  Generally this is only a concern for CMT transactions.  The default
	 * should be to auto join.  JPA defines an explicit operation for joining a CMT transaction.
	 *
	 * @return Should we automatically join transactions
	 */
	public boolean shouldAutoJoinTransaction();

	/**
	 * Should session automatically be closed after transaction completion in this context?
	 *
	 * @return {@literal true}/{@literal false} appropriately.
	 */
	public boolean isAutoCloseSessionEnabled();

	/**
	 * Is this context already closed?
	 *
	 * @return {@literal true}/{@literal false} appropriately.
	 */
	public boolean isClosed();

	/**
	 * Should flushes only happen manually for this context?
	 *
	 * @return {@literal true}/{@literal false} appropriately.
	 */
	public boolean isFlushModeNever();

	/**
	 * Should before transaction completion processing perform a flush when initiated from JTA synchronization for this
	 * context?
	 *
	 * @return {@literal true}/{@literal false} appropriately.
	 */
	public boolean isFlushBeforeCompletionEnabled();

	/**
	 * Perform a managed flush.
	 */
	public void managedFlush();

	/**
	 * Should JTA synchronization processing perform a automatic close (call to {@link #managedClose} for this
	 * context?
	 * 
	 * @return {@literal true}/{@literal false} appropriately.
	 */
	public boolean shouldAutoClose();

	/**
	 * Perform a managed close.
	 */
	public void managedClose();

	public void afterTransactionBegin(TransactionImplementor hibernateTransaction);

	public void beforeTransactionCompletion(TransactionImplementor hibernateTransaction);

	public void afterTransactionCompletion(TransactionImplementor hibernateTransaction, boolean successful);

	public String onPrepareStatement(String sql); 

	public JdbcConnectionAccess getJdbcConnectionAccess();
}
