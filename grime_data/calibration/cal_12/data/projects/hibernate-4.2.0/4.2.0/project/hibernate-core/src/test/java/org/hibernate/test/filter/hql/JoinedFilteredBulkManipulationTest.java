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
package org.hibernate.test.filter.hql;
import java.util.Date;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class JoinedFilteredBulkManipulationTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] {
			"filter/hql/filter-defs.hbm.xml",
			"filter/hql/Joined.hbm.xml"
		};
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testFilteredJoinedSubclassHqlDeleteRoot() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new Employee( "John", 'M', "john", new Date() ) );
		s.save( new Employee( "Jane", 'F', "jane", new Date() ) );
		s.save( new Customer( "Charlie", 'M', "charlie", "Acme" ) );
		s.save( new Customer( "Wanda", 'F', "wanda", "ABC" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.enableFilter( "sex" ).setParameter( "sexCode", Character.valueOf( 'M' ) );
		int count = s.createQuery( "delete Person" ).executeUpdate();
		assertEquals( 2, count );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete Person" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testFilteredJoinedSubclassHqlDeleteNonLeaf() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new Employee( "John", 'M', "john", new Date() ) );
		s.save( new Employee( "Jane", 'F', "jane", new Date() ) );
		s.save( new Customer( "Charlie", 'M', "charlie", "Acme" ) );
		s.save( new Customer( "Wanda", 'F', "wanda", "ABC" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.enableFilter( "sex" ).setParameter( "sexCode", Character.valueOf( 'M' ) );
		int count = s.createQuery( "delete User" ).executeUpdate();
		assertEquals( 2, count );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete Person" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testFilteredJoinedSubclassHqlDeleteLeaf() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new Employee( "John", 'M', "john", new Date() ) );
		s.save( new Employee( "Jane", 'F', "jane", new Date() ) );
		s.save( new Customer( "Charlie", 'M', "charlie", "Acme" ) );
		s.save( new Customer( "Wanda", 'F', "wanda", "ABC" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.enableFilter( "sex" ).setParameter( "sexCode", Character.valueOf( 'M' ) );
		int count = s.createQuery( "delete Employee" ).executeUpdate();
		assertEquals( 1, count );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete Person" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testFilteredJoinedSubclassHqlUpdateRoot() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new Employee( "John", 'M', "john", new Date() ) );
		s.save( new Employee( "Jane", 'F', "jane", new Date() ) );
		s.save( new Customer( "Charlie", 'M', "charlie", "Acme" ) );
		s.save( new Customer( "Wanda", 'F', "wanda", "ABC" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.enableFilter( "sex" ).setParameter( "sexCode", Character.valueOf( 'M' ) );
		int count = s.createQuery( "update Person p set p.name = '<male>'" ).executeUpdate();
		assertEquals( 2, count );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete Person" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testFilteredJoinedSubclassHqlUpdateNonLeaf() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new Employee( "John", 'M', "john", new Date() ) );
		s.save( new Employee( "Jane", 'F', "jane", new Date() ) );
		s.save( new Customer( "Charlie", 'M', "charlie", "Acme" ) );
		s.save( new Customer( "Wanda", 'F', "wanda", "ABC" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.enableFilter( "sex" ).setParameter( "sexCode", Character.valueOf( 'M' ) );
		int count = s.createQuery( "update User u set u.username = :un where u.name = :n" )
				.setString( "un", "charlie" )
				.setString( "n", "Wanda" )
				.executeUpdate();
		assertEquals( 0, count );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete Person" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public void testFilteredJoinedSubclassHqlUpdateLeaf() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new Employee( "John", 'M', "john", new Date() ) );
		s.save( new Employee( "Jane", 'F', "jane", new Date() ) );
		s.save( new Customer( "Charlie", 'M', "charlie", "Acme" ) );
		s.save( new Customer( "Wanda", 'F', "wanda", "ABC" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.enableFilter( "sex" ).setParameter( "sexCode", Character.valueOf( 'M' ) );
		int count = s.createQuery( "update Customer c set c.company = 'XYZ'" ).executeUpdate();
		assertEquals( 1, count );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete Person" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}
}
