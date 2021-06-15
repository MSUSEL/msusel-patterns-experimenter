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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;

/**
 * JDBC foreign key metadata
 *
 * @author Christoph Sturm
 */
public class ForeignKeyMetadata {
	private final String name;
	private final String refTable;
	private final Map references = new HashMap();

	ForeignKeyMetadata(ResultSet rs) throws SQLException {
		name = rs.getString("FK_NAME");
		refTable = rs.getString("PKTABLE_NAME");
	}

	public String getName() {
		return name;
	}

	public String getReferencedTableName() {
		return refTable;
	}

	void addReference(ResultSet rs) throws SQLException {
		references.put( rs.getString("FKCOLUMN_NAME").toLowerCase(), rs.getString("PKCOLUMN_NAME") );
	}

	private boolean hasReference(Column column, Column ref) {
		String refName = (String) references.get(column.getName().toLowerCase());
		return ref.getName().equalsIgnoreCase(refName);
	}

	public boolean matches(ForeignKey fk) {
		if ( refTable.equalsIgnoreCase( fk.getReferencedTable().getName() ) ) {
			if ( fk.getColumnSpan() == references.size() ) {
				List fkRefs;
				if ( fk.isReferenceToPrimaryKey() ) {
					fkRefs = fk.getReferencedTable().getPrimaryKey().getColumns();
				}
				else {
					fkRefs = fk.getReferencedColumns();
				}
				for ( int i = 0; i < fk.getColumnSpan(); i++ ) {
					Column column = fk.getColumn( i );
					Column ref = ( Column ) fkRefs.get( i );
					if ( !hasReference( column, ref ) ) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return "ForeignKeyMetadata(" + name + ')';
	}
}






