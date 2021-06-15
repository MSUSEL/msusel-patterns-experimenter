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
package org.hibernate.service.jta.platform.internal;

import java.util.Map;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.jndi.spi.JndiService;
import org.hibernate.service.jta.platform.spi.JtaPlatform;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractJtaPlatform
		implements JtaPlatform, Configurable, ServiceRegistryAwareService, TransactionManagerAccess {
	private boolean cacheTransactionManager;
	private boolean cacheUserTransaction;
	private ServiceRegistryImplementor serviceRegistry;

	private final JtaSynchronizationStrategy tmSynchronizationStrategy = new TransactionManagerBasedSynchronizationStrategy( this );

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	protected ServiceRegistry serviceRegistry() {
		return serviceRegistry;
	}

	protected JndiService jndiService() {
		return serviceRegistry().getService( JndiService.class );
	}

	protected abstract TransactionManager locateTransactionManager();
	protected abstract UserTransaction locateUserTransaction();

	public void configure(Map configValues) {
		cacheTransactionManager = ConfigurationHelper.getBoolean(
				AvailableSettings.JTA_CACHE_TM,
				configValues,
				canCacheTransactionManagerByDefault()
		);
		cacheUserTransaction = ConfigurationHelper.getBoolean(
				AvailableSettings.JTA_CACHE_UT,
				configValues,
				canCacheUserTransactionByDefault()
		);
	}

	protected boolean canCacheTransactionManagerByDefault() {
		return true;
	}

	protected boolean canCacheUserTransactionByDefault() {
		return false;
	}

	protected boolean canCacheTransactionManager() {
		return cacheTransactionManager;
	}

	protected boolean canCacheUserTransaction() {
		return cacheUserTransaction;
	}

	private TransactionManager transactionManager;

	@Override
	public TransactionManager retrieveTransactionManager() {
		if ( canCacheTransactionManager() ) {
			if ( transactionManager == null ) {
				transactionManager = locateTransactionManager();
			}
			return transactionManager;
		}
		else {
			return locateTransactionManager();
		}
	}

	@Override
	public TransactionManager getTransactionManager() {
		return retrieveTransactionManager();
	}

	private UserTransaction userTransaction;

	@Override
	public UserTransaction retrieveUserTransaction() {
		if ( canCacheUserTransaction() ) {
			if ( userTransaction == null ) {
				userTransaction = locateUserTransaction();
			}
			return userTransaction;
		}
		return locateUserTransaction();
	}

	@Override
	public Object getTransactionIdentifier(Transaction transaction) {
		// generally we use the transaction itself.
		return transaction;
	}

	protected JtaSynchronizationStrategy getSynchronizationStrategy() {
		return tmSynchronizationStrategy;
	}

	@Override
	public void registerSynchronization(Synchronization synchronization) {
		getSynchronizationStrategy().registerSynchronization( synchronization );
	}

	@Override
	public boolean canRegisterSynchronization() {
		return getSynchronizationStrategy().canRegisterSynchronization();
	}

	@Override
	public int getCurrentStatus() throws SystemException {
		return retrieveTransactionManager().getStatus();
	}
}
