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
import org.hibernate.internal.util.StringHelper;
import org.hibernate.type.Type;

/**
 * A property value, or grouped property value
 * @author Gavin King
 */
public class PropertyProjection extends SimpleProjection {

	private String propertyName;
	private boolean grouped;
	
	protected PropertyProjection(String prop, boolean grouped) {
		this.propertyName = prop;
		this.grouped = grouped;
	}
	
	protected PropertyProjection(String prop) {
		this(prop, false);
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	public String toString() {
		return propertyName;
	}

	public Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		return new Type[] { criteriaQuery.getType(criteria, propertyName) };
	}

	public String toSqlString(Criteria criteria, int position, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		StringBuilder buf = new StringBuilder();
		String[] cols = criteriaQuery.getColumns( propertyName, criteria );
		for ( int i=0; i<cols.length; i++ ) {
			buf.append( cols[i] )
				.append(" as y")
				.append(position + i)
				.append('_');
			if (i < cols.length -1)
			   buf.append(", ");
		}
		return buf.toString();
	}

	public boolean isGrouped() {
		return grouped;
	}
	
	public String toGroupSqlString(Criteria criteria, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		if (!grouped) {
			return super.toGroupSqlString(criteria, criteriaQuery);
		}
		else {
			return StringHelper.join( ", ", criteriaQuery.getColumns( propertyName, criteria ) );
		}
	}

}
