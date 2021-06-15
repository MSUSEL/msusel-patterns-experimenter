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
package org.hibernate.collection.internal;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.BasicCollectionPersister;

/**
 * A persistent wrapper for a <tt>java.util.SortedMap</tt>. Underlying
 * collection is a <tt>TreeMap</tt>.
 *
 * @see java.util.TreeMap
 * @author <a href="mailto:doug.currie@alum.mit.edu">e</a>
 */
public class PersistentSortedMap extends PersistentMap implements SortedMap {

	protected Comparator comparator;

	protected Serializable snapshot(BasicCollectionPersister persister, EntityMode entityMode) throws HibernateException {
		TreeMap clonedMap = new TreeMap(comparator);
		Iterator iter = map.entrySet().iterator();
		while ( iter.hasNext() ) {
			Map.Entry e = (Map.Entry) iter.next();
			clonedMap.put( e.getKey(), persister.getElementType().deepCopy( e.getValue(), persister.getFactory() ) );
		}
		return clonedMap;
	}

	public PersistentSortedMap(SessionImplementor session) {
		super(session);
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public PersistentSortedMap(SessionImplementor session, SortedMap map) {
		super(session, map);
		comparator = map.comparator();
	}

	public PersistentSortedMap() {} //needed for SOAP libraries, etc

	/**
	 * @see PersistentSortedMap#comparator()
	 */
	public Comparator comparator() {
		return comparator;
	}

	/**
	 * @see PersistentSortedMap#subMap(Object, Object)
	 */
	public SortedMap subMap(Object fromKey, Object toKey) {
		read();
		SortedMap m = ( (SortedMap) map ).subMap(fromKey, toKey);
		return new SortedSubMap(m);
	}

	/**
	 * @see PersistentSortedMap#headMap(Object)
	 */
	public SortedMap headMap(Object toKey) {
		read();
		SortedMap m;
		m = ( (SortedMap) map ).headMap(toKey);
		return new SortedSubMap(m);
	}

	/**
	 * @see PersistentSortedMap#tailMap(Object)
	 */
	public SortedMap tailMap(Object fromKey) {
		read();
		SortedMap m;
		m = ( (SortedMap) map ).tailMap(fromKey);
		return new SortedSubMap(m);
	}

	/**
	 * @see PersistentSortedMap#firstKey()
	 */
	public Object firstKey() {
		read();
		return ( (SortedMap) map ).firstKey();
	}

	/**
	 * @see PersistentSortedMap#lastKey()
	 */
	public Object lastKey() {
		read();
		return ( (SortedMap) map ).lastKey();
	}

	class SortedSubMap implements SortedMap {

		SortedMap submap;

		SortedSubMap(SortedMap m) {
			this.submap = m;
		}
		// from Map
		public int size() {
			return submap.size();
		}
		public boolean isEmpty() {
			return submap.isEmpty();
		}
		public boolean containsKey(Object key) {
			return submap.containsKey(key);
		}
		public boolean containsValue(Object key) {
			return submap.containsValue(key) ;
		}
		public Object get(Object key) {
			return submap.get(key);
		}
		public Object put(Object key, Object value) {
			write();
			return submap.put(key,  value);
		}
		public Object remove(Object key) {
			write();
			return submap.remove(key);
		}
		public void putAll(Map other) {
			write();
			submap.putAll(other);
		}
		public void clear() {
			write();
			submap.clear();
		}
		public Set keySet() {
			return new SetProxy( submap.keySet() );
		}
		public Collection values() {
			return new SetProxy( submap.values() );
		}
		public Set entrySet() {
			return new EntrySetProxy( submap.entrySet() );
		}
		// from SortedMap
		public Comparator comparator() {
			return submap.comparator();
		}
		public SortedMap subMap(Object fromKey, Object toKey) {
			SortedMap m;
			m = submap.subMap(fromKey, toKey);
			return new SortedSubMap( m );
		}
		public SortedMap headMap(Object toKey) {
			SortedMap m;
			m = submap.headMap(toKey);
			return new SortedSubMap(m);
		}
		public SortedMap tailMap(Object fromKey) {
			SortedMap m;
			m = submap.tailMap(fromKey);
			return new SortedSubMap(m);
		}
		public Object firstKey() {
			return  submap.firstKey();
		}
		public Object lastKey() {
			return submap.lastKey();
		}

	}

}







