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
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.TypedValue;

/**
 * A criterion representing a "like" expression
 *
 * @author Scott Marlow
 * @author Steve Ebersole
 */
public class LikeExpression implements Criterion {
	private final String propertyName;
	private final Object value;
	private final Character escapeChar;
	private final boolean ignoreCase;

	protected LikeExpression(
			String propertyName,
			String value,
			Character escapeChar,
			boolean ignoreCase) {
		this.propertyName = propertyName;
		this.value = value;
		this.escapeChar = escapeChar;
		this.ignoreCase = ignoreCase;
	}

	protected LikeExpression(
			String propertyName,
			String value) {
		this( propertyName, value, null, false );
	}

	protected LikeExpression(
			String propertyName,
			String value,
			MatchMode matchMode) {
		this( propertyName, matchMode.toMatchString( value ) );
	}

	protected LikeExpression(
			String propertyName,
			String value,
			MatchMode matchMode,
			Character escapeChar,
			boolean ignoreCase) {
		this( propertyName, matchMode.toMatchString( value ), escapeChar, ignoreCase );
	}

	public String toSqlString(
			Criteria criteria,
			CriteriaQuery criteriaQuery) throws HibernateException {
		Dialect dialect = criteriaQuery.getFactory().getDialect();
		String[] columns = criteriaQuery.findColumns(propertyName, criteria);
		if ( columns.length != 1 ) {
			throw new HibernateException( "Like may only be used with single-column properties" );
		}
		String escape = escapeChar == null ? "" : " escape \'" + escapeChar + "\'";
		String column = columns[0];
		if ( ignoreCase ) {
			if ( dialect.supportsCaseInsensitiveLike() ) {
				return column +" " + dialect.getCaseInsensitiveLike() + " ?" + escape;
			}
			else {
				return dialect.getLowercaseFunction() + '(' + column + ')' + " like ?" + escape;
			}
		}
		else {
			return column + " like ?" + escape;
		}
	}

	public TypedValue[] getTypedValues(
			Criteria criteria,
			CriteriaQuery criteriaQuery) throws HibernateException {
		return new TypedValue[] {
				criteriaQuery.getTypedValue( criteria, propertyName, ignoreCase ? value.toString().toLowerCase() : value.toString() )
		};
	}
}
