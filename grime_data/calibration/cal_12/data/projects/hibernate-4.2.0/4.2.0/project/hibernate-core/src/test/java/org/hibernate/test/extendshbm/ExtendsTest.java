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
//$Id: ExtendsTest.java 10977 2006-12-12 23:28:04Z steve.ebersole@jboss.com $
package org.hibernate.test.extendshbm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.testing.ServiceRegistryBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Gavin King
 */
public class ExtendsTest extends BaseUnitTestCase {
	private StandardServiceRegistryImpl serviceRegistry;

	@Before
	public void setUp() {
		serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry();
	}

	@After
	public void tearDown() {
		ServiceRegistryBuilder.destroy( serviceRegistry );
	}

	private String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	@Test
	public void testAllInOne() {
		Configuration cfg = new Configuration();

		cfg.addResource( getBaseForMappings() + "extendshbm/allinone.hbm.xml" );
		cfg.buildMappings();
		assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" ) );
		assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Person" ) );
		assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Employee" ) );
	}

	@Test
	public void testOutOfOrder() {
		Configuration cfg = new Configuration();

		try {
			cfg.addResource( getBaseForMappings() + "extendshbm/Customer.hbm.xml" );
			assertNull(
					"cannot be in the configuration yet!",
					cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" )
			);
			cfg.addResource( getBaseForMappings() + "extendshbm/Person.hbm.xml" );
			cfg.addResource( getBaseForMappings() + "extendshbm/Employee.hbm.xml" );

			cfg.buildSessionFactory( serviceRegistry );

			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" ) );
			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Person" ) );
			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Employee" ) );

		}
		catch ( HibernateException e ) {
			fail( "should not fail with exception! " + e );
		}

	}

	@Test
	public void testNwaitingForSuper() {
		Configuration cfg = new Configuration();

		try {
			cfg.addResource( getBaseForMappings() + "extendshbm/Customer.hbm.xml" );
			assertNull(
					"cannot be in the configuration yet!",
					cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" )
			);
			cfg.addResource( getBaseForMappings() + "extendshbm/Employee.hbm.xml" );
			assertNull(
					"cannot be in the configuration yet!",
					cfg.getClassMapping( "org.hibernate.test.extendshbm.Employee" )
			);
			cfg.addResource( getBaseForMappings() + "extendshbm/Person.hbm.xml" );

			cfg.buildMappings();

			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Person" ) );
			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Employee" ) );
			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" ) );


		}
		catch ( HibernateException e ) {
			e.printStackTrace();
			fail( "should not fail with exception! " + e );

		}

	}

	@Test
	public void testMissingSuper() {
		Configuration cfg = new Configuration();

		try {
			cfg.addResource( getBaseForMappings() + "extendshbm/Customer.hbm.xml" );
			assertNull(
					"cannot be in the configuration yet!",
					cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" )
			);
			cfg.addResource( getBaseForMappings() + "extendshbm/Employee.hbm.xml" );

			cfg.buildSessionFactory( serviceRegistry );

			fail( "Should not be able to build sessionFactory without a Person" );
		}
		catch ( HibernateException e ) {

		}

	}

	@Test
	public void testAllSeparateInOne() {
		Configuration cfg = new Configuration();

		try {
			cfg.addResource( getBaseForMappings() + "extendshbm/allseparateinone.hbm.xml" );

			cfg.buildSessionFactory( serviceRegistry );

			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" ) );
			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Person" ) );
			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Employee" ) );

		}
		catch ( HibernateException e ) {
			fail( "should not fail with exception! " + e );
		}

	}

	@Test
	public void testJoinedSubclassAndEntityNamesOnly() {
		Configuration cfg = new Configuration();

		try {
			cfg.addResource( getBaseForMappings() + "extendshbm/entitynames.hbm.xml" );

			cfg.buildMappings();

			assertNotNull( cfg.getClassMapping( "EntityHasName" ) );
			assertNotNull( cfg.getClassMapping( "EntityCompany" ) );

		}
		catch ( HibernateException e ) {
			e.printStackTrace();
			fail( "should not fail with exception! " + e );

		}
	}

	@Test
	public void testEntityNamesWithPackage() {
		Configuration cfg = new Configuration();
		try {
			cfg.addResource( getBaseForMappings() + "extendshbm/packageentitynames.hbm.xml" );

			cfg.buildMappings();

			assertNotNull( cfg.getClassMapping( "EntityHasName" ) );
			assertNotNull( cfg.getClassMapping( "EntityCompany" ) );

		}
		catch ( HibernateException e ) {
			e.printStackTrace();
			fail( "should not fail with exception! " + e );

		}
	}

	@Test
	public void testUnionSubclass() {
		Configuration cfg = new Configuration();

		try {
			cfg.addResource( getBaseForMappings() + "extendshbm/unionsubclass.hbm.xml" );

			cfg.buildMappings();

			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Person" ) );
			assertNotNull( cfg.getClassMapping( "org.hibernate.test.extendshbm.Customer" ) );

		}
		catch ( HibernateException e ) {
			e.printStackTrace();
			fail( "should not fail with exception! " + e );

		}
	}

}

