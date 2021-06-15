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
package org.hibernate.criterion;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.type.Type;

/**
 * Base class for standard aggregation functions.
 *
 * @author max
 */
public class AggregateProjection extends SimpleProjection {
	protected final String propertyName;
	private final String functionName;
	
	protected AggregateProjection(String functionName, String propertyName) {
		this.functionName = functionName;
		this.propertyName = propertyName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String toString() {
		return functionName + "(" + propertyName + ')';
	}

	/**
	 * {@inheritDoc}
	 */
	public Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return new Type[] {
				getFunction( criteriaQuery ).getReturnType(
						criteriaQuery.getType( criteria, getPropertyName() ),
						criteriaQuery.getFactory()
				)
		};
	}

	/**
	 * {@inheritDoc}
	 */
	public String toSqlString(Criteria criteria, int loc, CriteriaQuery criteriaQuery) throws HibernateException {
		final String functionFragment = getFunction( criteriaQuery ).render(
				criteriaQuery.getType( criteria, getPropertyName() ),
				buildFunctionParameterList( criteria, criteriaQuery ),
				criteriaQuery.getFactory()
		);
		return functionFragment + " as y" + loc + '_';
	}

	protected SQLFunction getFunction(CriteriaQuery criteriaQuery) {
		return getFunction( getFunctionName(), criteriaQuery );
	}

	protected SQLFunction getFunction(String functionName, CriteriaQuery criteriaQuery) {
		SQLFunction function = criteriaQuery.getFactory()
				.getSqlFunctionRegistry()
				.findSQLFunction( functionName );
		if ( function == null ) {
			throw new HibernateException( "Unable to locate mapping for function named [" + functionName + "]" );
		}
		return function;
	}

	protected List buildFunctionParameterList(Criteria criteria, CriteriaQuery criteriaQuery) {
		return buildFunctionParameterList( criteriaQuery.getColumn( criteria, getPropertyName() ) );
	}

	protected List buildFunctionParameterList(String column) {
		return Collections.singletonList( column );
	}
}
