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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.collection.internal.PersistentMap;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;


public class MapType extends CollectionType {

	/**
	 * @deprecated Use {@link #MapType(TypeFactory.TypeScope, String, String ) } instead.
	 * See Jira issue: <a href="https://hibernate.onjira.com/browse/HHH-7771">HHH-7771</a>
	 */
	@Deprecated
	public MapType(TypeFactory.TypeScope typeScope, String role, String propertyRef, boolean isEmbeddedInXML) {
		super( typeScope, role, propertyRef, isEmbeddedInXML );
	}

	public MapType(TypeFactory.TypeScope typeScope, String role, String propertyRef) {
		super( typeScope, role, propertyRef );
	}

	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key) {
		return new PersistentMap(session);
	}

	public Class getReturnedClass() {
		return Map.class;
	}

	public Iterator getElementsIterator(Object collection) {
		return ( (java.util.Map) collection ).values().iterator();
	}

	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new PersistentMap( session, (java.util.Map) collection );
	}
	
	public Object instantiate(int anticipatedSize) {
		return anticipatedSize <= 0 
		       ? new HashMap()
		       : new HashMap( anticipatedSize + (int)( anticipatedSize * .75f ), .75f );
	}

	public Object replaceElements(
		final Object original,
		final Object target,
		final Object owner, 
		final java.util.Map copyCache, 
		final SessionImplementor session)
		throws HibernateException {

		CollectionPersister cp = session.getFactory().getCollectionPersister( getRole() );
		
		java.util.Map result = (java.util.Map) target;
		result.clear();
		
		Iterator iter = ( (java.util.Map) original ).entrySet().iterator();
		while ( iter.hasNext() ) {
			java.util.Map.Entry me = (java.util.Map.Entry) iter.next();
			Object key = cp.getIndexType().replace( me.getKey(), null, session, owner, copyCache );
			Object value = cp.getElementType().replace( me.getValue(), null, session, owner, copyCache );
			result.put(key, value);
		}
		
		return result;
		
	}
	
	public Object indexOf(Object collection, Object element) {
		Iterator iter = ( (Map) collection ).entrySet().iterator();
		while ( iter.hasNext() ) {
			Map.Entry me = (Map.Entry) iter.next();
			//TODO: proxies!
			if ( me.getValue()==element ) return me.getKey();
		}
		return null;
	}

}
