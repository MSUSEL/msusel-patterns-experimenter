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

import java.util.Map;

import org.hibernate.cfg.Mappings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.HqlSqlWalker;
import org.hibernate.persister.entity.Queryable;

/**
 * Generalized strategy contract for handling multi-table bulk HQL operations.
 *
 * @author Steve Ebersole
 */
public interface MultiTableBulkIdStrategy {
	/**
	 * Prepare the strategy.  Called as the SessionFactory is being built.  Intended patterns here include:<ul>
	 *     <li>Adding tables to the passed Mappings, to be picked by by "schema management tools"</li>
	 *     <li>Manually creating the tables immediately through the passed JDBC Connection access</li>
	 * </ul>
	 *
	 * @param jdbcServices The JdbcService object
	 * @param connectionAccess Access to the JDBC Connection
	 * @param mappings The Hibernate Mappings object, for access to O/RM mapping information
	 * @param mapping The Hibernate Mapping contract, mainly for use in DDL generation
	 * @param settings Configuration settings
	 */
	public void prepare(JdbcServices jdbcServices, JdbcConnectionAccess connectionAccess, Mappings mappings, Mapping mapping, Map settings);

	/**
	 * Release the strategy.   Called as the SessionFactory is being shut down.
	 *
	 * @param jdbcServices The JdbcService object
	 * @param connectionAccess Access to the JDBC Connection
	 */
	public void release(JdbcServices jdbcServices, JdbcConnectionAccess connectionAccess);

	/**
	 * Handler for dealing with multi-table HQL bulk update statements.
	 */
	public static interface UpdateHandler {
		public Queryable getTargetedQueryable();
		public String[] getSqlStatements();

		public int execute(SessionImplementor session, QueryParameters queryParameters);
	}

	/**
	 * Build a handler capable of handling the bulk update indicated by the given walker.
	 *
	 * @param factory The SessionFactory
	 * @param walker The AST walker, representing the update query
	 *
	 * @return The handler
	 */
	public UpdateHandler buildUpdateHandler(SessionFactoryImplementor factory, HqlSqlWalker walker);

	/**
	 * Handler for dealing with multi-table HQL bulk delete statements.
	 */
	public static interface DeleteHandler {
		public Queryable getTargetedQueryable();
		public String[] getSqlStatements();

		public int execute(SessionImplementor session, QueryParameters queryParameters);
	}

	/**
	 * Build a handler capable of handling the bulk delete indicated by the given walker.
	 *
	 * @param factory The SessionFactory
	 * @param walker The AST walker, representing the delete query
	 *
	 * @return The handler
	 */
	public DeleteHandler buildDeleteHandler(SessionFactoryImplementor factory, HqlSqlWalker walker);
}
