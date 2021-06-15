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
package org.hibernate.test.annotations.embedded.one2many;

import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.FailureExpected;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class EmbeddableWithOne2ManyTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
//		return new Class[] { Alias.class, Person.class };
		return new Class[] {  };
	}

	@Test
	@FailureExpected( jiraKey = "HHH-4883")
	public void testJoinAcrossEmbedded() {
		// NOTE : this may or may not work now with HHH-4883 fixed,
		// but i cannot do this checking until HHH-4599 is done.
		Session session = openSession();
		session.beginTransaction();
		session.createQuery( "from Person p join p.name.aliases a where a.source = 'FBI'" )
				.list();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@FailureExpected( jiraKey = "HHH-4599")
	public void testBasicOps() {
		Session session = openSession();
		session.beginTransaction();
		Alias alias = new Alias( "Public Enemy", "Number 1", "FBI" );
		session.persist( alias );
		Person person = new Person( "John", "Dillinger" );
		person.getName().getAliases().add( alias );
		session.persist( person );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		person = (Person) session.load( Person.class, person.getId() );
		session.delete( person );
		List aliases = session.createQuery( "from Alias" ).list();
		assertEquals( 0, aliases.size() );
		session.getTransaction().commit();
		session.close();
	}
}