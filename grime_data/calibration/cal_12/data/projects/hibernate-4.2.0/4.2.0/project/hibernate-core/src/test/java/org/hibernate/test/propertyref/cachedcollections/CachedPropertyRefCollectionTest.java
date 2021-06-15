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
package org.hibernate.test.propertyref.cachedcollections;

import org.junit.Test;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Set of tests originally developed to verify and fix HHH-5853
 *
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-5853" )
public class CachedPropertyRefCollectionTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[]{"propertyref/cachedcollections/Mappings.hbm.xml"};
	}

	@Test
	public void testRetrievalOfCachedCollectionWithPropertyRefKey() {
		// create the test data...
		Session session = openSession();
		session.beginTransaction();
		ManagedObject mo = new ManagedObject( "test", "test" );
		mo.getMembers().add( "members" );
		session.save( mo );
		session.getTransaction().commit();
		session.close();

		// First attempt to load it via PK lookup
		session = openSession();
		session.beginTransaction();
		ManagedObject obj = (ManagedObject) session.get( ManagedObject.class, 1L );
		assertNotNull( obj );
		assertTrue( Hibernate.isInitialized( obj ) );
		obj.getMembers().size();
		assertTrue( Hibernate.isInitialized( obj.getMembers() ) );
		session.getTransaction().commit();
		session.close();

		// Now try to access it via natural key
		session = openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria( ManagedObject.class )
				.add( Restrictions.naturalId().set( "name", "test" ) )
				.setCacheable( true )
				.setFetchMode( "members", FetchMode.JOIN );
		obj = (ManagedObject) criteria.uniqueResult();
		assertNotNull( obj );
		assertTrue( Hibernate.isInitialized( obj ) );
		obj.getMembers().size();
		assertTrue( Hibernate.isInitialized( obj.getMembers() ) );
		session.getTransaction().commit();
		session.close();

		// Clean up
		session = openSession();
		session.beginTransaction();
		session.delete( obj );
		session.getTransaction().commit();
		session.close();
	}
}

