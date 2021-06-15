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
package org.hibernate.test.annotations.fetchprofile;

import java.util.Date;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class MoreFetchProfileTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testFetchWithTwoOverrides() throws Exception {
		Session s = openSession();
		s.enableFetchProfile( "customer-with-orders-and-country" );
		final Transaction transaction = s.beginTransaction();
		Country ctry = new Country();
		ctry.setName( "France" );
		Order o = new Order();
		o.setCountry( ctry );
		o.setDeliveryDate( new Date() );
		o.setOrderNumber( 1 );
		Order o2 = new Order();
		o2.setCountry( ctry );
		o2.setDeliveryDate( new Date() );
		o2.setOrderNumber( 2 );
		Customer c = new Customer();
		c.setCustomerNumber( 1 );
		c.setName( "Emmanuel" );
		c.getOrders().add( o );
		c.setLastOrder( o2 );

		s.persist( ctry );
		s.persist( o );
		s.persist( o2 );
		s.persist( c );

		s.flush();

		s.clear();

		c = ( Customer ) s.get( Customer.class, c.getId() );
		assertTrue( Hibernate.isInitialized( c.getLastOrder() ) );
		assertTrue( Hibernate.isInitialized( c.getOrders() ) );
		for ( Order so : c.getOrders() ) {
			assertTrue( Hibernate.isInitialized( so.getCountry() ) );
		}
		final Order order = c.getOrders().iterator().next();
		c.getOrders().remove( order );
		s.delete( c );
		final Order lastOrder = c.getLastOrder();
		c.setLastOrder( null );
		s.delete( order.getCountry() );
		s.delete( lastOrder );
		s.delete( order );

		transaction.commit();
		s.close();

	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Order.class,
				Country.class,
				Customer.class,
				SupportTickets.class
		};
	}
}
