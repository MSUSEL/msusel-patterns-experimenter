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
package org.hibernate.test.propertyref.component.complete;


import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class CompleteComponentPropertyRefTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "propertyref/component/complete/Mapping.hbm.xml" };
	}

	@Override
	protected String getCacheConcurrencyStrategy() {
		return "nonstrict-read-write";
	}

	@Test
	public void testComponentPropertyRef() {
		Session s = openSession();
		s.beginTransaction();
		Person p = new Person();
		p.setIdentity( new Identity() );
		Account a = new Account();
		a.setNumber("123-12345-1236");
		a.setOwner(p);
		p.getIdentity().setName("Gavin");
		p.getIdentity().setSsn("123-12-1234");
		s.persist(p);
		s.persist(a);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		a = (Account) s.createQuery("from Account a left join fetch a.owner").uniqueResult();
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );
		assertNotNull( a.getOwner() );
		assertEquals( "Gavin", a.getOwner().getIdentity().getName() );
		s.clear();

		a = (Account) s.get(Account.class, "123-12345-1236");
		assertFalse( Hibernate.isInitialized( a.getOwner() ) );
		assertNotNull( a.getOwner() );
		assertEquals( "Gavin", a.getOwner().getIdentity().getName() );
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );

		s.clear();

		sessionFactory().getCache().evictEntityRegion( Account.class );
		sessionFactory().getCache().evictEntityRegion( Person.class );

		a = (Account) s.get(Account.class, "123-12345-1236");
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );
		assertNotNull( a.getOwner() );
		assertEquals( "Gavin", a.getOwner().getIdentity().getName() );
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );

		s.delete( a );
		s.delete( a.getOwner() );
		s.getTransaction().commit();
		s.close();
	}
}
