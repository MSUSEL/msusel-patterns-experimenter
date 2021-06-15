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
package org.hibernate.internal.util.collections;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Set implementation that use == instead of equals() as its comparison
 * mechanism.  This is achieved by internally using an IdentityHashMap.
 *
 * @author Emmanuel Bernard
 */
public class IdentitySet implements Set {
	private static final Object DUMP_VALUE = new Object();

	private final IdentityHashMap map;

	/**
	 * Create an IdentitySet with default sizing.
	 */
	public IdentitySet() {
		this.map = new IdentityHashMap();
	}

	/**
	 * Create an IdentitySet with the given sizing.
	 *
	 * @param sizing The sizing of the set to create.
	 */
	public IdentitySet(int sizing) {
		this.map = new IdentityHashMap( sizing );
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean contains(Object o) {
		return map.get( o ) == DUMP_VALUE;
	}

	public Iterator iterator() {
		return map.keySet().iterator();
	}

	public Object[] toArray() {
		return map.keySet().toArray();
	}

	public Object[] toArray(Object[] a) {
		return map.keySet().toArray( a );
	}

	public boolean add(Object o) {
		return map.put( o, DUMP_VALUE ) == null;
	}

	public boolean remove(Object o) {
		return map.remove( o ) == DUMP_VALUE;
	}

	public boolean containsAll(Collection c) {
		Iterator it = c.iterator();
		while ( it.hasNext() ) {
			if ( !map.containsKey( it.next() ) ) {
				return false;
			}
		}
		return true;
	}

	public boolean addAll(Collection c) {
		Iterator it = c.iterator();
		boolean changed = false;
		while ( it.hasNext() ) {
			if ( this.add( it.next() ) ) {
				changed = true;
			}
		}
		return changed;
	}

	public boolean retainAll(Collection c) {
		//doable if needed
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection c) {
		Iterator it = c.iterator();
		boolean changed = false;
		while ( it.hasNext() ) {
			if ( this.remove( it.next() ) ) {
				changed = true;
			}
		}
		return changed;
	}

	public void clear() {
		map.clear();
	}
}
