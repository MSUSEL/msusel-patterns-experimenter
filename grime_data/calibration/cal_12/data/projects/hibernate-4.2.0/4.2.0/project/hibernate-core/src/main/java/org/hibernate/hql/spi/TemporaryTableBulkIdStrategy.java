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
package org.hibernate.hql.spi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Map;

import org.hibernate.cfg.Mappings;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.HqlSqlWalker;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.jdbc.AbstractWork;
import org.hibernate.persister.entity.Queryable;
import org.jboss.logging.Logger;

/**
 * @author Steve Ebersole
 */
public class TemporaryTableBulkIdStrategy implements MultiTableBulkIdStrategy {
	public static final TemporaryTableBulkIdStrategy INSTANCE = new TemporaryTableBulkIdStrategy();

	public static final String SHORT_NAME = "temporary";

	private static final CoreMessageLogger log = Logger.getMessageLogger(
			CoreMessageLogger.class,
			TemporaryTableBulkIdStrategy.class.getName()
	);

	@Override
	public void prepare(JdbcServices jdbcServices, JdbcConnectionAccess connectionAccess, Mappings mappings, Mapping mapping, Map settings) {
		// nothing to do
	}

	@Override
	public void release(JdbcServices jdbcServices, JdbcConnectionAccess connectionAccess) {
		// nothing to do
	}

	@Override
	public UpdateHandler buildUpdateHandler(SessionFactoryImplementor factory, HqlSqlWalker walker) {
		return new TableBasedUpdateHandlerImpl( factory, walker ) {
			@Override
			protected void prepareForUse(Queryable persister, SessionImplementor session) {
				createTempTable( persister, session );
			}

			@Override
			protected void releaseFromUse(Queryable persister, SessionImplementor session) {
				releaseTempTable( persister, session );
			}
		};
	}

	@Override
	public DeleteHandler buildDeleteHandler(SessionFactoryImplementor factory, HqlSqlWalker walker) {
		return new TableBasedDeleteHandlerImpl( factory, walker ) {
			@Override
			protected void prepareForUse(Queryable persister, SessionImplementor session) {
				createTempTable( persister, session );
			}

			@Override
			protected void releaseFromUse(Queryable persister, SessionImplementor session) {
				releaseTempTable( persister, session );
			}
		};
	}


	protected void createTempTable(Queryable persister, SessionImplementor session) {
		// Don't really know all the codes required to adequately decipher returned jdbc exceptions here.
		// simply allow the failure to be eaten and the subsequent insert-selects/deletes should fail
		TemporaryTableCreationWork work = new TemporaryTableCreationWork( persister );
		if ( shouldIsolateTemporaryTableDDL( session ) ) {
			session.getTransactionCoordinator()
					.getTransaction()
					.createIsolationDelegate()
					.delegateWork( work, shouldTransactIsolatedTemporaryTableDDL( session ) );
		}
		else {
			final Connection connection = session.getTransactionCoordinator()
					.getJdbcCoordinator()
					.getLogicalConnection()
					.getConnection();
			work.execute( connection );
			session.getTransactionCoordinator()
					.getJdbcCoordinator()
					.afterStatementExecution();
		}
	}

	protected void releaseTempTable(Queryable persister, SessionImplementor session) {
		if ( session.getFactory().getDialect().dropTemporaryTableAfterUse() ) {
			TemporaryTableDropWork work = new TemporaryTableDropWork( persister, session );
			if ( shouldIsolateTemporaryTableDDL( session ) ) {
				session.getTransactionCoordinator()
						.getTransaction()
						.createIsolationDelegate()
						.delegateWork( work, shouldTransactIsolatedTemporaryTableDDL( session ) );
			}
			else {
				final Connection connection = session.getTransactionCoordinator()
						.getJdbcCoordinator()
						.getLogicalConnection()
						.getConnection();
				work.execute( connection );
				session.getTransactionCoordinator()
						.getJdbcCoordinator()
						.afterStatementExecution();
			}
		}
		else {
			// at the very least cleanup the data :)
			PreparedStatement ps = null;
			try {
				final String sql = "delete from " + persister.getTemporaryIdTableName();
				ps = session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( sql, false );
				session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( ps );
			}
			catch( Throwable t ) {
				log.unableToCleanupTemporaryIdTable(t);
			}
			finally {
				if ( ps != null ) {
					try {
						session.getTransactionCoordinator().getJdbcCoordinator().release( ps );
					}
					catch( Throwable ignore ) {
						// ignore
					}
				}
			}
		}
	}

	@SuppressWarnings({ "UnnecessaryUnboxing" })
	protected boolean shouldIsolateTemporaryTableDDL(SessionImplementor session) {
		Boolean dialectVote = session.getFactory().getDialect().performTemporaryTableDDLInIsolation();
		if ( dialectVote != null ) {
			return dialectVote.booleanValue();
		}
		return session.getFactory().getSettings().isDataDefinitionImplicitCommit();
	}

	@SuppressWarnings({ "UnnecessaryUnboxing" })
	protected boolean shouldTransactIsolatedTemporaryTableDDL(SessionImplementor session) {
		// is there ever a time when it makes sense to do this?
//		return session.getFactory().getSettings().isDataDefinitionInTransactionSupported();
		return false;
	}

	private static class TemporaryTableCreationWork extends AbstractWork {
		private final Queryable persister;

		private TemporaryTableCreationWork(Queryable persister) {
			this.persister = persister;
		}

		@Override
		public void execute(Connection connection) {
			try {
				Statement statement = connection.createStatement();
				try {
					statement.executeUpdate( persister.getTemporaryIdTableDDL() );
					persister.getFactory()
							.getServiceRegistry()
							.getService( JdbcServices.class )
							.getSqlExceptionHelper()
							.handleAndClearWarnings( statement, CREATION_WARNING_HANDLER );
				}
				finally {
					try {
						statement.close();
					}
					catch( Throwable ignore ) {
						// ignore
					}
				}
			}
			catch( Exception e ) {
				log.debug( "unable to create temporary id table [" + e.getMessage() + "]" );
			}
		}
	}

	private static SqlExceptionHelper.WarningHandler CREATION_WARNING_HANDLER = new SqlExceptionHelper.WarningHandlerLoggingSupport() {
		public boolean doProcess() {
			return log.isDebugEnabled();
		}

		public void prepare(SQLWarning warning) {
			log.warningsCreatingTempTable( warning );
		}

		@Override
		protected void logWarning(String description, String message) {
			log.debug( description );
			log.debug( message );
		}
	};

	private static class TemporaryTableDropWork extends AbstractWork {
		private final Queryable persister;
		private final SessionImplementor session;

		private TemporaryTableDropWork(Queryable persister, SessionImplementor session) {
			this.persister = persister;
			this.session = session;
		}

		@Override
		public void execute(Connection connection) {
			final String command = session.getFactory().getDialect().getDropTemporaryTableString()
					+ ' ' + persister.getTemporaryIdTableName();
			try {
				Statement statement = connection.createStatement();
				try {
					statement.executeUpdate( command );
				}
				finally {
					try {
						statement.close();
					}
					catch( Throwable ignore ) {
						// ignore
					}
				}
			}
			catch( Exception e ) {
				log.warn( "unable to drop temporary id table after use [" + e.getMessage() + "]" );
			}
		}
	}

}
