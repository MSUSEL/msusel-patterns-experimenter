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
package org.hibernate.test.unconstrained;

import org.junit.Test;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class UnconstrainedTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "unconstrained/Person.hbm.xml" };
	}

	@Test
	public void testUnconstrainedNoCache() {
		Session session = openSession();
		Transaction tx = session.beginTransaction();
		Person p = new Person("gavin");
		p.setEmployeeId("123456");
		session.persist(p);
		tx.commit();
		session.close();
		
		sessionFactory().getCache().evictEntityRegion( Person.class );
		
		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.get(Person.class, "gavin");
		assertNull( p.getEmployee() );
		p.setEmployee( new Employee("123456") );
		tx.commit();
		session.close();

		sessionFactory().getCache().evictEntityRegion( Person.class );
		
		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.get(Person.class, "gavin");
		assertTrue( Hibernate.isInitialized( p.getEmployee() ) );
		assertNotNull( p.getEmployee() );
		session.delete(p);
		tx.commit();
		session.close();
	}

	@Test
	public void testUnconstrainedOuterJoinFetch() {
		Session session = openSession();
		Transaction tx = session.beginTransaction();
		Person p = new Person("gavin");
		p.setEmployeeId("123456");
		session.persist(p);
		tx.commit();
		session.close();
		
		sessionFactory().getCache().evictEntityRegion( Person.class );
		
		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.createCriteria(Person.class)
			.setFetchMode("employee", FetchMode.JOIN)
			.add( Restrictions.idEq("gavin") )
			.uniqueResult();
		assertNull( p.getEmployee() );
		p.setEmployee( new Employee("123456") );
		tx.commit();
		session.close();

		sessionFactory().getCache().evictEntityRegion( Person.class );
		
		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.createCriteria(Person.class)
			.setFetchMode("employee", FetchMode.JOIN)
			.add( Restrictions.idEq("gavin") )
			.uniqueResult();
		assertTrue( Hibernate.isInitialized( p.getEmployee() ) );
		assertNotNull( p.getEmployee() );
		session.delete(p);
		tx.commit();
		session.close();
	}

	@Test
	public void testUnconstrained() {
		Session session = openSession();
		Transaction tx = session.beginTransaction();
		Person p = new Person("gavin");
		p.setEmployeeId("123456");
		session.persist(p);
		tx.commit();
		session.close();
		
		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.get(Person.class, "gavin");
		assertNull( p.getEmployee() );
		p.setEmployee( new Employee("123456") );
		tx.commit();
		session.close();

		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.get(Person.class, "gavin");
		assertTrue( Hibernate.isInitialized( p.getEmployee() ) );
		assertNotNull( p.getEmployee() );
		session.delete(p);
		tx.commit();
		session.close();
	}

}

