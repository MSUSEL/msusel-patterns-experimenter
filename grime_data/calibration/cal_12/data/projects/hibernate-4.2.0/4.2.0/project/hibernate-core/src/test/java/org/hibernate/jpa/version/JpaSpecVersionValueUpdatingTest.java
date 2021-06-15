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
package org.hibernate.jpa.version;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-7138" )
public class JpaSpecVersionValueUpdatingTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Customer.class, Order.class };
	}

	@Test
	public void testVersionNotIncrementedOnModificationOfNonOwningCollectionNonCascaded() {
		Session session = openSession();
		session.beginTransaction();
		Customer customer = new Customer();
		customer.id = 1L;
		session.save( customer );
		session.getTransaction().commit();
		session.close();

		long initial = customer.version;

		session = openSession();
		session.beginTransaction();
		customer = (Customer) session.get( Customer.class, 1L );
		assertEquals( initial, customer.version );
		Order order = new Order();
		order.id = 1L;
		order.customer = customer;
		customer.orders.add( order );
		session.save( order );
		session.getTransaction().commit();
		session.close();

		assertEquals( initial, customer.version );

		session = openSession();
		session.beginTransaction();
		customer = (Customer) session.get( Customer.class, 1L );
		assertEquals( initial, customer.version );
		Order order2 = new Order();
		order2.id = 2L;
		order2.customer = customer;
		customer.orders.add( order2 );
		session.save( order2 );
		session.getTransaction().commit();
		session.close();

		assertEquals( initial, customer.version );

		session = openSession();
		session.beginTransaction();
		customer = (Customer) session.load( Customer.class, 1L );
		assertEquals( initial, customer.version );
		session.delete( customer );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testVersionNotIncrementedOnModificationOfNonOwningCollectionCascaded() {
		Customer customer = new Customer();
		customer.id = 1L;

		Session session = openSession();
		session.beginTransaction();
		session.save( customer );
		session.getTransaction().commit();
		session.close();

		long initial = customer.version;

		session = openSession();
		session.beginTransaction();
		customer = (Customer) session.get( Customer.class, 1L );
		assertEquals( initial, customer.version );
		Order order = new Order();
		order.id = 1L;
		order.customer = customer;
		customer.orders.add( order );
		session.getTransaction().commit();
		session.close();

		assertEquals( initial, customer.version );

		session = openSession();
		session.beginTransaction();
		customer = (Customer) session.get( Customer.class, 1L );
		Order order2 = new Order();
		order2.id = 2L;
		order2.customer = customer;
		customer.orders.add( order2 );
		session.getTransaction().commit();
		session.close();

		assertEquals( initial, customer.version );

		session = openSession();
		session.beginTransaction();
		customer = (Customer) session.load( Customer.class, 1L );
		assertEquals( initial, customer.version );
		session.delete( customer );
		session.getTransaction().commit();
		session.close();
	}
}
