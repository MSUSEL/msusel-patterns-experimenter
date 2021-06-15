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
package org.hibernate.mapping;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.internal.util.StringHelper;

/**
 * A relational table index
 *
 * @author Gavin King
 */
public class Index implements RelationalModel, Serializable {

	private Table table;
	private List columns = new ArrayList();
	private String name;

	public String sqlCreateString(Dialect dialect, Mapping mapping, String defaultCatalog, String defaultSchema)
			throws HibernateException {
		return buildSqlCreateIndexString(
				dialect,
				getName(),
				getTable(),
				getColumnIterator(),
				false,
				defaultCatalog,
				defaultSchema
		);
	}

	public static String buildSqlDropIndexString(
			Dialect dialect,
			Table table,
			String name,
			String defaultCatalog,
			String defaultSchema
	) {
		return "drop index " +
				StringHelper.qualify(
						table.getQualifiedName( dialect, defaultCatalog, defaultSchema ),
						name
				);
	}

	public static String buildSqlCreateIndexString(
			Dialect dialect,
			String name,
			Table table,
			Iterator columns,
			boolean unique,
			String defaultCatalog,
			String defaultSchema
	) {
		StringBuilder buf = new StringBuilder( "create" )
				.append( unique ?
						" unique" :
						"" )
				.append( " index " )
				.append( dialect.qualifyIndexName() ?
						name :
						StringHelper.unqualify( name ) )
				.append( " on " )
				.append( table.getQualifiedName( dialect, defaultCatalog, defaultSchema ) )
				.append( " (" );
		Iterator iter = columns;
		while ( iter.hasNext() ) {
			buf.append( ( (Column) iter.next() ).getQuotedName( dialect ) );
			if ( iter.hasNext() ) buf.append( ", " );
		}
		buf.append( ")" );
		return buf.toString();
	}


	// Used only in Table for sqlCreateString (but commented out at the moment)
	public String sqlConstraintString(Dialect dialect) {
		StringBuilder buf = new StringBuilder( " index (" );
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			buf.append( ( (Column) iter.next() ).getQuotedName( dialect ) );
			if ( iter.hasNext() ) buf.append( ", " );
		}
		return buf.append( ')' ).toString();
	}

	public String sqlDropString(Dialect dialect, String defaultCatalog, String defaultSchema) {
		return "drop index " +
				StringHelper.qualify(
						table.getQualifiedName( dialect, defaultCatalog, defaultSchema ),
						name
				);
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public int getColumnSpan() {
		return columns.size();
	}

	public Iterator getColumnIterator() {
		return columns.iterator();
	}

	public void addColumn(Column column) {
		if ( !columns.contains( column ) ) columns.add( column );
	}

	public void addColumns(Iterator extraColumns) {
		while ( extraColumns.hasNext() ) addColumn( (Column) extraColumns.next() );
	}

	/**
	 * @param column
	 * @return true if this constraint already contains a column with same name.
	 */
	public boolean containsColumn(Column column) {
		return columns.contains( column );
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getClass().getName() + "(" + getName() + ")";
	}
}
