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
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeMap;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.BasicCollectionPersister;

/**
 * A persistent wrapper for a <tt>java.util.SortedSet</tt>. Underlying
 * collection is a <tt>TreeSet</tt>.
 *
 * @see java.util.TreeSet
 * @author <a href="mailto:doug.currie@alum.mit.edu">e</a>
 */
public class PersistentSortedSet extends PersistentSet implements SortedSet {

	protected Comparator comparator;

	protected Serializable snapshot(BasicCollectionPersister persister, EntityMode entityMode) 
	throws HibernateException {
		//if (set==null) return new Set(session);
		TreeMap clonedSet = new TreeMap(comparator);
		Iterator iter = set.iterator();
		while ( iter.hasNext() ) {
			Object copy = persister.getElementType().deepCopy( iter.next(), persister.getFactory() );
			clonedSet.put(copy, copy);
		}
		return clonedSet;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public PersistentSortedSet(SessionImplementor session) {
		super(session);
	}

	public PersistentSortedSet(SessionImplementor session, SortedSet set) {
		super(session, set);
		comparator = set.comparator();
	}

	public PersistentSortedSet() {} //needed for SOAP libraries, etc

	/**
	 * @see PersistentSortedSet#comparator()
	 */
	public Comparator comparator() {
		return comparator;
	}

	/**
	 * @see PersistentSortedSet#subSet(Object,Object)
	 */
	public SortedSet subSet(Object fromElement, Object toElement) {
		read();
		SortedSet s;
		s = ( (SortedSet) set ).subSet(fromElement, toElement);
		return new SubSetProxy(s);
	}

	/**
	 * @see PersistentSortedSet#headSet(Object)
	 */
	public SortedSet headSet(Object toElement) {
		read();
		SortedSet s = ( (SortedSet) set ).headSet(toElement);
		return new SubSetProxy(s);
	}

	/**
	 * @see PersistentSortedSet#tailSet(Object)
	 */
	public SortedSet tailSet(Object fromElement) {
		read();
		SortedSet s = ( (SortedSet) set ).tailSet(fromElement);
		return new SubSetProxy(s);
	}

	/**
	 * @see PersistentSortedSet#first()
	 */
	public Object first() {
		read();
		return ( (SortedSet) set ).first();
	}

	/**
	 * @see PersistentSortedSet#last()
	 */
	public Object last() {
		read();
		return ( (SortedSet) set ).last();
	}

	/** wrapper for subSets to propagate write to its backing set */
	class SubSetProxy extends SetProxy implements SortedSet {

		SubSetProxy(SortedSet s) {
			super(s);
		}

		public Comparator comparator() {
			return ( (SortedSet) this.set ).comparator();
		}

		public Object first() {
			return ( (SortedSet) this.set ).first();
		}

		public SortedSet headSet(Object toValue) {
			return new SubSetProxy( ( (SortedSet) this.set ).headSet(toValue) );
		}

		public Object last() {
			return ( (SortedSet) this.set ).last();
		}

		public SortedSet subSet(Object fromValue, Object toValue) {
			return new SubSetProxy( ( (SortedSet) this.set ).subSet(fromValue, toValue) );
		}

		public SortedSet tailSet(Object fromValue) {
			return new SubSetProxy( ( (SortedSet) this.set ).tailSet(fromValue) );
		}

	}

}







