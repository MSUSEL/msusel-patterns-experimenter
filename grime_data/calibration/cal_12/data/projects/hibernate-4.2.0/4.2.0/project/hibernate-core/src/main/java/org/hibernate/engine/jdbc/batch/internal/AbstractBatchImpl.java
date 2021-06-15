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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.jboss.logging.Logger;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.engine.jdbc.batch.spi.BatchObserver;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.internal.CoreMessageLogger;

/**
 * Convenience base class for implementers of the Batch interface.
 *
 * @author Steve Ebersole
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public abstract class AbstractBatchImpl implements Batch {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, AbstractBatchImpl.class.getName());

	private final BatchKey key;
	private final JdbcCoordinator jdbcCoordinator;
	private LinkedHashMap<String,PreparedStatement> statements = new LinkedHashMap<String,PreparedStatement>();
	private LinkedHashSet<BatchObserver> observers = new LinkedHashSet<BatchObserver>();

	protected AbstractBatchImpl(BatchKey key, JdbcCoordinator jdbcCoordinator) {
		if ( key == null ) {
			throw new IllegalArgumentException( "batch key cannot be null" );
		}
		if ( jdbcCoordinator == null ) {
			throw new IllegalArgumentException( "JDBC coordinator cannot be null" );
		}
		this.key = key;
		this.jdbcCoordinator = jdbcCoordinator;
	}

	/**
	 * Perform batch execution.
	 * <p/>
	 * This is called from the explicit {@link #execute() execution}, but may also be called from elsewhere
	 * depending on the exact implementation.
	 */
	protected abstract void doExecuteBatch();

	/**
	 * Convenience access to the SQLException helper.
	 *
	 * @return The underlying SQLException helper.
	 */
	protected SqlExceptionHelper sqlExceptionHelper() {
		return jdbcCoordinator.getTransactionCoordinator()
				.getTransactionContext()
				.getTransactionEnvironment()
				.getJdbcServices()
				.getSqlExceptionHelper();
	}

	/**
	 * Convenience access to the SQL statement logger.
	 *
	 * @return The underlying JDBC services.
	 */
	protected SqlStatementLogger sqlStatementLogger() {
		return jdbcCoordinator.getTransactionCoordinator()
				.getTransactionContext()
				.getTransactionEnvironment()
				.getJdbcServices()
				.getSqlStatementLogger();
	}

	/**
	 * Access to the batch's map of statements (keyed by SQL statement string).
	 *
	 * @return This batch's statements.
	 */
	protected LinkedHashMap<String,PreparedStatement> getStatements() {
		return statements;
	}

	@Override
	public final BatchKey getKey() {
		return key;
	}

	@Override
	public void addObserver(BatchObserver observer) {
		observers.add( observer );
	}

	@Override
	public PreparedStatement getBatchStatement(String sql, boolean callable) {
		if ( sql == null ) {
			throw new IllegalArgumentException( "sql must be non-null." );
		}
		PreparedStatement statement = statements.get( sql );
		if ( statement == null ) {
			statement = buildBatchStatement( sql, callable );
			statements.put( sql, statement );
		}
		else {
			LOG.debug( "Reusing batch statement" );
			sqlStatementLogger().logStatement( sql );
		}
		return statement;
	}

	private PreparedStatement buildBatchStatement(String sql, boolean callable) {
		return jdbcCoordinator.getStatementPreparer().prepareStatement( sql, callable );
	}

	@Override
	public final void execute() {
		notifyObserversExplicitExecution();
		if ( statements.isEmpty() ) {
			return;
		}
		try {
			try {
				doExecuteBatch();
			}
			finally {
				releaseStatements();
			}
		}
		finally {
			statements.clear();
		}
	}

	private void releaseStatements() {
		for ( PreparedStatement statement : getStatements().values() ) {
			try {
				statement.clearBatch();
				jdbcCoordinator.release( statement );
			}
			catch ( SQLException e ) {
				LOG.unableToReleaseBatchStatement();
				LOG.sqlExceptionEscapedProxy( e );
			}
		}
		getStatements().clear();
	}

	/**
	 * Convenience method to notify registered observers of an explicit execution of this batch.
	 */
	protected final void notifyObserversExplicitExecution() {
		for ( BatchObserver observer : observers ) {
			observer.batchExplicitlyExecuted();
		}
	}

	/**
	 * Convenience method to notify registered observers of an implicit execution of this batch.
	 */
	protected final void notifyObserversImplicitExecution() {
		for ( BatchObserver observer : observers ) {
			observer.batchImplicitlyExecuted();
		}
	}

	@Override
	public void release() {
        if ( getStatements() != null && !getStatements().isEmpty() ) {
			LOG.batchContainedStatementsOnRelease();
		}
		releaseStatements();
		observers.clear();
	}
}
