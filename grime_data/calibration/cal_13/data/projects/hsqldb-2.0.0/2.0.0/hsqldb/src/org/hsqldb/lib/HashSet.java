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

package org.hsqldb.lib;

import org.hsqldb.store.BaseHashMap;

/**
 * This class does not store null keys.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public class HashSet extends BaseHashMap implements Set {

    public HashSet() {
        this(8);
    }

    public HashSet(int initialCapacity) throws IllegalArgumentException {
        super(initialCapacity, BaseHashMap.objectKeyOrValue,
              BaseHashMap.noKeyOrValue, false);
    }

    public boolean contains(Object key) {
        return super.containsKey(key);
    }

    public boolean containsAll(Collection col) {

        Iterator it = col.iterator();

        while (it.hasNext()) {
            if (contains(it.next())) {
                continue;
            }

            return false;
        }

        return true;
    }

    public Object get(Object key) {

        int lookup = getLookup(key, key.hashCode());

        if (lookup < 0) {
            return null;
        } else {
            return objectKeyTable[lookup];
        }
    }

    /** returns true if added */
    public boolean add(Object key) {

        int oldSize = size();

        super.addOrRemove(0, 0, key, null, false);

        return oldSize != size();
    }

    /** returns true if any added */
    public boolean addAll(Collection c) {

        boolean  changed = false;
        Iterator it      = c.iterator();

        while (it.hasNext()) {
            changed |= add(it.next());
        }

        return changed;
    }

    /** returns true if any added */
    public boolean addAll(Object[] keys) {

        boolean changed = false;

        for (int i = 0; i < keys.length; i++) {
            changed |= add(keys[i]);
        }

        return changed;
    }

    /** returns true if any added */
    public boolean addAll(Object[] keys, int start, int limit) {

        boolean changed = false;

        for (int i = start; i < keys.length && i < limit; i++) {
            changed |= add(keys[i]);
        }

        return changed;
    }

    /** returns true if removed */
    public boolean remove(Object key) {

        int oldSize = size();

        return super.removeObject(key, false) != null;
    }

    /** returns true if all were removed */
    public boolean removeAll(Collection c) {

        Iterator it     = c.iterator();
        boolean  result = true;

        while (it.hasNext()) {
            result &= remove(it.next());
        }

        return result;
    }

    /** returns true if all were removed */
    public boolean removeAll(Object[] keys) {

        boolean result = true;

        for (int i = 0; i < keys.length; i++) {
            result &= remove(keys[i]);
        }

        return result;
    }

    public Object[] toArray(Object[] a) {

        if (a == null || a.length < size()) {
            a = new Object[size()];
        }

        Iterator it = iterator();

        for (int i = 0; it.hasNext(); i++) {
            a[i] = it.next();
        }

        return a;
    }

    public Iterator iterator() {
        return new BaseHashIterator(true);
    }

    /**
     * Returns a String like "[Drei, zwei, Eins]", exactly like
     * java.util.HashSet.
     */
    public String toString() {

        Iterator     it = iterator();
        StringBuffer sb = new StringBuffer();

        while (it.hasNext()) {
            if (sb.length() > 0) {
                sb.append(", ");
            } else {
                sb.append('[');
            }

            sb.append(it.next());
        }

        return sb.toString() + ']';
    }
}
