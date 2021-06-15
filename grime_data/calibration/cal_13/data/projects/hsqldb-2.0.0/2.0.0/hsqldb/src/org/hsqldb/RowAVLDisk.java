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

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.index.NodeAVL;
import org.hsqldb.index.NodeAVLDisk;
import org.hsqldb.lib.IntLookup;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

// fredt@users 20020221 - patch 513005 by sqlbob@users (RMP)
// fredt@users 20020920 - patch 1.7.1 - refactoring to cut memory footprint
// fredt@users 20021205 - patch 1.7.2 - enhancements
// fredt@users 20021215 - doc 1.7.2 - javadoc comments
// boucherb@users - 20040411 - doc 1.7.2 - javadoc comments

/**
 *  In-memory representation of a disk-based database row object with  methods
 *  for serialization and de-serialization. <p>
 *
 *  A CachedRow is normally part of a circular double linked list which
 *  contains all of the Rows currently in the Cache for the database. It is
 *  unlinked from this list when it is freed from the Cache to make way for
 *  other rows.<p>
 *
 *  New class derived from Hypersonic SQL code and enhanced in HSQLDB. <p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge dot net)
 * @author Thomas Mueller (Hypersonic SQL Group)
 * @version 1.9.0
 * @since Hypersonic SQL
 */
public class RowAVLDisk extends RowAVL {

    public static final int NO_POS = -1;

    //
    int     storageSize;
    int     keepCount;
    boolean isInMemory;
    int     accessCount;

    /**
     *  Flag indicating unwritten data.
     */
    protected boolean hasDataChanged;

    /**
     *  Flag indicating Node data has changed.
     */
    boolean hasNodesChanged;

    /**
     *  Constructor for new Rows.  Variable hasDataChanged is set to true in
     *  order to indicate the data needs saving.
     *
     * @param t table
     * @param o row data
     */
    public RowAVLDisk(TableBase t, Object[] o) {

        super(t, o);

        setNewNodes();

        hasDataChanged = hasNodesChanged = true;
    }

    /**
     *  Constructor when read from the disk into the Cache.
     *
     * @param t table
     * @param in data source
     * @throws IOException
     */
    public RowAVLDisk(TableBase t, RowInputInterface in) throws IOException {

        super(t, null);

        position    = in.getPos();
        storageSize = in.getSize();

        int indexcount = t.getIndexCount();

        nPrimaryNode = new NodeAVLDisk(this, in, 0);

        NodeAVL n = nPrimaryNode;

        for (int i = 1; i < indexcount; i++) {
            n.nNext = new NodeAVLDisk(this, in, i);
            n       = n.nNext;
        }

        rowData = in.readData(table.getColumnTypes());
    }

    public NodeAVL insertNode(int index) {
        return null;
    }

    private void readRowInfo(RowInputInterface in) throws IOException {

        // for use when additional transaction info is attached to rows
    }

    /**
     * Sets flag for Node data change.
     */
    public synchronized void setNodesChanged() {
        hasNodesChanged = true;
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

    public boolean isMemory() {
        return false;
    }

    /**
     * Sets the file position for the row
     *
     * @param pos position in data file
     */
    public void setPos(int pos) {

        position = pos;

        NodeAVL n = nPrimaryNode;

        while (n != null) {
            ((NodeAVLDisk) n).iData = position;
            n                       = n.nNext;
        }
    }

    /**
     * Sets flag for row data change.
     */
    public synchronized void setChanged() {
        hasDataChanged = true;
    }

    /**
     * Returns true if Node data has changed.
     *
     * @return boolean
     */
    public synchronized boolean hasChanged() {
        return hasNodesChanged || hasDataChanged;
    }

    /**
     * Returns the Table to which this Row belongs.
     *
     * @return Table
     */
    public TableBase getTable() {
        return table;
    }

    public void setStorageSize(int size) {
        storageSize = size;
    }

    /**
     * Returns true if any of the Nodes for this row is a root node.
     * Used only in Cache.java to avoid removing the row from the cache.
     *
     * @return boolean
     */
    public synchronized boolean isKeepInMemory() {
        return keepCount > 0;
    }

    /**
     * Only unlinks nodes. Is not a destroy() method
     */
    public void delete(PersistentStore store) {

        RowAVLDisk row = this;

        if (!row.keepInMemory(true)) {
            row = (RowAVLDisk) store.get(row, true);
        }

        super.delete(store);
        row.keepInMemory(false);
    }

    public void destroy() {
        nPrimaryNode = null;
        table        = null;
    }

    public synchronized boolean keepInMemory(boolean keep) {

        if (!isInMemory) {
            return false;
        }

        if (keep) {
            keepCount++;
        } else {
            keepCount--;

            if (keepCount < 0) {
                throw Error.runtimeError(ErrorCode.U_S0500,
                                         "RowAVLDisk - keep count");
            }
        }

        return true;
    }

    public synchronized boolean isInMemory() {
        return isInMemory;
    }

    public synchronized void setInMemory(boolean in) {

        isInMemory = in;

        if (in) {
            return;
        }

        NodeAVL n = nPrimaryNode;

        while (n != null) {
            n.setInMemory(in);

            n = n.nNext;
        }
    }

    /**
     * used in CachedDataRow
     */
    public void setNewNodes() {

        int indexcount = table.getIndexCount();

        nPrimaryNode = new NodeAVLDisk(this, 0);

        NodeAVL n = nPrimaryNode;

        for (int i = 1; i < indexcount; i++) {
            n.nNext = new NodeAVLDisk(this, i);
            n       = n.nNext;
        }
    }

    public int getRealSize(RowOutputInterface out) {

        int size = out.getSize((RowAVLDisk) this)
                   + table.getIndexCount() * NodeAVLDisk.SIZE_IN_BYTE;

        return size;
    }

    /**
     * Used exclusively by Cache to save the row to disk. New implementation in
     * 1.7.2 writes out only the Node data if the table row data has not
     * changed. This situation accounts for the majority of invocations as for
     * each row deleted or inserted, the Nodes for several other rows will
     * change.
     */
    public void write(RowOutputInterface out) {

        try {
            writeNodes(out);

            if (hasDataChanged) {
                out.writeData(rowData, table.colTypes);
                out.writeEnd();

                hasDataChanged = false;
            }
        } catch (IOException e) {}
    }

    public void write(RowOutputInterface out, IntLookup lookup) {

        out.writeSize(storageSize);

        NodeAVL rownode = nPrimaryNode;

        while (rownode != null) {
            ((NodeAVLDisk) rownode).write(out, lookup);

            rownode = rownode.nNext;
        }

        out.writeData(getData(), table.colTypes);
        out.writeEnd();
    }

    /**
     *  Writes the Nodes, immediately after the row size.
     *
     * @param out
     *
     * @throws IOException
     */
    private void writeNodes(RowOutputInterface out) throws IOException {

        out.writeSize(storageSize);

        NodeAVL n = nPrimaryNode;

        while (n != null) {
            n.write(out);

            n = n.nNext;
        }

        hasNodesChanged = false;
    }

    /**
     * Lifetime scope of this method depends on the operations performed on
     * any cached tables since this row or the parameter were constructed.
     * If only deletes or only inserts have been performed, this method
     * remains valid. Otherwise it can return invalid results.
     *
     * @param obj row to compare
     * @return boolean
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof RowAVLDisk) {
            return ((RowAVLDisk) obj).position == position;
        }

        return false;
    }

    /**
     * Hash code is valid only until a modification to the cache
     *
     * @return file position of row
     */
    public int hashCode() {
        return position;
    }
}
