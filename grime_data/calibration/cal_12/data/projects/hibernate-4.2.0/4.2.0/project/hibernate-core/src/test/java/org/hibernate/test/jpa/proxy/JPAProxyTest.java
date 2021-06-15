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
package org.hibernate.test.jpa.proxy;

import javax.persistence.EntityNotFoundException;

import junit.framework.AssertionFailedError;
import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.jpa.AbstractJPATest;
import org.hibernate.test.jpa.Item;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test relation between proxies and get()/load() processing
 * and make sure the interactions match the ejb3 expectations
 *
 * @author Steve Ebersole
 */
public class JPAProxyTest extends AbstractJPATest {
	@Test
	public void testEjb3ProxyUsage() {
		Session s = openSession();
		Transaction txn = s.beginTransaction();

		Item item = ( Item ) s.load( Item.class, new Long(-1) );
		assertFalse( Hibernate.isInitialized( item ) );
		try {
			Hibernate.initialize( item );
			fail( "proxy access did not fail on non-existent proxy" );
		}
		catch ( EntityNotFoundException e ) {
			// expected behavior
		}
		catch ( Throwable t ) {
			fail( "unexpected exception type on non-existent proxy access : " + t );
		}

		s.clear();

		Item item2 = ( Item ) s.load( Item.class, new Long(-1) );
		assertFalse( Hibernate.isInitialized( item2 ) );
		assertFalse( item == item2 );
		try {
			item2.getName();
			fail( "proxy access did not fail on non-existent proxy" );
		}
		catch ( EntityNotFoundException e ) {
			// expected behavior
		}
		catch ( Throwable t ) {
			fail( "unexpected exception type on non-existent proxy access : " + t );
		}

		txn.commit();
		s.close();
	}

	/**
	 * The ejb3 find() method maps to the Hibernate get() method
	 */
	@Test
	public void testGetSemantics() {
		Long nonExistentId = new Long( -1 );
		Session s = openSession();
		Transaction txn = s.beginTransaction();
		Item item = ( Item ) s.get( Item.class, nonExistentId );
		assertNull( "get() of non-existent entity did not return null", item );
		txn.commit();
		s.close();

		s = openSession();
		txn = s.beginTransaction();
		// first load() it to generate a proxy...
		item = ( Item ) s.load( Item.class, nonExistentId );
		assertFalse( Hibernate.isInitialized( item ) );
		// then try to get() it to make sure we get an exception
		try {
			s.get( Item.class, nonExistentId );
			fail( "force load did not fail on non-existent entity" );
		}
		catch ( EntityNotFoundException e ) {
			// expected behavior
		}
		catch( AssertionFailedError e ) {
			throw e;
		}
		catch ( Throwable t ) {
			fail( "unexpected exception type on non-existent entity force load : " + t );
		}
		txn.commit();
		s.close();
	}
}
