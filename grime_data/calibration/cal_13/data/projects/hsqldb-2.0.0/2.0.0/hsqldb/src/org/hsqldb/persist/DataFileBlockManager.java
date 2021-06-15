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

package org.hsqldb.persist;

import org.hsqldb.lib.DoubleIntIndex;

/**
 * Maintains a list of free file blocks with fixed capacity.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.8.0
 * @since 1.8.0
 */
public class DataFileBlockManager {

    private DoubleIntIndex lookup;
    private final int      capacity;
    private int            midSize;
    private final int      scale;
    private long           releaseCount;
    private long           requestCount;
    private long           requestSize;

    // reporting vars
    long    lostFreeBlockSize;
    boolean isModified;

    /**
     *
     */
    public DataFileBlockManager(int capacity, int scale, long lostSize) {

        lookup = new DoubleIntIndex(capacity, true);

        lookup.setValuesSearchTarget();

        this.capacity          = capacity;
        this.scale             = scale;
        this.lostFreeBlockSize = lostSize;
        this.midSize           = 128;    // arbitrary initial value
    }

    /**
     */
    void add(int pos, int rowSize) {

        isModified = true;

        if (capacity == 0) {
            lostFreeBlockSize += rowSize;

            return;
        }

        releaseCount++;

        //
        if (lookup.size() == capacity) {
            resetList();
        }

        lookup.add(pos, rowSize);
    }

    /**
     * Returns the position of a free block or 0.
     */
    int get(int rowSize) {

        if (lookup.size() == 0) {
            return -1;
        }

        int index = lookup.findFirstGreaterEqualKeyIndex(rowSize);

        if (index == -1) {
            return -1;
        }

        // statistics for successful requests only - to be used later for midSize
        requestCount++;

        requestSize += rowSize;

        int length     = lookup.getValue(index);
        int difference = length - rowSize;
        int key        = lookup.getKey(index);

        lookup.remove(index);

        if (difference >= midSize) {
            int pos = key + (rowSize / scale);

            lookup.add(pos, difference);
        } else {
            lostFreeBlockSize += difference;
        }

        return key;
    }

    int size() {
        return lookup.size();
    }

    long getLostBlocksSize() {
        return lostFreeBlockSize;
    }

    boolean isModified() {
        return isModified;
    }

    void clear() {
        removeBlocks(lookup.size());
    }

    private void resetList() {

        if (requestCount != 0) {
            midSize = (int) (requestSize / requestCount);
        }

        int first = lookup.findFirstGreaterEqualSlotIndex(midSize);

        if (first < lookup.size() / 4) {
            first = lookup.size() / 4;
        }

        removeBlocks(first);
    }

    private void removeBlocks(int blocks) {

        for (int i = 0; i < blocks; i++) {
            lostFreeBlockSize += lookup.getValue(i);
        }

        lookup.removeRange(0, blocks);
    }

    private void checkIntegrity() throws NullPointerException {}
}
