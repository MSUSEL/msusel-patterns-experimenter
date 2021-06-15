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
package org.hibernate.hql.internal.ast.exec;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import antlr.RecognitionException;

import org.hibernate.HibernateException;
import org.hibernate.action.internal.BulkOperationCleanupAction;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.spi.EventSource;
import org.hibernate.hql.internal.ast.HqlSqlWalker;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.hibernate.hql.internal.ast.SqlGenerator;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.persister.entity.Queryable;

/**
 * Implementation of BasicExecutor.
 *
 * @author Steve Ebersole
 */
public class BasicExecutor implements StatementExecutor {
	private final SessionFactoryImplementor factory;
	private final Queryable persister;
	private final String sql;
	private final List parameterSpecifications;

	public BasicExecutor(HqlSqlWalker walker, Queryable persister) {
		this.factory = walker.getSessionFactoryHelper().getFactory();
		this.persister = persister;
		try {
			SqlGenerator gen = new SqlGenerator( factory );
			gen.statement( walker.getAST() );
			sql = gen.getSQL();
			gen.getParseErrorHandler().throwQueryException();
			parameterSpecifications = gen.getCollectedParameters();
		}
		catch ( RecognitionException e ) {
			throw QuerySyntaxException.convert( e );
		}
	}

	public String[] getSqlStatements() {
		return new String[] { sql };
	}

	public int execute(QueryParameters parameters, SessionImplementor session) throws HibernateException {
		BulkOperationCleanupAction action = new BulkOperationCleanupAction( session, persister );
		if ( session.isEventSource() ) {
			( (EventSource) session ).getActionQueue().addAction( action );
		}
		else {
			action.getAfterTransactionCompletionProcess().doAfterTransactionCompletion( true, session );
		}

		PreparedStatement st = null;
		RowSelection selection = parameters.getRowSelection();

		try {
			try {
				st = session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( sql, false );
				Iterator parameterSpecifications = this.parameterSpecifications.iterator();
				int pos = 1;
				while ( parameterSpecifications.hasNext() ) {
					final ParameterSpecification paramSpec = ( ParameterSpecification ) parameterSpecifications.next();
					pos += paramSpec.bind( st, parameters, session, pos );
				}
				if ( selection != null ) {
					if ( selection.getTimeout() != null ) {
						st.setQueryTimeout( selection.getTimeout().intValue() );
					}
				}

				return session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( st );
			}
			finally {
				if ( st != null ) {
					session.getTransactionCoordinator().getJdbcCoordinator().release( st );
				}
			}
		}
		catch( SQLException sqle ) {
			throw factory.getSQLExceptionHelper().convert( sqle, "could not execute update query", sql );
		}
	}
}
