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
package org.hibernate.test.transaction.jta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.jdbc.spi.LogicalConnectionImplementor;
import org.hibernate.engine.transaction.internal.TransactionCoordinatorImpl;
import org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory;
import org.hibernate.engine.transaction.spi.TransactionContext;
import org.hibernate.engine.transaction.spi.TransactionImplementor;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.service.jta.platform.spi.JtaPlatform;
import org.hibernate.test.common.JournalingTransactionObserver;
import org.hibernate.test.common.TransactionContextImpl;
import org.hibernate.test.common.TransactionEnvironmentImpl;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.jta.TestingJtaBootstrap;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing transaction facade handling when the transaction is being driven by something other than the facade.
 *
 * @author Steve Ebersole
 */
@RequiresDialect(H2Dialect.class)
public class ManagedDrivingTest extends BaseUnitTestCase {
	private StandardServiceRegistryImpl serviceRegistry;

	@Before
	@SuppressWarnings( {"unchecked"})
	public void setUp() throws Exception {
		Map configValues = new HashMap();
		TestingJtaBootstrap.prepare( configValues );
		configValues.put( Environment.TRANSACTION_STRATEGY, CMTTransactionFactory.class.getName() );

		serviceRegistry = (StandardServiceRegistryImpl) new ServiceRegistryBuilder()
				.applySettings( configValues )
				.buildServiceRegistry();
	}

	@After
	public void tearDown() throws Exception {
		serviceRegistry.destroy();
	}

	@Test
	public void testBasicUsage() throws Throwable {
		final TransactionContext transactionContext = new TransactionContextImpl( new TransactionEnvironmentImpl( serviceRegistry ) ) {
			@Override
			public ConnectionReleaseMode getConnectionReleaseMode() {
				return ConnectionReleaseMode.AFTER_STATEMENT;
			}
		};

		final TransactionCoordinatorImpl transactionCoordinator = new TransactionCoordinatorImpl( null, transactionContext );
		final JournalingTransactionObserver transactionObserver = new JournalingTransactionObserver();
		transactionCoordinator.addObserver( transactionObserver );

		JdbcCoordinator jdbcCoordinator = transactionCoordinator.getJdbcCoordinator();
		LogicalConnectionImplementor logicalConnection = jdbcCoordinator.getLogicalConnection();

		// set up some tables to use
		Statement statement = jdbcCoordinator.getStatementPreparer().createStatement();
		jdbcCoordinator.getResultSetReturn().execute( statement, "drop table SANDBOX_JDBC_TST if exists" );
		jdbcCoordinator.getResultSetReturn().execute( statement, "create table SANDBOX_JDBC_TST ( ID integer, NAME varchar(100) )" );
		assertTrue( jdbcCoordinator.hasRegisteredResources() );
		assertTrue( logicalConnection.isPhysicallyConnected() );
		jdbcCoordinator.release( statement );
		assertFalse( jdbcCoordinator.hasRegisteredResources() );
		assertFalse( logicalConnection.isPhysicallyConnected() ); // after_statement specified

		JtaPlatform instance = serviceRegistry.getService( JtaPlatform.class );
		TransactionManager transactionManager = instance.retrieveTransactionManager();

		// start the cmt
		transactionManager.begin();

		// ok, now we can get down to it...
		TransactionImplementor txn = transactionCoordinator.getTransaction();  // same as Session#getTransaction
		txn.begin();
		assertEquals( 1, transactionObserver.getBegins() );
		assertFalse( txn.isInitiator() );
		try {
			PreparedStatement ps = jdbcCoordinator.getStatementPreparer().prepareStatement( "insert into SANDBOX_JDBC_TST( ID, NAME ) values ( ?, ? )" );
			ps.setLong( 1, 1 );
			ps.setString( 2, "name" );
			jdbcCoordinator.getResultSetReturn().execute( ps );
			assertTrue( jdbcCoordinator.hasRegisteredResources() );
			jdbcCoordinator.release( ps );
			assertFalse( jdbcCoordinator.hasRegisteredResources() );

			ps = jdbcCoordinator.getStatementPreparer().prepareStatement( "select * from SANDBOX_JDBC_TST" );
			jdbcCoordinator.getResultSetReturn().extract( ps );
			ps = jdbcCoordinator.getStatementPreparer().prepareStatement( "delete from SANDBOX_JDBC_TST" );
			jdbcCoordinator.getResultSetReturn().execute( ps );
			// lets forget to close these...
			assertTrue( jdbcCoordinator.hasRegisteredResources() );
			assertTrue( logicalConnection.isPhysicallyConnected() );

			// and commit the transaction...
			txn.commit();

			// since txn is not a driver, nothing should have changed...
			assertTrue( jdbcCoordinator.hasRegisteredResources() );
			assertTrue( logicalConnection.isPhysicallyConnected() );
			assertEquals( 0, transactionObserver.getBeforeCompletions() );
			assertEquals( 0, transactionObserver.getAfterCompletions() );

			transactionManager.commit();
			assertFalse( jdbcCoordinator.hasRegisteredResources() );
			assertFalse( logicalConnection.isPhysicallyConnected() );
			assertEquals( 1, transactionObserver.getBeforeCompletions() );
			assertEquals( 1, transactionObserver.getAfterCompletions() );
		}
		catch ( SQLException sqle ) {
			try {
				transactionManager.rollback();
			}
			catch (Exception ignore) {
			}
			fail( "incorrect exception type : SQLException" );
		}
		catch (Throwable reThrowable) {
			try {
				transactionManager.rollback();
			}
			catch (Exception ignore) {
			}
			throw reThrowable;
		}
		finally {
			logicalConnection.close();
		}
	}
}
