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
package org.hibernate.test.sql.hand.custom;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.sql.hand.Employment;
import org.hibernate.test.sql.hand.Organization;
import org.hibernate.test.sql.hand.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Abstract test case defining tests of stored procedure support.
 *
 * @author Gail Badner
 */
@SuppressWarnings( {"UnusedDeclaration"})
public abstract class CustomStoredProcTestSupport extends CustomSQLTestSupport {
	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testScalarStoredProcedure() throws HibernateException, SQLException {
		Session s = openSession();
		Query namedQuery = s.getNamedQuery( "simpleScalar" );
		namedQuery.setLong( "number", 43 );
		List list = namedQuery.list();
		Object o[] = ( Object[] ) list.get( 0 );
		assertEquals( o[0], "getAll" );
		assertEquals( o[1], Long.valueOf( 43 ) );
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testParameterHandling() throws HibernateException, SQLException {
		Session s = openSession();

		Query namedQuery = s.getNamedQuery( "paramhandling" );
		namedQuery.setLong( 0, 10 );
		namedQuery.setLong( 1, 20 );
		List list = namedQuery.list();
		Object[] o = ( Object[] ) list.get( 0 );
		assertEquals( o[0], Long.valueOf( 10 ) );
		assertEquals( o[1], Long.valueOf( 20 ) );

		namedQuery = s.getNamedQuery( "paramhandling_mixed" );
		namedQuery.setLong( 0, 10 );
		namedQuery.setLong( "second", 20 );
		list = namedQuery.list();
		o = ( Object[] ) list.get( 0 );
		assertEquals( o[0], Long.valueOf( 10 ) );
		assertEquals( o[1], Long.valueOf( 20 ) );
		s.close();
	}

	@Test
	public void testEntityStoredProcedure() throws HibernateException, SQLException {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		Organization ifa = new Organization( "IFA" );
		Organization jboss = new Organization( "JBoss" );
		Person gavin = new Person( "Gavin" );
		Employment emp = new Employment( gavin, jboss, "AU" );
		s.persist( ifa );
		s.persist( jboss );
		s.persist( gavin );
		s.persist( emp );

		Query namedQuery = s.getNamedQuery( "selectAllEmployments" );
		List list = namedQuery.list();
		assertTrue( list.get( 0 ) instanceof Employment );
		s.delete( emp );
		s.delete( ifa );
		s.delete( jboss );
		s.delete( gavin );

		t.commit();
		s.close();
	}


}
