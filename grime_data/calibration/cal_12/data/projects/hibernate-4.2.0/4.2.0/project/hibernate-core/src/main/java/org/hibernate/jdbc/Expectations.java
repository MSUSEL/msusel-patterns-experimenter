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
package org.hibernate.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.StaleStateException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.spi.ExecuteUpdateResultCheckStyle;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.internal.CoreMessageLogger;

/**
 * Holds various often used {@link Expectation} definitions.
 *
 * @author Steve Ebersole
 */
public class Expectations {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, Expectations.class.getName());
	private static SqlExceptionHelper sqlExceptionHelper = new SqlExceptionHelper();

	public static final int USUAL_EXPECTED_COUNT = 1;
	public static final int USUAL_PARAM_POSITION = 1;


	// Base Expectation impls ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static class BasicExpectation implements Expectation {
		private final int expectedRowCount;

		protected BasicExpectation(int expectedRowCount) {
			this.expectedRowCount = expectedRowCount;
			if ( expectedRowCount < 0 ) {
				throw new IllegalArgumentException( "Expected row count must be greater than zero" );
			}
		}

		public final void verifyOutcome(int rowCount, PreparedStatement statement, int batchPosition) {
			rowCount = determineRowCount( rowCount, statement );
			if ( batchPosition < 0 ) {
				checkNonBatched( rowCount );
			}
			else {
				checkBatched( rowCount, batchPosition );
			}
		}

		private void checkBatched(int rowCount, int batchPosition) {
            if (rowCount == -2) LOG.debugf("Success of batch update unknown: %s", batchPosition);
            else if (rowCount == -3) throw new BatchFailedException("Batch update failed: " + batchPosition);
			else {
                if (expectedRowCount > rowCount) throw new StaleStateException(
                                                                               "Batch update returned unexpected row count from update ["
                                                                               + batchPosition + "]; actual row count: " + rowCount
                                                                               + "; expected: " + expectedRowCount);
				if ( expectedRowCount < rowCount ) {
					String msg = "Batch update returned unexpected row count from update [" +
					             batchPosition + "]; actual row count: " + rowCount +
					             "; expected: " + expectedRowCount;
					throw new BatchedTooManyRowsAffectedException( msg, expectedRowCount, rowCount, batchPosition );
				}
			}
		}

		private void checkNonBatched(int rowCount) {
			if ( expectedRowCount > rowCount ) {
				throw new StaleStateException(
						"Unexpected row count: " + rowCount + "; expected: " + expectedRowCount
				);
			}
			if ( expectedRowCount < rowCount ) {
				String msg = "Unexpected row count: " + rowCount + "; expected: " + expectedRowCount;
				throw new TooManyRowsAffectedException( msg, expectedRowCount, rowCount );
			}
		}

		public int prepare(PreparedStatement statement) throws SQLException, HibernateException {
			return 0;
		}

		public boolean canBeBatched() {
			return true;
		}

		protected int determineRowCount(int reportedRowCount, PreparedStatement statement) {
			return reportedRowCount;
		}
	}

	public static class BasicParamExpectation extends BasicExpectation {
		private final int parameterPosition;
		protected BasicParamExpectation(int expectedRowCount, int parameterPosition) {
			super( expectedRowCount );
			this.parameterPosition = parameterPosition;
		}

		@Override
        public int prepare(PreparedStatement statement) throws SQLException, HibernateException {
			toCallableStatement( statement ).registerOutParameter( parameterPosition, Types.NUMERIC );
			return 1;
		}

		@Override
        public boolean canBeBatched() {
			return false;
		}

		@Override
        protected int determineRowCount(int reportedRowCount, PreparedStatement statement) {
			try {
				return toCallableStatement( statement ).getInt( parameterPosition );
			}
			catch( SQLException sqle ) {
				sqlExceptionHelper.logExceptions( sqle, "could not extract row counts from CallableStatement" );
				throw new GenericJDBCException( "could not extract row counts from CallableStatement", sqle );
			}
		}

		private CallableStatement toCallableStatement(PreparedStatement statement) {
			if ( ! CallableStatement.class.isInstance( statement ) ) {
				throw new HibernateException( "BasicParamExpectation operates exclusively on CallableStatements : " + statement.getClass() );
			}
			return ( CallableStatement ) statement;
		}
	}


	// Various Expectation instances ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final Expectation NONE = new Expectation() {
		public void verifyOutcome(int rowCount, PreparedStatement statement, int batchPosition) {
			// explicitly doAfterTransactionCompletion no checking...
		}

		public int prepare(PreparedStatement statement) {
			return 0;
		}

		public boolean canBeBatched() {
			return true;
		}
	};

	public static final Expectation BASIC = new BasicExpectation( USUAL_EXPECTED_COUNT );

	public static final Expectation PARAM = new BasicParamExpectation( USUAL_EXPECTED_COUNT, USUAL_PARAM_POSITION );


	public static Expectation appropriateExpectation(ExecuteUpdateResultCheckStyle style) {
		if ( style == ExecuteUpdateResultCheckStyle.NONE ) {
			return NONE;
		}
		else if ( style == ExecuteUpdateResultCheckStyle.COUNT ) {
			return BASIC;
		}
		else if ( style == ExecuteUpdateResultCheckStyle.PARAM ) {
			return PARAM;
		}
		else {
			throw new HibernateException( "unknown check style : " + style );
		}
	}

	private Expectations() {
	}
}
