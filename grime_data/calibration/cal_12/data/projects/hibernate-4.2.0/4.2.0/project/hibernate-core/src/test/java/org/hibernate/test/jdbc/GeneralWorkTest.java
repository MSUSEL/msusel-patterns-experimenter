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
package org.hibernate.test.jdbc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * GeneralWorkTest implementation
 *
 * @author Steve Ebersole
 */
public class GeneralWorkTest extends BaseCoreFunctionalTestCase {
	@Override
	public String getBaseForMappings() {
		return "org/hibernate/test/jdbc/";
	}

	@Override
	public String[] getMappings() {
		return new String[] { "Mappings.hbm.xml" };
	}

	@Test
	public void testGeneralUsage() throws Throwable {
		final Session session = openSession();
		session.beginTransaction();
		session.doWork(
				new Work() {
					public void execute(Connection connection) throws SQLException {
						// in this current form, users must handle try/catches themselves for proper resource release
						Statement statement = null;
						try {
							statement = ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().createStatement();
							ResultSet resultSet = null;
							try {
								
								resultSet = ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( statement, "select * from T_JDBC_PERSON" );
							}
							finally {
								releaseQuietly( ((SessionImplementor)session), resultSet );
							}
							try {
								((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( statement, "select * from T_JDBC_BOAT" );
							}
							finally {
								releaseQuietly( ((SessionImplementor)session), resultSet );
							}
						}
						finally {
							releaseQuietly( ((SessionImplementor)session), statement );
						}
					}
				}
		);
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testSQLExceptionThrowing() {
		final Session session = openSession();
		session.beginTransaction();
		try {
			session.doWork(
					new Work() {
						public void execute(Connection connection) throws SQLException {
							Statement statement = null;
							try {
								statement = ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().createStatement();
								((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( statement, "select * from non_existent" );
							}
							finally {
								releaseQuietly( ((SessionImplementor)session), statement );
							}
						}
					}
			);
			fail( "expecting exception" );
		}
		catch ( JDBCException expected ) {
			// expected outcome
		}
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testGeneralReturningUsage() throws Throwable {
		Session session = openSession();
		session.beginTransaction();
		Person p = new Person( "Abe", "Lincoln" );
		session.save( p );
		session.getTransaction().commit();

		final Session session2 = openSession();
		session2.beginTransaction();
		long count = session2.doReturningWork(
				new ReturningWork<Long>() {
					public Long execute(Connection connection) throws SQLException {
						// in this current form, users must handle try/catches themselves for proper resource release
						Statement statement = null;
						long personCount = 0;
						try {
							statement = ((SessionImplementor)session2).getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().createStatement();
							ResultSet resultSet = null;
							try {
								resultSet = ((SessionImplementor)session2).getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( statement, "select count(*) from T_JDBC_PERSON" );
								resultSet.next();
								personCount = resultSet.getLong( 1 );
								assertEquals( 1L, personCount );
							}
							finally {
								releaseQuietly( ((SessionImplementor)session2), resultSet );
							}
						}
						finally {
							releaseQuietly( ((SessionImplementor)session2), statement );
						}
						return personCount;
					}
				}
		);
		session2.getTransaction().commit();
		session2.close();
		assertEquals( 1L, count );

		session = openSession();
		session.beginTransaction();
		session.delete( p );
		session.getTransaction().commit();
		session.close();
	}

	private void releaseQuietly(SessionImplementor s, Statement statement) {
		if ( statement == null ) {
			return;
		}
		try {
			s.getTransactionCoordinator().getJdbcCoordinator().release( statement );
		}
		catch (Exception e) {
			// ignore
		}
	}

	private void releaseQuietly(SessionImplementor s, ResultSet resultSet) {
		if ( resultSet == null ) {
			return;
		}
		try {
			s.getTransactionCoordinator().getJdbcCoordinator().release( resultSet );
		}
		catch (Exception e) {
			// ignore
		}
	}
}
