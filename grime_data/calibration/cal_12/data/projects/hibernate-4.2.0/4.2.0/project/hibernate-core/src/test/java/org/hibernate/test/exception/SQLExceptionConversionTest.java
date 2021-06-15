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
package org.hibernate.test.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.dialect.MySQLMyISAMDialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.jdbc.Work;
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.fail;

/**
 * Implementation of SQLExceptionConversionTest.
 *
 * @author Steve Ebersole
 */
public class SQLExceptionConversionTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] {"exception/User.hbm.xml", "exception/Group.hbm.xml"};
	}

	@Test
	@SkipForDialect(
			value = MySQLMyISAMDialect.class,
			comment = "MySQL (MyISAM) does not support FK violation checking"
	)
	public void testIntegrityViolation() throws Exception {
		final Session session = openSession();
		session.beginTransaction();

		session.doWork(
				new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						// Attempt to insert some bad values into the T_MEMBERSHIP table that should
						// result in a constraint violation
						PreparedStatement ps = null;
						try {
							ps = ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( "INSERT INTO T_MEMBERSHIP (user_id, group_id) VALUES (?, ?)" );
							ps.setLong(1, 52134241);    // Non-existent user_id
							ps.setLong(2, 5342);        // Non-existent group_id
							((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( ps );

							fail("INSERT should have failed");
						}
						catch (ConstraintViolationException ignore) {
							// expected outcome
						}
						finally {
							if ( ps != null ) {
								try {
									((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().release( ps );
								}
								catch( Throwable ignore ) {
									// ignore...
								}
							}
						}
					}
				}
		);

		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void testBadGrammar() throws Exception {
		final Session session = openSession();
		session.beginTransaction();

		session.doWork(
				new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						// prepare/execute a query against a non-existent table
						PreparedStatement ps = null;
						try {
							ps = ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( "SELECT user_id, user_name FROM tbl_no_there" );
							((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( ps );

							fail("SQL compilation should have failed");
						}
						catch (SQLGrammarException ignored) {
							// expected outcome
						}
						finally {
							if ( ps != null ) {
								try {
									((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().release( ps );
								}
								catch( Throwable ignore ) {
									// ignore...
								}
							}
						}
					}
				}
		);

		session.getTransaction().rollback();
		session.close();
	}
}
