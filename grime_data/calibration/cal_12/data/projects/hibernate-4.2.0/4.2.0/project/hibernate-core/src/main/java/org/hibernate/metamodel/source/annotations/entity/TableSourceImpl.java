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
package org.hibernate.metamodel.source.annotations.entity;

import org.hibernate.metamodel.source.binder.TableSource;

class TableSourceImpl implements TableSource {
	private final String schema;
	private final String catalog;
	private final String tableName;
	private final String logicalName;

	TableSourceImpl(String schema, String catalog, String tableName, String logicalName) {
		this.schema = schema;
		this.catalog = catalog;
		this.tableName = tableName;
		this.logicalName = logicalName;
	}

	@Override
	public String getExplicitSchemaName() {
		return schema;
	}

	@Override
	public String getExplicitCatalogName() {
		return catalog;
	}

	@Override
	public String getExplicitTableName() {
		return tableName;
	}

	@Override
	public String getLogicalName() {
		return logicalName;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		TableSourceImpl that = (TableSourceImpl) o;

		if ( catalog != null ? !catalog.equals( that.catalog ) : that.catalog != null ) {
			return false;
		}
		if ( logicalName != null ? !logicalName.equals( that.logicalName ) : that.logicalName != null ) {
			return false;
		}
		if ( schema != null ? !schema.equals( that.schema ) : that.schema != null ) {
			return false;
		}
		if ( tableName != null ? !tableName.equals( that.tableName ) : that.tableName != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = schema != null ? schema.hashCode() : 0;
		result = 31 * result + ( catalog != null ? catalog.hashCode() : 0 );
		result = 31 * result + ( tableName != null ? tableName.hashCode() : 0 );
		result = 31 * result + ( logicalName != null ? logicalName.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "TableSourceImpl" );
		sb.append( "{schema='" ).append( schema ).append( '\'' );
		sb.append( ", catalog='" ).append( catalog ).append( '\'' );
		sb.append( ", tableName='" ).append( tableName ).append( '\'' );
		sb.append( ", logicalName='" ).append( logicalName ).append( '\'' );
		sb.append( '}' );
		return sb.toString();
	}
}


