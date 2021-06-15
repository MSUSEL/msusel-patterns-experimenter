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
package org.hibernate.test.annotations.derivedidentities.e4.b;

import java.util.Date;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class DerivedIdentitySimpleParentSimpleDepMapsIdTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testOneToOneExplicitJoinColumn() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "MedicalHistory", "FK", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "MedicalHistory", "id", configuration() ) );
		Person e = new Person();
		e.ssn = "aaa";
		Session s = openSession(  );
		s.getTransaction().begin();
		s.persist( e );
		MedicalHistory d = new MedicalHistory();
		d.patient = e;
		//d.id = "aaa"; //FIXME not needed when foreign is enabled
		s.persist( d );
		s.flush();
		s.clear();
		d = (MedicalHistory) s.get( MedicalHistory.class, d.id );
		assertEquals( d.id, d.patient.ssn );
		d.lastupdate = new Date();
		s.flush();
		s.clear();
		d = (MedicalHistory) s.get( MedicalHistory.class, d.id );
		assertNotNull( d.lastupdate );
		s.getTransaction().rollback();
		s.close();
	}

	@Test
	public void testManyToOneExplicitJoinColumn() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "FinancialHistory", "FK", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "FinancialHistory", "id", configuration() ) );
		Person e = new Person();
		e.ssn = "aaa";
		Session s = openSession(  );
		s.getTransaction().begin();
		s.persist( e );
		FinancialHistory d = new FinancialHistory();
		d.patient = e;
		//d.id = "aaa"; //FIXME not needed when foreign is enabled
		s.persist( d );
		s.flush();
		s.clear();
		d = (FinancialHistory) s.get( FinancialHistory.class, d.id );
		assertEquals( d.id, d.patient.ssn );
		d.lastupdate = new Date();
		s.flush();
		s.clear();
		d = (FinancialHistory) s.get( FinancialHistory.class, d.id );
		assertNotNull( d.lastupdate );
		s.getTransaction().rollback();
		s.close();
	}

	@Test
	public void testExplicitlyAssignedDependentIdAttributeValue() {
		// even though the id is by definition generated (using the "foreign" strategy), JPA
		// still does allow manually setting the generated id attribute value which providers
		// are expected to promptly disregard :?
		Session s = openSession();
		s.beginTransaction();
		Person person = new Person( "123456789" );
		MedicalHistory medicalHistory = new MedicalHistory( "987654321", person );
		s.persist( person );
		s.persist( medicalHistory );
		s.getTransaction().commit();
		s.close();

		// again, even though we specified an id value of "987654321" prior to persist,
		// Hibernate should have replaced that with the "123456789" from the associated
		// person
		assertEquals( person.ssn, medicalHistory.patient.ssn );
		assertEquals( person, medicalHistory.patient );
		assertEquals( person.ssn, medicalHistory.id );

		s = openSession();
		s.beginTransaction();
		// Should return null...
		MedicalHistory separateMedicalHistory = (MedicalHistory) s.get( MedicalHistory.class, "987654321" );
		assertNull( separateMedicalHistory );
		// Now we should find it...
		separateMedicalHistory = (MedicalHistory) s.get( MedicalHistory.class, "123456789" );
		assertNotNull( separateMedicalHistory );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.delete( medicalHistory );
		s.delete( person );
		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				MedicalHistory.class,
				Person.class,
				FinancialHistory.class
		};
	}
}