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
package org.hibernate.ejb.test.ops;

import java.util.Map;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 * @author Hardy Ferentschik
 */
public class GetLoadTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testGetLoad() {
		clearCounts();

		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Session s = ( Session ) em.getDelegate();

		Employer emp = new Employer();
		s.persist( emp );
		Node node = new Node( "foo" );
		Node parent = new Node( "bar" );
		parent.addChild( node );
		s.persist( parent );
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		s = ( Session ) em.getDelegate();
		emp = ( Employer ) s.get( Employer.class, emp.getId() );
		assertTrue( Hibernate.isInitialized( emp ) );
		assertFalse( Hibernate.isInitialized( emp.getEmployees() ) );
		node = ( Node ) s.get( Node.class, node.getName() );
		assertTrue( Hibernate.isInitialized( node ) );
		assertFalse( Hibernate.isInitialized( node.getChildren() ) );
		assertFalse( Hibernate.isInitialized( node.getParent() ) );
		assertNull( s.get( Node.class, "xyz" ) );
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		s = ( Session ) em.getDelegate();
		emp = ( Employer ) s.load( Employer.class, emp.getId() );
		emp.getId();
		assertFalse( Hibernate.isInitialized( emp ) );
		node = ( Node ) s.load( Node.class, node.getName() );
		assertEquals( node.getName(), "foo" );
		assertFalse( Hibernate.isInitialized( node ) );
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		s = ( Session ) em.getDelegate();
		emp = ( Employer ) s.get( "org.hibernate.ejb.test.ops.Employer", emp.getId() );
		assertTrue( Hibernate.isInitialized( emp ) );
		node = ( Node ) s.get( "org.hibernate.ejb.test.ops.Node", node.getName() );
		assertTrue( Hibernate.isInitialized( node ) );
		em.getTransaction().commit();
		em.close();

		em = getOrCreateEntityManager();
		em.getTransaction().begin();
		s = ( Session ) em.getDelegate();
		emp = ( Employer ) s.load( "org.hibernate.ejb.test.ops.Employer", emp.getId() );
		emp.getId();
		assertFalse( Hibernate.isInitialized( emp ) );
		node = ( Node ) s.load( "org.hibernate.ejb.test.ops.Node", node.getName() );
		assertEquals( node.getName(), "foo" );
		assertFalse( Hibernate.isInitialized( node ) );
		em.getTransaction().commit();
		em.close();

		assertFetchCount( 0 );
	}

	private void clearCounts() {
		( ( EntityManagerFactoryImpl ) entityManagerFactory() ).getSessionFactory().getStatistics().clear();
	}

	private void assertFetchCount(int count) {
		int fetches = ( int ) ( ( EntityManagerFactoryImpl ) entityManagerFactory() ).getSessionFactory()
				.getStatistics()
				.getEntityFetchCount();
		assertEquals( count, fetches );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	protected void addConfigOptions(Map options) {
		options.put( Environment.GENERATE_STATISTICS, "true" );
		options.put( Environment.STATEMENT_BATCH_SIZE, "0" );
	}

	@Override
	protected String[] getMappings() {
		return new String[] {
				"org/hibernate/ejb/test/ops/Node.hbm.xml",
				"org/hibernate/ejb/test/ops/Employer.hbm.xml"
		};
	}
}

