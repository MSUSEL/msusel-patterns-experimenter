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
package org.hibernate.test.jdbc.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.batch.internal.BasicBatchKey;
import org.hibernate.engine.jdbc.batch.internal.BatchBuilderImpl;
import org.hibernate.engine.jdbc.batch.internal.BatchingBatch;
import org.hibernate.engine.jdbc.batch.internal.NonBatchingBatch;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.hibernate.engine.jdbc.batch.spi.BatchBuilder;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.jdbc.spi.LogicalConnectionImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.transaction.spi.TransactionCoordinator;
import org.hibernate.engine.transaction.spi.TransactionImplementor;
import org.hibernate.jdbc.Expectation;
import org.hibernate.jdbc.Expectations;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.test.common.JournalingBatchObserver;
import org.hibernate.test.common.JournalingTransactionObserver;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * @author Steve Ebersole
 * @author Brett Meyer
 */
public class BatchingTest extends BaseCoreFunctionalTestCase implements BatchKey {
	private StandardServiceRegistryImpl serviceRegistry;

	@Override
	public int getBatchedStatementCount() {
		return 1;
	}

	@Override
	public Expectation getExpectation() {
		return Expectations.BASIC;
	}

	@Test
	public void testNonBatchingUsage() throws Exception {
		Session session = openSession();
		SessionImplementor sessionImpl = (SessionImplementor) session;
		
		TransactionCoordinator transactionCoordinator = sessionImpl.getTransactionCoordinator();
		JournalingTransactionObserver observer = new JournalingTransactionObserver();
		transactionCoordinator.addObserver( observer );

		final JdbcCoordinator jdbcCoordinator = transactionCoordinator.getJdbcCoordinator();
		LogicalConnectionImplementor logicalConnection = jdbcCoordinator.getLogicalConnection();

		// set up some tables to use
		Statement statement = jdbcCoordinator.getStatementPreparer().createStatement();
		String dropSql = getDialect().getDropTableString( "SANDBOX_JDBC_TST" );
		try {
			jdbcCoordinator.getResultSetReturn().execute( statement, dropSql );
		}
		catch ( Exception e ) {
			// ignore if the DB doesn't support "if exists" and the table doesn't exist
		}
		jdbcCoordinator.getResultSetReturn().execute( statement, "create table SANDBOX_JDBC_TST ( ID integer, NAME varchar(100) )" );
		assertTrue( jdbcCoordinator.hasRegisteredResources() );
		assertTrue( logicalConnection.isPhysicallyConnected() );
		jdbcCoordinator.release( statement );
		assertFalse( jdbcCoordinator.hasRegisteredResources() );
		assertTrue( logicalConnection.isPhysicallyConnected() ); // after_transaction specified

		// ok, now we can get down to it...
		TransactionImplementor txn = transactionCoordinator.getTransaction();  // same as Session#getTransaction
		txn.begin();
		assertEquals( 1, observer.getBegins() );

		final String insertSql = "insert into SANDBOX_JDBC_TST( ID, NAME ) values ( ?, ? )";

		final BatchBuilder batchBuilder = new BatchBuilderImpl( -1 );
		final BatchKey batchKey = new BasicBatchKey( "this", Expectations.BASIC );
		final Batch insertBatch = batchBuilder.buildBatch( batchKey, jdbcCoordinator );

		final JournalingBatchObserver batchObserver = new JournalingBatchObserver();
		insertBatch.addObserver( batchObserver );

		assertTrue( "unexpected Batch impl", NonBatchingBatch.class.isInstance( insertBatch ) );
		PreparedStatement insert = insertBatch.getBatchStatement( insertSql, false );
		insert.setLong( 1, 1 );
		insert.setString( 2, "name" );
		assertEquals( 0, batchObserver.getExplicitExecutionCount() );
		assertEquals( 0, batchObserver.getImplicitExecutionCount() );
		insertBatch.addToBatch();
		assertEquals( 0, batchObserver.getExplicitExecutionCount() );
		assertEquals( 1, batchObserver.getImplicitExecutionCount() );
		assertFalse( jdbcCoordinator.hasRegisteredResources() );

		insertBatch.execute();
		assertEquals( 1, batchObserver.getExplicitExecutionCount() );
		assertEquals( 1, batchObserver.getImplicitExecutionCount() );
		assertFalse( jdbcCoordinator.hasRegisteredResources() );

		insertBatch.release();

		txn.commit();
		session.close();
	}

