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
package org.hibernate.type;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeMap;

import org.hibernate.collection.internal.PersistentSortedMap;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;


public class SortedMapType extends MapType {

	private final Comparator comparator;

	/**
	 * @deprecated Use {@link #SortedMapType(org.hibernate.type.TypeFactory.TypeScope, String, String, java.util.Comparator)}
	 * instead.
	 * See Jira issue: <a href="https://hibernate.onjira.com/browse/HHH-7771">HHH-7771</a>
	 */
	@Deprecated
	public SortedMapType(TypeFactory.TypeScope typeScope, String role, String propertyRef, Comparator comparator, boolean isEmbeddedInXML) {
		super( typeScope, role, propertyRef, isEmbeddedInXML );
		this.comparator = comparator;
	}

	public SortedMapType(TypeFactory.TypeScope typeScope, String role, String propertyRef, Comparator comparator) {
		super( typeScope, role, propertyRef );
		this.comparator = comparator;
	}

	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key) {
		PersistentSortedMap map = new PersistentSortedMap(session);
		map.setComparator(comparator);
		return map;
	}

	public Class getReturnedClass() {
		return java.util.SortedMap.class;
	}

	@SuppressWarnings( {"unchecked"})
	public Object instantiate(int anticipatedSize) {
		return new TreeMap(comparator);
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new PersistentSortedMap( session, (java.util.SortedMap) collection );
	}

}






