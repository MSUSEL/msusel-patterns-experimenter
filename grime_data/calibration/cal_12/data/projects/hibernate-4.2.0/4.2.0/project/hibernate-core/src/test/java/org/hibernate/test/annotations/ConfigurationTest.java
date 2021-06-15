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
//$Id$
package org.hibernate.test.annotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 */
public class ConfigurationTest {
	private ServiceRegistry serviceRegistry;
    @Before
	public void setUp() {
		serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( Environment.getProperties() );
	}
     @After
	public void tearDown() {
		if ( serviceRegistry != null ) {
			ServiceRegistryBuilder.destroy( serviceRegistry );
		}
	}
     @Test
	public void testDeclarativeMix() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure( "org/hibernate/test/annotations/hibernate.cfg.xml" );
		cfg.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		SessionFactory sf = cfg.buildSessionFactory( serviceRegistry );
		assertNotNull( sf );
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery( "from Boat" );
		assertEquals( 0, q.list().size() );
		q = s.createQuery( "from Plane" );
		assertEquals( 0, q.list().size() );
		tx.commit();
		s.close();
		sf.close();
	}
     @Test
	public void testIgnoringHbm() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure( "org/hibernate/test/annotations/hibernate.cfg.xml" );
		cfg.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		cfg.setProperty( Configuration.ARTEFACT_PROCESSING_ORDER, "class" );
		SessionFactory sf = cfg.buildSessionFactory( serviceRegistry );
		assertNotNull( sf );
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		Query q;
		try {
			s.createQuery( "from Boat" ).list();
			fail( "Boat should not be mapped" );
		}
		catch (HibernateException e) {
			//all good
		}
		q = s.createQuery( "from Plane" );
		assertEquals( 0, q.list().size() );
		tx.commit();
		s.close();
		sf.close();
	}
    @Test
	public void testPrecedenceHbm() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure( "org/hibernate/test/annotations/hibernate.cfg.xml" );
		cfg.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		cfg.addAnnotatedClass( Boat.class );
		SessionFactory sf = cfg.buildSessionFactory( serviceRegistry );
		assertNotNull( sf );
		Session s = sf.openSession();
		s.getTransaction().begin();
		Boat boat = new Boat();
		boat.setSize( 12 );
		boat.setWeight( 34 );
		s.persist( boat );
		s.getTransaction().commit();
		s.clear();
		Transaction tx = s.beginTransaction();
		boat = (Boat) s.get( Boat.class, boat.getId() );
		assertTrue( "Annotation has precedence", 34 != boat.getWeight() );
		s.delete( boat );
		//s.getTransaction().commit();
		tx.commit();
		s.close();
		sf.close();
	}
     @Test
	public void testPrecedenceAnnotation() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure( "org/hibernate/test/annotations/hibernate.cfg.xml" );
		cfg.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		cfg.setProperty( Configuration.ARTEFACT_PROCESSING_ORDER, "class, hbm" );
		cfg.addAnnotatedClass( Boat.class );
		SessionFactory sf = cfg.buildSessionFactory( serviceRegistry );
		assertNotNull( sf );
		Session s = sf.openSession();
		s.getTransaction().begin();
		Boat boat = new Boat();
		boat.setSize( 12 );
		boat.setWeight( 34 );
		s.persist( boat );
		s.getTransaction().commit();
		s.clear();
		Transaction tx = s.beginTransaction();
		boat = (Boat) s.get( Boat.class, boat.getId() );
		assertTrue( "Annotation has precedence", 34 == boat.getWeight() );
		s.delete( boat );
		tx.commit();
		s.close();
		sf.close();
	}
     @Test
	public void testHbmWithSubclassExtends() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure( "org/hibernate/test/annotations/hibernate.cfg.xml" );
		cfg.addClass( Ferry.class );
		cfg.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		SessionFactory sf = cfg.buildSessionFactory( serviceRegistry );
		assertNotNull( sf );
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery( "from Ferry" );
		assertEquals( 0, q.list().size() );
		q = s.createQuery( "from Plane" );
		assertEquals( 0, q.list().size() );
		tx.commit();
		s.close();
		sf.close();
	}
      @Test
	public void testAnnReferencesHbm() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure( "org/hibernate/test/annotations/hibernate.cfg.xml" );
		cfg.addAnnotatedClass( Port.class );
		cfg.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		SessionFactory sf = cfg.buildSessionFactory( serviceRegistry );
		assertNotNull( sf );
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery( "from Boat" );
		assertEquals( 0, q.list().size() );
		q = s.createQuery( "from Port" );
		assertEquals( 0, q.list().size() );
		tx.commit();
		s.close();
		sf.close();
	}
}
