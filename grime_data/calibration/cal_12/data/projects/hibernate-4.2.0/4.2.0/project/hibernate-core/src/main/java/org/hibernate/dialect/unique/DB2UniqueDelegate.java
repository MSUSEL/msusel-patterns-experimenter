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
package org.hibernate.dialect.unique;

import java.util.Iterator;

import org.hibernate.dialect.Dialect;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.Index;
import org.hibernate.metamodel.relational.UniqueKey;

/**
 * DB2 does not allow unique constraints on nullable columns.  Rather than
 * forcing "not null", use unique *indexes* instead.
 * 
 * @author Brett Meyer
 */
public class DB2UniqueDelegate extends DefaultUniqueDelegate {
	
	public DB2UniqueDelegate( Dialect dialect ) {
		super( dialect );
	}
	
	@Override
	public String applyUniquesOnAlter( org.hibernate.mapping.UniqueKey uniqueKey,
			String defaultCatalog, String defaultSchema ) {
		if ( hasNullable( uniqueKey ) ) {
			return org.hibernate.mapping.Index.buildSqlCreateIndexString(
					dialect, uniqueKey.getName(), uniqueKey.getTable(),
					uniqueKey.columnIterator(), true, defaultCatalog,
					defaultSchema );
		} else {
			return super.applyUniquesOnAlter(
					uniqueKey, defaultCatalog, defaultSchema );
		}
	}
	
	@Override
	public String applyUniquesOnAlter( UniqueKey uniqueKey ) {
		if ( hasNullable( uniqueKey ) ) {
			return Index.buildSqlCreateIndexString(
					dialect, uniqueKey.getName(), uniqueKey.getTable(),
					uniqueKey.getColumns(), true );
		} else {
			return super.applyUniquesOnAlter( uniqueKey );
		}
	}
	
	@Override
	public String dropUniquesOnAlter( org.hibernate.mapping.UniqueKey uniqueKey,
			String defaultCatalog, String defaultSchema ) {
		if ( hasNullable( uniqueKey ) ) {
			return org.hibernate.mapping.Index.buildSqlDropIndexString(
					dialect, uniqueKey.getTable(), uniqueKey.getName(),
					defaultCatalog, defaultSchema );
		} else {
			return super.dropUniquesOnAlter(
					uniqueKey, defaultCatalog, defaultSchema );
		}
	}
	
	@Override
	public String dropUniquesOnAlter( UniqueKey uniqueKey ) {
		if ( hasNullable( uniqueKey ) ) {
			return Index.buildSqlDropIndexString(
					dialect, uniqueKey.getTable(), uniqueKey.getName() );
		} else {
			return super.dropUniquesOnAlter( uniqueKey );
		}
	}
	
	private boolean hasNullable( org.hibernate.mapping.UniqueKey uniqueKey ) {
		Iterator iter = uniqueKey.getColumnIterator();
		while ( iter.hasNext() ) {
			if ( ( ( org.hibernate.mapping.Column ) iter.next() ).isNullable() ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasNullable( UniqueKey uniqueKey ) {
		Iterator iter = uniqueKey.getColumns().iterator();
		while ( iter.hasNext() ) {
			if ( ( ( Column ) iter.next() ).isNullable() ) {
				return true;
			}
		}
		return false;
	}
}
