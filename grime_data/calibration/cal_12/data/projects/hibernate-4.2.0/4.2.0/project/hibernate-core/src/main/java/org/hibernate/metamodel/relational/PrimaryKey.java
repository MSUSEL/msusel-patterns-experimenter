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
package org.hibernate.metamodel.relational;

import org.hibernate.dialect.Dialect;

/**
 * Models a table's primary key.
 * <p/>
 * NOTE : This need not be a physical primary key; we just mean a column or columns which uniquely identify rows in
 * the table.  Of course it is recommended to define proper integrity constraints, including primary keys.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class PrimaryKey extends AbstractConstraint implements Constraint, Exportable {
	// IMPL NOTE : I override the name behavior here because:
	//		(1) primary keys are not required to be named.
	//		(2) because a primary key is required for each table, it is easier to allow setting the constraint name
	// 			later in terms of building the metamodel
	//
	// todo : default name?  {TABLE_NAME}_PK maybe?
	private String name;

	protected PrimaryKey(TableSpecification table) {
		super( table, null );
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getExportIdentifier() {
		return getTable().getLoggableValueQualifier() + ".PK";
	}

	public String sqlConstraintStringInCreateTable(Dialect dialect) {
		StringBuilder buf = new StringBuilder("primary key (");
		boolean first = true;
		for ( Column column : getColumns() ) {
			if ( first ) {
				first = false;
			}
			else {
				buf.append(", ");
			}
			buf.append( column.getColumnName().encloseInQuotesIfQuoted( dialect ) );
		}
		return buf.append(')').toString();
	}

	public String sqlConstraintStringInAlterTable(Dialect dialect) {
		StringBuilder buf = new StringBuilder(
			dialect.getAddPrimaryKeyConstraintString( getName() )
		).append('(');
		boolean first = true;
		for ( Column column : getColumns() ) {
			if ( first ) {
				first = false;
			}
			else {
				buf.append(", ");
			}
			buf.append( column.getColumnName().encloseInQuotesIfQuoted( dialect ) );
		}
		return buf.append(')').toString();
	}

}
