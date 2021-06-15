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
package org.hibernate.engine.spi;

import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryReturn;

/**
 * Definition of a named native SQL query, defined in the mapping metadata.
 * 
 * @author Max Andersen
 */
public class NamedSQLQueryDefinition extends NamedQueryDefinition {

	private NativeSQLQueryReturn[] queryReturns;
	private final List<String> querySpaces;
	private final boolean callable;
	private String resultSetRef;

	/**
	 * This form used to construct a NamedSQLQueryDefinition from the binder
	 * code when a the result-set mapping information is explicitly
	 * provided in the query definition (i.e., no resultset-mapping used)
	 *
	 * @param name The name of named query
	 * @param query The sql query string
	 * @param queryReturns The in-lined query return definitions
	 * @param querySpaces Any specified query spaces (used for auto-flushing)
	 * @param cacheable Whether the query results are cacheable
	 * @param cacheRegion If cacheable, the region into which to store the results
	 * @param timeout A JDBC-level timeout to be applied
	 * @param fetchSize A JDBC-level fetch-size to be applied
	 * @param flushMode The flush mode to use for this query
	 * @param cacheMode The cache mode to use during execution and subsequent result loading
	 * @param readOnly Whether returned entities should be marked as read-only in the session
	 * @param comment Any sql comment to be applied to the query
	 * @param parameterTypes parameter type map
	 * @param callable Does the query string represent a callable object (i.e., proc)
	 */
	public NamedSQLQueryDefinition(
			String name,
			String query,
			NativeSQLQueryReturn[] queryReturns,
			List<String> querySpaces,
			boolean cacheable,
			String cacheRegion,
			Integer timeout,
			Integer fetchSize,
			FlushMode flushMode,
			CacheMode cacheMode,
			boolean readOnly,
			String comment,
			Map parameterTypes,
			boolean callable) {
		super(
				name,
				query.trim(), /* trim done to workaround stupid oracle bug that cant handle whitespaces before a { in a sp */
				cacheable,
				cacheRegion,
				timeout,
				fetchSize,
				flushMode,
				cacheMode,
				readOnly,
				comment,
				parameterTypes
		);
		this.queryReturns = queryReturns;
		this.querySpaces = querySpaces;
		this.callable = callable;
	}

	/**
	 * This form used to construct a NamedSQLQueryDefinition from the binder
	 * code when a resultset-mapping reference is used.
	 *
	 * @param name The name of named query
	 * @param query The sql query string
	 * @param resultSetRef The resultset-mapping name
	 * @param querySpaces Any specified query spaces (used for auto-flushing)
	 * @param cacheable Whether the query results are cacheable
	 * @param cacheRegion If cacheable, the region into which to store the results
	 * @param timeout A JDBC-level timeout to be applied
	 * @param fetchSize A JDBC-level fetch-size to be applied
	 * @param flushMode The flush mode to use for this query
	 * @param cacheMode The cache mode to use during execution and subsequent result loading
	 * @param readOnly Whether returned entities should be marked as read-only in the session
	 * @param comment Any sql comment to be applied to the query
	 * @param parameterTypes parameter type map
	 * @param callable Does the query string represent a callable object (i.e., proc)
	 */
	public NamedSQLQueryDefinition(
			String name,
			String query,
			String resultSetRef,
			List<String> querySpaces,
			boolean cacheable,
			String cacheRegion,
			Integer timeout,
			Integer fetchSize,
			FlushMode flushMode,
			CacheMode cacheMode,
			boolean readOnly,
			String comment,
			Map parameterTypes,
			boolean callable) {
		super(
				name,
				query.trim(), /* trim done to workaround stupid oracle bug that cant handle whitespaces before a { in a sp */
				cacheable,
				cacheRegion,
				timeout,
				fetchSize,
				flushMode,
				cacheMode,
				readOnly,
				comment,
				parameterTypes
		);
		this.resultSetRef = resultSetRef;
		this.querySpaces = querySpaces;
		this.callable = callable;
	}

	/**
	 * This form used from annotations (?).  Essentially the same as the above using a
	 * resultset-mapping reference, but without cacheMode, readOnly, and comment.
	 *
	 * FIXME: annotations do not use it, so it can be remove from my POV
	 * @deprecated
	 *
	 *
	 * @param query The sql query string
	 * @param resultSetRef The result-set-mapping name
	 * @param querySpaces Any specified query spaces (used for auto-flushing)
	 * @param cacheable Whether the query results are cacheable
	 * @param cacheRegion If cacheable, the region into which to store the results
	 * @param timeout A JDBC-level timeout to be applied
	 * @param fetchSize A JDBC-level fetch-size to be applied
	 * @param flushMode The flush mode to use for this query
	 * @param parameterTypes parameter type map
	 * @param callable Does the query string represent a callable object (i.e., proc)
	 */
	public NamedSQLQueryDefinition(
			String query,
			String resultSetRef,
			List<String> querySpaces,
			boolean cacheable,
			String cacheRegion,
			Integer timeout,
			Integer fetchSize,
			FlushMode flushMode,
			Map parameterTypes,
			boolean callable) {
		this(
				null,
				query,
				resultSetRef,
				querySpaces,
				cacheable,
				cacheRegion,
				timeout,
				fetchSize,
				flushMode,
				null,
				false,
				null,
				parameterTypes,
				callable
		);
	}

	public NativeSQLQueryReturn[] getQueryReturns() {
		return queryReturns;
	}

	public List<String> getQuerySpaces() {
		return querySpaces;
	}

	public boolean isCallable() {
		return callable;
	}

	public String getResultSetRef() {
		return resultSetRef;
	}
}