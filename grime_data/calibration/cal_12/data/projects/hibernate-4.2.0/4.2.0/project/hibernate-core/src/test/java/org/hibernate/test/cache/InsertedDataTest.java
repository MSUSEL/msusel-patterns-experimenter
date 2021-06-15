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
package org.hibernate.test.cache;

import java.util.Map;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for handling of data just inserted during a transaction being read from the database
 * and placed into cache.  Initially these cases went through putFromRead which causes problems because it
 * loses the context of that data having just been read.
 *
 * @author Steve Ebersole
 */
public class InsertedDataTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { CacheableItem.class };
	}

	@Override
	protected void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.CACHE_REGION_PREFIX, "" );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}

	@Test
	public void testInsert() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.getTransaction().commit();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 1, cacheMap.size() );

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete CacheableItem" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testInsertWithRollback() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.flush();
		s.getTransaction().rollback();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 0, cacheMap.size() );
	}

	@Test
	public void testInsertThenUpdate() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.flush();
		item.setName( "new data" );
		s.getTransaction().commit();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 1, cacheMap.size() );

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete CacheableItem" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testInsertThenUpdateThenRollback() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.flush();
		item.setName( "new data" );
		s.getTransaction().rollback();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 0, cacheMap.size() );

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete CacheableItem" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testInsertWithRefresh() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.flush();
		s.refresh( item );
		s.getTransaction().commit();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 1, cacheMap.size() );

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete CacheableItem" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testInsertWithRefreshThenRollback() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.flush();
		s.refresh( item );
		s.getTransaction().rollback();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 0, cacheMap.size() );

		s = openSession();
		s.beginTransaction();
		item = (CacheableItem) s.get( CacheableItem.class, item.getId() );
		s.getTransaction().commit();
		s.close();

		assertNull( "it should be null", item );
	}

	@Test
	public void testInsertWithClear() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.flush();
		s.clear();
		s.getTransaction().commit();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 1, cacheMap.size() );

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete CacheableItem" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testInsertWithClearThenRollback() {
		sessionFactory().getCache().evictEntityRegions();
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		CacheableItem item = new CacheableItem( "data" );
		s.save( item );
		s.flush();
		s.clear();
		item = (CacheableItem) s.get( CacheableItem.class, item.getId() );
		s.getTransaction().rollback();
		s.close();

		Map cacheMap = sessionFactory().getStatistics().getSecondLevelCacheStatistics( "item" ).getEntries();
		assertEquals( 0, cacheMap.size() );

		s = openSession();
		s.beginTransaction();
		item = (CacheableItem) s.get( CacheableItem.class, item.getId() );
		s.getTransaction().commit();
		s.close();

		assertNull( "it should be null", item );
	}
}
