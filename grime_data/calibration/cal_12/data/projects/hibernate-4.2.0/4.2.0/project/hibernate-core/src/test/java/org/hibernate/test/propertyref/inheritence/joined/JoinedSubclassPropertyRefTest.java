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
package org.hibernate.test.propertyref.inheritence.joined;

import org.junit.Test;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class JoinedSubclassPropertyRefTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "propertyref/inheritence/joined/Person.hbm.xml" };
	}

	@Test
	public void testPropertyRefToJoinedSubclass() {
		Session session = openSession();
		Transaction tx = session.beginTransaction();
		Person p = new Person();
		p.setName("Gavin King");
		BankAccount acc = new BankAccount();
		acc.setBsb("0634");
		acc.setType('B');
		acc.setAccountNumber("xxx-123-abc");
		p.setBankAccount(acc);
		session.persist(p);
		tx.commit();
		session.close();

		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.get(Person.class, p.getId());
		assertNotNull( p.getBankAccount() );
		assertTrue( Hibernate.isInitialized( p.getBankAccount() ) );
		tx.commit();
		session.close();

		session = openSession();
		tx = session.beginTransaction();
		p = (Person) session.createCriteria(Person.class)
			.setFetchMode("bankAccount", FetchMode.JOIN)
			.uniqueResult();
		assertNotNull( p.getBankAccount() );
		assertTrue( Hibernate.isInitialized( p.getBankAccount() ) );
		tx.commit();
		session.close();

		session = openSession();
		tx = session.beginTransaction();
		session.delete(p);
		tx.commit();
		session.close();
	}

}

