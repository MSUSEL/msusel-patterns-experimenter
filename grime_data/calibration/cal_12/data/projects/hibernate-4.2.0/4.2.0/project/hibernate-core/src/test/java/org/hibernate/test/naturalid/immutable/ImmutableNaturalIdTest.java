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
package org.hibernate.test.naturalid.immutable;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Restrictions;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
public class ImmutableNaturalIdTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "naturalid/immutable/User.hbm.xml" };
	}

	public void configure(Configuration cfg) {
		cfg.setProperty( Environment.USE_SECOND_LEVEL_CACHE, "true" );
		cfg.setProperty( Environment.USE_QUERY_CACHE, "true" );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}

	@Test
	public void testUpdate() {
		// prepare some test data...
		Session session = openSession();
    	session.beginTransaction();
	  	User user = new User();
    	user.setUserName( "steve" );
    	user.setEmail( "steve@hibernate.org" );
    	user.setPassword( "brewhaha" );
		session.save( user );
    	session.getTransaction().commit();
    	session.close();

		// 'user' is now a detached entity, so lets change a property and reattch...
		user.setPassword( "homebrew" );
		session = openSession();
		session.beginTransaction();
		session.update( user );
		session.getTransaction().commit();
		session.close();

		// clean up
		session = openSession();
		session.beginTransaction();
		session.delete( user );
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testNaturalIdCheck() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		User u = new User( "steve", "superSecret" );
		s.persist( u );
		u.setUserName( "Steve" );
		try {
			s.flush();
			fail();
		}
		catch ( HibernateException he ) {
		}
		u.setUserName( "steve" );
		s.delete( u );
		t.commit();
		s.close();
	}

	@Test
	public void testNaturalIdCache() {
		Session s = openSession();
		s.beginTransaction();
		User u = new User( "steve", "superSecret" );
		s.persist( u );
		s.getTransaction().commit();
		s.close();

		sessionFactory().getStatistics().clear();

		s = openSession();
		s.beginTransaction();
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNotNull( u );
		s.getTransaction().commit();
		s.close();

		assertEquals( 1, sessionFactory().getStatistics().getNaturalIdQueryExecutionCount() );
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCacheHitCount() ); //0: no stats since hbm.xml can't enable NaturalId caching
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCachePutCount() ); //1: no stats since hbm.xml can't enable NaturalId caching

		s = openSession();
		s.beginTransaction();
		User v = new User( "gavin", "supsup" );
		s.persist( v );
		s.getTransaction().commit();
		s.close();

		sessionFactory().getStatistics().clear();

		s = openSession();
		s.beginTransaction();
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNotNull( u );
		assertEquals( 1, sessionFactory().getStatistics().getNaturalIdQueryExecutionCount() ); //0: incorrect stats since hbm.xml can't enable NaturalId caching
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCacheHitCount() ); //1: no stats since hbm.xml can't enable NaturalId caching
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNotNull( u );
		assertEquals( 1, sessionFactory().getStatistics().getNaturalIdQueryExecutionCount() ); //0: incorrect stats since hbm.xml can't enable NaturalId caching
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCacheHitCount() ); //2: no stats since hbm.xml can't enable NaturalId caching
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete User" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testNaturalIdDeleteUsingCache() {
		Session s = openSession();
		s.beginTransaction();
		User u = new User( "steve", "superSecret" );
		s.persist( u );
		s.getTransaction().commit();
		s.close();

		sessionFactory().getStatistics().clear();

		s = openSession();
		s.beginTransaction();
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNotNull( u );
		s.getTransaction().commit();
		s.close();

		assertEquals( 1, sessionFactory().getStatistics().getNaturalIdQueryExecutionCount() );
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCacheHitCount() );
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCachePutCount() ); //1: no stats since hbm.xml can't enable NaturalId caching

		sessionFactory().getStatistics().clear();

		s = openSession();
		s.beginTransaction();
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNotNull( u );
		assertEquals( 1, sessionFactory().getStatistics().getNaturalIdQueryExecutionCount() ); //0: incorrect stats since hbm.xml can't enable NaturalId caching
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCacheHitCount() ); //1: no stats since hbm.xml can't enable NaturalId caching

		s.delete( u );

		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNull( u );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testNaturalIdRecreateUsingCache() {
		testNaturalIdDeleteUsingCache();

		Session s = openSession();
		s.beginTransaction();
		User u = new User( "steve", "superSecret" );
		s.persist( u );
		s.getTransaction().commit();
		s.close();

		sessionFactory().getStatistics().clear();

		s = openSession();
		s.beginTransaction();
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNotNull( u );

		assertEquals( 1, sessionFactory().getStatistics().getNaturalIdQueryExecutionCount() );
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCacheHitCount() );
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCachePutCount() ); //1: no stats since hbm.xml can't enable NaturalId caching

		sessionFactory().getStatistics().clear();
		s.getTransaction().commit();
		s = openSession();
		s.beginTransaction();
		u = ( User ) s.createCriteria( User.class )
				.add( Restrictions.naturalId().set( "userName", "steve" ) )
				.setCacheable( true )
				.uniqueResult();
		assertNotNull( u );
		assertEquals( 1, sessionFactory().getStatistics().getNaturalIdQueryExecutionCount() ); //0: incorrect stats since hbm.xml can't enable NaturalId caching
		assertEquals( 0, sessionFactory().getStatistics().getNaturalIdCacheHitCount() ); //1: no stats since hbm.xml can't enable NaturalId caching

		s.delete( u );

		s.getTransaction().commit();
		s.close();
	}

}
