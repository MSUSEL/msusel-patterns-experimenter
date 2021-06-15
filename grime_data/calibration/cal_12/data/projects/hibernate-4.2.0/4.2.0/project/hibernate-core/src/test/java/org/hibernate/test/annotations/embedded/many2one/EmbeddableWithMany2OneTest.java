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
package org.hibernate.test.annotations.embedded.many2one;

import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class EmbeddableWithMany2OneTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Person.class, Country.class };
	}

	@Test
	public void testJoinAcrossEmbedded() {
		Session session = openSession();
		session.beginTransaction();
		session.createQuery( "from Person p join p.address as a join a.country as c where c.name = 'US'" )
				.list();
		session.createQuery( "from Person p join p.address as a join a.country as c where c.id = 'US'" )
				.list();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testBasicOps() {
		Session session = openSession();
		session.beginTransaction();
		Country country = new Country( "US", "United States of America" );
		session.persist( country );
		Person person = new Person( "Steve", new Address() );
		person.getAddress().setLine1( "123 Main" );
		person.getAddress().setCity( "Anywhere" );
		person.getAddress().setCountry( country );
		person.getAddress().setPostalCode( "123456789" );
		session.persist( person );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.createQuery( "from Person p where p.address.country.iso2 = 'US'" )
				.list();
		// same query!
		session.createQuery( "from Person p where p.address.country.id = 'US'" )
				.list();
		person = (Person) session.load( Person.class, person.getId() );
		session.delete( person );
		List countries = session.createQuery( "from Country" ).list();
		assertEquals( 1, countries.size() );
		session.delete( countries.get( 0 ) );

		session.getTransaction().commit();
		session.close();
	}
}
