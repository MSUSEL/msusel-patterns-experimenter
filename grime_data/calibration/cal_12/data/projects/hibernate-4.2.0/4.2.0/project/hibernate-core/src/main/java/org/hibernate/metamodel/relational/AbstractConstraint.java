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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.AssertionFailure;
import org.hibernate.dialect.Dialect;

/**
 * Support for writing {@link Constraint} implementations
 *
 * @todo do we need to support defining these on particular schemas/catalogs?
 *
 * @author Steve Ebersole
 * @author Gail Badner
 */
public abstract class AbstractConstraint implements Constraint {
	private final TableSpecification table;
	private final String name;
	private List<Column> columns = new ArrayList<Column>();

	protected AbstractConstraint(TableSpecification table, String name) {
		this.table = table;
		this.name = name;
	}

	public TableSpecification getTable() {
		return table;
	}

	public String getName() {
		return name;
	}

	public Iterable<Column> getColumns() {
		return columns;
	}

	protected int getColumnSpan() {
		return columns.size();
	}

	protected List<Column> internalColumnAccess() {
		return columns;
	}

	public void addColumn(Column column) {
		internalAddColumn( column );
	}

	protected void internalAddColumn(Column column) {
		if ( column.getTable() != getTable() ) {
			throw new AssertionFailure(
					String.format(
							"Unable to add column to constraint; tables [%s, %s] did not match",
							column.getTable().toLoggableString(),
							getTable().toLoggableString()
					)
			);
		}
		columns.add( column );
	}

	protected boolean isCreationVetoed(Dialect dialect) {
		return false;
	}

	protected abstract String sqlConstraintStringInAlterTable(Dialect dialect);

	public String[] sqlDropStrings(Dialect dialect) {
		if ( isCreationVetoed( dialect ) ) {
			return null;
		}
		else {
			return new String[] {
					new StringBuilder()
						.append( "alter table " )
						.append( getTable().getQualifiedName( dialect ) )
						.append( " drop constraint " )
						.append( dialect.quote( getName() ) )
						.toString()
			};
		}
	}

	public String[] sqlCreateStrings(Dialect dialect) {
		if ( isCreationVetoed( dialect ) ) {
			return null;
		}
		else {
			return new String[] {
					new StringBuilder( "alter table " )
							.append( getTable().getQualifiedName( dialect ) )
							.append( sqlConstraintStringInAlterTable( dialect ) )
							.toString()
			};
		}
	}
}
