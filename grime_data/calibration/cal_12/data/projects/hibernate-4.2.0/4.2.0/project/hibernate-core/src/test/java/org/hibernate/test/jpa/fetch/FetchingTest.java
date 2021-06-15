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
package org.hibernate.test.jpa.fetch;
import java.util.Date;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.jpa.AbstractJPATest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class FetchingTest extends AbstractJPATest {
	@Override
	public String[] getMappings() {
		return new String[] { "jpa/fetch/Person.hbm.xml" };
	}

	@Test
	public void testLazy() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Person p = new Person( "Gavin", "King", "JBoss Inc" );
		Stay stay = new Stay( p, new Date(), new Date(), "A380", "Blah", "Blah" );
		p.addStay( stay );
		s.persist( p );
		tx.commit();
		s.clear();
		tx = s.beginTransaction();
		p = (Person) s.createQuery( "select p from Person p where p.firstName = :name" )
				.setParameter( "name", "Gavin" ).uniqueResult();
		assertFalse( Hibernate.isInitialized( p.getStays() ) );
		s.delete( p );
		tx.commit();
		s.close();
	}

	@Test
	public void testHibernateFetchingLazy() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Person p = new Person( "Gavin", "King", "JBoss Inc" );
		Stay stay = new Stay( null, new Date(), new Date(), "A380", "Blah", "Blah" );
		Stay stay2 = new Stay( null, new Date(), new Date(), "A320", "Blah", "Blah" );
		Stay stay3 = new Stay( null, new Date(), new Date(), "A340", "Blah", "Blah" );
		stay.setOldPerson( p );
		stay2.setVeryOldPerson( p );
		stay3.setVeryOldPerson( p );
		p.addOldStay( stay );
		p.addVeryOldStay( stay2 );
		p.addVeryOldStay( stay3 );
		s.persist( p );
		tx.commit();
		s.clear();
		tx = s.beginTransaction();
		p = (Person) s.createQuery( "select p from Person p where p.firstName = :name" )
				.setParameter( "name", "Gavin" ).uniqueResult();
		assertFalse( Hibernate.isInitialized( p.getOldStays() ) );
		assertEquals( 1, p.getOldStays().size() );
		assertFalse( "lazy extra is failing", Hibernate.isInitialized( p.getOldStays() ) );
		s.clear();
		stay = (Stay) s.get( Stay.class, stay.getId() );
		assertTrue( ! Hibernate.isInitialized( stay.getOldPerson() ) );
		s.clear();
		stay3 = (Stay) s.get( Stay.class, stay3.getId() );
		assertTrue( "FetchMode.JOIN should overrides lazy options", Hibernate.isInitialized( stay3.getVeryOldPerson() ) );
		s.delete( stay3.getVeryOldPerson() );
		tx.commit();
		s.close();
	}
}
