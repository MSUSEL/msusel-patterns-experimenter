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

import org.hsqldb.lib.IntLookup;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rowio.RowOutputInterface;

/**
 * Base class for a database row object.
 *
 * @author Fred Toussi (fredt@users dot sourceforge dot net)
 * @version 1.9.0
 */
public class Row implements CachedObject {

    int                       position;
    Object[]                  rowData;
    public volatile RowAction rowAction;
    protected TableBase       table;

    public RowAction getAction() {
        return rowAction;
    }

    /**
     *  Default constructor used only in subclasses.
     */
    public Row(TableBase table, Object[] data) {
        this.table   = table;
        this.rowData = data;
    }

    /**
     * Returns the array of fields in the database row.
     */
    public Object[] getData() {
        return rowData;
    }

    boolean isDeleted(Session session, PersistentStore store) {

        Row       row    = (Row) store.get(this, false);
        RowAction action = row.rowAction;

        if (action == null) {
            return false;
        }

        return !action.canRead(session, TransactionManager.ACTION_READ);
    }

    public void setChanged() {}

    public void setStorageSize(int size) {}

    public int getStorageSize() {
        return 0;
    }

    public boolean isMemory() {
        return true;
    }

    public void updateAccessCount(int count) {}

    public int getAccessCount() {
        return 0;
    }

    public int getPos() {
        return position;
    }

    public long getId() {
        return ((long) table.getId() << 32) + (long) position;
    }

    public void setPos(int pos) {
        position = pos;
    }

    public boolean hasChanged() {
        return false;
    }

    public boolean isKeepInMemory() {
        return true;
    }

    public boolean keepInMemory(boolean keep) {
        return true;
    }

    public boolean isInMemory() {
        return true;
    }

    public void setInMemory(boolean in) {}

    public void delete(PersistentStore store) {}

    public void restore() {}

    public void destroy() {}

    public int getRealSize(RowOutputInterface out) {
        return 0;
    }

    public TableBase getTable() {
        return table;
    }

    public void write(RowOutputInterface out) {}

    public void write(RowOutputInterface out, IntLookup lookup) {}

    /**
     * Lifetime scope of this method is limited depends on the operations
     * performed. Rows deleted completely can equal rows produced later.
     * This can return invalid results if used with deleted rows.
     *
     * @param obj row to compare
     * @return boolean
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof Row) {
            return ((Row) obj).position == position;
        }

        return false;
    }

    /**
     * Hash code is always valid.
     *
     * @return file position of row
     */
    public int hashCode() {
        return position;
    }
}
