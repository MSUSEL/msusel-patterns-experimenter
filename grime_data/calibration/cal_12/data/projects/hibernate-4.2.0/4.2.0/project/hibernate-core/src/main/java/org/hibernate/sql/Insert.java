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
package org.hibernate.sql;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.LiteralType;

/**
 * An SQL <tt>INSERT</tt> statement
 *
 * @author Gavin King
 */
public class Insert {
	private Dialect dialect;
	private String tableName;
	private String comment;
	private Map columns = new LinkedHashMap();

	public Insert(Dialect dialect) {
		this.dialect = dialect;
	}

	protected Dialect getDialect() {
		return dialect;
	}

	public Insert setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public Insert addColumn(String columnName) {
		return addColumn(columnName, "?");
	}

	public Insert addColumns(String[] columnNames) {
		for ( int i=0; i<columnNames.length; i++ ) {
			addColumn( columnNames[i] );
		}
		return this;
	}

	public Insert addColumns(String[] columnNames, boolean[] insertable) {
		for ( int i=0; i<columnNames.length; i++ ) {
			if ( insertable[i] ) {
				addColumn( columnNames[i] );
			}
		}
		return this;
	}

	public Insert addColumns(String[] columnNames, boolean[] insertable, String[] valueExpressions) {
		for ( int i=0; i<columnNames.length; i++ ) {
			if ( insertable[i] ) {
				addColumn( columnNames[i], valueExpressions[i] );
			}
		}
		return this;
	}

	public Insert addColumn(String columnName, String valueExpression) {
		columns.put(columnName, valueExpression);
		return this;
	}

	public Insert addColumn(String columnName, Object value, LiteralType type) throws Exception {
		return addColumn( columnName, type.objectToSQLString(value, dialect) );
	}

	public Insert addIdentityColumn(String columnName) {
		String value = dialect.getIdentityInsertString();
		if ( value != null ) {
			addColumn( columnName, value );
		}
		return this;
	}

	public Insert setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public String toStatementString() {
		StringBuilder buf = new StringBuilder( columns.size()*15 + tableName.length() + 10 );
		if ( comment != null ) {
			buf.append( "/* " ).append( comment ).append( " */ " );
		}
		buf.append("insert into ")
			.append(tableName);
		if ( columns.size()==0 ) {
			buf.append(' ').append( dialect.getNoColumnsInsertString() );
		}
		else {
			buf.append(" (");
			Iterator iter = columns.keySet().iterator();
			while ( iter.hasNext() ) {
				buf.append( iter.next() );
				if ( iter.hasNext() ) {
					buf.append( ", " );
				}
			}
			buf.append(") values (");
			iter = columns.values().iterator();
			while ( iter.hasNext() ) {
				buf.append( iter.next() );
				if ( iter.hasNext() ) {
					buf.append( ", " );
				}
			}
			buf.append(')');
		}
		return buf.toString();
	}
}
