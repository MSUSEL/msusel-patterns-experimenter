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
package org.hibernate.osgi;

import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.service.jta.platform.spi.JtaPlatform;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Offers the JTA Platform provided by the OSGi container. The Enterprise
 * OSGi spec requires all containers to register UserTransaction
 * and TransactionManager OSGi services.
 * 
 * @author Brett Meyer
 */
public class OsgiJtaPlatform implements JtaPlatform {

	private static final long serialVersionUID = 1L;
	
	private BundleContext bundleContext;

	public OsgiJtaPlatform(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public TransactionManager retrieveTransactionManager() {
		ServiceReference sr = bundleContext.getServiceReference( TransactionManager.class.getName() );
		return (TransactionManager) bundleContext.getService( sr );
	}

	@Override
	public UserTransaction retrieveUserTransaction() {
		ServiceReference sr = bundleContext.getServiceReference( UserTransaction.class.getName() );
		return (UserTransaction) bundleContext.getService( sr );
	}

	@Override
	public Object getTransactionIdentifier(Transaction transaction) {
		// AbstractJtaPlatform just uses the transaction itself.
		return transaction;
	}

	@Override
	public boolean canRegisterSynchronization() {
		// TODO
		return false;
	}

	@Override
	public void registerSynchronization(Synchronization synchronization) {
		// TODO
	}

	@Override
	public int getCurrentStatus() throws SystemException {
		return retrieveTransactionManager().getStatus();
	}

}
