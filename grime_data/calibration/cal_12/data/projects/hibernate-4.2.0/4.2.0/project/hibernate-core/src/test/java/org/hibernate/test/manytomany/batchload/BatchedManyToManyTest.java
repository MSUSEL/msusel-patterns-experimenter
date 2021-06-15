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
package org.hibernate.test.manytomany.batchload;

import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Hibernate;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.batch.internal.BatchBuilderImpl;
import org.hibernate.engine.jdbc.batch.internal.NonBatchingBatch;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.stat.CollectionStatistics;

import org.junit.Test;
import junit.framework.Assert;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests loading of many-to-many collection which should trigger
 * a batch load.
 *
 * @author Steve Ebersole
 */
public class BatchedManyToManyTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "manytomany/batchload/UserGroupBatchLoad.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		cfg.setProperty( Environment.USE_SECOND_LEVEL_CACHE, "false" );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
		cfg.setProperty( Environment.BATCH_STRATEGY, TestingBatchBuilder.class.getName() );
	}

	public static class TestingBatchBuilder extends BatchBuilderImpl {
		@Override
		public Batch buildBatch(BatchKey key, JdbcCoordinator jdbcCoordinator) {
			return new TestingBatch( key, jdbcCoordinator );
		}
	}

	public static class TestingBatch extends NonBatchingBatch {
		public TestingBatch(BatchKey key, JdbcCoordinator jdbcCoordinator) {
			super( key, jdbcCoordinator );
		}
	}

	@Test
	public void testLoadingNonInverseSide() {
		prepareTestData();

		sessionFactory().getStatistics().clear();
		CollectionStatistics userGroupStats = sessionFactory().getStatistics()
				.getCollectionStatistics( User.class.getName() + ".groups" );
		CollectionStatistics groupUserStats = sessionFactory().getStatistics()
				.getCollectionStatistics( Group.class.getName() + ".users" );

		Interceptor testingInterceptor = new EmptyInterceptor() {
			@Override
            public String onPrepareStatement(String sql) {
				// ugh, this is the best way I could come up with to assert this.
				// unfortunately, this is highly dependent on the dialect and its
				// outer join fragment.  But at least this wil fail on the majority
				// of dialects...
				Assert.assertFalse(
						"batch load of many-to-many should use inner join",
						sql.toLowerCase().contains( "left outer join" )
				);
				return super.onPrepareStatement( sql );
			}
		};

		Session s = openSession( testingInterceptor );
		s.beginTransaction();
		List users = s.createQuery( "from User u" ).list();
		User user = ( User ) users.get( 0 );
		assertTrue( Hibernate.isInitialized( user ) );
		assertTrue( Hibernate.isInitialized( user.getGroups() ) );
		user = ( User ) users.get( 1 );
		assertTrue( Hibernate.isInitialized( user ) );
		assertTrue( Hibernate.isInitialized( user.getGroups() ) );
		assertEquals( 1, userGroupStats.getFetchCount() ); // should have been just one fetch (the batch fetch)
		assertEquals( 1, groupUserStats.getFetchCount() ); // should have been just one fetch (the batch fetch)
		s.getTransaction().commit();
		s.close();

	}

	protected void prepareTestData() {
		// set up the test data
		User me = new User( "steve" );
		User you = new User( "not steve" );
		Group developers = new Group( "developers" );
		Group translators = new Group( "translators" );
		Group contributors = new Group( "contributors" );
		me.getGroups().add( developers );
		developers.getUsers().add( me );
		you.getGroups().add( translators );
		translators.getUsers().add( you );
		you.getGroups().add( contributors );
		contributors.getUsers().add( you );
		Session s = openSession();
		s.beginTransaction();
		s.save( me );
		s.save( you );
		s.getTransaction().commit();
		s.close();
	}

	protected void cleanupTestData() {
		// clean up the test data
		Session s = openSession();
		s.beginTransaction();
		// User is the non-inverse side...
		List<User> users = s.createQuery( "from User" ).list();
		for ( User user : users ) {
			s.delete( user );
		}
		s.flush();
		s.createQuery( "delete Group" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected boolean isCleanupTestDataRequired() {
		return true;
	}
}
