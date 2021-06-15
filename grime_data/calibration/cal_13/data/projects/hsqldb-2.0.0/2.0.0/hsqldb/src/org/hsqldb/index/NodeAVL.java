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

package org.hsqldb.index;

import org.hsqldb.Row;
import org.hsqldb.RowAVLDisk;
import org.hsqldb.lib.IntLookup;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rowio.RowOutputInterface;

// fredt@users 20020221 - patch 513005 by sqlbob@users (RMP)
// fredt@users 20020920 - path 1.7.1 - refactoring to cut mamory footprint
// fredt@users 20021205 - path 1.7.2 - enhancements
// fredt@users 20021215 - doc 1.7.2 - javadoc comments

/**
 *  The parent for all AVL node implementations. Subclasses of Node vary
 *  in the way they hold
 *  references to other Nodes in the AVL tree, or to their Row data.<br>
 *
 *  nNext links the Node objects belonging to different indexes for each
 *  table row. It is used solely by Row to locate the node belonging to a
 *  particular index.<br>
 *
 *  New class derived from Hypersonic SQL code and enhanced in HSQLDB. <p>
 *
 * @author Thomas Mueller (Hypersonic SQL Group)
 * @version 1.9.0
 * @since Hypersonic SQL
 */
public class NodeAVL implements CachedObject {

    static final int NO_POS = RowAVLDisk.NO_POS;
    public int       iBalance;
    public NodeAVL   nNext;    // node of next index (nNext==null || nNext.iId=iId+1)

    //
    protected NodeAVL nLeft;
    protected NodeAVL nRight;
    protected NodeAVL nParent;
    protected final Row row;

    NodeAVL() {
        row = null;
    }

    public NodeAVL(Row r) {
        row = r;
    }

    public void delete() {
        iBalance = 0;
        nLeft    = nRight = nParent = null;
    }

    NodeAVL getLeft(PersistentStore store) {
        return nLeft;
    }

    NodeAVL setLeft(PersistentStore persistentStore, NodeAVL n) {

        nLeft = n;

        return this;
    }

    public int getBalance(PersistentStore store) {
        return iBalance;
    }

    boolean isLeft(NodeAVL node) {
        return nLeft == node;
    }

    boolean isRight(NodeAVL node) {
        return nRight == node;
    }

    NodeAVL getRight(PersistentStore persistentStore) {
        return nRight;
    }

    NodeAVL setRight(PersistentStore persistentStore, NodeAVL n) {

        nRight = n;

        return this;
    }

    NodeAVL getParent(PersistentStore store) {
        return nParent;
    }

    boolean isRoot(PersistentStore store) {
        return nParent == null;
    }

    NodeAVL setParent(PersistentStore persistentStore, NodeAVL n) {

        nParent = n;

        return this;
    }

    public NodeAVL setBalance(PersistentStore store, int b) {

        iBalance = b;

        return this;
    }

    boolean isFromLeft(PersistentStore store) {

        if (nParent == null) {
            return true;
        }

        return this == nParent.nLeft;
    }

    public NodeAVL child(PersistentStore store, boolean isleft) {
        return isleft ? getLeft(store)
            : getRight(store);
    }

    public NodeAVL set(PersistentStore store, boolean isLeft, NodeAVL n) {

        if (isLeft) {
            nLeft = n;
        } else {
            nRight = n;
        }

        if (n != null) {
            n.nParent = this;
        }

        return this;
    }

    public void replace(PersistentStore store, Index index, NodeAVL n) {

        if (nParent == null) {
            if (n != null) {
                n = n.setParent(store, null);
            }

            store.setAccessor(index, n);
        } else {
            nParent.set(store, isFromLeft(store), n);
        }
    }

    boolean equals(NodeAVL n) {
        return n == this;
    }

    public void setInMemory(boolean in) {}

    public void write(RowOutputInterface out) {}

    public void write(RowOutputInterface out, IntLookup lookup) {}

    public int getPos() {
        return 0;
    }

    protected Row getRow(PersistentStore store) {
        return row;
    }

    protected Object[] getData(PersistentStore store) {
        return row.getData();
    }


    public void updateAccessCount(int count) {}

    public int getAccessCount() {
        return 0;
    }

    public void setStorageSize(int size) {}

    public int getStorageSize() {
        return 0;
    }

    public void setPos(int pos) {}

    public boolean hasChanged() {
        return false;
    }

    public boolean isKeepInMemory() {
        return false;
    }
    ;

    public boolean keepInMemory(boolean keep) {
        return true;
    }

    public boolean isInMemory() {
        return false;
    }

    public void restore() {}

    public void destroy() {}

    public int getRealSize(RowOutputInterface out) {
        return 0;
    }

    public boolean isMemory() {
        return true;
    }

}
