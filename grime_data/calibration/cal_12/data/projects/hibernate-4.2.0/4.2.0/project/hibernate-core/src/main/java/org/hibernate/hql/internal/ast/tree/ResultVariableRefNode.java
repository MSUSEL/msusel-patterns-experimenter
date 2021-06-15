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

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.StringHelper;

/**
 * Represents a reference to a result_variable as defined in the JPA 2 spec.
 * For example:
 * <code>
 * select v as value from tab1 order by value
 * </code>
 * <p/>
 * "value" used in the order by clause is a reference to the
 * result_variable, "value", defined in the select clause.
 *
 * @author Gail Badner
 */
public class ResultVariableRefNode extends HqlSqlWalkerNode {
	private SelectExpression selectExpression;

	/**
	 * Set the select expression that defines the result variable.
	 *
	 * @param selectExpression the select expression;
	 *        selectExpression.getAlias() must be non-null
	 * @throws SemanticException if selectExpression or
	 *         selectExpression.getAlias() is null.
	 */
	public void setSelectExpression(SelectExpression selectExpression) throws SemanticException {
		if ( selectExpression == null || selectExpression.getAlias() == null ) {
			throw new SemanticException( "A ResultVariableRefNode must refer to a non-null alias." );
		}
		this.selectExpression = selectExpression;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
    public String getRenderText(SessionFactoryImplementor sessionFactory) {
		int scalarColumnIndex = selectExpression.getScalarColumnIndex();
		if ( scalarColumnIndex < 0 ) {
			throw new IllegalStateException(
					"selectExpression.getScalarColumnIndex() must be >= 0; actual = " + scalarColumnIndex
			);
		}
		return sessionFactory.getDialect().replaceResultVariableInOrderByClauseWithPosition() ?
			getColumnPositionsString( scalarColumnIndex ) :
			getColumnNamesString( scalarColumnIndex );

	}

	private String getColumnPositionsString(int scalarColumnIndex ) {
		int startPosition = getWalker().getSelectClause().getColumnNamesStartPosition( scalarColumnIndex );
		StringBuilder buf = new StringBuilder();
		int nColumns = getWalker().getSelectClause().getColumnNames()[ scalarColumnIndex ].length;
		for ( int i = startPosition; i < startPosition + nColumns; i++ ) {
			if ( i > startPosition ) {
				buf.append( ", " );
			}
			buf.append( i );
		}
		return buf.toString();
	}

	private String getColumnNamesString(int scalarColumnIndex) {
		return StringHelper.join( ", ", getWalker().getSelectClause().getColumnNames()[scalarColumnIndex] );
	}
}
