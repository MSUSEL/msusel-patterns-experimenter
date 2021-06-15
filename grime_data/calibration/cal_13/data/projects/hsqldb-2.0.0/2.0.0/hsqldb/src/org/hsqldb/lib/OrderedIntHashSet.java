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
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class OrderedIntHashSet extends BaseHashMap {

    public OrderedIntHashSet() {
        this(8);
    }

    public OrderedIntHashSet(int initialCapacity)
    throws IllegalArgumentException {

        super(initialCapacity, BaseHashMap.intKeyOrValue,
              BaseHashMap.noKeyOrValue, false);

        isList = true;
    }

    public boolean contains(int key) {
        return super.containsKey(key);
    }

    public boolean add(int key) {

        int oldSize = size();

        super.addOrRemove(key, 0, null, null, false);

        return oldSize != size();
    }

    public boolean remove(int key) {

        int oldSize = size();

        super.addOrRemove(key, 0, null, null, true);

        boolean result = oldSize != size();

        if (result) {
            int[] array = toArray();

            super.clear();

            for (int i = 0; i < array.length; i++) {
                add(array[i]);
            }
        }

        return result;
    }

    public int get(int index) {

        checkRange(index);

        return intKeyTable[index];
    }

    public int getIndex(int value) {
        return getLookup(value);
    }

    public int getStartMatchCount(int[] array) {

        int i = 0;

        for (; i < array.length; i++) {
            if (!super.containsKey(array[i])) {
                break;
            }
        }

        return i;
    }

    public int getOrderedStartMatchCount(int[] array) {

        int i = 0;

        for (; i < array.length; i++) {
            if (i >= size() || get(i) != array[i]) {
                break;
            }
        }

        return i;
    }

    public boolean addAll(Collection col) {

        int      oldSize = size();
        Iterator it      = col.iterator();

        while (it.hasNext()) {
            add(it.nextInt());
        }

        return oldSize != size();
    }

    public int[] toArray() {

        int   lookup = -1;
        int[] array  = new int[size()];

        for (int i = 0; i < array.length; i++) {
            lookup = super.nextLookup(lookup);

            int value = intKeyTable[lookup];

            array[i] = value;
        }

        return array;
    }

    private void checkRange(int i) {

        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
    }
}
