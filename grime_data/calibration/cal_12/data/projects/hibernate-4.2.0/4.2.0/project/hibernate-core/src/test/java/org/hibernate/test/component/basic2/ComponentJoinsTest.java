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
package org.hibernate.test.component.basic2;

import org.hibernate.testing.TestForIssue;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * Tests related to specifying joins on components (embedded values).
 *
 * @author Steve Ebersole
 */
public class ComponentJoinsTest extends BaseCoreFunctionalTestCase {
	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[] {
				Person.class,
				Component.class,
				Component.Emb.Stuff.class };
	}

	@Test
	public void testComponentJoins() {
		// Just checking proper query construction and syntax checking via database query parser...
		Session session = openSession();
		session.beginTransaction();
		// use it in WHERE
		session.createQuery( "select p from Person p join p.name as n where n.lastName like '%'" ).list();
		// use it in SELECT
		session.createQuery( "select n.lastName from Person p join p.name as n" ).list();
		session.createQuery( "select n from Person p join p.name as n" ).list();
		// use it in ORDER BY
		session.createQuery( "select n from Person p join p.name as n order by n.lastName" ).list();
		session.createQuery( "select n from Person p join p.name as n order by p" ).list();
		session.createQuery( "select n from Person p join p.name as n order by n" ).list();
		session.getTransaction().commit();
		session.close();
	}

	@Test
	@TestForIssue(jiraKey = "HHH-7849")
	public void testComponentJoinsHHH7849() {
		// Just checking proper query construction and syntax checking via database query parser...
		Session session = openSession();
		session.beginTransaction();
		// use it in WHERE
		session.createQuery( "select c from Component c join c.emb as e where e.stuffs is empty " ).list();

		session.getTransaction().commit();
		session.close();
	}
}
