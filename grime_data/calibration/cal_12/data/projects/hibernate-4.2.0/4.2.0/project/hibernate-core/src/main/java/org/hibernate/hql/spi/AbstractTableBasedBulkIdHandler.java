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

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import antlr.RecognitionException;
import antlr.collections.AST;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.HqlSqlWalker;
import org.hibernate.hql.internal.ast.SqlGenerator;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.Table;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.sql.InsertSelect;
import org.hibernate.sql.Select;
import org.hibernate.sql.SelectValues;

/**
 * @author Steve Ebersole
 */
public class AbstractTableBasedBulkIdHandler {
	private final SessionFactoryImplementor sessionFactory;
	private final HqlSqlWalker walker;

	private final String catalog;
	private final String schema;

	public AbstractTableBasedBulkIdHandler(
			SessionFactoryImplementor sessionFactory,
			HqlSqlWalker walker,
			String catalog,
			String schema) {
		this.sessionFactory = sessionFactory;
		this.walker = walker;
		this.catalog = catalog;
		this.schema = schema;
	}

	protected SessionFactoryImplementor factory() {
		return sessionFactory;
	}

	protected HqlSqlWalker walker() {
		return walker;
	}

	protected JDBCException convert(SQLException e, String message, String sql) {
		throw factory().getSQLExceptionHelper().convert( e, message, sql );
	}

	protected static class ProcessedWhereClause {
		public static final ProcessedWhereClause NO_WHERE_CLAUSE = new ProcessedWhereClause();

		private final String userWhereClauseFragment;
		private final List<ParameterSpecification> idSelectParameterSpecifications;

		private ProcessedWhereClause() {
			this( "", Collections.<ParameterSpecification>emptyList() );
		}

		public ProcessedWhereClause(String userWhereClauseFragment, List<ParameterSpecification> idSelectParameterSpecifications) {
			this.userWhereClauseFragment = userWhereClauseFragment;
			this.idSelectParameterSpecifications = idSelectParameterSpecifications;
		}

		public String getUserWhereClauseFragment() {
			return userWhereClauseFragment;
		}

		public List<ParameterSpecification> getIdSelectParameterSpecifications() {
			return idSelectParameterSpecifications;
		}
	}

	@SuppressWarnings("unchecked")
	protected ProcessedWhereClause processWhereClause(AST whereClause) {
		if ( whereClause.getNumberOfChildren() != 0 ) {
			// If a where clause was specified in the update/delete query, use it to limit the
			// returned ids here...
			try {
				SqlGenerator sqlGenerator = new SqlGenerator( sessionFactory );
				sqlGenerator.whereClause( whereClause );
				String userWhereClause = sqlGenerator.getSQL().substring( 7 );  // strip the " where "
				List<ParameterSpecification> idSelectParameterSpecifications = sqlGenerator.getCollectedParameters();

				return new ProcessedWhereClause( userWhereClause, idSelectParameterSpecifications );
			}
			catch ( RecognitionException e ) {
				throw new HibernateException( "Unable to generate id select for DML operation", e );
			}
		}
		else {
			return ProcessedWhereClause.NO_WHERE_CLAUSE;
		}
	}

	protected String generateIdInsertSelect(Queryable persister, String tableAlias, ProcessedWhereClause whereClause) {
		Select select = new Select( sessionFactory.getDialect() );
		SelectValues selectClause = new SelectValues( sessionFactory.getDialect() )
				.addColumns( tableAlias, persister.getIdentifierColumnNames(), persister.getIdentifierColumnNames() );
		addAnyExtraIdSelectValues( selectClause );
		select.setSelectClause( selectClause.render() );

		String rootTableName = persister.getTableName();
		String fromJoinFragment = persister.fromJoinFragment( tableAlias, true, false );
		String whereJoinFragment = persister.whereJoinFragment( tableAlias, true, false );

		select.setFromClause( rootTableName + ' ' + tableAlias + fromJoinFragment );

		if ( whereJoinFragment == null ) {
			whereJoinFragment = "";
		}
		else {
			whereJoinFragment = whereJoinFragment.trim();
			if ( whereJoinFragment.startsWith( "and" ) ) {
				whereJoinFragment = whereJoinFragment.substring( 4 );
			}
		}

		if ( whereClause.getUserWhereClauseFragment().length() > 0 ) {
			if ( whereJoinFragment.length() > 0 ) {
				whereJoinFragment += " and ";
			}
		}
		select.setWhereClause( whereJoinFragment + whereClause.getUserWhereClauseFragment() );

		InsertSelect insert = new InsertSelect( sessionFactory.getDialect() );
		if ( sessionFactory.getSettings().isCommentsEnabled() ) {
			insert.setComment( "insert-select for " + persister.getEntityName() + " ids" );
		}
		insert.setTableName( determineIdTableName( persister ) );
		insert.setSelect( select );
		return insert.toStatementString();
	}

	protected void addAnyExtraIdSelectValues(SelectValues selectClause) {
	}

	protected String determineIdTableName(Queryable persister) {
		// todo : use the identifier/name qualifier service once we pull that over to master
		return Table.qualify( catalog, schema, persister.getTemporaryIdTableName() );
	}

	protected String generateIdSubselect(Queryable persister) {
		return "select " + StringHelper.join( ", ", persister.getIdentifierColumnNames() ) +
				" from " + determineIdTableName( persister );
	}

	protected void prepareForUse(Queryable persister, SessionImplementor session) {
	}

	protected void releaseFromUse(Queryable persister, SessionImplementor session) {
	}
}
