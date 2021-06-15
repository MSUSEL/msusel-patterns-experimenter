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
import java.util.Iterator;

import org.hibernate.MappingException;
import org.hibernate.cfg.Mappings;
import org.hibernate.engine.spi.Mapping;

/**
 * Indexed collections include Lists, Maps, arrays and
 * primitive arrays.
 * @author Gavin King
 */
public abstract class IndexedCollection extends Collection {

	public static final String DEFAULT_INDEX_COLUMN_NAME = "idx";

	private Value index;
	private String indexNodeName;

	public IndexedCollection(Mappings mappings, PersistentClass owner) {
		super( mappings, owner );
	}

	public Value getIndex() {
		return index;
	}
	public void setIndex(Value index) {
		this.index = index;
	}
	public final boolean isIndexed() {
		return true;
	}

	void createPrimaryKey() {
		if ( !isOneToMany() ) {
			PrimaryKey pk = new PrimaryKey();
			pk.addColumns( getKey().getColumnIterator() );
			
			// index should be last column listed
			boolean isFormula = false;
			Iterator iter = getIndex().getColumnIterator();
			while ( iter.hasNext() ) {
				if ( ( (Selectable) iter.next() ).isFormula() ) isFormula=true;
			}
			if (isFormula) {
				//if it is a formula index, use the element columns in the PK
				pk.addColumns( getElement().getColumnIterator() );
			}
			else {
				pk.addColumns( getIndex().getColumnIterator() ); 
			}
			getCollectionTable().setPrimaryKey(pk);
		}
		else {
			// don't create a unique key, 'cos some
			// databases don't like a UK on nullable
			// columns
			/*ArrayList list = new ArrayList();
			list.addAll( getKey().getConstraintColumns() );
			list.addAll( getIndex().getConstraintColumns() );
			getCollectionTable().createUniqueKey(list);*/
		}
	}

	public void validate(Mapping mapping) throws MappingException {
		super.validate(mapping);
		if ( !getIndex().isValid(mapping) ) {
			throw new MappingException(
				"collection index mapping has wrong number of columns: " +
				getRole() +
				" type: " +
				getIndex().getType().getName()
			);
		}
		if ( indexNodeName!=null && !indexNodeName.startsWith("@") ) {
			throw new MappingException("index node must be an attribute: " + indexNodeName );
		}
	}
	
	public boolean isList() {
		return false;
	}

	public String getIndexNodeName() {
		return indexNodeName;
	}

	public void setIndexNodeName(String indexNodeName) {
		this.indexNodeName = indexNodeName;
	}
	

}
