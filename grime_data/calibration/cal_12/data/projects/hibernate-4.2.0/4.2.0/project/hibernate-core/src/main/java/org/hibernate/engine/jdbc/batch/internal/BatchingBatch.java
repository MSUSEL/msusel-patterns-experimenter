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
package org.hibernate.engine.jdbc.batch.internal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.internal.CoreMessageLogger;

/**
 * A {@link org.hibernate.engine.jdbc.batch.spi.Batch} implementation which does bathing based on a given size.  Once
 * the batch size is reached for a statement in the batch, the entire batch is implicitly executed.
 *
 * @author Steve Ebersole
 */
public class BatchingBatch extends AbstractBatchImpl {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, BatchingBatch.class.getName() );

	// IMPL NOTE : Until HHH-5797 is fixed, there will only be 1 statement in a batch

	private final int batchSize;
	private int batchPosition;
	private int statementPosition;

	public BatchingBatch(
			BatchKey key,
			JdbcCoordinator jdbcCoordinator,
			int batchSize) {
		super( key, jdbcCoordinator );
		if ( ! key.getExpectation().canBeBatched() ) {
			throw new HibernateException( "attempting to batch an operation which cannot be batched" );
		}
		this.batchSize = batchSize;
	}

	private String currentStatementSql;
	private PreparedStatement currentStatement;

	@Override
	public PreparedStatement getBatchStatement(String sql, boolean callable) {
		currentStatementSql = sql;
		currentStatement = super.getBatchStatement( sql, callable );
		return currentStatement;
	}

	@Override
	public void addToBatch() {
		try {
			currentStatement.addBatch();
		}
		catch ( SQLException e ) {
			LOG.debugf( "SQLException escaped proxy", e );
			throw sqlExceptionHelper().convert( e, "could not perform addBatch", currentStatementSql );
		}
		statementPosition++;
		if ( statementPosition >= getKey().getBatchedStatementCount() ) {
			batchPosition++;
			if ( batchPosition == batchSize ) {
				notifyObserversImplicitExecution();
				performExecution();
				batchPosition = 0;
			}
			statementPosition = 0;
		}
	}

	@Override
	protected void doExecuteBatch() {
		if ( batchPosition == 0 ) {
			LOG.debug( "No batched statements to execute" );
		}
		else {
			LOG.debugf( "Executing batch size: %s", batchPosition );
			performExecution();
		}
	}

	private void performExecution() {
		try {
			for ( Map.Entry<String,PreparedStatement> entry : getStatements().entrySet() ) {
				try {
					final PreparedStatement statement = entry.getValue();
					checkRowCounts( statement.executeBatch(), statement );
				}
				catch ( SQLException e ) {
					LOG.debug( "SQLException escaped proxy", e );
					throw sqlExceptionHelper().convert( e, "could not perform addBatch", entry.getKey() );
				}
			}
		}
		catch ( RuntimeException re ) {
			LOG.unableToExecuteBatch( re.getMessage() );
			throw re;
		}
		finally {
			batchPosition = 0;
		}
	}

	private void checkRowCounts(int[] rowCounts, PreparedStatement ps) throws SQLException, HibernateException {
		int numberOfRowCounts = rowCounts.length;
		if ( numberOfRowCounts != batchPosition ) {
			LOG.unexpectedRowCounts();
		}
		for ( int i = 0; i < numberOfRowCounts; i++ ) {
			getKey().getExpectation().verifyOutcome( rowCounts[i], ps, i );
		}
	}
}