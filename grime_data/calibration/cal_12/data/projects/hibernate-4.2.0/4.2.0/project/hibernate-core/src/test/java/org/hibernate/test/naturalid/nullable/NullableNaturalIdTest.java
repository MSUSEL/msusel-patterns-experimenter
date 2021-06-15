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
package org.hibernate.test.naturalid.nullable;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Steve Ebersole
 */
public class NullableNaturalIdTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { A.class, B.class, C.class, D.class };
	}

	@Test
	public void testNaturalIdNullValueOnPersist() {
		Session session = openSession();
		session.beginTransaction();
		C c = new C();
		session.persist( c );
		c.name = "someName";
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.delete( c );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testUniqueAssociation() {
		Session session = openSession();
		session.beginTransaction();
		A a = new A();
		B b = new B();
		b.naturalid = 100;
		session.persist( a );
		session.persist( b ); //b.assA is declared NaturalId, his value is null this moment
		b.assA = a;
		a.assB.add( b );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		// this is OK
		assertNotNull( session.byNaturalId( B.class ).using( "naturalid", 100 ).using( "assA", a ).load() );
		// this fails, cause EntityType.compare(Object x, Object y) always returns 0 !
		assertNull( session.byNaturalId( B.class ).using( "naturalid", 100 ).using( "assA", null ).load() );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.delete( b );
		session.delete( a );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testNaturalIdQuerySupportingNullValues() {
		Session session = openSession();
		session.beginTransaction();
		D d1 = new D();
		d1.name = "Titi";
		d1.associatedC = null;
		D d2 = new D();
		d2.name = null;
		C c = new C();
		d2.associatedC = c;
		session.persist( d1 );
		session.persist( d2 );
		session.persist( c );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		assertNotNull( session.byNaturalId( D.class ).using( "name", null ).using( "associatedC", c ).load() );
		assertNotNull( session.byNaturalId( D.class ).using( "name", "Titi" ).using( "associatedC", null ).load() );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.delete( c );
		session.delete( d1 );
		session.delete( d2 );
		session.getTransaction().commit();
		session.close();
	}
}
