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
import antlr.SemanticException;
import antlr.collections.AST;
import org.jboss.logging.Logger;

import org.hibernate.hql.internal.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.internal.antlr.SqlTokenTypes;
import org.hibernate.hql.internal.ast.util.ASTUtil;
import org.hibernate.hql.internal.ast.util.ColumnHelper;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.type.Type;

/**
 * Defines a top-level AST node representing an HQL select statement.
 *
 * @author Joshua Davis
 */
public class QueryNode extends AbstractRestrictableStatement implements SelectExpression {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, QueryNode.class.getName());

	private OrderByClause orderByClause;
	private int scalarColumnIndex = -1;

	/**
	 * @see Statement#getStatementType()
	 */
	public int getStatementType() {
		return HqlSqlTokenTypes.QUERY;
	}

	/**
	 * @see Statement#needsExecutor()
	 */
	public boolean needsExecutor() {
		return false;
	}

	@Override
    protected int getWhereClauseParentTokenType() {
		return SqlTokenTypes.FROM;
	}

	@Override
    protected CoreMessageLogger getLog() {
        return LOG;
	}

	/**
	 * Locate the select clause that is part of this select statement.
	 * </p>
	 * Note, that this might return null as derived select clauses (i.e., no
	 * select clause at the HQL-level) get generated much later than when we
	 * get created; thus it depends upon lifecycle.
	 *
	 * @return Our select clause, or null.
	 */
	public final SelectClause getSelectClause() {
		// Due to the complexity in initializing the SelectClause, do not generate one here.
		// If it is not found; simply return null...
		//
		// Also, do not cache since it gets generated well after we are created.
		return ( SelectClause ) ASTUtil.findTypeInChildren( this, SqlTokenTypes.SELECT_CLAUSE );
	}

	public final boolean hasOrderByClause() {
		OrderByClause orderByClause = locateOrderByClause();
		return orderByClause != null && orderByClause.getNumberOfChildren() > 0;
	}

	public final OrderByClause getOrderByClause() {
		if ( orderByClause == null ) {
			orderByClause = locateOrderByClause();

			// if there is no order by, make one
			if ( orderByClause == null ) {
				LOG.debug( "getOrderByClause() : Creating a new ORDER BY clause" );
				orderByClause = ( OrderByClause ) ASTUtil.create( getWalker().getASTFactory(), SqlTokenTypes.ORDER, "ORDER" );

				// Find the WHERE; if there is no WHERE, find the FROM...
				AST prevSibling = ASTUtil.findTypeInChildren( this, SqlTokenTypes.WHERE );
				if ( prevSibling == null ) {
					prevSibling = ASTUtil.findTypeInChildren( this, SqlTokenTypes.FROM );
				}

				// Now, inject the newly built ORDER BY into the tree
				orderByClause.setNextSibling( prevSibling.getNextSibling() );
				prevSibling.setNextSibling( orderByClause );
			}
		}
		return orderByClause;
	}

	private OrderByClause locateOrderByClause() {
		return ( OrderByClause ) ASTUtil.findTypeInChildren( this, SqlTokenTypes.ORDER );
	}


	private String alias;

	public String getAlias() {
		return alias;
	}

	public FromElement getFromElement() {
		return null;
	}

	public boolean isConstructor() {
		return false;
	}

	public boolean isReturnableEntity() throws SemanticException {
		return false;
	}

	public boolean isScalar() throws SemanticException {
		return true;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setScalarColumn(int i) throws SemanticException {
		scalarColumnIndex = i;
		setScalarColumnText( i );
	}

	public int getScalarColumnIndex() {
		return scalarColumnIndex;
	}

	public void setScalarColumnText(int i) throws SemanticException {
		ColumnHelper.generateSingleScalarColumn( this, i );
	}

	@Override
    public Type getDataType() {
		return ( (SelectExpression) getSelectClause().getFirstSelectExpression() ).getDataType();
	}

}
