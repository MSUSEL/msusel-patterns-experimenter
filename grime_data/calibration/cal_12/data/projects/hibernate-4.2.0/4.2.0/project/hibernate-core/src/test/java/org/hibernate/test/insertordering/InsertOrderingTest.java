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
package org.hibernate.test.insertordering;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.batch.internal.BatchBuilderImpl;
import org.hibernate.engine.jdbc.batch.internal.BatchBuilderInitiator;
import org.hibernate.engine.jdbc.batch.internal.BatchingBatch;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class InsertOrderingTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "insertordering/Mapping.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.ORDER_INSERTS, "true" );
		cfg.setProperty( Environment.STATEMENT_BATCH_SIZE, "10" );
		cfg.setProperty( BatchBuilderInitiator.BUILDER, StatsBatchBuilder.class.getName() );
	}

	@Test
	public void testBatchOrdering() {
		Session s = openSession();
		s.beginTransaction();
		int iterations = 12;
		for ( int i = 0; i < iterations; i++ ) {
			User user = new User( "user-" + i );
			Group group = new Group( "group-" + i );
			s.save( user );
			s.save( group );
			user.addMembership( group );
		}
		StatsBatch.reset();
		s.getTransaction().commit();
		s.close();

		assertEquals( 3, StatsBatch.batchSizes.size() );

		s = openSession();
		s.beginTransaction();
		Iterator users = s.createQuery( "from User u left join fetch u.memberships m left join fetch m.group" ).list().iterator();
		while ( users.hasNext() ) {
			s.delete( users.next() );
		}
		s.getTransaction().commit();
		s.close();
	}

	public static class Counter {
		public int count = 0;
	}

	public static class StatsBatch extends BatchingBatch {
		private static String batchSQL;
		private static List batchSizes = new ArrayList();
		private static int currentBatch = -1;

		public StatsBatch(BatchKey key, JdbcCoordinator jdbcCoordinator, int jdbcBatchSize) {
			super( key, jdbcCoordinator, jdbcBatchSize );
		}

		static void reset() {
			batchSizes = new ArrayList();
			currentBatch = -1;
			batchSQL = null;
		}

		@Override
		public PreparedStatement getBatchStatement(String sql, boolean callable) {
			if ( batchSQL == null || ! batchSQL.equals( sql ) ) {
				currentBatch++;
				batchSQL = sql;
				batchSizes.add( currentBatch, new Counter() );
			}
			return super.getBatchStatement( sql, callable );
		}

		@Override
		public void addToBatch() {
			Counter counter = ( Counter ) batchSizes.get( currentBatch );
			counter.count++;
			super.addToBatch();
		}
	}

	public static class StatsBatchBuilder extends BatchBuilderImpl {
		private int jdbcBatchSize;

		@Override
        public void setJdbcBatchSize(int jdbcBatchSize) {
			this.jdbcBatchSize = jdbcBatchSize;
		}

		@Override
		public Batch buildBatch(BatchKey key, JdbcCoordinator jdbcCoordinator) {
			return new StatsBatch( key, jdbcCoordinator, jdbcBatchSize );
		}
	}
}
