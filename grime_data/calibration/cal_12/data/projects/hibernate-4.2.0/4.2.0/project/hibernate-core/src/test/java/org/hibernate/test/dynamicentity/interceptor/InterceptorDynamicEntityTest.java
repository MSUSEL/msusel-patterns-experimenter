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
package org.hibernate.test.dynamicentity.interceptor;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.test.dynamicentity.Company;
import org.hibernate.test.dynamicentity.Customer;
import org.hibernate.test.dynamicentity.ProxyHelper;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Demonstrates custom interpretation of entity-name through
 * an Interceptor.
 * <p/>
 * Here, we are generating dynamic
 * {@link java.lang.reflect.Proxy proxies} on the fly to represent
 * our entities.  Because of this, Hibernate would not be able to
 * determine the appropriate entity mapping to use given one of
 * these proxies (they are named like $Proxy1, or such).  Thus, we
 * plug a custom Interceptor into the session to perform this
 * entity-name interpretation.
 *
 * @see ProxyInterceptor
 *
 * @author Steve Ebersole
 */
public class InterceptorDynamicEntityTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "dynamicentity/interceptor/Customer.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		cfg.setInterceptor( new ProxyInterceptor() );
	}

	@Test
	public void testIt() {
		// Test saving these dyna-proxies
		Session session = openSession();
		session.beginTransaction();
		Company company = ProxyHelper.newCompanyProxy();
		company.setName( "acme" );
		session.save( company );
		Customer customer = ProxyHelper.newCustomerProxy();
		customer.setName( "Steve" );
		customer.setCompany( company );
		session.save( customer );
		session.getTransaction().commit();
		session.close();

		assertNotNull( "company id not assigned", company.getId() );
		assertNotNull( "customer id not assigned", customer.getId() );

		// Test loading these dyna-proxies, along with flush processing
		session = openSession();
		session.beginTransaction();
		customer = ( Customer ) session.load( Customer.class, customer.getId() );
		assertFalse( "should-be-proxy was initialized", Hibernate.isInitialized( customer ) );

		customer.setName( "other" );
		session.flush();
		assertFalse( "should-be-proxy was initialized", Hibernate.isInitialized( customer.getCompany() ) );

		session.refresh( customer );
		assertEquals( "name not updated", "other", customer.getName() );
		assertEquals( "company association not correct", "acme", customer.getCompany().getName() );

		session.getTransaction().commit();
		session.close();

		// Test detached entity re-attachment with these dyna-proxies
		customer.setName( "Steve" );
		session = openSession();
		session.beginTransaction();
		session.update( customer );
		session.flush();
		session.refresh( customer );
		assertEquals( "name not updated", "Steve", customer.getName() );
		session.getTransaction().commit();
		session.close();

		// Test querying
		session = openSession();
		session.beginTransaction();
		int count = session.createQuery( "from Customer" ).list().size();
		assertEquals( "querying dynamic entity", 1, count );
		session.clear();
		count = session.createQuery( "from Person" ).list().size();
		assertEquals( "querying dynamic entity", 1, count );
		session.getTransaction().commit();
		session.close();

		// test deleteing
		session = openSession();
		session.beginTransaction();
		session.delete( company );
		session.delete( customer );
		session.getTransaction().commit();
		session.close();
	}

}
