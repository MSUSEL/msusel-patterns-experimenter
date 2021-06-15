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
package org.hibernate.engine.transaction.internal;

import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.transaction.internal.jdbc.JdbcTransactionFactory;
import org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory;
import org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory;
import org.hibernate.engine.transaction.spi.TransactionFactory;
import org.hibernate.engine.transaction.spi.TransactionImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.BasicServiceInitiator;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * Standard instantiator for the standard {@link TransactionFactory} service.
 *
 * @author Steve Ebersole
 */
public class TransactionFactoryInitiator<T extends TransactionImplementor> implements BasicServiceInitiator<TransactionFactory> {
    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       TransactionFactoryInitiator.class.getName());

	public static final TransactionFactoryInitiator INSTANCE = new TransactionFactoryInitiator();

	@Override
	@SuppressWarnings( {"unchecked"})
	public Class<TransactionFactory> getServiceInitiated() {
		return TransactionFactory.class;
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public TransactionFactory initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		final Object strategy = configurationValues.get( Environment.TRANSACTION_STRATEGY );
		if ( TransactionFactory.class.isInstance( strategy ) ) {
			return (TransactionFactory) strategy;
		}

		if ( strategy == null ) {
			LOG.usingDefaultTransactionStrategy();
			return new JdbcTransactionFactory();
		}

		final String strategyClassName = mapLegacyNames( strategy.toString() );
		LOG.transactionStrategy( strategyClassName );

		ClassLoaderService classLoaderService = registry.getService( ClassLoaderService.class );
		try {
			return (TransactionFactory) classLoaderService.classForName( strategyClassName ).newInstance();
		}
		catch ( Exception e ) {
			throw new HibernateException( "Unable to instantiate specified TransactionFactory class [" + strategyClassName + "]", e );
		}
	}

	private String mapLegacyNames(String name) {
		if ( "org.hibernate.transaction.JDBCTransactionFactory".equals( name ) ) {
			return JdbcTransactionFactory.class.getName();
		}

		if ( "org.hibernate.transaction.JTATransactionFactory".equals( name ) ) {
			return JtaTransactionFactory.class.getName();
		}

		if ( "org.hibernate.transaction.CMTTransactionFactory".equals( name ) ) {
			return CMTTransactionFactory.class.getName();
		}

		return name;
	}
}

