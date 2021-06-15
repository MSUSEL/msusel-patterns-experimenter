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
package org.hibernate.test.subclassfilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class UnionSubclassFilterTest extends BaseCoreFunctionalTestCase {
	public final String[] getMappings() {
		return new String[] { "subclassfilter/union-subclass.hbm.xml" };
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testFiltersWithUnionSubclass() {
		Session s = openSession();
		s.enableFilter( "region" ).setParameter( "userRegion", "US" );
		Transaction t = s.beginTransaction();

		prepareTestData( s );
		s.clear();

		List results;
		Iterator itr;

		results = s.createQuery( "from Person" ).list();
		assertEquals( "Incorrect qry result count", 4, results.size() );
		s.clear();

		results = s.createQuery( "from Employee" ).list();
		assertEquals( "Incorrect qry result count", 2, results.size() );
		s.clear();

		results = new ArrayList( new HashSet( s.createQuery( "from Person as p left join fetch p.minions" ).list() ) );
		assertEquals( "Incorrect qry result count", 4, results.size() );
		itr = results.iterator();
		while ( itr.hasNext() ) {
			// find john
			final Person p = ( Person ) itr.next();
			if ( p.getName().equals( "John Doe" ) ) {
				Employee john = ( Employee ) p;
				assertEquals( "Incorrect fecthed minions count", 1, john.getMinions().size() );
				break;
			}
		}
		s.clear();

		results = new ArrayList( new HashSet( s.createQuery( "from Employee as p left join fetch p.minions" ).list() ) );
		assertEquals( "Incorrect qry result count", 2, results.size() );
		itr = results.iterator();
		while ( itr.hasNext() ) {
			// find john
			final Person p = ( Person ) itr.next();
			if ( p.getName().equals( "John Doe" ) ) {
				Employee john = ( Employee ) p;
				assertEquals( "Incorrect fecthed minions count", 1, john.getMinions().size() );
				break;
			}
		}

		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		for ( Object entity : s.createQuery( "from Person" ).list() ) {
			s.delete( entity );
		}
		t.commit();
		s.close();

	}

	@SuppressWarnings( {"unchecked"})
	private void prepareTestData(Session s) {
		Employee john = new Employee( "John Doe" );
		john.setCompany( "JBoss" );
		john.setDepartment( "hr" );
		john.setTitle( "hr guru" );
		john.setRegion( "US" );

		Employee polli = new Employee( "Polli Wog" );
		polli.setCompany( "JBoss" );
		polli.setDepartment( "hr" );
		polli.setTitle( "hr novice" );
		polli.setRegion( "US" );
		polli.setManager( john );
		john.getMinions().add( polli );

		Employee suzie = new Employee( "Suzie Q" );
		suzie.setCompany( "JBoss" );
		suzie.setDepartment( "hr" );
		suzie.setTitle( "hr novice" );
		suzie.setRegion( "EMEA" );
		suzie.setManager( john );
		john.getMinions().add( suzie );

		Customer cust = new Customer( "John Q Public" );
		cust.setCompany( "Acme" );
		cust.setRegion( "US" );
		cust.setContactOwner( john );

		Person ups = new Person( "UPS guy" );
		ups.setCompany( "UPS" );
		ups.setRegion( "US" );

		s.save( john );
		s.save( cust );
		s.save( ups );

		s.flush();
	}
}
