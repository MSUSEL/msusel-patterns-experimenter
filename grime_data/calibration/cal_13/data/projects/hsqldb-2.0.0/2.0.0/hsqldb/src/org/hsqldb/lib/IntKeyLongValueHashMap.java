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

import java.util.NoSuchElementException;

import org.hsqldb.store.BaseHashMap;

/**
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public class IntKeyLongValueHashMap extends BaseHashMap {

    public IntKeyLongValueHashMap() {
        this(8);
    }

    public IntKeyLongValueHashMap(int initialCapacity)
    throws IllegalArgumentException {
        super(initialCapacity, BaseHashMap.intKeyOrValue,
              BaseHashMap.longKeyOrValue, false);
    }

    public long get(int key) throws NoSuchElementException {

        int lookup = getLookup(key);

        if (lookup != -1) {
            return longValueTable[lookup];
        }

        throw new NoSuchElementException();
    }

    public long get(int key, int defaultValue) {

        int lookup = getLookup(key);

        if (lookup != -1) {
            return longValueTable[lookup];
        }

        return defaultValue;
    }

    public boolean get(int key, long[] value) {

        int lookup = getLookup(key);

        if (lookup != -1) {
            value[0] = longValueTable[lookup];

            return true;
        }

        return false;
    }

    public boolean put(int key, int value) {

        int oldSize = size();

        super.addOrRemove(key, value, null, null, false);

        return oldSize != size();
    }

    public boolean remove(int key) {

        int oldSize = size();

        super.addOrRemove(key, 0, null, null, true);

        return oldSize != size();
    }
}
