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
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.type.CompositeType;
import org.hibernate.type.Type;

/**
 * Constrains the property to a specified list of values
 * @author Gavin King
 */
public class InExpression implements Criterion {

	private final String propertyName;
	private final Object[] values;

	protected InExpression(String propertyName, Object[] values) {
		this.propertyName = propertyName;
		this.values = values;
	}

    public String toSqlString( Criteria criteria, CriteriaQuery criteriaQuery )
            throws HibernateException {
        String[] columns = criteriaQuery.findColumns(propertyName, criteria);
        if ( criteriaQuery.getFactory().getDialect()
                .supportsRowValueConstructorSyntaxInInList() || columns.length<=1) {

            String singleValueParam = StringHelper.repeat( "?, ",
                    columns.length - 1 )
                    + "?";
            if ( columns.length > 1 )
                singleValueParam = '(' + singleValueParam + ')';
            String params = values.length > 0 ? StringHelper.repeat(
                    singleValueParam + ", ", values.length - 1 )
                    + singleValueParam : "";
            String cols = StringHelper.join( ", ", columns );
            if ( columns.length > 1 )
                cols = '(' + cols + ')';
            return cols + " in (" + params + ')';
        } else {
           String cols = " ( " + StringHelper.join( " = ? and ", columns ) + "= ? ) ";
             cols = values.length > 0 ? StringHelper.repeat( cols
                    + "or ", values.length - 1 )
                    + cols : "";
            cols = " ( " + cols + " ) ";
            return cols;
        }
    }

	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery)
	throws HibernateException {
		ArrayList list = new ArrayList();
		Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
		if ( type.isComponentType() ) {
			CompositeType actype = (CompositeType) type;
			Type[] types = actype.getSubtypes();
			for ( int j=0; j<values.length; j++ ) {
				for ( int i=0; i<types.length; i++ ) {
					Object subval = values[j]==null ? 
						null : 
						actype.getPropertyValues( values[j], EntityMode.POJO )[i];
					list.add( new TypedValue( types[i], subval, EntityMode.POJO ) );
				}
			}
		}
		else {
			for ( int j=0; j<values.length; j++ ) {
				list.add( new TypedValue( type, values[j], EntityMode.POJO ) );
			}
		}
		return (TypedValue[]) list.toArray( new TypedValue[ list.size() ] );
	}

	public String toString() {
		return propertyName + " in (" + StringHelper.toString(values) + ')';
	}

}
