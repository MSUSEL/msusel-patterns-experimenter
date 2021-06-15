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
package org.hibernate.test.naturalid.mutable.cached;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.cache.CachingRegionFactory;
import org.junit.Test;

public class CachedMutableNaturalIdStrictReadWriteTest extends
		CachedMutableNaturalIdTest {

	@Override
	public void configure(Configuration cfg) {
		super.configure(cfg);
		cfg.setProperty( CachingRegionFactory.DEFAULT_ACCESSTYPE, "read-write" );
	}
	
	@Test
	@TestForIssue( jiraKey = "HHH-7278" )
	public void testInsertedNaturalIdCachedAfterTransactionSuccess() {
		
		Session session = openSession();
		session.getSessionFactory().getStatistics().clear();
		session.beginTransaction();
		Another it = new Another( "it");
		session.save( it );
		session.flush();
		session.getTransaction().commit();
		session.close();
		
		session = openSession();
		session.beginTransaction();
		it = (Another) session.bySimpleNaturalId(Another.class).load("it");
		assertNotNull(it);
		session.delete(it);
		session.getTransaction().commit();
		assertEquals(1, session.getSessionFactory().getStatistics().getNaturalIdCacheHitCount());
	}
	
	@Test
	@TestForIssue( jiraKey = "HHH-7278" )
	public void testInsertedNaturalIdNotCachedAfterTransactionFailure() {
		
		Session session = openSession();
		session.getSessionFactory().getStatistics().clear();
		session.beginTransaction();
		Another it = new Another( "it");
		session.save( it );
		session.flush();
		session.getTransaction().rollback();
		session.close();
		
		session = openSession();
		session.beginTransaction();
		it = (Another) session.bySimpleNaturalId(Another.class).load("it");
		assertNull(it);
		assertEquals(0, session.getSessionFactory().getStatistics().getNaturalIdCacheHitCount());
	}
	
	@Test
	@TestForIssue( jiraKey = "HHH-7278" )
	public void testChangedNaturalIdCachedAfterTransactionSuccess() {
		Session session = openSession();
		session.beginTransaction();
		Another it = new Another( "it");
		session.save( it );
		session.getTransaction().commit();
		session.close();
		
		session = openSession();
		session.beginTransaction();
		it = (Another) session.bySimpleNaturalId(Another.class).load("it");
		assertNotNull(it);
		
		it.setName("modified");
		session.flush();
		session.getTransaction().commit(); 
		session.close();
		
		session.getSessionFactory().getStatistics().clear();
		
		session = openSession();
		session.beginTransaction();
		it = (Another) session.bySimpleNaturalId(Another.class).load("modified");
		assertNotNull(it);
		session.delete(it);
		session.getTransaction().commit(); 
		session.close();
		
		assertEquals(1, session.getSessionFactory().getStatistics().getNaturalIdCacheHitCount());
	}
	
	@Test
	@TestForIssue( jiraKey = "HHH-7278" )
	public void testChangedNaturalIdNotCachedAfterTransactionFailure() {
		Session session = openSession();
		session.beginTransaction();
		Another it = new Another( "it");
		session.save( it );
		session.getTransaction().commit();
		session.close();
		
		session = openSession();
		session.beginTransaction();
		it = (Another) session.bySimpleNaturalId(Another.class).load("it");
		assertNotNull(it);
		
		it.setName("modified");
		session.flush();
		session.getTransaction().rollback(); 
		session.close();
		
		session.getSessionFactory().getStatistics().clear();
		
		session = openSession();
		session.beginTransaction();
		it = (Another) session.bySimpleNaturalId(Another.class).load("modified");
		assertNull(it);
		it = (Another) session.bySimpleNaturalId(Another.class).load("it");
		session.delete(it);
		session.getTransaction().commit(); 
		session.close();
		
		assertEquals(0, session.getSessionFactory().getStatistics().getNaturalIdCacheHitCount());
	}
	
	@Test
	@TestForIssue( jiraKey = "HHH-7309" )
	public void testInsertUpdateEntity_NaturalIdCachedAfterTransactionSuccess() {
		
		Session session = openSession();
		session.getSessionFactory().getStatistics().clear();
		session.beginTransaction();
		Another it = new Another( "it");
		session.save( it );    // schedules an InsertAction
		it.setSurname("1234"); // schedules an UpdateAction, without bug-fix
		// this will re-cache natural-id with identical key and at same time invalidate it
		session.flush();
		session.getTransaction().commit();
		session.close();
		
		session = openSession();
		session.beginTransaction();
		it = (Another) session.bySimpleNaturalId(Another.class).load("it");
		assertNotNull(it);
		session.delete(it);
		session.getTransaction().commit();
		assertEquals("In a strict access strategy we would excpect a hit here", 1, session.getSessionFactory().getStatistics().getNaturalIdCacheHitCount());
	}
}
