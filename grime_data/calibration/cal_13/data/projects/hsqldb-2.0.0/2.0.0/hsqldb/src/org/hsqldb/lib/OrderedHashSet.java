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

/**
 * Implementation of an ordered Set which maintains the inserted order of
 * elements and allows access by index. Iterators return the
 * elements in the index order.
 *
 * This class does not store null elements.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class OrderedHashSet extends HashSet implements HsqlList, Set {

    public OrderedHashSet() {

        super(8);

        isList = true;
    }

    public boolean remove(Object key) {

        int oldSize = size();

        super.removeObject(key, true);

        return oldSize != size();
    }

    public Object remove(int index) throws IndexOutOfBoundsException {

        checkRange(index);

        Object result = objectKeyTable[index];

        remove(result);

        return result;
    }

    public boolean insert(int index,
                          Object key) throws IndexOutOfBoundsException {

        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        if (contains(key)) {
            return false;
        }

        if (index == size()) {
            return add(key);
        }

        Object[] set = toArray(new Object[size()]);

        super.clear();

        for (int i = 0; i < index; i++) {
            add(set[i]);
        }

        add(key);

        for (int i = index; i < set.length; i++) {
            add(set[i]);
        }

        return true;
    }

    public Object set(int index, Object key) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException();
    }

    public void add(int index, Object key) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException();
    }

    public Object get(int index) throws IndexOutOfBoundsException {

        checkRange(index);

        return objectKeyTable[index];
    }

    public int getIndex(Object key) {
        return getLookup(key, key.hashCode());
    }

    public int getLargestIndex(OrderedHashSet other) {

        int max = -1;

        for (int i = 0, size = other.size(); i < size; i++) {
            int index = getIndex(other.get(i));

            if (index > max) {
                max = index;
            }
        }

        return max;
    }

    public int getCommonElementCount(Set other) {

        int count = 0;

        for (int i = 0, size = size(); i < size; i++) {
            if (other.contains(objectKeyTable[i])) {
                count++;
            }
        }

        return count;
    }

    public static OrderedHashSet addAll(OrderedHashSet first,
                                          OrderedHashSet second) {

        if (second == null) {
            return first;
        }

        if (first == null) {
            first = new OrderedHashSet();
        }

        first.addAll(second);

        return first;
    }

    public static OrderedHashSet add(OrderedHashSet first,
                                          Object value) {

        if (value == null) {
            return first;
        }

        if (first == null) {
            first = new OrderedHashSet();
        }

        first.add(value);

        return first;
    }

    private void checkRange(int i) {

        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
    }
}
