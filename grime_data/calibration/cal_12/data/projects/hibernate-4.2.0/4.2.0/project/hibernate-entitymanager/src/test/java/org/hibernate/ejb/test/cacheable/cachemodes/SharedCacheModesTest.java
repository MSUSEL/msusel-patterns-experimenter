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
package org.hibernate.ejb.test.cacheable.cachemodes;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateQuery;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.internal.AbstractQueryImpl;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class SharedCacheModesTest extends BaseEntityManagerFunctionalTestCase {
	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[] { SimpleEntity.class };
	}

	@Test
	public void testEntityManagerCacheModes() {

		EntityManager em;
		Session session;

		em = getOrCreateEntityManager();
		session = ( (HibernateEntityManager) em ).getSession();

		// defaults...
		assertEquals( CacheStoreMode.USE, em.getProperties().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheRetrieveMode.USE, em.getProperties().get( AvailableSettings.SHARED_CACHE_RETRIEVE_MODE ) );
		assertEquals( CacheMode.NORMAL, session.getCacheMode() );

		// overrides...
		em.setProperty( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.REFRESH );
		assertEquals( CacheStoreMode.REFRESH, em.getProperties().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.REFRESH, session.getCacheMode() );

		em.setProperty( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.BYPASS );
		assertEquals( CacheStoreMode.BYPASS, em.getProperties().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.GET, session.getCacheMode() );

		em.setProperty( AvailableSettings.SHARED_CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS );
		assertEquals( CacheRetrieveMode.BYPASS, em.getProperties().get( AvailableSettings.SHARED_CACHE_RETRIEVE_MODE ) );
		assertEquals( CacheMode.IGNORE, session.getCacheMode() );

		em.setProperty( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.USE );
		assertEquals( CacheStoreMode.USE, em.getProperties().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.PUT, session.getCacheMode() );

		em.setProperty( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.REFRESH );
		assertEquals( CacheStoreMode.REFRESH, em.getProperties().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.REFRESH, session.getCacheMode() );
	}

	@Test
	public void testQueryCacheModes() {
		EntityManager em = getOrCreateEntityManager();
		Query jpaQuery = em.createQuery( "from SimpleEntity" );
		AbstractQueryImpl hibQuery = (AbstractQueryImpl) ( (HibernateQuery) jpaQuery ).getHibernateQuery();

		jpaQuery.setHint( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.USE );
		assertEquals( CacheStoreMode.USE, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.NORMAL, hibQuery.getCacheMode() );

		jpaQuery.setHint( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.BYPASS );
		assertEquals( CacheStoreMode.BYPASS, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.GET, hibQuery.getCacheMode() );

		jpaQuery.setHint( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.REFRESH );
		assertEquals( CacheStoreMode.REFRESH, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.REFRESH, hibQuery.getCacheMode() );

		jpaQuery.setHint( AvailableSettings.SHARED_CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS );
		assertEquals( CacheRetrieveMode.BYPASS, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_RETRIEVE_MODE ) );
		assertEquals( CacheStoreMode.REFRESH, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.REFRESH, hibQuery.getCacheMode() );

		jpaQuery.setHint( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.BYPASS );
		assertEquals( CacheRetrieveMode.BYPASS, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_RETRIEVE_MODE ) );
		assertEquals( CacheStoreMode.BYPASS, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.IGNORE, hibQuery.getCacheMode() );

		jpaQuery.setHint( AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.USE );
		assertEquals( CacheRetrieveMode.BYPASS, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_RETRIEVE_MODE ) );
		assertEquals( CacheStoreMode.USE, jpaQuery.getHints().get( AvailableSettings.SHARED_CACHE_STORE_MODE ) );
		assertEquals( CacheMode.PUT, hibQuery.getCacheMode() );
	}

}
