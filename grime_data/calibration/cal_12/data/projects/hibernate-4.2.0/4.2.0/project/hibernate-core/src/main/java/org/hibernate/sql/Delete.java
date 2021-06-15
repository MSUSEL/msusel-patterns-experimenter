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

/**
 * An SQL <tt>DELETE</tt> statement
 *
 * @author Gavin King
 */
public class Delete {

	private String tableName;
	private String versionColumnName;
	private String where;

	private Map primaryKeyColumns = new LinkedHashMap();	
	
	private String comment;
	public Delete setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public Delete setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public String toStatementString() {
		StringBuilder buf = new StringBuilder( tableName.length() + 10 );
		if ( comment!=null ) {
			buf.append( "/* " ).append(comment).append( " */ " );
		}
		buf.append( "delete from " ).append(tableName);
		if ( where != null || !primaryKeyColumns.isEmpty() || versionColumnName != null ) {
			buf.append( " where " );
		}
		boolean conditionsAppended = false;
		Iterator iter = primaryKeyColumns.entrySet().iterator();
		while ( iter.hasNext() ) {
			Map.Entry e = (Map.Entry) iter.next();
			buf.append( e.getKey() ).append( '=' ).append( e.getValue() );
			if ( iter.hasNext() ) {
				buf.append( " and " );
			}
			conditionsAppended = true;
		}
		if ( where!=null ) {
			if ( conditionsAppended ) {
				buf.append( " and " );
			}
			buf.append( where );
			conditionsAppended = true;
		}
		if ( versionColumnName!=null ) {
			if ( conditionsAppended ) {
				buf.append( " and " );
			}
			buf.append( versionColumnName ).append( "=?" );
		}
		return buf.toString();
	}

	public Delete setWhere(String where) {
		this.where=where;
		return this;
	}

	public Delete addWhereFragment(String fragment) {
		if ( where == null ) {
			where = fragment;
		}
		else {
			where += ( " and " + fragment );
		}
		return this;
	}

	public Delete setPrimaryKeyColumnNames(String[] columnNames) {
		this.primaryKeyColumns.clear();
		addPrimaryKeyColumns(columnNames);
		return this;
	}	

	public Delete addPrimaryKeyColumns(String[] columnNames) {
		for ( int i=0; i<columnNames.length; i++ ) {
			addPrimaryKeyColumn( columnNames[i], "?" );
		}
		return this;
	}
	
	public Delete addPrimaryKeyColumns(String[] columnNames, boolean[] includeColumns, String[] valueExpressions) {
		for ( int i=0; i<columnNames.length; i++ ) {
			if( includeColumns[i] ) addPrimaryKeyColumn( columnNames[i], valueExpressions[i] );
		}
		return this;
	}
	
	public Delete addPrimaryKeyColumns(String[] columnNames, String[] valueExpressions) {
		for ( int i=0; i<columnNames.length; i++ ) {
			addPrimaryKeyColumn( columnNames[i], valueExpressions[i] );
		}
		return this;
	}	

	public Delete addPrimaryKeyColumn(String columnName, String valueExpression) {
		this.primaryKeyColumns.put(columnName, valueExpression);
		return this;
	}

	public Delete setVersionColumnName(String versionColumnName) {
		this.versionColumnName = versionColumnName;
		return this;
	}

}
