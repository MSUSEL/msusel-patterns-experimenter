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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.util.StringHelper;

/**
 * superclass for comparisons between two properties (with SQL binary operators)
 * @author Gavin King
 */
public class PropertyExpression implements Criterion {

	private final String propertyName;
	private final String otherPropertyName;
	private final String op;

	private static final TypedValue[] NO_TYPED_VALUES = new TypedValue[0];

	protected PropertyExpression(String propertyName, String otherPropertyName, String op) {
		this.propertyName = propertyName;
		this.otherPropertyName = otherPropertyName;
		this.op = op;
	}

	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		String[] xcols = criteriaQuery.findColumns(propertyName, criteria);
		String[] ycols = criteriaQuery.findColumns(otherPropertyName, criteria);
		String result = StringHelper.join(
			" and ",
			StringHelper.add( xcols, getOp(), ycols )
		);
		if (xcols.length>1) result = '(' + result + ')';
		return result;
		//TODO: get SQL rendering out of this package!
	}

	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		return NO_TYPED_VALUES;
	}

	public String toString() {
		return propertyName + getOp() + otherPropertyName;
	}

	public String getOp() {
		return op;
	}

}
