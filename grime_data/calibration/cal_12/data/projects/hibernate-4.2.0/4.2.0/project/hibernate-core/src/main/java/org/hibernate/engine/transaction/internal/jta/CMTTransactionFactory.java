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

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.TransactionException;
import org.hibernate.engine.transaction.spi.TransactionCoordinator;
import org.hibernate.engine.transaction.spi.TransactionFactory;

/**
 * Factory for Container Managed Transaction (CMT) based transaction facades.
 *
 * @author Steve Ebersole
 * @author Gavin King
 */
public class CMTTransactionFactory  implements TransactionFactory<CMTTransaction> {
	@Override
	public CMTTransaction createTransaction(TransactionCoordinator transactionCoordinator) {
		return new CMTTransaction( transactionCoordinator );
	}

	@Override
	public boolean canBeDriver() {
		return false;
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
	public boolean isJoinableJtaTransaction(TransactionCoordinator transactionCoordinator, CMTTransaction transaction) {
		try {
			final int status = transactionCoordinator
					.getTransactionContext()
					.getTransactionEnvironment()
					.getJtaPlatform()
					.getCurrentStatus();
			return JtaStatusHelper.isActive( status );
		}
		catch( SystemException se ) {
			throw new TransactionException( "Unable to check transaction status", se );
		}
	}

}
