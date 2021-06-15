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
package org.hibernate.test.sql.refcursor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.jdbc.spi.ResultSetReturn;
import org.hibernate.engine.jdbc.spi.StatementPreparer;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.Work;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@RequiresDialect( Oracle8iDialect.class )
public class CursorFromCallableTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { NumValue.class };
	}

	@Before
	public void createRefCursorFunction() {
		executeStatement( "CREATE OR REPLACE FUNCTION f_test_return_cursor RETURN SYS_REFCURSOR " +
				"IS " +
				"    l_Cursor SYS_REFCURSOR; " +
				"BEGIN " +
				"    OPEN l_Cursor FOR " +
				"      SELECT 1 AS BOT_NUM " +
				"           , 'Line 1' AS BOT_VALUE " +
				"        FROM DUAL " +
				"      UNION " +
				"      SELECT 2 AS BOT_NUM " +
				"           , 'Line 2' AS BOT_VALUE " +
				"        FROM DUAL; " +
				"    RETURN(l_Cursor); " +
				"END f_test_return_cursor;" );
	}

	@After
	public void dropRefCursorFunction() {
		executeStatement( "DROP FUNCTION f_test_return_cursor" );
	}

	@Test
	@TestForIssue( jiraKey = "HHH-8022" )
	public void testReadResultSetFromRefCursor() {
		Session session = openSession();
		session.getTransaction().begin();

		Assert.assertEquals(
				Arrays.asList( new NumValue( 1, "Line 1" ), new NumValue( 2, "Line 2" ) ),
				session.getNamedQuery( "NumValue.getSomeValues" ).list()
		);

		session.getTransaction().commit();
		session.close();
	}

	private void executeStatement(final String sql) {
		final Session session = openSession();
		session.getTransaction().begin();

		session.doWork( new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				final JdbcCoordinator jdbcCoordinator = ( (SessionImplementor) session ).getTransactionCoordinator().getJdbcCoordinator();
				final StatementPreparer statementPreparer = jdbcCoordinator.getStatementPreparer();
				final ResultSetReturn resultSetReturn = jdbcCoordinator.getResultSetReturn();
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = statementPreparer.prepareStatement( sql );
					resultSetReturn.execute( preparedStatement );
				}
				finally {
					if ( preparedStatement != null ) {
						try {
							jdbcCoordinator.release( preparedStatement );
						}
						catch ( Throwable ignore ) {
							// ignore...
						}
					}
				}
			}
		} );

		session.getTransaction().commit();
		session.close();
	}
}
