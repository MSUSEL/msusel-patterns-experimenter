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
package org.hibernate.test.multitenancy.schema;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.RootClass;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.service.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.testing.cache.CachingRegionFactory;
import org.hibernate.testing.env.ConnectionProviderBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.tool.hbm2ddl.ConnectionHelper;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * @author Steve Ebersole
 */
public class SchemaBasedMultiTenancyTest extends BaseUnitTestCase {
	private DriverManagerConnectionProviderImpl acmeProvider;
	private DriverManagerConnectionProviderImpl jbossProvider;

	private ServiceRegistryImplementor serviceRegistry;

	protected SessionFactoryImplementor sessionFactory;

	@Before
	public void setUp() {
		AbstractMultiTenantConnectionProvider multiTenantConnectionProvider = buildMultiTenantConnectionProvider();
		Configuration cfg = buildConfiguration();

		serviceRegistry = (ServiceRegistryImplementor) new ServiceRegistryBuilder()
				.applySettings( cfg.getProperties() )
				.addService( MultiTenantConnectionProvider.class, multiTenantConnectionProvider )
				.buildServiceRegistry();

		sessionFactory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
	}

	protected Configuration buildConfiguration() {
		Configuration cfg = new Configuration();
		cfg.getProperties().put( Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA );
		cfg.setProperty( Environment.CACHE_REGION_FACTORY, CachingRegionFactory.class.getName() );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
		cfg.addAnnotatedClass( Customer.class );

		cfg.buildMappings();
		RootClass meta = (RootClass) cfg.getClassMapping( Customer.class.getName() );
		meta.setCacheConcurrencyStrategy( "read-write" );

		// do the acme export
		new SchemaExport(
				new ConnectionHelper() {
					private Connection connection;
					@Override
					public void prepare(boolean needsAutoCommit) throws SQLException {
						connection = acmeProvider.getConnection();
					}

					@Override
					public Connection getConnection() throws SQLException {
						return connection;
					}

					@Override
					public void release() throws SQLException {
						acmeProvider.closeConnection( connection );
					}
				},
				cfg.generateDropSchemaScript( ConnectionProviderBuilder.getCorrespondingDialect() ),
				cfg.generateSchemaCreationScript( ConnectionProviderBuilder.getCorrespondingDialect() )
		).execute(		 // so stupid...
						   false,	 // do not script the export (write it to file)
						   true,	 // do run it against the database
						   false,	 // do not *just* perform the drop
						   false	// do not *just* perform the create
		);

		// do the jboss export
		new SchemaExport(
				new ConnectionHelper() {
					private Connection connection;
					@Override
					public void prepare(boolean needsAutoCommit) throws SQLException {
						connection = jbossProvider.getConnection();
					}

					@Override
					public Connection getConnection() throws SQLException {
						return connection;
					}

					@Override
					public void release() throws SQLException {
						jbossProvider.closeConnection( connection );
					}
				},
				cfg.generateDropSchemaScript( ConnectionProviderBuilder.getCorrespondingDialect() ),
				cfg.generateSchemaCreationScript( ConnectionProviderBuilder.getCorrespondingDialect() )
		).execute( 		// so stupid...
						   false, 	// do not script the export (write it to file)
						   true, 	// do run it against the database
						   false, 	// do not *just* perform the drop
						   false	// do not *just* perform the create
		);
		return cfg;
	}

