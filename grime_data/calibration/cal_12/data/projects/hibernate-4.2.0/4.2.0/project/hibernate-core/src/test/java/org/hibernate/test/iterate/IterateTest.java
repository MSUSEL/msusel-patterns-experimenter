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
package org.hibernate.test.iterate;
import java.util.Iterator;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class IterateTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "iterate/Item.hbm.xml" };
	}

	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.USE_QUERY_CACHE, "true" );
		cfg.setProperty( Environment.CACHE_REGION_PREFIX, "foo" );
		cfg.setProperty( Environment.USE_SECOND_LEVEL_CACHE, "true" );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}

	@Test
	public void testIterate() throws Exception {
		sessionFactory().getStatistics().clear();
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Item i1 = new Item("foo");
		Item i2 = new Item("bar");
		s.persist("Item", i1);
		s.persist("Item", i2);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Iterator iter = s.getNamedQuery("Item.nameDesc").iterate();
		i1 = (Item) iter.next();
		i2 = (Item) iter.next();
		assertFalse( Hibernate.isInitialized(i1) );
		assertFalse( Hibernate.isInitialized(i2) );
		i1.getName();
		i2.getName();
		assertFalse( Hibernate.isInitialized(i1) );
		assertFalse( Hibernate.isInitialized(i2) );
		assertEquals( i1.getName(), "foo" );
		assertEquals( i2.getName(), "bar" );
		Hibernate.initialize(i1);
		assertFalse( iter.hasNext() );
		s.delete(i1);
		s.delete(i2);
		t.commit();
		s.close();
		assertEquals( sessionFactory().getStatistics().getEntityFetchCount(), 2 );
	}

	@Test
	public void testScroll() throws Exception {
		sessionFactory().getStatistics().clear();
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Item i1 = new Item("foo");
		Item i2 = new Item("bar");
		s.persist("Item", i1);
		s.persist("Item", i2);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		ScrollableResults sr = s.getNamedQuery("Item.nameDesc").scroll();
		assertTrue( sr.next() );
		i1 = (Item) sr.get(0);
		assertTrue( sr.next() );
		i2 = (Item) sr.get(0);
		assertTrue( Hibernate.isInitialized(i1) );
		assertTrue( Hibernate.isInitialized(i2) );
		assertEquals( i1.getName(), "foo" );
		assertEquals( i2.getName(), "bar" );
		assertFalse( sr.next() );
		s.delete(i1);
		s.delete(i2);
		t.commit();
		s.close();
		assertEquals( sessionFactory().getStatistics().getEntityFetchCount(), 0 );
	}
}

