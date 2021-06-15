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
package org.hibernate.engine.query.spi;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.QueryException;
import org.hibernate.action.internal.BulkOperationCleanupAction;
import org.hibernate.engine.query.spi.sql.NativeSQLQuerySpecification;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.event.spi.EventSource;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.loader.custom.sql.SQLCustomQuery;
import org.hibernate.type.Type;

/**
 * Defines a query execution plan for a native-SQL query.
 *
 * @author Steve Ebersole
 */
public class NativeSQLQueryPlan implements Serializable {
	private final String sourceQuery;

	private final SQLCustomQuery customQuery;

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, NativeSQLQueryPlan.class.getName());

	public NativeSQLQueryPlan(
			NativeSQLQuerySpecification specification,
			SessionFactoryImplementor factory) {
		this.sourceQuery = specification.getQueryString();

		customQuery = new SQLCustomQuery(
				specification.getQueryString(),
				specification.getQueryReturns(),
				specification.getQuerySpaces(),
				factory );
	}

	public String getSourceQuery() {
		return sourceQuery;
	}

	public SQLCustomQuery getCustomQuery() {
		return customQuery;
	}

	private int[] getNamedParameterLocs(String name) throws QueryException {
		Object loc = customQuery.getNamedParameterBindPoints().get( name );
		if ( loc == null ) {
			throw new QueryException(
					"Named parameter does not appear in Query: " + name,
					customQuery.getSQL() );
		}
		if ( loc instanceof Integer ) {
			return new int[] { ((Integer) loc ).intValue() };
		}
		else {
			return ArrayHelper.toIntArray( (List) loc );
		}
	}

	/**
	 * Perform binding of all the JDBC bind parameter values based on the user-defined
	 * positional query parameters (these are the '?'-style hibernate query
	 * params) into the JDBC {@link PreparedStatement}.
	 *
	 * @param st The prepared statement to which to bind the parameter values.
	 * @param queryParameters The query parameters specified by the application.
	 * @param start JDBC paramer binds are positional, so this is the position
	 * from which to start binding.
	 * @param session The session from which the query originated.
	 *
	 * @return The number of JDBC bind positions accounted for during execution.
	 *
	 * @throws SQLException Some form of JDBC error binding the values.
	 * @throws HibernateException Generally indicates a mapping problem or type mismatch.
	 */
	private int bindPositionalParameters(
			final PreparedStatement st,
			final QueryParameters queryParameters,
			final int start,
			final SessionImplementor session) throws SQLException {
		final Object[] values = queryParameters.getFilteredPositionalParameterValues();
		final Type[] types = queryParameters.getFilteredPositionalParameterTypes();
		int span = 0;
		for (int i = 0; i < values.length; i++) {
			types[i].nullSafeSet( st, values[i], start + span, session );
			span += types[i].getColumnSpan( session.getFactory() );
		}
		return span;
	}

	/**
	 * Perform binding of all the JDBC bind parameter values based on the user-defined
	 * named query parameters into the JDBC {@link PreparedStatement}.
	 *
	 * @param ps The prepared statement to which to bind the parameter values.
	 * @param namedParams The named query parameters specified by the application.
	 * @param start JDBC paramer binds are positional, so this is the position
	 * from which to start binding.
	 * @param session The session from which the query originated.
	 *
	 * @return The number of JDBC bind positions accounted for during execution.
	 *
	 * @throws SQLException Some form of JDBC error binding the values.
	 * @throws HibernateException Generally indicates a mapping problem or type mismatch.
	 */
	private int bindNamedParameters(
			final PreparedStatement ps,
			final Map namedParams,
			final int start,
			final SessionImplementor session) throws SQLException {
		if ( namedParams != null ) {
			// assumes that types are all of span 1
			Iterator iter = namedParams.entrySet().iterator();
			int result = 0;
			while ( iter.hasNext() ) {
				Map.Entry e = (Map.Entry) iter.next();
				String name = (String) e.getKey();
				TypedValue typedval = (TypedValue) e.getValue();
				int[] locs = getNamedParameterLocs( name );
				for (int i = 0; i < locs.length; i++) {
                    LOG.debugf("bindNamedParameters() %s -> %s [%s]", typedval.getValue(), name, locs[i] + start);
					typedval.getType().nullSafeSet( ps, typedval.getValue(),
							locs[i] + start, session );
				}
				result += locs.length;
			}
			return result;
		}
        return 0;
	}

	protected void coordinateSharedCacheCleanup(SessionImplementor session) {
		BulkOperationCleanupAction action = new BulkOperationCleanupAction( session, getCustomQuery().getQuerySpaces() );

		if ( session.isEventSource() ) {
			( ( EventSource ) session ).getActionQueue().addAction( action );
		}
		else {
			action.getAfterTransactionCompletionProcess().doAfterTransactionCompletion( true, session );
		}
	}

	public int performExecuteUpdate(QueryParameters queryParameters,
			SessionImplementor session) throws HibernateException {

		coordinateSharedCacheCleanup( session );

		if(queryParameters.isCallable()) {
			throw new IllegalArgumentException("callable not yet supported for native queries");
		}

		int result = 0;
		PreparedStatement ps;
		try {
			queryParameters.processFilters( this.customQuery.getSQL(),
					session );
			String sql = queryParameters.getFilteredSQL();

			ps = session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( sql, false );

			try {
				int col = 1;
				col += bindPositionalParameters( ps, queryParameters, col,
						session );
				col += bindNamedParameters( ps, queryParameters
						.getNamedParameters(), col, session );
				result = session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( ps );
			}
			finally {
				if ( ps != null ) {
					session.getTransactionCoordinator().getJdbcCoordinator().release( ps );
				}
			}
		}
		catch (SQLException sqle) {
			throw session.getFactory().getSQLExceptionHelper().convert(
					sqle, "could not execute native bulk manipulation query", this.sourceQuery );
		}

		return result;
	}
}
