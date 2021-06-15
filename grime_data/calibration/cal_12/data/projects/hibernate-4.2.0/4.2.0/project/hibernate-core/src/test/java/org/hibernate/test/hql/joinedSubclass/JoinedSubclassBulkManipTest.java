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
package org.hibernate.test.hql.joinedSubclass;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Steve Ebersole
 */
public class JoinedSubclassBulkManipTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Employee.class };
	}

	@Test
	@TestForIssue( jiraKey = "HHH-1657" )
	public void testHqlDeleteOnJoinedSubclass() {
		Session s = openSession();
		s.beginTransaction();
		// syntax checking on the database...
		s.createQuery( "delete from Employee" ).executeUpdate();
		s.createQuery( "delete from Person" ).executeUpdate();
		s.createQuery( "delete from Employee e" ).executeUpdate();
		s.createQuery( "delete from Person p" ).executeUpdate();
		s.createQuery( "delete from Employee where name like 'S%'" ).executeUpdate();
		s.createQuery( "delete from Employee e where e.name like 'S%'" ).executeUpdate();
		s.createQuery( "delete from Person where name like 'S%'" ).executeUpdate();
		s.createQuery( "delete from Person p where p.name like 'S%'" ).executeUpdate();

		// now the forms that actually fail from problem underlying HHH-1657
		// which is limited to references to properties mapped to column names existing in both tables
		// which is normally just the pks.  super critical ;)

		s.createQuery( "delete from Employee where id = 1" ).executeUpdate();
		s.createQuery( "delete from Employee e where e.id = 1" ).executeUpdate();
		s.createQuery( "delete from Person where id = 1" ).executeUpdate();
		s.createQuery( "delete from Person p where p.id = 1" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-1657" )
	public void testHqlUpdateOnJoinedSubclass() {
		Session s = openSession();
		s.beginTransaction();
		// syntax checking on the database...
		s.createQuery( "update Employee set name = 'Some Other Name' where employeeNumber like 'A%'" ).executeUpdate();
		s.createQuery( "update Employee e set e.name = 'Some Other Name' where e.employeeNumber like 'A%'" ).executeUpdate();
		s.createQuery( "update Person set name = 'Some Other Name' where name like 'S%'" ).executeUpdate();
		s.createQuery( "update Person p set p.name = 'Some Other Name' where p.name like 'S%'" ).executeUpdate();

		// now the forms that actually fail from problem underlying HHH-1657
		// which is limited to references to properties mapped to column names existing in both tables
		// which is normally just the pks.  super critical ;)

		s.createQuery( "update Employee set name = 'Some Other Name' where id = 1" ).executeUpdate();
		s.createQuery( "update Employee e set e.name = 'Some Other Name' where e.id = 1" ).executeUpdate();
		s.createQuery( "update Person set name = 'Some Other Name' where id = 1" ).executeUpdate();
		s.createQuery( "update Person p set p.name = 'Some Other Name' where p.id = 1" ).executeUpdate();

		s.getTransaction().commit();
		s.close();
	}
}
