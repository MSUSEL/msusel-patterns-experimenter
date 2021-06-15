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
package org.hibernate.service.jta.platform.spi;

import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.service.Service;

/**
 * Defines how we interact with various JTA services on the given platform/environment.
 *
 * @author Steve Ebersole
 */
public interface JtaPlatform extends Service {

	/**
	 * Locate the {@link TransactionManager}
	 *
	 * @return The {@link TransactionManager}
	 */
	public TransactionManager retrieveTransactionManager();

	/**
	 * Locate the {@link UserTransaction}
	 *
	 * @return The {@link UserTransaction}
	 */
	public UserTransaction retrieveUserTransaction();

	/**
	 * Determine an identifier for the given transaction appropriate for use in caching/lookup usages.
	 * <p/>
	 * Generally speaking the transaction itself will be returned here.  This method was added specifically
	 * for use in WebSphere and other unfriendly JEE containers (although WebSphere is still the only known
	 * such brain-dead, sales-driven impl).
	 *
	 * @param transaction The transaction to be identified.
	 * @return An appropriate identifier
	 */
	public Object getTransactionIdentifier(Transaction transaction);

	/**
	 * Can we currently register a {@link Synchronization}?
	 *
	 * @return True if registering a {@link Synchronization} is currently allowed; false otherwise.
	 */
	public boolean canRegisterSynchronization();

	/**
	 * Register a JTA {@link Synchronization} in the means defined by the platform.
	 *
	 * @param synchronization The synchronization to register
	 */
	public void registerSynchronization(Synchronization synchronization);

	/**
	 * Obtain the current transaction status using whatever means is preferred for this platform
	 *
	 * @return The current status.
	 *
	 * @throws SystemException Indicates a problem access the underlying status
	 */
	public int getCurrentStatus() throws SystemException;
}