	private AbstractMultiTenantConnectionProvider buildMultiTenantConnectionProvider() {
		acmeProvider = ConnectionProviderBuilder.buildConnectionProvider( "acme" );
		jbossProvider = ConnectionProviderBuilder.buildConnectionProvider( "jboss" );
		return new AbstractMultiTenantConnectionProvider() {
			@Override
			protected ConnectionProvider getAnyConnectionProvider() {
				return acmeProvider;
			}

			@Override
			protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
				if ( "acme".equals( tenantIdentifier ) ) {
					return acmeProvider;
				}
				else if ( "jboss".equals( tenantIdentifier ) ) {
					return jbossProvider;
				}
				throw new HibernateException( "Unknown tenant identifier" );
			}
		};
	}

	@After
	public void tearDown() {
		if ( sessionFactory != null ) {
			sessionFactory.close();
		}
		if ( serviceRegistry != null ) {
			serviceRegistry.destroy();
		}
		if ( jbossProvider != null ) {
			jbossProvider.stop();
		}
		if ( acmeProvider != null ) {
			acmeProvider.stop();
		}
	}

	@Test
	public void testBasicExpectedBehavior() {
		Session session = getNewSession("jboss");
		session.beginTransaction();
		Customer steve = new Customer( 1L, "steve" );
		session.save( steve );
		session.getTransaction().commit();
		session.close();

		session = getNewSession("acme");
		try {
			session.beginTransaction();
			Customer check = (Customer) session.get( Customer.class, steve.getId() );
			Assert.assertNull( "tenancy not properly isolated", check );
		}
		finally {
			session.getTransaction().commit();
			session.close();
		}

		session = getNewSession("jboss");
		session.beginTransaction();
		session.delete( steve );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testSameIdentifiers() {
		// create a customer 'steve' in jboss
		Session session = getNewSession("jboss");
		session.beginTransaction();
		Customer steve = new Customer( 1L, "steve" );
		session.save( steve );
		session.getTransaction().commit();
		session.close();

		// now, create a customer 'john' in acme
		session = getNewSession("acme");
		session.beginTransaction();
		Customer john = new Customer( 1L, "john" );
		session.save( john );
		session.getTransaction().commit();
		session.close();

		sessionFactory.getStatisticsImplementor().clear();

		// make sure we get the correct people back, from cache
		// first, jboss
		{
			session = getNewSession("jboss");
			session.beginTransaction();
			Customer customer = (Customer) session.load( Customer.class, 1L );
			Assert.assertEquals( "steve", customer.getName() );
			// also, make sure this came from second level
			Assert.assertEquals( 1, sessionFactory.getStatisticsImplementor().getSecondLevelCacheHitCount() );
			session.getTransaction().commit();
			session.close();
		}
		sessionFactory.getStatisticsImplementor().clear();
		// then, acme
		{
			session = getNewSession("acme");
			session.beginTransaction();
			Customer customer = (Customer) session.load( Customer.class, 1L );
			Assert.assertEquals( "john", customer.getName() );
			// also, make sure this came from second level
			Assert.assertEquals( 1, sessionFactory.getStatisticsImplementor().getSecondLevelCacheHitCount() );
			session.getTransaction().commit();
			session.close();
		}

		// make sure the same works from datastore too
		sessionFactory.getStatisticsImplementor().clear();
		sessionFactory.getCache().evictEntityRegions();
		// first jboss
		{
			session = getNewSession("jboss");
			session.beginTransaction();
			Customer customer = (Customer) session.load( Customer.class, 1L );
			Assert.assertEquals( "steve", customer.getName() );
			// also, make sure this came from second level
			Assert.assertEquals( 0, sessionFactory.getStatisticsImplementor().getSecondLevelCacheHitCount() );
			session.getTransaction().commit();
			session.close();
		}
		sessionFactory.getStatisticsImplementor().clear();
		// then, acme
		{
			session = getNewSession("acme");
			session.beginTransaction();
			Customer customer = (Customer) session.load( Customer.class, 1L );
			Assert.assertEquals( "john", customer.getName() );
			// also, make sure this came from second level
			Assert.assertEquals( 0, sessionFactory.getStatisticsImplementor().getSecondLevelCacheHitCount() );
			session.getTransaction().commit();
			session.close();
		}

		session = getNewSession("jboss");
		session.beginTransaction();
		session.delete( steve );
		session.getTransaction().commit();
		session.close();

		session = getNewSession("acme");
		session.beginTransaction();
		session.delete( john );
		session.getTransaction().commit();
		session.close();
	}

	protected Session getNewSession(String tenant) {
		return sessionFactory.withOptions().tenantIdentifier( tenant ).openSession();
	}

}
