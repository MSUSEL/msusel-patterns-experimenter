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

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.relational.state.ColumnRelationalState;

/**
 * Models a physical column
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class Column extends AbstractSimpleValue {
	private final Identifier columnName;
	private boolean nullable;
	private boolean unique;

	private String defaultValue;
	private String checkCondition;
	private String sqlType;

	private String readFragment;
	private String writeFragment;

	private String comment;

	private Size size = new Size();

	protected Column(TableSpecification table, int position, String name) {
		this( table, position, Identifier.toIdentifier( name ) );
	}

	protected Column(TableSpecification table, int position, Identifier name) {
		super( table, position );
		this.columnName = name;
	}

	public void initialize(ColumnRelationalState state, boolean forceNonNullable, boolean forceUnique) {
		size.initialize( state.getSize() );
		nullable = ! forceNonNullable &&  state.isNullable();
		unique = ! forceUnique && state.isUnique();
		checkCondition = state.getCheckCondition();
		defaultValue = state.getDefault();
		sqlType = state.getSqlType();

		// TODO: this should go into binding instead (I think???)
		writeFragment = state.getCustomWriteFragment();
		readFragment = state.getCustomReadFragment();
		comment = state.getComment();
		for ( String uniqueKey : state.getUniqueKeys() ) {
			getTable().getOrCreateUniqueKey( uniqueKey ).addColumn( this );
		}
		for ( String index : state.getIndexes() ) {
			getTable().getOrCreateIndex( index ).addColumn( this );
		}
	}

	public Identifier getColumnName() {
		return columnName;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getCheckCondition() {
		return checkCondition;
	}

	public void setCheckCondition(String checkCondition) {
		this.checkCondition = checkCondition;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getReadFragment() {
		return readFragment;
	}

	public void setReadFragment(String readFragment) {
		this.readFragment = readFragment;
	}

	public String getWriteFragment() {
		return writeFragment;
	}

	public void setWriteFragment(String writeFragment) {
		this.writeFragment = writeFragment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	@Override
	public String toLoggableString() {
		return getTable().getLoggableValueQualifier() + '.' + getColumnName();
	}

	@Override
	public String getAlias(Dialect dialect) {
		String alias = columnName.getName();
		int lastLetter = StringHelper.lastIndexOfLetter( columnName.getName() );
		if ( lastLetter == -1 ) {
			alias = "column";
		}
		boolean useRawName =
				columnName.getName().equals( alias ) &&
						alias.length() <= dialect.getMaxAliasLength() &&
						! columnName.isQuoted() &&
						! columnName.getName().toLowerCase().equals( "rowid" );
		if ( ! useRawName ) {
			String unique =
					new StringBuilder()
					.append( getPosition() )
					.append( '_' )
					.append( getTable().getTableNumber() )
					.append( '_' )
					.toString();
			if ( unique.length() >= dialect.getMaxAliasLength() ) {
				throw new MappingException(
						"Unique suffix [" + unique + "] length must be less than maximum [" + dialect.getMaxAliasLength() + "]"
				);
			}
			if ( alias.length() + unique.length() > dialect.getMaxAliasLength()) {
				alias = alias.substring( 0, dialect.getMaxAliasLength() - unique.length() );
			}
			alias = alias + unique;
		}
		return alias;
	}
}
