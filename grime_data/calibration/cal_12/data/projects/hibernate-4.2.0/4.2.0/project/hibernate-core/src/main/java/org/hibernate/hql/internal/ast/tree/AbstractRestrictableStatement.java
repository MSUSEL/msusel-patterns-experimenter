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

import org.hibernate.hql.internal.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.internal.ast.util.ASTUtil;
import org.hibernate.internal.CoreMessageLogger;

/**
 * Convenience implementation of {@link RestrictableStatement}
 * to centralize common functionality.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractRestrictableStatement extends AbstractStatement implements RestrictableStatement {

	private FromClause fromClause;
	private AST whereClause;

	protected abstract int getWhereClauseParentTokenType();

    protected abstract CoreMessageLogger getLog();

	/**
	 * @see org.hibernate.hql.internal.ast.tree.RestrictableStatement#getFromClause
	 */
	public final FromClause getFromClause() {
		if ( fromClause == null ) {
			fromClause = ( FromClause ) ASTUtil.findTypeInChildren( this, HqlSqlTokenTypes.FROM );
		}
		return fromClause;
	}

	/**
	 * @see RestrictableStatement#hasWhereClause
	 */
	public final boolean hasWhereClause() {
		AST whereClause = locateWhereClause();
		return whereClause != null && whereClause.getNumberOfChildren() > 0;
	}

	/**
	 * @see org.hibernate.hql.internal.ast.tree.RestrictableStatement#getWhereClause
	 */
	public final AST getWhereClause() {
		if ( whereClause == null ) {
			whereClause = locateWhereClause();
			// If there is no WHERE node, make one.
			if ( whereClause == null ) {
				getLog().debug( "getWhereClause() : Creating a new WHERE clause..." );
				whereClause = ASTUtil.create( getWalker().getASTFactory(), HqlSqlTokenTypes.WHERE, "WHERE" );
				// inject the WHERE after the parent
				AST parent = ASTUtil.findTypeInChildren( this, getWhereClauseParentTokenType() );
				whereClause.setNextSibling( parent.getNextSibling() );
				parent.setNextSibling( whereClause );
			}
		}
		return whereClause;
	}

	protected AST locateWhereClause() {
		return ASTUtil.findTypeInChildren( this, HqlSqlTokenTypes.WHERE );
	}
}
