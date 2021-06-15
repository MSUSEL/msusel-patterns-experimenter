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

import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.hql.internal.ast.util.ColumnHelper;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.type.Type;

/**
 * Represents an aggregate function i.e. min, max, sum, avg.
 *
 * @author Joshua Davis
 */
public class AggregateNode extends AbstractSelectExpression implements SelectExpression, FunctionNode {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, AggregateNode.class.getName() );

	private SQLFunction sqlFunction;

	public SQLFunction getSQLFunction() {
		return sqlFunction;
	}

	public void resolve() {
		resolveFunction();
	}

	private SQLFunction resolveFunction() {
		if ( sqlFunction == null ) {
			final String name = getText();
			sqlFunction = getSessionFactoryHelper().findSQLFunction( getText() );
			if ( sqlFunction == null ) {
				LOG.unableToResolveAggregateFunction( name );
				sqlFunction = new StandardSQLFunction( name );
			}
		}
		return sqlFunction;
	}

	public Type getFirstArgumentType() {
		AST argument = getFirstChild();
		while ( argument != null ) {
			if ( argument instanceof SqlNode ) {
				final Type type = ( (SqlNode) argument ).getDataType();
				if ( type != null ) {
					return type;
				}
				argument = argument.getNextSibling();
			}
		}
		return null;
	}

	@Override
	public Type getDataType() {
		// Get the function return value type, based on the type of the first argument.
		return getSessionFactoryHelper().findFunctionReturnType( getText(), resolveFunction(), getFirstChild() );
	}

	public void setScalarColumnText(int i) throws SemanticException {
		ColumnHelper.generateSingleScalarColumn( this, i );
	}

	@Override
	public boolean isScalar() throws SemanticException {
		// functions in a SELECT should always be considered scalar.
		return true;
	}
}
