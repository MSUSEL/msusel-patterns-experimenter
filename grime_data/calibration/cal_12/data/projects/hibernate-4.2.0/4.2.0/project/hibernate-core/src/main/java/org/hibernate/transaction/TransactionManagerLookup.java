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
package org.hibernate.transaction;
import java.util.Properties;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.hibernate.HibernateException;

/**
 * Contract for locating the JTA {@link TransactionManager} on given platform.
 * <p/>
 * NOTE: this contract has expanded over time, and basically is a platform
 * abstraction contract for JTA-related information.
 *
 * @author Gavin King
 */
public interface TransactionManagerLookup {

	/**
	 * Obtain the JTA {@link TransactionManager}.
	 *
	 * @param props The configuration properties.
	 * @return The JTA {@link TransactionManager}.
	 *
	 * @throws HibernateException Indicates problem locating {@link TransactionManager}.
	 */
	public TransactionManager getTransactionManager(Properties props) throws HibernateException;

	/**
	 * Return the JNDI namespace of the JTA
	 * {@link javax.transaction.UserTransaction} for this platform or <tt>null</tt>;
	 * optional operation.
	 *
	 * @return The JNDI namespace where we can locate the
	 * {@link javax.transaction.UserTransaction} for this platform.
	 */
	public String getUserTransactionName();

	/**
	 * Determine an identifier for the given transaction appropriate for use in caching/lookup usages.
	 * <p/>
	 * Generally speaking the transaction itself will be returned here.  This method was added specifically
	 * for use in WebSphere and other unfriendly JEE containers (although WebSphere is still the only known
	 * such brain-dead, sales-driven impl).
	 *
	 * @param transaction The transaction to be identified.
	 * @return An appropropriate identifier
	 */
	public Object getTransactionIdentifier(Transaction transaction);
}

