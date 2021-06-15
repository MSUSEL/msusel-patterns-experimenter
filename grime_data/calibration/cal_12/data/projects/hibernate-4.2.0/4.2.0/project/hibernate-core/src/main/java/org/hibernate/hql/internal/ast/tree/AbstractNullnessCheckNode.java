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

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.antlr.HqlSqlTokenTypes;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.type.Type;

/**
 * Base class for nodes dealing 'is null' and 'is not null' operators.
 * <p/>
 * todo : a good deal of this is copied from BinaryLogicOperatorNode; look at consolidating these code fragments
 *
 * @author Steve Ebersole
 */
public abstract class AbstractNullnessCheckNode extends UnaryLogicOperatorNode {

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void initialize() {
		// TODO : this really needs to be delayed unitl after we definitively know the operand node type;
		// where this is currently a problem is parameters for which where we cannot unequivocally
		// resolve an expected type
		Type operandType = extractDataType( getOperand() );
		if ( operandType == null ) {
			return;
		}
		SessionFactoryImplementor sessionFactory = getSessionFactoryHelper().getFactory();
		int operandColumnSpan = operandType.getColumnSpan( sessionFactory );
		if ( operandColumnSpan > 1 ) {
			mutateRowValueConstructorSyntax( operandColumnSpan );
		}
	}

	/**
	 * When (if) we need to expand a row value constructor, what is the type of connector to use between the
	 * expansion fragments.
	 *
	 * @return The expansion connector type.
	 */
	protected abstract int getExpansionConnectorType();

	/**
	 * When (if) we need to expand a row value constructor, what is the text of the connector to use between the
	 * expansion fragments.
	 *
	 * @return The expansion connector text.
	 */
	protected abstract String getExpansionConnectorText();

	private void mutateRowValueConstructorSyntax(int operandColumnSpan) {
		final int comparisonType = getType();
		final String comparisonText = getText();

		final int expansionConnectorType = getExpansionConnectorType();
		final String expansionConnectorText = getExpansionConnectorText();

		setType( expansionConnectorType );
		setText( expansionConnectorText );

		String[] mutationTexts = extractMutationTexts( getOperand(), operandColumnSpan );

		AST container = this;
		for ( int i = operandColumnSpan - 1; i > 0; i-- ) {
			if ( i == 1 ) {
				AST op1 = getASTFactory().create( comparisonType, comparisonText );
				AST operand1 = getASTFactory().create( HqlSqlTokenTypes.SQL_TOKEN, mutationTexts[0] );
				op1.setFirstChild( operand1 );
				container.setFirstChild( op1 );
				AST op2 = getASTFactory().create( comparisonType, comparisonText );
				AST operand2 = getASTFactory().create( HqlSqlTokenTypes.SQL_TOKEN, mutationTexts[1] );
				op2.setFirstChild( operand2 );
				op1.setNextSibling( op2 );
			}
			else {
				AST op = getASTFactory().create( comparisonType, comparisonText );
				AST operand = getASTFactory().create( HqlSqlTokenTypes.SQL_TOKEN, mutationTexts[i] );
				op.setFirstChild( operand );
				AST newContainer = getASTFactory().create( expansionConnectorType, expansionConnectorText );
				container.setFirstChild( newContainer );
				newContainer.setNextSibling( op );
				container = newContainer;
			}
		}
	}

	private static Type extractDataType(Node operand) {
		Type type = null;
		if ( operand instanceof SqlNode ) {
			type = ( ( SqlNode ) operand ).getDataType();
		}
		if ( type == null && operand instanceof ExpectedTypeAwareNode ) {
			type = ( ( ExpectedTypeAwareNode ) operand ).getExpectedType();
		}
		return type;
	}

	private static String[] extractMutationTexts(Node operand, int count) {
		if ( operand instanceof ParameterNode ) {
			String[] rtn = new String[count];
			for ( int i = 0; i < count; i++ ) {
				rtn[i] = "?";
			}
			return rtn;
		}
		else if ( operand.getType() == HqlSqlTokenTypes.VECTOR_EXPR ) {
			String[] rtn = new String[ operand.getNumberOfChildren() ];
			int x = 0;
			AST node = operand.getFirstChild();
			while ( node != null ) {
				rtn[ x++ ] = node.getText();
				node = node.getNextSibling();
			}
			return rtn;
		}
		else if ( operand instanceof SqlNode ) {
			String nodeText = operand.getText();
			if ( nodeText.startsWith( "(" ) ) {
				nodeText = nodeText.substring( 1 );
			}
			if ( nodeText.endsWith( ")" ) ) {
				nodeText = nodeText.substring( 0, nodeText.length() - 1 );
			}
			String[] splits = StringHelper.split( ", ", nodeText );
			if ( count != splits.length ) {
				throw new HibernateException( "SqlNode's text did not reference expected number of columns" );
			}
			return splits;
		}
		else {
			throw new HibernateException( "dont know how to extract row value elements from node : " + operand );
		}
	}
}
