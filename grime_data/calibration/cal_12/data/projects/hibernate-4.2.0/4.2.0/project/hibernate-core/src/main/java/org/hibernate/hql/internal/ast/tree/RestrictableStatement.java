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
package org.hibernate.hql.internal.ast.tree;
import antlr.collections.AST;

/**
 * Type definition for Statements which are restrictable via a where-clause (and
 * thus also having a from-clause).
 *
 * @author Steve Ebersole
 */
public interface RestrictableStatement extends Statement {
	/**
	 * Retreives the from-clause in effect for this statement.
	 *
	 * @return The from-clause for this statement; could be null if the from-clause
	 * has not yet been parsed/generated.
	 */
	public FromClause getFromClause();

	/**
	 * Does this statement tree currently contain a where clause?
	 *
	 * @return True if a where-clause is found in the statement tree and
	 * that where clause actually defines restrictions; false otherwise.
	 */
	public boolean hasWhereClause();

	/**
	 * Retreives the where-clause defining the restriction(s) in effect for
	 * this statement.
	 * <p/>
	 * Note that this will generate a where-clause if one was not found, so caution
	 * needs to taken prior to calling this that restrictions will actually exist
	 * in the resulting statement tree (otherwise "unexpected end of subtree" errors
	 * might occur during rendering).
	 *
	 * @return The where clause.
	 */
	public AST getWhereClause();
}
