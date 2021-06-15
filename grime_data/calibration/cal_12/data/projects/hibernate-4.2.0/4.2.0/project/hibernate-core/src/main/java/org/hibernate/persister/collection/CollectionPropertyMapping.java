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
package org.hibernate.persister.collection;

import org.hibernate.QueryException;
import org.hibernate.persister.entity.PropertyMapping;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * @author Gavin King
 */
public class CollectionPropertyMapping implements PropertyMapping {

	private final QueryableCollection memberPersister;

	public CollectionPropertyMapping(QueryableCollection memberPersister) {
		this.memberPersister = memberPersister;
	}

	public Type toType(String propertyName) throws QueryException {
		if ( propertyName.equals(CollectionPropertyNames.COLLECTION_ELEMENTS) ) {
			return memberPersister.getElementType();
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_INDICES) ) {
			if ( !memberPersister.hasIndex() ) throw new QueryException("unindexed collection before indices()");
			return memberPersister.getIndexType();
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_SIZE) ) {
			return StandardBasicTypes.INTEGER;
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MAX_INDEX) ) {
			return memberPersister.getIndexType();
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MIN_INDEX) ) {
			return memberPersister.getIndexType();
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MAX_ELEMENT) ) {
			return memberPersister.getElementType();
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MIN_ELEMENT) ) {
			return memberPersister.getElementType();
		}
		else {
			//return memberPersister.getPropertyType(propertyName);
			throw new QueryException("illegal syntax near collection: " + propertyName);
		}
	}

	public String[] toColumns(String alias, String propertyName) throws QueryException {
		if ( propertyName.equals(CollectionPropertyNames.COLLECTION_ELEMENTS) ) {
			return memberPersister.getElementColumnNames(alias);
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_INDICES) ) {
			if ( !memberPersister.hasIndex() ) throw new QueryException("unindexed collection in indices()");
			return memberPersister.getIndexColumnNames(alias);
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_SIZE) ) {
			String[] cols = memberPersister.getKeyColumnNames();
			return new String[] { "count(" + alias + '.' + cols[0] + ')' };
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MAX_INDEX) ) {
			if ( !memberPersister.hasIndex() ) throw new QueryException("unindexed collection in maxIndex()");
			String[] cols = memberPersister.getIndexColumnNames(alias);
			if ( cols.length!=1 ) throw new QueryException("composite collection index in maxIndex()");
			return new String[] { "max(" + cols[0] + ')' };
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MIN_INDEX) ) {
			if ( !memberPersister.hasIndex() ) throw new QueryException("unindexed collection in minIndex()");
			String[] cols = memberPersister.getIndexColumnNames(alias);
			if ( cols.length!=1 ) throw new QueryException("composite collection index in minIndex()");
			return new String[] { "min(" + cols[0] + ')' };
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MAX_ELEMENT) ) {
			String[] cols = memberPersister.getElementColumnNames(alias);
			if ( cols.length!=1 ) throw new QueryException("composite collection element in maxElement()");
			return new String[] { "max(" + cols[0] + ')' };
		}
		else if ( propertyName.equals(CollectionPropertyNames.COLLECTION_MIN_ELEMENT) ) {
			String[] cols = memberPersister.getElementColumnNames(alias);
			if ( cols.length!=1 ) throw new QueryException("composite collection element in minElement()");
			return new String[] { "min(" + cols[0] + ')' };
		}
		else {
			//return memberPersister.toColumns(alias, propertyName);
			throw new QueryException("illegal syntax near collection: " + propertyName);
		}
	}

	/**
	 * Given a property path, return the corresponding column name(s).
	 */
	public String[] toColumns(String propertyName) throws QueryException, UnsupportedOperationException {
		throw new UnsupportedOperationException( "References to collections must be define a SQL alias" );
	}

	public Type getType() {
		//return memberPersister.getType();
		return memberPersister.getCollectionType();
	}

}
