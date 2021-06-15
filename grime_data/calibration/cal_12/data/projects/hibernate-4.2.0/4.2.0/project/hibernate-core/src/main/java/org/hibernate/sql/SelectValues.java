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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jboss.logging.Logger;

import org.hibernate.dialect.Dialect;

/**
 * Models a SELECT values lists.  Eventually, rather than Strings, pass in the Column/Formula representations (something
 * like {@link org.hibernate.sql.ordering.antlr.ColumnReference}/{@link org.hibernate.sql.ordering.antlr.FormulaReference}
 *
 * @author Steve Ebersole
 */
public class SelectValues {
	private static final Logger log = Logger.getLogger( SelectValues.class );

	private static class SelectValue {
		private final String qualifier;
		private final String value;
		private final String alias;

		private SelectValue(String qualifier, String value, String alias) {
			this.qualifier = qualifier;
			this.value = value;
			this.alias = alias;
		}
	}

	private final Dialect dialect;
	private final ArrayList<SelectValue> selectValueList = new ArrayList<SelectValue>();

	public SelectValues(Dialect dialect) {
		this.dialect = dialect;
	}

	public SelectValues addColumns(String qualifier, String[] columnNames, String[] columnAliases) {
		for ( int i = 0; i < columnNames.length; i++ ) {
			if ( columnNames[i] != null ) {
				addColumn( qualifier, columnNames[i], columnAliases[i] );
			}
		}
		return this;
	}

	public SelectValues addColumn(String qualifier, String columnName, String columnAlias) {
		selectValueList.add( new SelectValue( qualifier, columnName, columnAlias ) );
		return this;
	}

	public SelectValues addParameter(int jdbcTypeCode, int length) {
		final String selectExpression = dialect.requiresCastingOfParametersInSelectClause()
				? dialect.cast( "?", jdbcTypeCode, length )
				: "?";
		selectValueList.add( new SelectValue( null, selectExpression, null ) );
		return this;
	}

	public SelectValues addParameter(int jdbcTypeCode, int precision, int scale) {
		final String selectExpression = dialect.requiresCastingOfParametersInSelectClause()
				? dialect.cast( "?", jdbcTypeCode, precision, scale )
				: "?";
		selectValueList.add( new SelectValue( null, selectExpression, null ) );
		return this;
	}

	public String render() {
		final StringBuilder buf = new StringBuilder( selectValueList.size() * 10 );
		final HashSet<String> uniqueAliases = new HashSet<String>();
		boolean firstExpression = true;
		for ( SelectValue selectValue : selectValueList ) {
			if ( selectValue.alias != null ) {
				if ( ! uniqueAliases.add( selectValue.alias ) ) {
					log.debug( "Skipping select-value with non-unique alias" );
					continue;
				}
			}

			if ( firstExpression ) {
				firstExpression = false;
			}
			else {
				buf.append( ", " );
			}

			if ( selectValue.qualifier != null ) {
				buf.append( selectValue.qualifier ).append( '.' );
			}
			buf.append( selectValue.value );
			if ( selectValue.alias != null ) {
				buf.append( " as " ).append( selectValue.alias );
			}
		}
		return buf.toString();
	}
}
