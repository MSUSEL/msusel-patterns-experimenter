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

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.test.sql.hand.Employment;
import org.hibernate.test.sql.hand.ImageHolder;
import org.hibernate.test.sql.hand.Organization;
import org.hibernate.test.sql.hand.Person;
import org.hibernate.test.sql.hand.TextHolder;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Abstract test case defining tests for the support for user-supplied (aka
 * custom) insert, update, delete SQL.
 *
 * @author Steve Ebersole
 */
@SuppressWarnings( {"UnusedDeclaration"})
public abstract class CustomSQLTestSupport extends BaseCoreFunctionalTestCase {
	public String getCacheConcurrencyStrategy() {
		return null;
	}

	@Test
	public void testHandSQL() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Organization ifa = new Organization( "IFA" );
		Organization jboss = new Organization( "JBoss" );
		Person gavin = new Person( "Gavin" );
		Employment emp = new Employment( gavin, jboss, "AU" );
		Serializable orgId = s.save( jboss );
		s.save( ifa );
		s.save( gavin );
		s.save( emp );
		t.commit();

		t = s.beginTransaction();
		Person christian = new Person( "Christian" );
		s.save( christian );
		Employment emp2 = new Employment( christian, jboss, "EU" );
		s.save( emp2 );
		t.commit();
		s.close();

		sessionFactory().getCache().evictEntityRegion( Organization.class );
		sessionFactory().getCache().evictEntityRegion( Person.class );
		sessionFactory().getCache().evictEntityRegion( Employment.class );

		s = openSession();
		t = s.beginTransaction();
		jboss = ( Organization ) s.get( Organization.class, orgId );
		assertEquals( jboss.getEmployments().size(), 2 );
		assertEquals( jboss.getName(), "JBOSS" );
		emp = ( Employment ) jboss.getEmployments().iterator().next();
		gavin = emp.getEmployee();
		assertEquals( "GAVIN" , gavin.getName() );
		assertEquals( LockMode.UPGRADE , s.getCurrentLockMode( gavin ));
		emp.setEndDate( new Date() );
		Employment emp3 = new Employment( gavin, jboss, "US" );
		s.save( emp3 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Iterator itr = s.getNamedQuery( "allOrganizationsWithEmployees" ).list().iterator();
		assertTrue( itr.hasNext() );
		Organization o = ( Organization ) itr.next();
		assertEquals( o.getEmployments().size(), 3 );
		Iterator itr2 = o.getEmployments().iterator();
		while ( itr2.hasNext() ) {
			Employment e = ( Employment ) itr2.next();
			s.delete( e );
		}
		itr2 = o.getEmployments().iterator();
		while ( itr2.hasNext() ) {
			Employment e = ( Employment ) itr2.next();
			s.delete( e.getEmployee() );
		}
		s.delete( o );
		assertFalse( itr.hasNext() );
		s.delete( ifa );
		t.commit();
		s.close();
	}

	@Test
	public void testTextProperty() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		String description = buildLongString( 15000, 'a' );
		TextHolder holder = new TextHolder( description );
		s.save( holder );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		holder = ( TextHolder ) s.get(  TextHolder.class, holder.getId() );
		assertEquals( description, holder.getDescription() );
		description = buildLongString( 15000, 'b' );
		holder.setDescription( description );
		s.save( holder );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		holder = ( TextHolder ) s.get(  TextHolder.class, holder.getId() );
		assertEquals( description, holder.getDescription() );
		s.delete( holder );
		t.commit();
		s.close();
	}

	@Test
	public void testImageProperty() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		byte[] photo = buildLongByteArray( 15000, true );
		ImageHolder holder = new ImageHolder( photo );
		s.save( holder );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		holder = ( ImageHolder ) s.get(  ImageHolder.class, holder.getId() );
		assertTrue( ArrayHelper.isEquals( photo, holder.getPhoto() ) );
		photo = buildLongByteArray( 15000, false );
		holder.setPhoto( photo );
		s.save( holder );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		holder = ( ImageHolder ) s.get(  ImageHolder.class, holder.getId() );
		assertTrue( ArrayHelper.isEquals( photo, holder.getPhoto() ) );
		s.delete( holder );
		t.commit();
		s.close();
	}

	private String buildLongString(int size, char baseChar) {
		StringBuilder buff = new StringBuilder();
		for( int i = 0; i < size; i++ ) {
			buff.append( baseChar );
		}
		return buff.toString();
	}

	private byte[] buildLongByteArray(int size, boolean on) {
		byte[] data = new byte[size];
		data[0] = mask( on );
		for ( int i = 0; i < size; i++ ) {
			data[i] = mask( on );
			on = !on;
		}
		return data;
	}

	private byte mask(boolean on) {
		return on ? ( byte ) 1 : ( byte ) 0;
	}
}
