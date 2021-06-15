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
package org.hibernate.test.cascade;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.TransientObjectException;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 * @author Gail Badner
 */
public class MultiPathCascadeTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] {
				"cascade/MultiPathCascade.hbm.xml"
		};
	}

	@Override
	protected void cleanupTest() {
		Session s = openSession();
		s.beginTransaction();
		s.createQuery( "delete from A" );
		s.createQuery( "delete from G" );
		s.createQuery( "delete from H" );
	}

	@Test
	public void testMultiPathMergeModifiedDetached() throws Exception {
		// persist a simple A in the database

		Session s = openSession();
		s.beginTransaction();
		A a = new A();
		a.setData( "Anna" );
		s.save( a );
		s.getTransaction().commit();
		s.close();

		// modify detached entity
		modifyEntity( a );

		s = openSession();
		s.beginTransaction();
		a = (A) s.merge( a );
		s.getTransaction().commit();
		s.close();

		verifyModifications( a.getId() );
	}

	@Test
	public void testMultiPathMergeModifiedDetachedIntoProxy() throws Exception {
		// persist a simple A in the database

		Session s = openSession();
		s.beginTransaction();
		A a = new A();
		a.setData( "Anna" );
		s.save( a );
		s.getTransaction().commit();
		s.close();

		// modify detached entity
		modifyEntity( a );

		s = openSession();
		s.beginTransaction();
		A aLoaded = (A) s.load( A.class, new Long( a.getId() ) );
		assertTrue( aLoaded instanceof HibernateProxy );
		assertSame( aLoaded, s.merge( a ) );
		s.getTransaction().commit();
		s.close();

		verifyModifications( a.getId() );
	}

	@Test
	public void testMultiPathUpdateModifiedDetached() throws Exception {
		// persist a simple A in the database

		Session s = openSession();
		s.beginTransaction();
		A a = new A();
		a.setData( "Anna" );
		s.save( a );
		s.getTransaction().commit();
		s.close();

		// modify detached entity
		modifyEntity( a );

		s = openSession();
		s.beginTransaction();
		s.update( a );
		s.getTransaction().commit();
		s.close();

		verifyModifications( a.getId() );
	}

	@Test
	public void testMultiPathGetAndModify() throws Exception {
		// persist a simple A in the database

		Session s = openSession();
		s.beginTransaction();
		A a = new A();
		a.setData( "Anna" );
		s.save( a );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		// retrieve the previously saved instance from the database, and update it
		a = (A) s.get( A.class, new Long( a.getId() ) );
		modifyEntity( a );
		s.getTransaction().commit();
		s.close();

		verifyModifications( a.getId() );
	}

	@Test
	public void testMultiPathMergeNonCascadedTransientEntityInCollection() throws Exception {
		// persist a simple A in the database

		Session s = openSession();
		s.beginTransaction();
		A a = new A();
		a.setData( "Anna" );
		s.save( a );
		s.getTransaction().commit();
		s.close();

		// modify detached entity
		modifyEntity( a );

		s = openSession();
		s.beginTransaction();
		a = (A) s.merge( a );
		s.getTransaction().commit();
		s.close();

		verifyModifications( a.getId() );

		// add a new (transient) G to collection in h
		// there is no cascade from H to the collection, so this should fail when merged
		assertEquals( 1, a.getHs().size() );
		H h = (H) a.getHs().iterator().next();
		G gNew = new G();
		gNew.setData( "Gail" );
		gNew.getHs().add( h );
		h.getGs().add( gNew );

		s = openSession();
		s.beginTransaction();
		try {
			s.merge( a );
			s.merge( h );
			s.getTransaction().commit();
			fail( "should have thrown TransientObjectException" );
		}
		catch (TransientObjectException ex) {
			// expected
		}
		finally {
			s.getTransaction().rollback();
		}
		s.close();
	}

	@Test
	public void testMultiPathMergeNonCascadedTransientEntityInOneToOne() throws Exception {
		// persist a simple A in the database

		Session s = openSession();
		s.beginTransaction();
		A a = new A();
		a.setData( "Anna" );
		s.save( a );
		s.getTransaction().commit();
		s.close();

		// modify detached entity
		modifyEntity( a );

		s = openSession();
		s.beginTransaction();
		a = (A) s.merge( a );
		s.getTransaction().commit();
		s.close();

		verifyModifications( a.getId() );

		// change the one-to-one association from g to be a new (transient) A
		// there is no cascade from G to A, so this should fail when merged
		G g = a.getG();
		a.setG( null );
		A aNew = new A();
		aNew.setData( "Alice" );
		g.setA( aNew );
		aNew.setG( g );

		s = openSession();
		s.beginTransaction();
		try {
			s.merge( a );
			s.merge( g );
			s.getTransaction().commit();
			fail( "should have thrown TransientObjectException" );
		}
		catch (TransientObjectException ex) {
			// expected
		}
		finally {
			s.getTransaction().rollback();
		}
		s.close();
	}

	@Test
	public void testMultiPathMergeNonCascadedTransientEntityInManyToOne() throws Exception {
		// persist a simple A in the database

		Session s = openSession();
		s.beginTransaction();
		A a = new A();
		a.setData( "Anna" );
		s.save( a );
		s.getTransaction().commit();
		s.close();

		// modify detached entity
		modifyEntity( a );

		s = openSession();
		s.beginTransaction();
		a = (A) s.merge( a );
		s.getTransaction().commit();
		s.close();

		verifyModifications( a.getId() );

		// change the many-to-one association from h to be a new (transient) A
		// there is no cascade from H to A, so this should fail when merged
		assertEquals( 1, a.getHs().size() );
		H h = (H) a.getHs().iterator().next();
		a.getHs().remove( h );
		A aNew = new A();
		aNew.setData( "Alice" );
		aNew.addH( h );

		s = openSession();
		s.beginTransaction();
		try {
			s.merge( a );
			s.merge( h );
			s.getTransaction().commit();
			fail( "should have thrown TransientObjectException" );
		}
		catch (TransientObjectException ex) {
			// expected
		}
		finally {
			s.getTransaction().rollback();
		}
		s.close();
	}

	private void modifyEntity(A a) {
		// create a *circular* graph in detached entity
		a.setData( "Anthony" );

		G g = new G();
		g.setData( "Giovanni" );

		H h = new H();
		h.setData( "Hellen" );

		a.setG( g );
		g.setA( a );

		a.getHs().add( h );
		h.setA( a );

		g.getHs().add( h );
		h.getGs().add( g );
	}

	private void verifyModifications(long aId) {
		Session s = openSession();
		s.beginTransaction();

		// retrieve the A object and check it
		A a = (A) s.get( A.class, new Long( aId ) );
		assertEquals( aId, a.getId() );
		assertEquals( "Anthony", a.getData() );
		assertNotNull( a.getG() );
		assertNotNull( a.getHs() );
		assertEquals( 1, a.getHs().size() );

		G gFromA = a.getG();
		H hFromA = (H) a.getHs().iterator().next();

		// check the G object
		assertEquals( "Giovanni", gFromA.getData() );
		assertSame( a, gFromA.getA() );
		assertNotNull( gFromA.getHs() );
		assertEquals( a.getHs(), gFromA.getHs() );
		assertSame( hFromA, gFromA.getHs().iterator().next() );

		// check the H object
		assertEquals( "Hellen", hFromA.getData() );
		assertSame( a, hFromA.getA() );
		assertNotNull( hFromA.getGs() );
		assertEquals( 1, hFromA.getGs().size() );
		assertSame( gFromA, hFromA.getGs().iterator().next() );

		s.getTransaction().commit();
		s.close();
	}

}
