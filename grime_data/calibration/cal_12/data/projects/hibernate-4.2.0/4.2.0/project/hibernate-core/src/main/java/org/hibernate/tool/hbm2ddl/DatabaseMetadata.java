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
package org.hibernate.tool.hbm2ddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.spi.SQLExceptionConverter;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.Table;

/**
 * JDBC database metadata
 * @author Christoph Sturm, Teodor Danciu
 */
public class DatabaseMetadata {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, DatabaseMetaData.class.getName());

	private final Map tables = new HashMap();
	private final Set sequences = new HashSet();
	private final boolean extras;

	private DatabaseMetaData meta;
	private SQLExceptionConverter sqlExceptionConverter;

	public DatabaseMetadata(Connection connection, Dialect dialect) throws SQLException {
		this(connection, dialect, true);
	}

	public DatabaseMetadata(Connection connection, Dialect dialect, boolean extras) throws SQLException {
		sqlExceptionConverter = dialect.buildSQLExceptionConverter();
		meta = connection.getMetaData();
		this.extras = extras;
		initSequences(connection, dialect);
	}

	private static final String[] TYPES = {"TABLE", "VIEW"};

	public TableMetadata getTableMetadata(String name, String schema, String catalog, boolean isQuoted) throws HibernateException {

		Object identifier = identifier(catalog, schema, name);
		TableMetadata table = (TableMetadata) tables.get(identifier);
		if (table!=null) {
			return table;
		}
		else {

			try {
				ResultSet rs = null;
				try {
					if ( (isQuoted && meta.storesMixedCaseQuotedIdentifiers())) {
						rs = meta.getTables(catalog, schema, name, TYPES);
					} else if ( (isQuoted && meta.storesUpperCaseQuotedIdentifiers())
						|| (!isQuoted && meta.storesUpperCaseIdentifiers() )) {
						rs = meta.getTables(
								StringHelper.toUpperCase(catalog),
								StringHelper.toUpperCase(schema),
								StringHelper.toUpperCase(name),
								TYPES
							);
					}
					else if ( (isQuoted && meta.storesLowerCaseQuotedIdentifiers())
							|| (!isQuoted && meta.storesLowerCaseIdentifiers() )) {
						rs = meta.getTables( 
								StringHelper.toLowerCase( catalog ),
								StringHelper.toLowerCase(schema), 
								StringHelper.toLowerCase(name), 
								TYPES 
							);
					}
					else {
						rs = meta.getTables(catalog, schema, name, TYPES);
					}

					while ( rs.next() ) {
						String tableName = rs.getString("TABLE_NAME");
						if ( name.equalsIgnoreCase(tableName) ) {
							table = new TableMetadata(rs, meta, extras);
							tables.put(identifier, table);
							return table;
						}
					}

					LOG.tableNotFound( name );
					return null;

				}
				finally {
					rs.close();
				}
			}
			catch (SQLException sqlException) {
				throw new SqlExceptionHelper( sqlExceptionConverter )
						.convert( sqlException, "could not get table metadata: " + name );
			}
		}

	}

	private Object identifier(String catalog, String schema, String name) {
		return Table.qualify(catalog,schema,name);
	}

	private void initSequences(Connection connection, Dialect dialect) throws SQLException {
		if ( dialect.supportsSequences() ) {
			String sql = dialect.getQuerySequencesString();
			if (sql!=null) {

				Statement statement = null;
				ResultSet rs = null;
				try {
					statement = connection.createStatement();
					rs = statement.executeQuery(sql);

					while ( rs.next() ) {
						sequences.add( rs.getString(1).toLowerCase().trim() );
					}
				}
				finally {
					rs.close();
					statement.close();
				}

			}
		}
	}

	public boolean isSequence(Object key) {
		if (key instanceof String){
			String[] strings = StringHelper.split(".", (String) key);
			return sequences.contains( strings[strings.length-1].toLowerCase());
		}
		return false;
	}

 	public boolean isTable(Object key) throws HibernateException {
 		if(key instanceof String) {
			Table tbl = new Table((String)key);
			if ( getTableMetadata( tbl.getName(), tbl.getSchema(), tbl.getCatalog(), tbl.isQuoted() ) != null ) {
 				return true;
 			} else {
 				String[] strings = StringHelper.split(".", (String) key);
 				if(strings.length==3) {
					tbl = new Table(strings[2]);
					tbl.setCatalog(strings[0]);
					tbl.setSchema(strings[1]);
					return getTableMetadata( tbl.getName(), tbl.getSchema(), tbl.getCatalog(), tbl.isQuoted() ) != null;
 				} else if (strings.length==2) {
					tbl = new Table(strings[1]);
					tbl.setSchema(strings[0]);
					return getTableMetadata( tbl.getName(), tbl.getSchema(), tbl.getCatalog(), tbl.isQuoted() ) != null;
 				}
 			}
 		}
 		return false;
 	}

	@Override
    public String toString() {
		return "DatabaseMetadata" + tables.keySet().toString() + sequences.toString();
	}
}
