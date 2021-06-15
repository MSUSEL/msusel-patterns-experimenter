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
import java.io.Serializable;
import java.sql.Types;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NullPrecedence;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * Represents an order imposed upon a <tt>Criteria</tt> result set
 * 
 * @author Gavin King
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 * @author Brett Meyer
 */
public class Order implements Serializable {
	private boolean ascending;
	private boolean ignoreCase;
	private String propertyName;
	private NullPrecedence nullPrecedence;
	
	public String toString() {
		return propertyName + ' ' + ( ascending ? "asc" : "desc" ) + ( nullPrecedence != null ? ' ' + nullPrecedence.name().toLowerCase() : "" );
	}
	
	public Order ignoreCase() {
		ignoreCase = true;
		return this;
	}

	public Order nulls(NullPrecedence nullPrecedence) {
		this.nullPrecedence = nullPrecedence;
		return this;
	}

	/**
	 * Constructor for Order.
	 */
	protected Order(String propertyName, boolean ascending) {
		this.propertyName = propertyName;
		this.ascending = ascending;
	}

	/**
	 * Render the SQL fragment
	 *
	 */
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
		Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
		StringBuilder fragment = new StringBuilder();
		for ( int i=0; i<columns.length; i++ ) {
			final StringBuilder expression = new StringBuilder();
			SessionFactoryImplementor factory = criteriaQuery.getFactory();
			boolean lower = false;
			if ( ignoreCase ) {
				int sqlType = type.sqlTypes( factory )[i];
				lower = sqlType == Types.VARCHAR
						|| sqlType == Types.CHAR
						|| sqlType == Types.LONGVARCHAR;
			}
			
			if (lower) {
				expression.append( factory.getDialect().getLowercaseFunction() ).append('(');
			}
			expression.append( columns[i] );
			if (lower) expression.append(')');
			fragment.append(
					factory.getDialect()
							.renderOrderByElement(
									expression.toString(),
									null,
									ascending ? "asc" : "desc",
									nullPrecedence != null ? nullPrecedence : factory.getSettings().getDefaultNullPrecedence()
							)
			);
			if ( i<columns.length-1 ) fragment.append(", ");
		}
		return fragment.toString();
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public boolean isAscending() {
		return ascending;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * Ascending order
	 *
	 * @param propertyName
	 * @return Order
	 */
	public static Order asc(String propertyName) {
		return new Order(propertyName, true);
	}

	/**
	 * Descending order
	 *
	 * @param propertyName
	 * @return Order
	 */
	public static Order desc(String propertyName) {
		return new Order(propertyName, false);
	}

}
