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
package org.hibernate.test.onetoone.link;

import java.util.Date;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class OneToOneLinkTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "onetoone/link/Person.hbm.xml" };
	}

	@Test
	public void testOneToOneViaAssociationTable() {
		Person p = new Person();
		p.setName("Gavin King");
		p.setDob( new Date() );
		Employee e = new Employee();
		p.setEmployee(e);
		e.setPerson(p);
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.persist(p);
		t.commit();
		s.close();
	
		s = openSession();
		t = s.beginTransaction();
		e = (Employee) s.createQuery("from Employee e where e.person.name like 'Gavin%'").uniqueResult();
		assertEquals( e.getPerson().getName(), "Gavin King" );
		assertFalse( Hibernate.isInitialized( e.getPerson() ) );
		assertNull( e.getPerson().getCustomer() );
		s.clear();

		e = (Employee) s.createQuery("from Employee e where e.person.dob = :date")
			.setDate("date", new Date() )
			.uniqueResult();
		assertEquals( e.getPerson().getName(), "Gavin King" );
		assertFalse( Hibernate.isInitialized( e.getPerson() ) );
		assertNull( e.getPerson().getCustomer() );
		s.clear();
		
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();

		e = (Employee) s.createQuery("from Employee e join fetch e.person p left join fetch p.customer").uniqueResult();
		assertTrue( Hibernate.isInitialized( e.getPerson() ) );
		assertNull( e.getPerson().getCustomer() );
		Customer c = new Customer();
		e.getPerson().setCustomer(c);
		c.setPerson( e.getPerson() );
		
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();

		e = (Employee) s.createQuery("from Employee e join fetch e.person p left join fetch p.customer").uniqueResult();
		assertTrue( Hibernate.isInitialized( e.getPerson() ) );
		assertTrue( Hibernate.isInitialized( e.getPerson().getCustomer() ) );
		assertNotNull( e.getPerson().getCustomer() );
		s.delete(e);
		t.commit();
		s.close();
		
	}

}

