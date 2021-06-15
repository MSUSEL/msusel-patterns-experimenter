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
package org.hibernate.test.eviction;

import org.hibernate.Session;

import org.junit.Test;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
public class EvictionTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { IsolatedEvictableEntity.class };
	}

	@Test
	@TestForIssue( jiraKey = "HHH-7912" )
	public void testNormalUsage() {
		Session session = openSession();
		session.beginTransaction();
		session.save( new IsolatedEvictableEntity( 1 ) );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		IsolatedEvictableEntity entity = (IsolatedEvictableEntity) session.get( IsolatedEvictableEntity.class, 1 );
		assertTrue( session.contains( entity ) );
		session.evict( entity );
		assertFalse( session.contains( entity ) );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.delete( entity );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-7912" )
	public void testEvictingNull() {
		Session session = openSession();
		session.beginTransaction();
		try {
			session.evict( null );
			fail( "Expecting evict(null) to throw NPE" );
		}
		catch (NullPointerException expected) {
		}
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-7912" )
	public void testEvictingTransientEntity() {
		Session session = openSession();
		session.beginTransaction();
		session.evict( new IsolatedEvictableEntity( 1 ) );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-7912" )
	public void testEvictingDetachedEntity() {
		Session session = openSession();
		session.beginTransaction();
		session.save( new IsolatedEvictableEntity( 1 ) );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		IsolatedEvictableEntity entity = (IsolatedEvictableEntity) session.get( IsolatedEvictableEntity.class, 1 );
		assertTrue( session.contains( entity ) );
		// detach the entity
		session.evict( entity );
		assertFalse( session.contains( entity ) );
		// evict it again the entity
		session.evict( entity );
		assertFalse( session.contains( entity ) );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.delete( entity );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-7912" )
	public void testEvictingNonEntity() {
		Session session = openSession();
		session.beginTransaction();
		try {
			session.evict( new EvictionTest() );
			fail( "Expecting evict(non-entity) to throw IAE" );
		}
		catch (IllegalArgumentException expected) {
		}
		session.getTransaction().commit();
		session.close();
	}

}
