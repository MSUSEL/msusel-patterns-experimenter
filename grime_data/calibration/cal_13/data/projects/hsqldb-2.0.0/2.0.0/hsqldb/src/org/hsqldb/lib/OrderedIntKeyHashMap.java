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
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderedIntKeyHashMap extends BaseHashMap {

    Set        keySet;
    Collection values;

    public OrderedIntKeyHashMap() {
        this(8);
    }

    public OrderedIntKeyHashMap(int initialCapacity)
    throws IllegalArgumentException {

        super(initialCapacity, BaseHashMap.intKeyOrValue,
              BaseHashMap.objectKeyOrValue, false);

        isList = true;
    }

    public Object get(int key) {

        int lookup = getLookup(key);

        if (lookup != -1) {
            return objectValueTable[lookup];
        }

        return null;
    }

    public Object put(int key, Object value) {
        return super.addOrRemove(key, value, null, false);
    }

    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    public Object remove(int key) {

        int lookup = getLookup(key, key);

        if (lookup < 0) {
            return null;
        }

        Object returnValue = super.addOrRemove(key, null, null, true);

        removeRow(lookup);

        return returnValue;
    }

    public boolean containsKey(int key) {
        return super.containsKey(key);
    }

    public void valuesToArray(Object[] array) {

        Iterator it = values().iterator();
        int      i  = 0;

        while (it.hasNext()) {
            array[i] = it.next();

            i++;
        }
    }

    public Set keySet() {

        if (keySet == null) {
            keySet = new KeySet();
        }

        return keySet;
    }

    public Collection values() {

        if (values == null) {
            values = new Values();
        }

        return values;
    }

    class KeySet implements Set {

        public Iterator iterator() {
            return OrderedIntKeyHashMap.this.new BaseHashIterator(true);
        }

        public int size() {
            return OrderedIntKeyHashMap.this.size();
        }

        public boolean contains(Object o) {
            throw new RuntimeException();
        }

        public Object get(Object key) {
            throw new RuntimeException();
        }

        public boolean add(Object value) {
            throw new RuntimeException();
        }

        public boolean addAll(Collection c) {
            throw new RuntimeException();
        }

        public boolean remove(Object o) {
            throw new RuntimeException();
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public void clear() {
            OrderedIntKeyHashMap.this.clear();
        }
    }

    class Values implements Collection {

        public Iterator iterator() {
            return OrderedIntKeyHashMap.this.new BaseHashIterator(false);
        }

        public int size() {
            return OrderedIntKeyHashMap.this.size();
        }

        public boolean contains(Object o) {
            throw new RuntimeException();
        }

        public boolean add(Object value) {
            throw new RuntimeException();
        }

        public boolean addAll(Collection c) {
            throw new RuntimeException();
        }

        public boolean remove(Object o) {
            throw new RuntimeException();
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public void clear() {
            OrderedIntKeyHashMap.this.clear();
        }
    }
}
