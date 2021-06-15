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
package org.hibernate.test.annotations.override;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 */
public class AssociationOverrideTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testOverriding() throws Exception {
		Location paris = new Location();
		paris.setName( "Paris" );
		Location atlanta = new Location();
		atlanta.setName( "Atlanta" );
		Trip trip = new Trip();
		trip.setFrom( paris );
		//trip.setTo( atlanta );
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		s.persist( paris );
		s.persist( atlanta );
		try {
			s.persist( trip );
			s.flush();
			fail( "Should be non nullable" );
		}
		catch (HibernateException e) {
			//success
		}
		finally {
			tx.rollback();
			s.close();
		}
	}

	@Test
	public void testDottedNotation() throws Exception {
		assertTrue( SchemaUtil.isTablePresent( "Employee", configuration() ) );
		assertTrue( "Overridden @JoinColumn fails",
				SchemaUtil.isColumnPresent( "Employee", "fld_address_fk", configuration() ) );

		assertTrue( "Overridden @JoinTable name fails", SchemaUtil.isTablePresent( "tbl_empl_sites", configuration() ) );
		assertTrue( "Overridden @JoinTable with default @JoinColumn fails",
				SchemaUtil.isColumnPresent( "tbl_empl_sites", "employee_id", configuration() ) );
		assertTrue( "Overridden @JoinTable.inverseJoinColumn fails",
				SchemaUtil.isColumnPresent( "tbl_empl_sites", "to_website_fk", configuration() ) );

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		ContactInfo ci = new ContactInfo();
		Addr address = new Addr();
		address.setCity("Boston");
		address.setCountry("USA");
		address.setState("MA");
		address.setStreet("27 School Street");
		address.setZipcode("02108");
		ci.setAddr(address);
		List<PhoneNumber> phoneNumbers = new ArrayList();
		PhoneNumber num = new PhoneNumber();
		num.setNumber(5577188);
		Employee e = new Employee();
		Collection employeeList = new ArrayList();
		employeeList.add(e);
		e.setContactInfo(ci);
		num.setEmployees(employeeList);
		phoneNumbers.add(num);
		ci.setPhoneNumbers(phoneNumbers);
		SocialTouchPoints socialPoints = new SocialTouchPoints();
		List<SocialSite> sites = new ArrayList<SocialSite>();
		SocialSite site = new SocialSite();
		site.setEmployee(employeeList);
		site.setWebsite("www.jboss.org");
		sites.add(site);
		socialPoints.setWebsite(sites);
		ci.setSocial(socialPoints);
		s.persist(e);
		tx.commit();

		tx = s.beginTransaction();
		s.clear();
		e = (Employee) s.get(Employee.class,e.getId());
		tx.commit();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{
				Employee.class,
				Location.class,
				Move.class,
				Trip.class,
				PhoneNumber.class,
				Addr.class,
				SocialSite.class,
				SocialTouchPoints.class
		};
	}
}
