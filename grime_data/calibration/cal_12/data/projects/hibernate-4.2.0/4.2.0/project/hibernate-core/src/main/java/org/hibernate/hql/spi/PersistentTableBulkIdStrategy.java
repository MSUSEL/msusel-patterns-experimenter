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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Mappings;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.HqlSqlWalker;
import org.hibernate.internal.AbstractSessionImpl;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.sql.SelectValues;
import org.hibernate.type.UUIDCharType;

/**
 * @author Steve Ebersole
 */
public class PersistentTableBulkIdStrategy implements MultiTableBulkIdStrategy {
	private static final CoreMessageLogger log = Logger.getMessageLogger(
			CoreMessageLogger.class,
			PersistentTableBulkIdStrategy.class.getName()
	);

	public static final String SHORT_NAME = "persistent";

	public static final String CLEAN_UP_ID_TABLES = "hibernate.hql.bulk_id_strategy.persistent.clean_up";
	public static final String SCHEMA = "hibernate.hql.bulk_id_strategy.persistent.schema";
	public static final String CATALOG = "hibernate.hql.bulk_id_strategy.persistent.catalog";

	private String catalog;
	private String schema;
	private boolean cleanUpTables;
	private List<String> tableCleanUpDdl;

	@Override
	public void prepare(
			JdbcServices jdbcServices,
			JdbcConnectionAccess connectionAccess,
			Mappings mappings,
			Mapping mapping,
			Map settings) {
		this.catalog = ConfigurationHelper.getString(
				CATALOG,
				settings,
				ConfigurationHelper.getString( AvailableSettings.DEFAULT_CATALOG, settings )
		);
		this.schema = ConfigurationHelper.getString(
				SCHEMA,
				settings,
				ConfigurationHelper.getString( AvailableSettings.DEFAULT_SCHEMA, settings )
		);
		this.cleanUpTables = ConfigurationHelper.getBoolean( CLEAN_UP_ID_TABLES, settings, false );

		final Iterator<PersistentClass> entityMappings = mappings.iterateClasses();
		final List<Table> idTableDefinitions = new ArrayList<Table>();
		while ( entityMappings.hasNext() ) {
			final PersistentClass entityMapping = entityMappings.next();
			final Table idTableDefinition = generateIdTableDefinition( entityMapping );
			idTableDefinitions.add( idTableDefinition );
		}
		exportTableDefinitions( idTableDefinitions, jdbcServices, connectionAccess, mappings, mapping );
	}

	protected Table generateIdTableDefinition(PersistentClass entityMapping) {
		Table idTable = new Table( entityMapping.getTemporaryIdTableName() );
		if ( catalog != null ) {
			idTable.setCatalog( catalog );
		}
		if ( schema != null ) {
			idTable.setSchema( schema );
		}
		Iterator itr = entityMapping.getTable().getPrimaryKey().getColumnIterator();
		while( itr.hasNext() ) {
			Column column = (Column) itr.next();
			idTable.addColumn( column.clone()  );
		}
		Column sessionIdColumn = new Column( "hib_sess_id" );
		sessionIdColumn.setSqlType( "CHAR(36)" );
		sessionIdColumn.setComment( "Used to hold the Hibernate Session identifier" );
		idTable.addColumn( sessionIdColumn );

		idTable.setComment( "Used to hold id values for the " + entityMapping.getEntityName() + " class" );
		return idTable;
	}

	protected void exportTableDefinitions(
			List<Table> idTableDefinitions,
			JdbcServices jdbcServices,
			JdbcConnectionAccess connectionAccess,
			Mappings mappings,
			Mapping mapping) {
		try {
			Connection connection;
			try {
				connection = connectionAccess.obtainConnection();
			}
			catch (UnsupportedOperationException e) {
				// assume this comes from org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl
				log.debug( "Unable to obtain JDBC connection; assuming ID tables already exist or wont be needed" );
				return;
			}

			try {
				// TODO: session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().createStatement();
				Statement statement = connection.createStatement();
				for ( Table idTableDefinition : idTableDefinitions ) {
					if ( cleanUpTables ) {
						if ( tableCleanUpDdl == null ) {
							tableCleanUpDdl = new ArrayList<String>();
						}
						tableCleanUpDdl.add( idTableDefinition.sqlDropString( jdbcServices.getDialect(), null, null  ) );
					}
					try {
						final String sql = idTableDefinition.sqlCreateString( jdbcServices.getDialect(), mapping, null, null );
						jdbcServices.getSqlStatementLogger().logStatement( sql );
						// TODO: ResultSetExtractor
						statement.execute( sql );
					}
					catch (SQLException e) {
						log.debugf( "Error attempting to export id-table [%s] : %s", idTableDefinition.getName(), e.getMessage() );
					}
				}
				
				// TODO
//				session.getTransactionCoordinator().getJdbcCoordinator().release( statement );
				statement.close();
			}
			catch (SQLException e) {
				log.error( "Unable to use JDBC Connection to create Statement", e );
			}
			finally {
				try {
					connectionAccess.releaseConnection( connection );
				}
				catch (SQLException ignore) {
				}
			}
		}
		catch (SQLException e) {
			log.error( "Unable obtain JDBC Connection", e );
		}
	}

