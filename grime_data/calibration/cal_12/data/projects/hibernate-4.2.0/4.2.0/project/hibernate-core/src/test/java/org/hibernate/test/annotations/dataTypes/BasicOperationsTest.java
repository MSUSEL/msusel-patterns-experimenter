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
package org.hibernate.test.annotations.dataTypes;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.Work;
import org.hibernate.testing.DialectCheck;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.type.descriptor.JdbcTypeNameMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
@RequiresDialectFeature(value = {DialectChecks.SupportsExpectedLobUsagePattern.class, BasicOperationsTest.OracleDialectChecker.class}, jiraKey = "HHH-6834")
public class BasicOperationsTest extends BaseCoreFunctionalTestCase {

	private static final String SOME_ENTITY_TABLE_NAME = "SOMEENTITY";
	private static final String SOME_OTHER_ENTITY_TABLE_NAME = "SOMEOTHERENTITY";

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { SomeEntity.class, SomeOtherEntity.class };
	}
	public static class OracleDialectChecker implements DialectCheck{
		@Override
		public boolean isMatch(Dialect dialect) {
			return ! (dialect instanceof Oracle8iDialect);
		}
	}

	@Test
	public void testCreateAndDelete() {
		Date now = new Date();

		Session s = openSession();

		s.doWork( new ValidateSomeEntityColumns( (SessionImplementor) s ) );
		s.doWork( new ValidateRowCount( (SessionImplementor) s, SOME_ENTITY_TABLE_NAME, 0 ) );
		s.doWork( new ValidateRowCount( (SessionImplementor) s, SOME_OTHER_ENTITY_TABLE_NAME, 0 ) );

		s.beginTransaction();
		SomeEntity someEntity = new SomeEntity( now );
		SomeOtherEntity someOtherEntity = new SomeOtherEntity( 1 );
		s.save( someEntity );
		s.save( someOtherEntity );
		s.getTransaction().commit();
		s.close();

		s = openSession();

		s.doWork( new ValidateRowCount( (SessionImplementor) s, SOME_ENTITY_TABLE_NAME, 1 ) );
		s.doWork( new ValidateRowCount( (SessionImplementor) s, SOME_OTHER_ENTITY_TABLE_NAME, 1 ) );

		s.beginTransaction();
		s.delete( someEntity );
		s.delete( someOtherEntity );
		s.getTransaction().commit();

		s.doWork( new ValidateRowCount( (SessionImplementor) s, SOME_ENTITY_TABLE_NAME, 0 ) );
		s.doWork( new ValidateRowCount( (SessionImplementor) s, SOME_OTHER_ENTITY_TABLE_NAME, 0 ) );

		s.close();
	}

	// verify all the expected columns are created
	class ValidateSomeEntityColumns implements Work {
		private SessionImplementor s;
		
		public ValidateSomeEntityColumns( SessionImplementor s ) {
			this.s = s;
		}
		
		public void execute(Connection connection) throws SQLException {
			// id -> java.util.Date (DATE - becase of explicit TemporalType)
			validateColumn( connection, "ID", java.sql.Types.DATE );

			// timeData -> java.sql.Time (TIME)
			validateColumn( connection, "TIMEDATA", java.sql.Types.TIME );

			// tsData -> java.sql.Timestamp (TIMESTAMP)
			validateColumn( connection, "TSDATA", java.sql.Types.TIMESTAMP );
		}

		private void validateColumn(Connection connection, String columnName, int expectedJdbcTypeCode)
				throws SQLException {
			DatabaseMetaData meta = connection.getMetaData();

			// DBs treat the meta information differently, in particular case sensitivity.
			// We need to use the meta information to find out how to treat names
			String tableNamePattern = generateFinalNamePattern( meta, SOME_ENTITY_TABLE_NAME );
			String columnNamePattern = generateFinalNamePattern( meta, columnName );

			ResultSet columnInfo = meta.getColumns( null, null, tableNamePattern, columnNamePattern );
			s.getTransactionCoordinator().getJdbcCoordinator().register(columnInfo);
			assertTrue( columnInfo.next() );
			int dataType = columnInfo.getInt( "DATA_TYPE" );
			s.getTransactionCoordinator().getJdbcCoordinator().release( columnInfo );
			assertEquals(
					columnName,
					JdbcTypeNameMapper.getTypeName( expectedJdbcTypeCode ),
					JdbcTypeNameMapper.getTypeName( dataType )
			);
		}

		private String generateFinalNamePattern(DatabaseMetaData meta, String name) throws SQLException {
			if ( meta.storesLowerCaseIdentifiers() ) {
				return name.toLowerCase();
			}
			else {
				return name;
			}
		}
	}

	// verify we have the right amount of columns
	class ValidateRowCount implements Work {
		private final int expectedRowCount;
		private final String table;

		private SessionImplementor s;
		
		public ValidateRowCount(SessionImplementor s, String table, int count) {
			this.s = s;
			this.expectedRowCount = count;
			this.table = table;
		}

		public void execute(Connection connection) throws SQLException {
			Statement st = s.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().createStatement();
			s.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( st, "SELECT COUNT(*) FROM " + table );
			ResultSet result = s.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( st, "SELECT COUNT(*) FROM " + table );
			result.next();
			int rowCount = result.getInt( 1 );
			assertEquals( "Unexpected row count", expectedRowCount, rowCount );
		}
	}
}

