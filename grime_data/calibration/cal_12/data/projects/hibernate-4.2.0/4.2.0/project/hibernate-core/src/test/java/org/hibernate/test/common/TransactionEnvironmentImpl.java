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

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.transaction.spi.TransactionEnvironment;
import org.hibernate.engine.transaction.spi.TransactionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.jta.platform.spi.JtaPlatform;
import org.hibernate.stat.internal.ConcurrentStatisticsImpl;
import org.hibernate.stat.spi.StatisticsImplementor;

/**
 * @author Steve Ebersole
 */
public class TransactionEnvironmentImpl implements TransactionEnvironment {
	private final ServiceRegistry serviceRegistry;
	private final ConcurrentStatisticsImpl statistics = new ConcurrentStatisticsImpl();
	private final SessionFactoryImplementor sessionFactory;

	public static final String NAME = "TransactionEnvironmentImpl_testSF";

	public TransactionEnvironmentImpl(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;

		Configuration cfg = new Configuration()
				.setProperty( AvailableSettings.SESSION_FACTORY_NAME, NAME )
				.setProperty( AvailableSettings.SESSION_FACTORY_NAME_IS_JNDI, "false" ); // default is true
		sessionFactory = (SessionFactoryImplementor) cfg.buildSessionFactory(serviceRegistry);
	}

	@Override
	public SessionFactoryImplementor getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public JdbcServices getJdbcServices() {
		return serviceRegistry.getService( JdbcServices.class );
	}

	@Override
	public JtaPlatform getJtaPlatform() {
		return serviceRegistry.getService( JtaPlatform.class );
	}

	@Override
	public TransactionFactory getTransactionFactory() {
		return serviceRegistry.getService( TransactionFactory.class );
	}

	@Override
	public StatisticsImplementor getStatisticsImplementor() {
		return statistics;
	}
}
