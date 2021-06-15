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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a named schema/catalog pair and manages objects defined within.
 *
 * @author Steve Ebersole
 */
public class Schema {
	private final Name name;
	private Map<String, InLineView> inLineViews = new HashMap<String, InLineView>();
	private Map<Identifier, Table> tables = new HashMap<Identifier, Table>();

	public Schema(Name name) {
		this.name = name;
	}

	public Schema(Identifier schema, Identifier catalog) {
		this( new Name( schema, catalog ) );
	}

	public Name getName() {
		return name;
	}

	public Table locateTable(Identifier name) {
		return tables.get( name );
	}

	public Table createTable(Identifier name) {
		Table table = new Table( this, name );
		tables.put( name, table );
		return table;
	}

	public Table locateOrCreateTable(Identifier name) {
		final Table existing = locateTable( name );
		if ( existing == null ) {
			return createTable( name );
		}
		return existing;
	}

	public Iterable<Table> getTables() {
		return tables.values();
	}

	public InLineView getInLineView(String logicalName) {
		return inLineViews.get( logicalName );
	}

	public InLineView createInLineView(String logicalName, String subSelect) {
		InLineView inLineView = new InLineView( this, logicalName, subSelect );
		inLineViews.put( logicalName, inLineView );
		return inLineView;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "Schema" );
		sb.append( "{name=" ).append( name );
		sb.append( '}' );
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Schema schema = (Schema) o;

		if ( name != null ? !name.equals( schema.name ) : schema.name != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	public static class Name {
		private final Identifier schema;
		private final Identifier catalog;

		public Name(Identifier schema, Identifier catalog) {
			this.schema = schema;
			this.catalog = catalog;
		}

		public Name(String schema, String catalog) {
			this( Identifier.toIdentifier( schema ), Identifier.toIdentifier( catalog ) );
		}

		public Identifier getSchema() {
			return schema;
		}

		public Identifier getCatalog() {
			return catalog;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append( "Name" );
			sb.append( "{schema=" ).append( schema );
			sb.append( ", catalog=" ).append( catalog );
			sb.append( '}' );
			return sb.toString();
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}

			Name name = (Name) o;

			if ( catalog != null ? !catalog.equals( name.catalog ) : name.catalog != null ) {
				return false;
			}
			if ( schema != null ? !schema.equals( name.schema ) : name.schema != null ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = schema != null ? schema.hashCode() : 0;
			result = 31 * result + ( catalog != null ? catalog.hashCode() : 0 );
			return result;
		}
	}
}
