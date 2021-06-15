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
import java.util.Iterator;
import java.util.List;

import org.jboss.logging.Logger;

import org.hibernate.AssertionFailure;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;

/**
 * Models the notion of a foreign key.
 * <p/>
 * Note that this need not mean a physical foreign key; we just mean a relationship between 2 table
 * specifications.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class ForeignKey extends AbstractConstraint implements Constraint, Exportable {
    private static final Logger LOG = Logger.getLogger( ForeignKey.class );

	private static final String ON_DELETE = " on delete ";
	private static final String ON_UPDATE = " on update ";

	private final TableSpecification targetTable;
	private List<Column> targetColumns;

	private ReferentialAction deleteRule = ReferentialAction.NO_ACTION;
	private ReferentialAction updateRule = ReferentialAction.NO_ACTION;

	protected ForeignKey(TableSpecification sourceTable, TableSpecification targetTable, String name) {
		super( sourceTable, name );
		this.targetTable = targetTable;
	}

	protected ForeignKey(TableSpecification sourceTable, TableSpecification targetTable) {
		this( sourceTable, targetTable, null );
	}

	public TableSpecification getSourceTable() {
		return getTable();
	}

	public TableSpecification getTargetTable() {
		return targetTable;
	}

	public Iterable<Column> getSourceColumns() {
		return getColumns();
	}

	public Iterable<Column> getTargetColumns() {
		return targetColumns == null
				? getTargetTable().getPrimaryKey().getColumns()
				: targetColumns;
	}

	@Override
	public void addColumn(Column column) {
		addColumnMapping( column, null );
	}

	public void addColumnMapping(Column sourceColumn, Column targetColumn) {
		if ( targetColumn == null ) {
			if ( targetColumns != null ) {
				LOG.debugf(
						"Attempt to map column [%s] to no target column after explicit target column(s) named for FK [name=%s]",
						sourceColumn.toLoggableString(),
						getName()
				);
			}
		}
		else {
			checkTargetTable( targetColumn );
			if ( targetColumns == null ) {
				if (!internalColumnAccess().isEmpty()) {
					LOG.debugf(
							"Value mapping mismatch as part of FK [table=%s, name=%s] while adding source column [%s]",
							getTable().toLoggableString(),
							getName(),
							sourceColumn.toLoggableString()
					);
				}
				targetColumns = new ArrayList<Column>();
			}
			targetColumns.add( targetColumn );
		}
		internalAddColumn( sourceColumn );
	}

	private void checkTargetTable(Column targetColumn) {
		if ( targetColumn.getTable() != getTargetTable() ) {
			throw new AssertionFailure(
					String.format(
							"Unable to add column to constraint; tables [%s, %s] did not match",
							targetColumn.getTable().toLoggableString(),
							getTargetTable().toLoggableString()
					)
			);
		}
	}

	@Override
	public String getExportIdentifier() {
		return getSourceTable().getLoggableValueQualifier() + ".FK-" + getName();
	}

	public ReferentialAction getDeleteRule() {
		return deleteRule;
	}

	public void setDeleteRule(ReferentialAction deleteRule) {
		this.deleteRule = deleteRule;
	}

	public ReferentialAction getUpdateRule() {
		return updateRule;
	}

	public void setUpdateRule(ReferentialAction updateRule) {
		this.updateRule = updateRule;
	}

	@Override
	public String[] sqlDropStrings(Dialect dialect) {
		return new String[] {
				"alter table " +
						getTable().getQualifiedName( dialect ) +
						dialect.getDropForeignKeyString() +
						getName()
		};
	}

	public String sqlConstraintStringInAlterTable(Dialect dialect) {
		String[] columnNames = new String[ getColumnSpan() ];
		String[] targetColumnNames = new String[ getColumnSpan() ];
		int i=0;
		Iterator<Column> itTargetColumn = getTargetColumns().iterator();
		for ( Column column : getColumns() ) {
			if ( ! itTargetColumn.hasNext() ) {
				throw new MappingException( "More constraint columns that foreign key target columns." );
			}
			columnNames[i] = column.getColumnName().encloseInQuotesIfQuoted( dialect );
			targetColumnNames[i] = ( itTargetColumn.next() ).getColumnName().encloseInQuotesIfQuoted( dialect );
			i++;
		}
		if ( itTargetColumn.hasNext() ) {
			throw new MappingException( "More foreign key target columns than constraint columns." );
		}
		StringBuilder sb =
				new StringBuilder(
						dialect.getAddForeignKeyConstraintString(
								getName(),
								columnNames,
								targetTable.getQualifiedName( dialect ),
								targetColumnNames,
								this.targetColumns == null
						)
				);
		// TODO: If a dialect does not support cascade-delete, can it support other actions? (HHH-6428)
		// For now, assume not.
		if ( dialect.supportsCascadeDelete() ) {
			if ( deleteRule != ReferentialAction.NO_ACTION ) {
				sb.append( ON_DELETE ).append( deleteRule.getActionString() );
			}
			if ( updateRule != ReferentialAction.NO_ACTION ) {
				sb.append( ON_UPDATE ).append( updateRule.getActionString() );
			}
		}
		return sb.toString();
	}

	public static enum ReferentialAction {
		NO_ACTION( "no action" ),
		CASCADE( "cascade" ),
		SET_NULL( "set null" ),
		SET_DEFAULT( "set default" ),
		RESTRICT( "restrict" );

		private final String actionString;

		private ReferentialAction(String actionString) {
			this.actionString = actionString;
		}

		public String getActionString() {
			return actionString;
		}
	}
}
