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
import javax.transaction.UserTransaction;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.TransactionException;
import org.hibernate.engine.transaction.spi.TransactionCoordinator;
import org.hibernate.engine.transaction.spi.TransactionFactory;
import org.hibernate.service.jta.platform.spi.JtaPlatform;

/**
 * Factory for {@link JtaTransaction} instances.
 *
 * @author Gavin King
 * @author Steve Ebersole
 * @author Les Hazlewood
 */
public class JtaTransactionFactory implements TransactionFactory<JtaTransaction> {
	@Override
	public JtaTransaction createTransaction(TransactionCoordinator transactionCoordinator) {
		return new JtaTransaction( transactionCoordinator );
	}

	@Override
	public boolean canBeDriver() {
		return true;
	}

	@Override
	public ConnectionReleaseMode getDefaultReleaseMode() {
		return ConnectionReleaseMode.AFTER_STATEMENT;
	}

	@Override
	public boolean compatibleWithJtaSynchronization() {
		return true;
	}

	@Override
	public boolean isJoinableJtaTransaction(TransactionCoordinator transactionCoordinator, JtaTransaction transaction) {
		try {
			// Essentially:
			// 1) If we have a local (Hibernate) transaction in progress
			//      and it already has the UserTransaction cached, use that
			//      UserTransaction to determine the status.
			// 2) If a transaction manager has been located, use
			//      that transaction manager to determine the status.
			// 3) Finally, as the last resort, try to lookup the
			//      UserTransaction via JNDI and use that to determine the
			//      status.
			if ( transaction != null ) {
				UserTransaction ut = transaction.getUserTransaction();
				if ( ut != null ) {
					return JtaStatusHelper.isActive( ut );
				}
			}

			final JtaPlatform jtaPlatform = transactionCoordinator
					.getTransactionContext()
					.getTransactionEnvironment()
					.getJtaPlatform();
			if ( jtaPlatform == null ) {
				throw new TransactionException( "Unable to check transaction status" );
			}
			if ( jtaPlatform.retrieveTransactionManager() != null ) {
				return JtaStatusHelper.isActive( jtaPlatform.retrieveTransactionManager().getStatus() );
			}
			else {
				final UserTransaction ut = jtaPlatform.retrieveUserTransaction();
				return ut != null && JtaStatusHelper.isActive( ut );
			}
		}
		catch ( SystemException se ) {
			throw new TransactionException( "Unable to check transaction status", se );
		}
	}

}