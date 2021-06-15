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
package org.hibernate.mapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.util.collections.JoinedIterator;

/**
 * @author Gavin King
 */
public class DenormalizedTable extends Table {
	
	private final Table includedTable;
	
	public DenormalizedTable(Table includedTable) {
		this.includedTable = includedTable;
		includedTable.setHasDenormalizedTables();
	}
	
	@Override
    public void createForeignKeys() {
		includedTable.createForeignKeys();
		Iterator iter = includedTable.getForeignKeyIterator();
		while ( iter.hasNext() ) {
			ForeignKey fk = (ForeignKey) iter.next();
			createForeignKey( 
					fk.getName() + Integer.toHexString( getName().hashCode() ), 
					fk.getColumns(), 
					fk.getReferencedEntityName() 
				);
		}
	}

	@Override
    public Column getColumn(Column column) {
		Column superColumn = super.getColumn( column );
		if (superColumn != null) {
			return superColumn;
		}
		else {
			return includedTable.getColumn( column );
		}
	}

	@Override
    public Iterator getColumnIterator() {
		return new JoinedIterator(
				includedTable.getColumnIterator(),
				super.getColumnIterator()
			);
	}

	@Override
    public boolean containsColumn(Column column) {
		return super.containsColumn(column) || includedTable.containsColumn(column);
	}

	@Override
    public PrimaryKey getPrimaryKey() {
		return includedTable.getPrimaryKey();
	}

	@Override
    public Iterator getUniqueKeyIterator() {
		//wierd implementation because of hacky behavior
		//of Table.sqlCreateString() which modifies the
		//list of unique keys by side-effect on some
		//dialects
		Map uks = new HashMap();
		uks.putAll( getUniqueKeys() );
		uks.putAll( includedTable.getUniqueKeys() );
		return uks.values().iterator();
	}

	@Override
    public Iterator getIndexIterator() {
		List indexes = new ArrayList();
		Iterator iter = includedTable.getIndexIterator();
		while ( iter.hasNext() ) {
			Index parentIndex = (Index) iter.next();
			Index index = new Index();
			index.setName( getName() + parentIndex.getName() );
			index.setTable(this);
			index.addColumns( parentIndex.getColumnIterator() );
			indexes.add( index );
		}
		return new JoinedIterator(
				indexes.iterator(),
				super.getIndexIterator()
			);
	}

}
