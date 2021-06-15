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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.HqlSqlWalker;
import org.hibernate.hql.internal.ast.tree.DeleteStatement;
import org.hibernate.hql.internal.ast.tree.FromElement;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.sql.Delete;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;
import org.jboss.logging.Logger;

/**
* @author Steve Ebersole
*/
public class TableBasedDeleteHandlerImpl
		extends AbstractTableBasedBulkIdHandler
		implements MultiTableBulkIdStrategy.DeleteHandler {
	private static final Logger log = Logger.getLogger( TableBasedDeleteHandlerImpl.class );

	private final Queryable targetedPersister;

	private final String idInsertSelect;
	private final List<ParameterSpecification> idSelectParameterSpecifications;
	private final List<String> deletes;

	public TableBasedDeleteHandlerImpl(SessionFactoryImplementor factory, HqlSqlWalker walker) {
		this( factory, walker, null, null );
	}

	public TableBasedDeleteHandlerImpl(
			SessionFactoryImplementor factory,
			HqlSqlWalker walker,
			String catalog,
			String schema) {
		super( factory, walker, catalog, schema );

		DeleteStatement deleteStatement = ( DeleteStatement ) walker.getAST();
		FromElement fromElement = deleteStatement.getFromClause().getFromElement();

		this.targetedPersister = fromElement.getQueryable();
		final String bulkTargetAlias = fromElement.getTableAlias();

		final ProcessedWhereClause processedWhereClause = processWhereClause( deleteStatement.getWhereClause() );
		this.idSelectParameterSpecifications = processedWhereClause.getIdSelectParameterSpecifications();
		this.idInsertSelect = generateIdInsertSelect( targetedPersister, bulkTargetAlias, processedWhereClause );
		log.tracev( "Generated ID-INSERT-SELECT SQL (multi-table delete) : {0}", idInsertSelect );
		
		final String idSubselect = generateIdSubselect( targetedPersister );
		deletes = new ArrayList<String>();
		
		// If many-to-many, delete the FK row in the collection table.
		for ( Type type : targetedPersister.getPropertyTypes() ) {
			if ( type.isCollectionType() ) {
				CollectionType cType = (CollectionType) type;
				AbstractCollectionPersister cPersister = (AbstractCollectionPersister)factory.getCollectionPersister( cType.getRole() );
				if ( cPersister.isManyToMany() ) {
					deletes.add( generateDelete( cPersister.getTableName(),
							cPersister.getKeyColumnNames(), idSubselect, "bulk delete - m2m join table cleanup"));
				}
			}
		}

		String[] tableNames = targetedPersister.getConstraintOrderedTableNameClosure();
		String[][] columnNames = targetedPersister.getContraintOrderedTableKeyColumnClosure();
		for ( int i = 0; i < tableNames.length; i++ ) {
			// TODO : an optimization here would be to consider cascade deletes and not gen those delete statements;
			//      the difficulty is the ordering of the tables here vs the cascade attributes on the persisters ->
			//          the table info gotten here should really be self-contained (i.e., a class representation
			//          defining all the needed attributes), then we could then get an array of those
			deletes.add( generateDelete( tableNames[i], columnNames[i], idSubselect, "bulk delete"));
		}
	}
	
	private String generateDelete(String tableName, String[] columnNames, String idSubselect, String comment) {
		final Delete delete = new Delete()
				.setTableName( tableName )
				.setWhere( "(" + StringHelper.join( ", ", columnNames ) + ") IN (" + idSubselect + ")" );
		if ( factory().getSettings().isCommentsEnabled() ) {
			delete.setComment( comment );
		}
		return delete.toStatementString();
	}

	@Override
	public Queryable getTargetedQueryable() {
		return targetedPersister;
	}

	@Override
	public String[] getSqlStatements() {
		return deletes.toArray( new String[deletes.size()] );
	}

	@Override
	public int execute(SessionImplementor session, QueryParameters queryParameters) {
		prepareForUse( targetedPersister, session );
		try {
			PreparedStatement ps = null;
			int resultCount = 0;
			try {
				try {
					ps = session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( idInsertSelect, false );
					int pos = 1;
					pos += handlePrependedParametersOnIdSelection( ps, session, pos );
					for ( ParameterSpecification parameterSpecification : idSelectParameterSpecifications ) {
						pos += parameterSpecification.bind( ps, queryParameters, session, pos );
					}
					resultCount = session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( ps );
				}
				finally {
					if ( ps != null ) {
						session.getTransactionCoordinator().getJdbcCoordinator().release( ps );
					}
				}
			}
			catch( SQLException e ) {
				throw convert( e, "could not insert/select ids for bulk delete", idInsertSelect );
			}

			// Start performing the deletes
			for ( String delete : deletes ) {
				try {
					try {
						ps = session.getTransactionCoordinator()
								.getJdbcCoordinator()
								.getStatementPreparer()
								.prepareStatement( delete, false );
						handleAddedParametersOnDelete( ps, session );
						session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( ps );
					}
					finally {
						if ( ps != null ) {
							session.getTransactionCoordinator().getJdbcCoordinator().release( ps );
						}
					}
				}
				catch (SQLException e) {
					throw convert( e, "error performing bulk delete", delete );
				}
			}

			return resultCount;

		}
		finally {
			releaseFromUse( targetedPersister, session );
		}
	}

	protected int handlePrependedParametersOnIdSelection(PreparedStatement ps, SessionImplementor session, int pos) throws SQLException {
		return 0;
	}

	protected void handleAddedParametersOnDelete(PreparedStatement ps, SessionImplementor session) throws SQLException {
	}
}
