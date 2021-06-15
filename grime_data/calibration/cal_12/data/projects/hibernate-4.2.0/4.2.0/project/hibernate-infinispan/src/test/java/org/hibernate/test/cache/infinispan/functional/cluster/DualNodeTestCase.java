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
package org.hibernate.test.cache.infinispan.functional.cluster;

import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Galder Zamarreño
 * @since 3.5
 */
public abstract class DualNodeTestCase extends BaseCoreFunctionalTestCase {
	private static final Log log = LogFactory.getLog( DualNodeTestCase.class );

	public static final String NODE_ID_PROP = "hibernate.test.cluster.node.id";
	public static final String NODE_ID_FIELD = "nodeId";
	public static final String LOCAL = "local";
	public static final String REMOTE = "remote";

	private SecondNodeEnvironment secondNodeEnvironment;

	@Override
	public String[] getMappings() {
		return new String[] {
				"cache/infinispan/functional/Contact.hbm.xml", "cache/infinispan/functional/Customer.hbm.xml"
		};
	}

	@Override
	public String getCacheConcurrencyStrategy() {
		return "transactional";
	}

	@Override
	public void configure(Configuration cfg) {
		standardConfigure( cfg );
		cfg.setProperty( NODE_ID_PROP, LOCAL );
		cfg.setProperty( NODE_ID_FIELD, LOCAL );
	}

	@Override
	protected void cleanupTest() throws Exception {
		cleanupTransactionManagement();
	}

	protected void cleanupTransactionManagement() {
		DualNodeJtaTransactionManagerImpl.cleanupTransactions();
		DualNodeJtaTransactionManagerImpl.cleanupTransactionManagers();
	}

	@Before
	public void prepare() throws Exception {
		secondNodeEnvironment = new SecondNodeEnvironment();
	}

	@After
	public void unPrepare() {
		if ( secondNodeEnvironment != null ) {
			secondNodeEnvironment.shutDown();
		}
	}

	protected SecondNodeEnvironment secondNodeEnvironment() {
		return secondNodeEnvironment;
	}

	protected Class getCacheRegionFactory() {
		return ClusterAwareRegionFactory.class;
	}

	protected Class getConnectionProviderClass() {
		return DualNodeConnectionProviderImpl.class;
	}

	protected Class getJtaPlatformClass() {
		return DualNodeJtaPlatformImpl.class;
	}

	protected Class getTransactionFactoryClass() {
		return CMTTransactionFactory.class;
	}

	protected void sleep(long ms) {
		try {
			Thread.sleep( ms );
		}
		catch (InterruptedException e) {
			log.warn( "Interrupted during sleep", e );
		}
	}

	protected boolean getUseQueryCache() {
		return true;
	}

	protected void configureSecondNode(Configuration cfg) {

	}

	protected void standardConfigure(Configuration cfg) {
		super.configure( cfg );

		cfg.setProperty( Environment.CONNECTION_PROVIDER, getConnectionProviderClass().getName() );
		cfg.setProperty( AvailableSettings.JTA_PLATFORM, getJtaPlatformClass().getName() );
		cfg.setProperty( Environment.TRANSACTION_STRATEGY, getTransactionFactoryClass().getName() );
		cfg.setProperty( Environment.CACHE_REGION_FACTORY, getCacheRegionFactory().getName() );
		cfg.setProperty( Environment.USE_QUERY_CACHE, String.valueOf( getUseQueryCache() ) );
	}

	public class SecondNodeEnvironment {
		private Configuration configuration;
		private StandardServiceRegistryImpl serviceRegistry;
		private SessionFactoryImplementor sessionFactory;

		public SecondNodeEnvironment() {
			configuration = constructConfiguration();
			standardConfigure( configuration );
			configuration.setProperty( NODE_ID_PROP, REMOTE );
			configuration.setProperty( NODE_ID_FIELD, REMOTE );
			configureSecondNode( configuration );
			addMappings(configuration);
			configuration.buildMappings();
			applyCacheSettings( configuration );
			afterConfigurationBuilt( configuration );
			serviceRegistry = buildServiceRegistry( configuration );
			sessionFactory = (SessionFactoryImplementor) configuration.buildSessionFactory( serviceRegistry );
		}

		public Configuration getConfiguration() {
			return configuration;
		}

		public StandardServiceRegistryImpl getServiceRegistry() {
			return serviceRegistry;
		}

		public SessionFactoryImplementor getSessionFactory() {
			return sessionFactory;
		}

		public void shutDown() {
			if ( sessionFactory != null ) {
				try {
					sessionFactory.close();
				}
				catch (Exception ignore) {
				}
			}
			if ( serviceRegistry != null ) {
				try {
					serviceRegistry.destroy();
				}
				catch (Exception ignore) {
				}
			}
		}
	}
}
