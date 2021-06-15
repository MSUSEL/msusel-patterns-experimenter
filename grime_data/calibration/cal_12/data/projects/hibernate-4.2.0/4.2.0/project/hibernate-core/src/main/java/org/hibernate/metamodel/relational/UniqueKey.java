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
import org.hibernate.internal.util.StringHelper;

/**
 * Models a SQL <tt>INDEX</tt> defined as UNIQUE
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class UniqueKey extends AbstractConstraint implements Constraint {
	protected UniqueKey(Table table, String name) {
		super( table, name );
	}

	@Override
	public String getExportIdentifier() {
		StringBuilder sb = new StringBuilder( getTable().getLoggableValueQualifier() );
		sb.append( ".UK" );
		for ( Column column : getColumns() ) {
			sb.append( '_' ).append( column.getColumnName().getName() );
		}
		return sb.toString();
	}

	@Override
	public String[] sqlCreateStrings(Dialect dialect) {
		String s = dialect.getUniqueDelegate().applyUniquesOnAlter(this);
		return StringHelper.toArrayElement( s );
	}

	@Override
	public String[] sqlDropStrings(Dialect dialect) {
		String s = dialect.getUniqueDelegate().dropUniquesOnAlter(this);
		return StringHelper.toArrayElement( s );
	}

	@Override
    protected String sqlConstraintStringInAlterTable(Dialect dialect) {
		// not used
		return "";
	}
}
