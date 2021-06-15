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

import org.hibernate.QueryException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.type.LiteralType;
import org.hibernate.type.Type;

/**
 * A node representing a static Java constant.
 *
 * @author Steve Ebersole
 */
public class JavaConstantNode extends Node implements ExpectedTypeAwareNode, SessionFactoryAwareNode {

	private SessionFactoryImplementor factory;

	private String constantExpression;
	private Object constantValue;
	private Type heuristicType;

	private Type expectedType;

	@Override
    public void setText(String s) {
		// for some reason the antlr.CommonAST initialization routines force
		// this method to get called twice.  The first time with an empty string
		if ( StringHelper.isNotEmpty( s ) ) {
			constantExpression = s;
			constantValue = ReflectHelper.getConstantValue( s );
			heuristicType = factory.getTypeResolver().heuristicType( constantValue.getClass().getName() );
			super.setText( s );
		}
	}

	public void setExpectedType(Type expectedType) {
		this.expectedType = expectedType;
	}

	public Type getExpectedType() {
		return expectedType;
	}

	public void setSessionFactory(SessionFactoryImplementor factory) {
		this.factory = factory;
	}

	@Override
    public String getRenderText(SessionFactoryImplementor sessionFactory) {
		Type type = expectedType == null
				? heuristicType
				: Number.class.isAssignableFrom( heuristicType.getReturnedClass() )
						? heuristicType
						: expectedType;
		try {
			LiteralType literalType = ( LiteralType ) type;
			Dialect dialect = factory.getDialect();
			return literalType.objectToSQLString( constantValue, dialect );
		}
		catch ( Throwable t ) {
			throw new QueryException( QueryTranslator.ERROR_CANNOT_FORMAT_LITERAL + constantExpression, t );
		}
	}
}
