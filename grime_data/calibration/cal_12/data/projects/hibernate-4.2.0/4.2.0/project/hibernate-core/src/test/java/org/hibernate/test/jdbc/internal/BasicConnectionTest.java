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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * @author Steve Ebersole
 * @author Brett Meyer
 */
public class BasicConnectionTest extends BaseCoreFunctionalTestCase {

	@Test
	public void testExceptionHandling() {
		Session session = openSession();
		SessionImplementor sessionImpl = (SessionImplementor) session;
		boolean caught = false;
		try {
			PreparedStatement ps = sessionImpl.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer()
					.prepareStatement( "select count(*) from NON_EXISTENT" );
			sessionImpl.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().execute( ps );
		}
		catch ( JDBCException ok ) {
			caught = true;
		}
		finally {
			session.close();
		}

		assertTrue( "The connection did not throw a JDBCException as expected", caught );
	}

	@Test
	public void testBasicJdbcUsage() throws JDBCException {
		Session session = openSession();
		SessionImplementor sessionImpl = (SessionImplementor) session;
		JdbcCoordinator jdbcCoord = sessionImpl.getTransactionCoordinator().getJdbcCoordinator();

		try {
			Statement statement = jdbcCoord.getStatementPreparer().createStatement();
			String dropSql = getDialect().getDropTableString( "SANDBOX_JDBC_TST" );
			try {
				jdbcCoord.getResultSetReturn().execute( statement, dropSql );
			}
			catch ( Exception e ) {
				// ignore if the DB doesn't support "if exists" and the table doesn't exist
			}
			jdbcCoord.getResultSetReturn().execute( statement,
					"create table SANDBOX_JDBC_TST ( ID integer, NAME varchar(100) )" );
			assertTrue( jdbcCoord.hasRegisteredResources() );
			assertTrue( jdbcCoord.getLogicalConnection().isPhysicallyConnected() );
			jdbcCoord.release( statement );
			assertFalse( jdbcCoord.hasRegisteredResources() );
			assertTrue( jdbcCoord.getLogicalConnection().isPhysicallyConnected() ); // after_transaction specified

			PreparedStatement ps = jdbcCoord.getStatementPreparer().prepareStatement(
					"insert into SANDBOX_JDBC_TST( ID, NAME ) values ( ?, ? )" );
			ps.setLong( 1, 1 );
			ps.setString( 2, "name" );
			jdbcCoord.getResultSetReturn().execute( ps );

			ps = jdbcCoord.getStatementPreparer().prepareStatement( "select * from SANDBOX_JDBC_TST" );
			jdbcCoord.getResultSetReturn().extract( ps );

			assertTrue( jdbcCoord.hasRegisteredResources() );
		}
		catch ( SQLException e ) {
			fail( "incorrect exception type : sqlexception" );
		}
		finally {
			session.close();
		}

		assertFalse( jdbcCoord.hasRegisteredResources() );
	}
}
