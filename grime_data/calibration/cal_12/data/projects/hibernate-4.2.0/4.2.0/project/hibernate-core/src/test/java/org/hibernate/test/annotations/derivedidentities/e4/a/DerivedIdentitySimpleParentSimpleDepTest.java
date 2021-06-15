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
package org.hibernate.test.annotations.derivedidentities.e4.a;

import java.util.Date;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class DerivedIdentitySimpleParentSimpleDepTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testOneToOneExplicitJoinColumn() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "MedicalHistory", "FK", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "MedicalHistory", "id", configuration() ) );

		Session s = openSession();
		s.getTransaction().begin();
		Person person = new Person( "aaa" );
		s.persist( person );
		MedicalHistory medicalHistory = new MedicalHistory( person );
		s.persist( medicalHistory );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		medicalHistory = (MedicalHistory) s.get( MedicalHistory.class, "aaa" );
		assertEquals( person.ssn, medicalHistory.patient.ssn );
		medicalHistory.lastupdate = new Date();
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		medicalHistory = (MedicalHistory) s.get( MedicalHistory.class, "aaa" );
		assertNotNull( medicalHistory.lastupdate );
		s.delete( medicalHistory );
		s.delete( medicalHistory.patient );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testManyToOneExplicitJoinColumn() throws Exception {
		assertTrue( SchemaUtil.isColumnPresent( "FinancialHistory", "patient_ssn", configuration() ) );
		assertTrue( ! SchemaUtil.isColumnPresent( "FinancialHistory", "id", configuration() ) );

		Session s = openSession();
		s.getTransaction().begin();
		Person person = new Person( "aaa" );
		s.persist( person );
		FinancialHistory financialHistory = new FinancialHistory( person );
		s.persist( financialHistory );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		financialHistory = (FinancialHistory) s.get( FinancialHistory.class, "aaa" );
		assertEquals( person.ssn, financialHistory.patient.ssn );
		financialHistory.lastUpdate = new Date();
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		financialHistory = (FinancialHistory) s.get( FinancialHistory.class, "aaa" );
		assertNotNull( financialHistory.lastUpdate );
		s.delete( financialHistory );
		s.delete( financialHistory.patient );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testSimplePkValueLoading() {
		Session s = openSession();
		s.getTransaction().begin();
		Person e = new Person( "aaa" );
		s.persist( e );
		FinancialHistory d = new FinancialHistory( e );
		s.persist( d );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		FinancialHistory history = (FinancialHistory) s.get( FinancialHistory.class, "aaa" );
		assertNotNull( history );
		s.delete( history );
		s.delete( history.patient );
		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				MedicalHistory.class,
				Simple.class,
				Person.class,
				FinancialHistory.class
		};
	}
}