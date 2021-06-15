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
package org.hibernate.test.cascade;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.Work;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * Implementation of RefreshTest.
 *
 * @author Steve Ebersole
 */
public class RefreshTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "cascade/Job.hbm.xml", "cascade/JobBatch.hbm.xml" };
	}

	@Test
	public void testRefreshCascade() throws Throwable {
		Session session = openSession();
		Transaction txn = session.beginTransaction();

		JobBatch batch = new JobBatch( new Date() );
		batch.createJob().setProcessingInstructions( "Just do it!" );
		batch.createJob().setProcessingInstructions( "I know you can do it!" );

		// write the stuff to the database; at this stage all job.status values are zero
		session.persist( batch );
		session.flush();

		// behind the session's back, let's modify the statuses
		updateStatuses( (SessionImplementor)session );

		// Now lets refresh the persistent batch, and see if the refresh cascaded to the jobs collection elements
		session.refresh( batch );

		Iterator itr = batch.getJobs().iterator();
		while( itr.hasNext() ) {
			Job job = ( Job ) itr.next();
			assertEquals( "Jobs not refreshed!", 1, job.getStatus() );
		}

		txn.rollback();
		session.close();
	}

	private void updateStatuses(final SessionImplementor session) throws Throwable {
		((Session)session).doWork(
				new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						PreparedStatement stmnt = null;
						try {
							stmnt = session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( "UPDATE T_JOB SET JOB_STATUS = 1" );
							session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( stmnt );
						}
						finally {
							if ( stmnt != null ) {
								try {
									session.getTransactionCoordinator().getJdbcCoordinator().release( stmnt );
								}
								catch( Throwable ignore ) {
								}
							}
						}
					}
				}
		);
	}
}
