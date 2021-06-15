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
package org.hibernate.engine.internal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.type.Type;

/**
 * Centralizes the commonality regarding binding of parameter values into PreparedStatements as this logic is
 * used in many places.
 * <p/>
 * Ideally would like to move to the parameter handling as it is done in the hql.ast package.
 *
 * @author Steve Ebersole
 */
public class ParameterBinder {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, ParameterBinder.class.getName());

	public static interface NamedParameterSource {
		public int[] getNamedParameterLocations(String name);
	}

	private ParameterBinder() {
	}

	public static int bindQueryParameters(
	        final PreparedStatement st,
	        final QueryParameters queryParameters,
	        final int start,
	        final NamedParameterSource source,
	        SessionImplementor session) throws SQLException, HibernateException {
		int col = start;
		col += bindPositionalParameters( st, queryParameters, col, session );
		col += bindNamedParameters( st, queryParameters, col, source, session );
		return col;
	}

	public static int bindPositionalParameters(
	        final PreparedStatement st,
	        final QueryParameters queryParameters,
	        final int start,
	        final SessionImplementor session) throws SQLException, HibernateException {
		return bindPositionalParameters(
		        st,
		        queryParameters.getPositionalParameterValues(),
		        queryParameters.getPositionalParameterTypes(),
		        start,
		        session
		);
	}

	public static int bindPositionalParameters(
	        final PreparedStatement st,
	        final Object[] values,
	        final Type[] types,
	        final int start,
	        final SessionImplementor session) throws SQLException, HibernateException {
		int span = 0;
		for ( int i = 0; i < values.length; i++ ) {
			types[i].nullSafeSet( st, values[i], start + span, session );
			span += types[i].getColumnSpan( session.getFactory() );
		}
		return span;
	}

	public static int bindNamedParameters(
	        final PreparedStatement ps,
	        final QueryParameters queryParameters,
	        final int start,
	        final NamedParameterSource source,
	        final SessionImplementor session) throws SQLException, HibernateException {
		return bindNamedParameters( ps, queryParameters.getNamedParameters(), start, source, session );
	}

	public static int bindNamedParameters(
	        final PreparedStatement ps,
	        final Map namedParams,
	        final int start,
	        final NamedParameterSource source,
	        final SessionImplementor session) throws SQLException, HibernateException {
		if ( namedParams != null ) {
			// assumes that types are all of span 1
			Iterator iter = namedParams.entrySet().iterator();
			int result = 0;
			while ( iter.hasNext() ) {
				Map.Entry e = ( Map.Entry ) iter.next();
				String name = ( String ) e.getKey();
				TypedValue typedval = (TypedValue) e.getValue();
				int[] locations = source.getNamedParameterLocations( name );
				for ( int i = 0; i < locations.length; i++ ) {
					if ( LOG.isDebugEnabled() ) {
						LOG.debugf("bindNamedParameters() %s -> %s [%s]", typedval.getValue(), name, locations[i] + start);
					}
					typedval.getType().nullSafeSet( ps, typedval.getValue(), locations[i] + start, session );
				}
				result += locations.length;
			}
			return result;
		}
        return 0;
	}
}
