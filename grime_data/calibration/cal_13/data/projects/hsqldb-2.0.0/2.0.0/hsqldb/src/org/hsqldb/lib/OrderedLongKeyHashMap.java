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
 * @version 1.9.0
 * @since 1.9.0
 */
public class OrderedLongKeyHashMap extends BaseHashMap {

    Set        keySet;
    Collection values;

    public OrderedLongKeyHashMap() {
        this(8);
    }

    public OrderedLongKeyHashMap(int initialCapacity)
    throws IllegalArgumentException {

        super(initialCapacity, BaseHashMap.longKeyOrValue,
              BaseHashMap.objectKeyOrValue, false);

        isList = true;
    }

    public OrderedLongKeyHashMap(int initialCapacity,
                                boolean hasThirdValue)
                                throws IllegalArgumentException {

        super(initialCapacity, BaseHashMap.longKeyOrValue,
              BaseHashMap.objectKeyOrValue, false);

        objectKeyTable   = new Object[objectValueTable.length];
        isTwoObjectValue = true;
        isList           = true;

        if (hasThirdValue) {
            objectValueTable2 = new Object[objectValueTable.length];
        }
    }

    public Object get(long key) {

        int lookup = getLookup(key);

        if (lookup != -1) {
            return objectValueTable[lookup];
        }

        return null;
    }

    public Object getValueByIndex(int index) {
        return objectValueTable[index];
    }

    public Object getSecondValueByIndex(int index) {
        return objectKeyTable[index];
    }

    public Object getThirdValueByIndex(int index) {
        return objectValueTable2[index];
    }

    public Object setThirdValueByIndex(int index, Object value) {

        Object oldValue = objectValueTable2[index];

        objectValueTable2[index] = value;

        return oldValue;
    }

    public Object put(long key, Object value) {
        return super.addOrRemove(key, value, null, false);
    }

    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    public Object remove(long key) {
        return super.addOrRemove(key, null, null, false);
    }

    public boolean containsKey(long key) {
        return super.containsKey(key);
    }

    /* methods for two object lookups */
    public Object put(long key, Object valueOne, Object valueTwo) {
        return super.addOrRemove(key, valueOne, valueTwo, false);
    }

    public int getLookup(long key) {
        return super.getLookup(key);
    }

    public Object getFirstByLookup(int lookup) {

        if (lookup == -1) {
            return null;
        }

        return objectValueTable[lookup];
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
            return OrderedLongKeyHashMap.this.new BaseHashIterator(true);
        }

        public int size() {
            return OrderedLongKeyHashMap.this.size();
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
            OrderedLongKeyHashMap.this.clear();
        }
    }

    class Values implements Collection {

        public Iterator iterator() {
            return OrderedLongKeyHashMap.this.new BaseHashIterator(false);
        }

        public int size() {
            return OrderedLongKeyHashMap.this.size();
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
            OrderedLongKeyHashMap.this.clear();
        }
    }
}
