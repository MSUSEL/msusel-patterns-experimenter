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

package org.hsqldb;

import java.io.IOException;

import org.hsqldb.index.NodeAVL;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

// fredt@users 20021205 - path 1.7.2 - enhancements
// fredt@users 20021215 - doc 1.7.2 - javadoc comments

/**
 * Implementation of rows for tables with memory resident indexes and
 * disk-based data, such as TEXT tables.
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @version 1.7.0
 */
public class RowAVLDiskData extends RowAVL {

    PersistentStore store;
    int             accessCount;
    boolean         hasDataChanged;
    int             storageSize;

    /**
     *  Constructor for new rows.
     */
    public RowAVLDiskData(PersistentStore store, TableBase t, Object[] o) {

        super(t, o);

        setNewNodes();

        hasDataChanged = true;
        this.store     = store;
    }

    /**
     *  Constructor when read from the disk into the Cache. The link with
     *  the Nodes is made separetly.
     */
    public RowAVLDiskData(PersistentStore store, TableBase t,
                          RowInputInterface in) throws IOException {

        super(t, (Object[]) null);

        setNewNodes();

        position       = in.getPos();
        storageSize    = in.getSize();
        rowData        = in.readData(table.getColumnTypes());
        hasDataChanged = false;
        this.store     = store;
    }

    public void setData(Object[] data) {
        this.rowData = data;
    }

    public Object[] getData() {

        if (rowData == null) {
            store.get(this, false);
        } else {
            accessCount++;
        }

        return rowData;
    }

    /**
     *  Used when data is read from the disk into the Cache the first time.
     *  New Nodes are created which are then indexed.
     */
    public void setNewNodes() {

        int index = table.getIndexCount();

        nPrimaryNode = new NodeAVL(this);

        NodeAVL n = nPrimaryNode;

        for (int i = 1; i < index; i++) {
            n.nNext = new NodeAVL(this);
            n       = n.nNext;
        }
    }

    public NodeAVL insertNode(int index) {

        NodeAVL backnode = getNode(index - 1);
        NodeAVL newnode  = new NodeAVL(this);

        newnode.nNext  = backnode.nNext;
        backnode.nNext = newnode;

        return newnode;
    }

    /**
     *  Used when data is re-read from the disk into the Cache. The Row is
     *  already indexed so it is linked with the Node in the primary index.
     *  the Nodes is made separetly.
     */
    void setPrimaryNode(NodeAVL primary) {
        nPrimaryNode = primary;
    }

    public int getRealSize(RowOutputInterface out) {
        return out.getSize((RowAVL) this);
    }

    /**
     *  Writes the data to disk. Unlike CachedRow, hasChanged is never set
     *  to true when changes are made to the Nodes. (Nodes are in-memory).
     *  The only time this is used is when a new Row is added to the Caches.
     */
    public void write(RowOutputInterface out) {

        out.writeSize(storageSize);
        out.writeData(getData(), table.colTypes);
        out.writeEnd();

        hasDataChanged = false;
    }

    public boolean hasChanged() {
        return hasDataChanged;
    }

    public void updateAccessCount(int count) {
        accessCount = count;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public int getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(int size) {
        storageSize = size;
    }
    /**
     * Sets the file position for the row and registers the row with
     * the table.
     *
     * @param pos position in data file
     */
    public void setPos(int pos) {
        position = pos;
    }

    public boolean isMemory() {
        return true;
    }

    /**
     * With the current implementation of TEXT table updates and inserts,
     * the lifetime scope of this method extends until redefinition of table
     * data source or shutdown.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj argument;
     *   <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        return obj == this;
    }

    public boolean isInMemory() {
        return rowData != null;
    }

    public boolean isKeepInMemory() {
        return false;
    }

    public boolean keepInMemory(boolean keep) {
        return true;
    }

    public void setInMemory(boolean in) {

        if (!in) {
            rowData = null;
        }
    }
}
