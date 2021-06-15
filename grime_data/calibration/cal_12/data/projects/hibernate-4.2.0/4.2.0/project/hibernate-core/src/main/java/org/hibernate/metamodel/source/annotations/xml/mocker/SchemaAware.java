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
package org.hibernate.metamodel.source.annotations.xml.mocker;

import org.hibernate.internal.jaxb.mapping.orm.JaxbCollectionTable;
import org.hibernate.internal.jaxb.mapping.orm.JaxbJoinTable;
import org.hibernate.internal.jaxb.mapping.orm.JaxbSecondaryTable;
import org.hibernate.internal.jaxb.mapping.orm.JaxbTable;

/**
 * @author Strong Liu
 */
interface SchemaAware {
	String getSchema();

	void setSchema(String schema);

	String getCatalog();

	void setCatalog(String catalog);

	static class SecondaryTableSchemaAware implements SchemaAware {
		private JaxbSecondaryTable table;

		SecondaryTableSchemaAware(JaxbSecondaryTable table) {
			this.table = table;
		}

		@Override
		public String getCatalog() {
			return table.getCatalog();
		}

		@Override
		public String getSchema() {
			return table.getSchema();
		}

		@Override
		public void setSchema(String schema) {
			table.setSchema( schema );
		}

		@Override
		public void setCatalog(String catalog) {
			table.setCatalog( catalog );
		}
	}

	static class TableSchemaAware implements SchemaAware {
		private JaxbTable table;

		public TableSchemaAware(JaxbTable table) {
			this.table = table;
		}

		@Override
		public String getCatalog() {
			return table.getCatalog();
		}

		@Override
		public String getSchema() {
			return table.getSchema();
		}

		@Override
		public void setSchema(String schema) {
			table.setSchema( schema );
		}

		@Override
		public void setCatalog(String catalog) {
			table.setCatalog( catalog );
		}
	}

	static class JoinTableSchemaAware implements SchemaAware {
		private JaxbJoinTable table;

		public JoinTableSchemaAware(JaxbJoinTable table) {
			this.table = table;
		}

		@Override
		public String getCatalog() {
			return table.getCatalog();
		}

		@Override
		public String getSchema() {
			return table.getSchema();
		}

		@Override
		public void setSchema(String schema) {
			table.setSchema( schema );
		}

		@Override
		public void setCatalog(String catalog) {
			table.setCatalog( catalog );
		}
	}

	static class CollectionTableSchemaAware implements SchemaAware {
		private JaxbCollectionTable table;

		public CollectionTableSchemaAware(JaxbCollectionTable table) {
			this.table = table;
		}

		@Override
		public String getCatalog() {
			return table.getCatalog();
		}

		@Override
		public String getSchema() {
			return table.getSchema();
		}

		@Override
		public void setSchema(String schema) {
			table.setSchema( schema );
		}

		@Override
		public void setCatalog(String catalog) {
			table.setCatalog( catalog );
		}
	}
}