	@Test
	public void testBatchingUsage() throws Exception {
		Session session = openSession();
		SessionImplementor sessionImpl = (SessionImplementor) session;
		
		TransactionCoordinator transactionCoordinator = sessionImpl.getTransactionCoordinator();
		JournalingTransactionObserver observer = new JournalingTransactionObserver();
		transactionCoordinator.addObserver( observer );

		final JdbcCoordinator jdbcCoordinator = transactionCoordinator.getJdbcCoordinator();
		LogicalConnectionImplementor logicalConnection = jdbcCoordinator.getLogicalConnection();

		// set up some tables to use
		Statement statement = jdbcCoordinator.getStatementPreparer().createStatement();
		String dropSql = getDialect().getDropTableString( "SANDBOX_JDBC_TST" );
		try {
			jdbcCoordinator.getResultSetReturn().execute( statement, dropSql );
		}
		catch ( Exception e ) {
			// ignore if the DB doesn't support "if exists" and the table doesn't exist
		}		jdbcCoordinator.getResultSetReturn().execute( statement, "create table SANDBOX_JDBC_TST ( ID integer, NAME varchar(100) )" );
		assertTrue( jdbcCoordinator.hasRegisteredResources() );
		assertTrue( logicalConnection.isPhysicallyConnected() );
		jdbcCoordinator.release( statement );
		assertFalse( jdbcCoordinator.hasRegisteredResources() );
		assertTrue( logicalConnection.isPhysicallyConnected() ); // after_transaction specified

		// ok, now we can get down to it...
		TransactionImplementor txn = transactionCoordinator.getTransaction();  // same as Session#getTransaction
		txn.begin();
		assertEquals( 1, observer.getBegins() );

		final BatchBuilder batchBuilder = new BatchBuilderImpl( 2 );
		final BatchKey batchKey = new BasicBatchKey( "this", Expectations.BASIC );
		final Batch insertBatch = batchBuilder.buildBatch( batchKey, jdbcCoordinator );
		assertTrue( "unexpected Batch impl", BatchingBatch.class.isInstance( insertBatch ) );

		final JournalingBatchObserver batchObserver = new JournalingBatchObserver();
		insertBatch.addObserver( batchObserver );

		final String insertSql = "insert into SANDBOX_JDBC_TST( ID, NAME ) values ( ?, ? )";

		PreparedStatement insert = insertBatch.getBatchStatement( insertSql, false );
		insert.setLong( 1, 1 );
		insert.setString( 2, "name" );
		assertEquals( 0, batchObserver.getExplicitExecutionCount() );
		assertEquals( 0, batchObserver.getImplicitExecutionCount() );
		insertBatch.addToBatch();
		assertEquals( 0, batchObserver.getExplicitExecutionCount() );
		assertEquals( 0, batchObserver.getImplicitExecutionCount() );
		assertTrue( jdbcCoordinator.hasRegisteredResources() );

		PreparedStatement insert2 = insertBatch.getBatchStatement( insertSql, false );
		assertSame( insert, insert2 );
		insert = insert2;
		insert.setLong( 1, 2 );
		insert.setString( 2, "another name" );
		assertEquals( 0, batchObserver.getExplicitExecutionCount() );
		assertEquals( 0, batchObserver.getImplicitExecutionCount() );
		insertBatch.addToBatch();
		assertEquals( 0, batchObserver.getExplicitExecutionCount() );
		assertEquals( 1, batchObserver.getImplicitExecutionCount() );
		assertTrue( jdbcCoordinator.hasRegisteredResources() );

		insertBatch.execute();
		assertEquals( 1, batchObserver.getExplicitExecutionCount() );
		assertEquals( 1, batchObserver.getImplicitExecutionCount() );
		assertFalse( jdbcCoordinator.hasRegisteredResources() );

		insertBatch.release();

		txn.commit();
		session.close();
	}

}
