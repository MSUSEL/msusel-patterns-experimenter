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
package org.hibernate.test.annotations.fetchprofile;

import java.io.InputStream;

import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test case for HHH-4812
 *
 * @author Hardy Ferentschik
 */
@TestForIssue( jiraKey = "HHH-4812" )
public class FetchProfileTest extends BaseUnitTestCase {
	private static final Logger log = Logger.getLogger( FetchProfileTest.class );

	private ServiceRegistry serviceRegistry;

	@Before
    public void setUp() {
		serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( Environment.getProperties() );
	}

	@After
    public void tearDown() {
        if (serviceRegistry != null) ServiceRegistryBuilder.destroy(serviceRegistry);
	}

	@Test
	public void testFetchProfileConfigured() {
		Configuration config = new Configuration();
		config.addAnnotatedClass( Customer.class );
		config.addAnnotatedClass( Order.class );
		config.addAnnotatedClass( SupportTickets.class );
		config.addAnnotatedClass( Country.class );
		SessionFactoryImplementor sessionImpl = ( SessionFactoryImplementor ) config.buildSessionFactory(
				serviceRegistry
		);

		assertTrue(
				"fetch profile not parsed properly",
				sessionImpl.containsFetchProfileDefinition( "customer-with-orders" )
		);
		assertFalse(
				"package info should not be parsed",
				sessionImpl.containsFetchProfileDefinition( "package-profile-1" )
		);
	}

	@Test
	public void testWrongAssociationName() {
		Configuration config = new Configuration();
		config.addAnnotatedClass( Customer2.class );
		config.addAnnotatedClass( Order.class );
		config.addAnnotatedClass( Country.class );

		try {
			config.buildSessionFactory( serviceRegistry );
			fail();
		}
		catch ( MappingException e ) {
            log.trace("success");
		}
	}

	@Test
	public void testWrongClass() {
		Configuration config = new Configuration();
		config.addAnnotatedClass( Customer3.class );
		config.addAnnotatedClass( Order.class );
		config.addAnnotatedClass( Country.class );

		try {
			config.buildSessionFactory( serviceRegistry );
			fail();
		}
		catch ( MappingException e ) {
            log.trace("success");
		}
	}

	@Test
	public void testUnsupportedFetchMode() {
		Configuration config = new Configuration();
		config.addAnnotatedClass( Customer4.class );
		config.addAnnotatedClass( Order.class );
		config.addAnnotatedClass( Country.class );

		try {
			config.buildSessionFactory( serviceRegistry );
			fail();
		}
		catch ( MappingException e ) {
            log.trace("success");
		}
	}

	@Test
	public void testXmlOverride() {
		Configuration config = new Configuration();
		config.addAnnotatedClass( Customer5.class );
		config.addAnnotatedClass( Order.class );
		config.addAnnotatedClass( Country.class );
		InputStream is = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream( "org/hibernate/test/annotations/fetchprofile/mappings.hbm.xml" );
		config.addInputStream( is );
		SessionFactoryImplementor sessionImpl = ( SessionFactoryImplementor ) config.buildSessionFactory(
				serviceRegistry
		);

		assertTrue(
				"fetch profile not parsed properly",
				sessionImpl.containsFetchProfileDefinition( "orders-profile" )
		);

		// now the same with no xml
		config = new Configuration();
		config.addAnnotatedClass( Customer5.class );
		config.addAnnotatedClass( Order.class );
		config.addAnnotatedClass( Country.class );
		try {
			config.buildSessionFactory( serviceRegistry );
			fail();
		}
		catch ( MappingException e ) {
            log.trace("success");
		}
	}

	@Test
	public void testPackageConfiguredFetchProfile() {
		Configuration config = new Configuration();
		config.addAnnotatedClass( Customer.class );
		config.addAnnotatedClass( Order.class );
		config.addAnnotatedClass( SupportTickets.class );
		config.addAnnotatedClass( Country.class );
		config.addPackage( Customer.class.getPackage().getName() );
		SessionFactoryImplementor sessionImpl = ( SessionFactoryImplementor ) config.buildSessionFactory(
				serviceRegistry
		);

		assertTrue(
				"fetch profile not parsed properly",
				sessionImpl.containsFetchProfileDefinition( "package-profile-1" )
		);
		assertTrue(
				"fetch profile not parsed properly",
				sessionImpl.containsFetchProfileDefinition( "package-profile-2" )
		);
	}
}