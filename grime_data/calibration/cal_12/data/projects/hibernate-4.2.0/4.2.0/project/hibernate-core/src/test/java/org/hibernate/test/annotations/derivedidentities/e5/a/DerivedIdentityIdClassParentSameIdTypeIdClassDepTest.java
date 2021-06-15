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
package org.hibernate.test.annotations.derivedidentities.e5.a;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class DerivedIdentityIdClassParentSameIdTypeIdClassDepTest extends BaseCoreFunctionalTestCase {
	private static final String FIRST_NAME = "Emmanuel";
	private static final String LAST_NAME = "Bernard";

	@Test
	public void testOneToOneExplicitJoinColumn() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "MedicalHistory", "FK1", configuration() ) );
		assertTrue( SchemaUtil.isColumnPresent( "MedicalHistory", "FK2", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "MedicalHistory", "firstname", configuration() ) );

		Session s = openSession();
		s.getTransaction().begin();
		Person e = new Person( FIRST_NAME, LAST_NAME );
		s.persist( e );
		MedicalHistory d = new MedicalHistory( e );
		s.persist( d );
		s.flush();
		s.refresh( d );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		PersonId pId = new PersonId( FIRST_NAME, LAST_NAME );
		MedicalHistory d2 = (MedicalHistory) s.get( MedicalHistory.class, pId );
		Person p2 = (Person) s.get( Person.class, pId );
		assertEquals( pId.firstName, d2.patient.firstName );
		assertEquals( pId.firstName, p2.firstName );
		s.delete( d2 );
		s.delete( p2 );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testTckLikeBehavior() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "MedicalHistory", "FK1", configuration() ) );
		assertTrue( SchemaUtil.isColumnPresent( "MedicalHistory", "FK2", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "MedicalHistory", "firstname", configuration() ) );

		Session s = openSession();
		s.getTransaction().begin();
		Person e = new Person( FIRST_NAME, LAST_NAME );
		s.persist( e );
		MedicalHistory d = new MedicalHistory( e );
		s.persist( d );
		s.flush();
		s.refresh( d );
		s.getTransaction().commit();

		// NOTE THAT WE LEAVE THE SESSION OPEN!

		s.getTransaction().begin();
		PersonId pId = new PersonId( FIRST_NAME, LAST_NAME );
		MedicalHistory d2 = (MedicalHistory) s.get( MedicalHistory.class, pId );
		Person p2 = (Person) s.get( Person.class, pId );
		assertEquals( pId.firstName, d2.patient.firstName );
		assertEquals( pId.firstName, p2.firstName );
		s.delete( d2 );
		s.delete( p2 );
		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				MedicalHistory.class,
				Person.class
		};
	}
}
