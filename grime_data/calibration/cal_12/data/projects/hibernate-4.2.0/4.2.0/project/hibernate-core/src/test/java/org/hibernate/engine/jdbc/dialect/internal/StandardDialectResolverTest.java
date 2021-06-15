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
package org.hibernate.engine.jdbc.dialect.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.dialect.SQLServer2005Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.service.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.junit.Test;

/**
 * Unit test of the {@link StandardDialectResolver} class.
 *
 * @author Bryan Turner
 */
public class StandardDialectResolverTest extends BaseUnitTestCase {

	@Test
	public void testResolveDialectInternalForSQLServer2000()
			throws SQLException {
		runSQLServerDialectTest( 8, SQLServerDialect.class );
	}

	@Test
	public void testResolveDialectInternalForSQLServer2005()
			throws SQLException {
		runSQLServerDialectTest( 9, SQLServer2005Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForSQLServer2008()
			throws SQLException {
		runSQLServerDialectTest( 10, SQLServer2008Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForSQLServer2012()
			throws SQLException {
		runSQLServerDialectTest( 11, SQLServer2008Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForUnknownSQLServerVersion()
			throws SQLException {
		runSQLServerDialectTest( 7, SQLServerDialect.class );
	}

	@Test
	public void testResolveDialectInternalForPostgres81()
			throws SQLException {
		runPostgresDialectTest( 8, 1, PostgreSQL81Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForPostgres82()
			throws SQLException {
		runPostgresDialectTest( 8, 2, PostgreSQL82Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForPostgres83() throws SQLException {
		runPostgresDialectTest( 8, 3, PostgreSQL82Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForPostgres84() throws SQLException {
		runPostgresDialectTest( 8, 4, PostgreSQL82Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForPostgres9() throws SQLException {
		runPostgresDialectTest( 9, 0, PostgreSQL82Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForPostgres91() throws SQLException {
		runPostgresDialectTest( 9, 1, PostgreSQL82Dialect.class );
	}

	@Test
	public void testResolveDialectInternalForPostgres92() throws SQLException {
		runPostgresDialectTest( 9, 2, PostgreSQL82Dialect.class );
	}

	private static void runSQLServerDialectTest(
			int version, Class<? extends SQLServerDialect> expectedDialect)
					throws SQLException {
		runDialectTest( "Microsoft SQL Server", version, 0,
				expectedDialect );
	}

	private static void runPostgresDialectTest(
			int majorVersion, int minorVersion,
			Class<? extends Dialect> expectedDialect) throws SQLException {
		runDialectTest( "PostgreSQL", majorVersion, minorVersion,
				expectedDialect );
	}

	private static void runDialectTest(
			String productName, int majorVersion, int minorVersion,
			Class<? extends Dialect> expectedDialect) throws SQLException {
		DatabaseMetaData metaData = mock( DatabaseMetaData.class );
		when( metaData.getDatabaseProductName() ).thenReturn( productName );
		when( metaData.getDatabaseMajorVersion() ).thenReturn( majorVersion );
		when( metaData.getDatabaseMinorVersion() ).thenReturn( minorVersion );

		Dialect dialect = new StandardDialectResolver().resolveDialect(
				metaData );

		StringBuilder builder = new StringBuilder( productName ).append( " " )
				.append( majorVersion );
		if ( minorVersion > 0 ) {
			builder.append( "." ).append( minorVersion );
		}
		String dbms = builder.toString();

		assertNotNull( "Dialect for " + dbms + " should not be null", dialect );
		assertTrue( "Dialect for " + dbms + " should be "
				+ expectedDialect.getSimpleName(),
				expectedDialect.isInstance( dialect ) );
	}
}