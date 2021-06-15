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

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;
import org.hibernate.engine.transaction.spi.TransactionContext;
import org.hibernate.engine.transaction.spi.TransactionEnvironment;
import org.hibernate.engine.transaction.spi.TransactionImplementor;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Steve Ebersole
 */
public class TransactionContextImpl implements TransactionContext {
	private final TransactionEnvironment transactionEnvironment;
	private final JdbcConnectionAccess jdbcConnectionAccess;

	public TransactionContextImpl(TransactionEnvironment transactionEnvironment, JdbcConnectionAccess jdbcConnectionAccess) {
		this.transactionEnvironment = transactionEnvironment;
		this.jdbcConnectionAccess = jdbcConnectionAccess;
	}

	public TransactionContextImpl(TransactionEnvironment transactionEnvironment, ServiceRegistry serviceRegistry) {
		this( transactionEnvironment, new JdbcConnectionAccessImpl( serviceRegistry ) );
	}

	public TransactionContextImpl(TransactionEnvironment transactionEnvironment) {
		this( transactionEnvironment, new JdbcConnectionAccessImpl( transactionEnvironment.getJdbcServices().getConnectionProvider() ) );
	}

	@Override
	public TransactionEnvironment getTransactionEnvironment() {
		return transactionEnvironment;
	}

	@Override
	public ConnectionReleaseMode getConnectionReleaseMode() {
		return transactionEnvironment.getTransactionFactory().getDefaultReleaseMode();
	}

	@Override
	public JdbcConnectionAccess getJdbcConnectionAccess() {
		return jdbcConnectionAccess;
	}

	@Override
	public boolean shouldAutoJoinTransaction() {
		return true;
	}

	@Override
	public boolean isAutoCloseSessionEnabled() {
		return false;
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public boolean isFlushModeNever() {
		return false;
	}

	@Override
	public boolean isFlushBeforeCompletionEnabled() {
		return true;
	}

	@Override
	public void managedFlush() {
	}

	@Override
	public boolean shouldAutoClose() {
		return false;
	}

	@Override
	public void managedClose() {
	}

	@Override
	public void afterTransactionBegin(TransactionImplementor hibernateTransaction) {
	}

	@Override
	public void beforeTransactionCompletion(TransactionImplementor hibernateTransaction) {
	}

	@Override
	public void afterTransactionCompletion(TransactionImplementor hibernateTransaction, boolean successful) {
	}

	@Override
	public String onPrepareStatement(String sql) {
		return sql;
	}
}
