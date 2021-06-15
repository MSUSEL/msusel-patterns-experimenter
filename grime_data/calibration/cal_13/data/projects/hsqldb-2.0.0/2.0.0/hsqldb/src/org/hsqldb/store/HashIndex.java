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

/**
 * A chained bucket hash index implementation.
 *
 * hashTable and linkTable are arrays of signed integral types. This
 * implementation uses int as the type but short or byte can be used for
 * smaller index sizes (cardinality).
 *
 * hashTable[index] contains the pointer to the first node with
 * (index == hash modulo hashTable.length) or -1 if there is no corresponding
 * node. linkTable[{0,newNodePointer}] (the range between 0 and newNodePointer)
 * contains either the pointer to the next node or -1 if there is no
 * such node. reclaimedNodeIndex contains a pointer to an element
 * of linkTable which is the first element in the list of reclaimed nodes
 * (nodes no longer in index) or -1 if there is no such node.
 *
 * elemenet at and above linkTable[newNodePointer] have never been used
 * as a node and their contents is not significant.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
class HashIndex {

    int[]   hashTable;
    int[]   linkTable;
    int     newNodePointer;
    int     elementCount;
    int     reclaimedNodePointer = -1;
    boolean fixedSize;
    boolean modified;

    HashIndex(int hashTableSize, int capacity, boolean fixedSize) {

        if (capacity < hashTableSize) {
            capacity = hashTableSize;
        }

        reset(hashTableSize, capacity);

        this.fixedSize = fixedSize;
    }

    /**
     * Reset the structure with a new size as empty.
     *
     * @param hashTableSize
     * @param capacity
     */
    void reset(int hashTableSize, int capacity) {

        int[] newHT = new int[hashTableSize];
        int[] newLT = new int[capacity];

        // allocate memory before assigning
        hashTable = newHT;
        linkTable = newLT;

        resetTables();
    }

    void resetTables() {

        int   to       = hashTable.length;
        int[] intArray = hashTable;

        while (--to >= 0) {
            intArray[to] = -1;
        }

        newNodePointer       = 0;
        elementCount         = 0;
        reclaimedNodePointer = -1;
        modified             = false;
    }

    /**
     * Reset the index as empty.
     */
    void clear() {

        int   to       = linkTable.length;
        int[] intArray = linkTable;

        while (--to >= 0) {
            intArray[to] = 0;
        }

        resetTables();
    }

    /**
     * @param hash
     */
    int getHashIndex(int hash) {
        return (hash & 0x7fffffff) % hashTable.length;
    }

    /**
     * Return the array index for a hash.
     *
     * @param hash the hash value used for indexing
     * @return either -1 or the first node for this hash value
     */
    int getLookup(int hash) {

        if (elementCount == 0) {
            return -1;
        }

        int index = (hash & 0x7fffffff) % hashTable.length;

        return hashTable[index];
    }

    /**
     * This looks from a given node, so the parameter is always > -1.
     *
     * @param lookup A valid node to look from
     * @return either -1 or the next node from this node
     */
    int getNextLookup(int lookup) {
        return linkTable[lookup];
    }

    /**
     * Link a new node to the end of the linked for a hash index.
     *
     * @param index an index into hashTable
     * @param lastLookup either -1 or the node to which the new node will be linked
     * @return the new node
     */
    int linkNode(int index, int lastLookup) {

        // get the first reclaimed slot
        int lookup = reclaimedNodePointer;

        if (lookup == -1) {
            lookup = newNodePointer++;
        } else {

            // reset the first reclaimed slot
            reclaimedNodePointer = linkTable[lookup];
        }

        // link the node
        if (lastLookup == -1) {
            hashTable[index] = lookup;
        } else {
            linkTable[lastLookup] = lookup;
        }

        linkTable[lookup] = -1;

        elementCount++;

        modified = true;

        return lookup;
    }

    /**
     * Unlink a node from a linked list and link into the reclaimed list.
     *
     * @param index an index into hashTable
     * @param lastLookup either -1 or the node to which the target node is linked
     * @param lookup the node to remove
     */
    void unlinkNode(int index, int lastLookup, int lookup) {

        // unlink the node
        if (lastLookup == -1) {
            hashTable[index] = linkTable[lookup];
        } else {
            linkTable[lastLookup] = linkTable[lookup];
        }

        // add to reclaimed list
        linkTable[lookup]    = reclaimedNodePointer;
        reclaimedNodePointer = lookup;

        elementCount--;
    }

    /**
     * Remove a node that has already been unlinked. This is not required
     * for index operations. It is used only when the row needs to be removed
     * from the data structures that store the actual indexed data and the
     * nodes need to be contiguous.
     *
     * @param lookup the node to remove
     * @return true if node found in unlinked state
     */
    boolean removeEmptyNode(int lookup) {

        boolean found      = false;
        int     lastLookup = -1;

        for (int i = reclaimedNodePointer; i >= 0;
                lastLookup = i, i = linkTable[i]) {
            if (i == lookup) {
                if (lastLookup == -1) {
                    reclaimedNodePointer = linkTable[lookup];
                } else {
                    linkTable[lastLookup] = linkTable[lookup];
                }

                found = true;

                break;
            }
        }

        if (!found) {
            return false;
        }

        for (int i = 0; i < newNodePointer; i++) {
            if (linkTable[i] > lookup) {
                linkTable[i]--;
            }
        }

        System.arraycopy(linkTable, lookup + 1, linkTable, lookup,
                         newNodePointer - lookup - 1);

        linkTable[newNodePointer - 1] = 0;

        newNodePointer--;

        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] > lookup) {
                hashTable[i]--;
            }
        }

        return true;
    }
}
