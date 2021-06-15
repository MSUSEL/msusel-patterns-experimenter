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

package org.hsqldb.store;

import org.hsqldb.lib.Collection;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.Set;

/**
 * Maps integer keys to Objects. Hashes the keys.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.8.0
 */
public final class ObjectCacheHashMap extends BaseHashMap {

    Set        keySet;
    Collection values;

    public ObjectCacheHashMap(int initialCapacity)
    throws IllegalArgumentException {
        super(initialCapacity, BaseHashMap.intKeyOrValue,
              BaseHashMap.objectKeyOrValue, true);
    }

    public Object get(int key) {

        if (accessCount > ACCESS_MAX) {
            resetAccessCount();
        }

        int lookup = getLookup(key);

        if (lookup == -1) {
            return null;
        }

        accessTable[lookup] = accessCount++;

        return objectValueTable[lookup];
    }

    public Object put(int key, Object value) {

        if (accessCount > ACCESS_MAX) {
            resetAccessCount();
        }

        return super.addOrRemove(key, value, null, false);
    }

    public Object remove(int key) {
        return super.addOrRemove(key, null, null, true);
    }

    /**
     * for count number of elements with the given margin, return the access
     * count.
     */
    public int getAccessCountCeiling(int count, int margin) {
        return super.getAccessCountCeiling(count, margin);
    }

    /**
     * This is called after all elements below count accessCount have been
     * removed
     */
    public void setAccessCountFloor(int count) {
        super.accessMin = count;
    }

    public ObjectCacheIterator iterator() {
        return new ObjectCacheIterator();
    }

    public final class ObjectCacheIterator extends BaseHashIterator
    implements Iterator {}
}