	@Override
	public void release(JdbcServices jdbcServices, JdbcConnectionAccess connectionAccess) {
		if ( ! cleanUpTables || tableCleanUpDdl == null ) {
			return;
		}

		try {
			Connection connection = connectionAccess.obtainConnection();

			try {
				// TODO: session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().createStatement();
				Statement statement = connection.createStatement();

				for ( String cleanupDdl : tableCleanUpDdl ) {
					try {
						jdbcServices.getSqlStatementLogger().logStatement( cleanupDdl );
						statement.execute( cleanupDdl );
					}
					catch (SQLException e) {
						log.debugf( "Error attempting to cleanup id-table : [%s]", e.getMessage() );
					}
				}
				
				// TODO
//				session.getTransactionCoordinator().getJdbcCoordinator().release( statement );
				statement.close();
			}
			catch (SQLException e) {
				log.error( "Unable to use JDBC Connection to create Statement", e );
			}
			finally {
				try {
					connectionAccess.releaseConnection( connection );
				}
				catch (SQLException ignore) {
				}
			}
		}
		catch (SQLException e) {
			log.error( "Unable obtain JDBC Connection", e );
		}
	}

	@Override
	public UpdateHandler buildUpdateHandler(SessionFactoryImplementor factory, HqlSqlWalker walker) {
		return new TableBasedUpdateHandlerImpl( factory, walker, catalog, schema ) {
			@Override
			protected void addAnyExtraIdSelectValues(SelectValues selectClause) {
				selectClause.addParameter( Types.CHAR, 36 );
			}

			@Override
			protected String generateIdSubselect(Queryable persister) {
				return super.generateIdSubselect( persister ) + " where hib_sess_id=?";
			}

			@Override
			protected int handlePrependedParametersOnIdSelection(PreparedStatement ps, SessionImplementor session, int pos) throws SQLException {
				bindSessionIdentifier( ps, session, pos );
				return 1;
			}

			@Override
			protected void handleAddedParametersOnUpdate(PreparedStatement ps, SessionImplementor session, int position) throws SQLException {
				bindSessionIdentifier( ps, session, position );
			}

			@Override
			protected void releaseFromUse(Queryable persister, SessionImplementor session) {
				// clean up our id-table rows
				cleanUpRows( determineIdTableName( persister ), session );
			}
		};
	}

	private void bindSessionIdentifier(PreparedStatement ps, SessionImplementor session, int position) throws SQLException {
		if ( ! AbstractSessionImpl.class.isInstance( session ) ) {
			throw new HibernateException( "Only available on SessionImpl instances" );
		}
		UUIDCharType.INSTANCE.set( ps, ( (AbstractSessionImpl) session ).getSessionIdentifier(), position, session );
	}

	private void cleanUpRows(String tableName, SessionImplementor session) {
		final String sql = "delete from " + tableName + " where hib_sess_id=?";
		try {
			PreparedStatement ps = null;
			try {
				ps = session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( sql, false );
				bindSessionIdentifier( ps, session, 1 );
				session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( ps );
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
		catch (SQLException e) {
			throw convert( session.getFactory(), e, "Unable to clean up id table [" + tableName + "]", sql );
		}
	}

	protected JDBCException convert(SessionFactoryImplementor factory, SQLException e, String message, String sql) {
		throw factory.getSQLExceptionHelper().convert( e, message, sql );
	}

	@Override
	public DeleteHandler buildDeleteHandler(SessionFactoryImplementor factory, HqlSqlWalker walker) {
		return new TableBasedDeleteHandlerImpl( factory, walker, catalog, schema ) {
			@Override
			protected void addAnyExtraIdSelectValues(SelectValues selectClause) {
				selectClause.addParameter( Types.CHAR, 36 );
			}

			@Override
			protected String generateIdSubselect(Queryable persister) {
				return super.generateIdSubselect( persister ) + " where hib_sess_id=?";
			}

			@Override
			protected int handlePrependedParametersOnIdSelection(PreparedStatement ps, SessionImplementor session, int pos) throws SQLException {
				bindSessionIdentifier( ps, session, pos );
				return 1;
			}

			@Override
			protected void handleAddedParametersOnDelete(PreparedStatement ps, SessionImplementor session) throws SQLException {
				bindSessionIdentifier( ps, session, 1 );
			}

			@Override
			protected void releaseFromUse(Queryable persister, SessionImplementor session) {
				// clean up our id-table rows
				cleanUpRows( determineIdTableName( persister ), session );
			}
		};
	}
}
