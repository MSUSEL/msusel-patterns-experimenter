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
package org.hibernate.test.locking.paging;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Test of paging and locking in combination
 *
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-1168" )
public class PagingAndLockingTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Door.class };
	}

	@Before
	public void createTestData() {
		Session session = openSession();
		session.beginTransaction();
		session.save( new Door( 1, "Front" ) );
		session.save( new Door( 2, "Back" ) );
		session.save( new Door( 3, "Garage" ) );
		session.save( new Door( 4, "French" ) );
		session.getTransaction().commit();
		session.close();
	}

	@After
	public void deleteTestData() {
		Session session = openSession();
		session.beginTransaction();
		session.createQuery( "delete Door" ).executeUpdate();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testHql() {
		Session session = openSession();
		session.beginTransaction();
		Query qry = session.createQuery( "from Door" );
		qry.getLockOptions().setLockMode( LockMode.PESSIMISTIC_WRITE );
		qry.setFirstResult( 2 );
		qry.setMaxResults( 2 );
		@SuppressWarnings("unchecked") List<Door> results = qry.list();
		assertEquals( 2, results.size() );
		for ( Door door : results ) {
			assertEquals( LockMode.PESSIMISTIC_WRITE, session.getCurrentLockMode( door ) );
		}
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testCriteria() {
		Session session = openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria( Door.class );
		criteria.setLockMode( LockMode.PESSIMISTIC_WRITE );
		criteria.setFirstResult( 2 );
		criteria.setMaxResults( 2 );
		@SuppressWarnings("unchecked") List<Door> results = criteria.list();
		assertEquals( 2, results.size() );
		for ( Door door : results ) {
			assertEquals( LockMode.PESSIMISTIC_WRITE, session.getCurrentLockMode( door ) );
		}
		session.getTransaction().commit();
		session.close();
	}

	@Test
//	@Ignore( "Support for locking on native-sql queries not yet implemented" )
	public void testNativeSql() {
		Session session = openSession();
		session.beginTransaction();
		SQLQuery qry = session.createSQLQuery( "select * from door" );
		qry.addRoot( "door", Door.class );
		qry.getLockOptions().setLockMode( LockMode.PESSIMISTIC_WRITE );
		qry.setFirstResult( 2 );
		qry.setMaxResults( 2 );
		@SuppressWarnings("unchecked") List<Door> results = qry.list();
		assertEquals( 2, results.size() );
		for ( Door door : results ) {
			assertEquals( LockMode.PESSIMISTIC_WRITE, session.getCurrentLockMode( door ) );
		}
		session.getTransaction().commit();
		session.close();
	}

}
