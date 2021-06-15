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

import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hsqldb.HsqlException;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAVLDiskData;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TransactionManager;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.index.Index;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.rowio.RowInputInterface;

/*
 * Implementation of PersistentStore for TEXT tables.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class RowStoreAVLDiskData extends RowStoreAVLDisk {

    ReentrantReadWriteLock           lock = new ReentrantReadWriteLock(true);
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    RowAVLDiskData                   currentRow;

    public RowStoreAVLDiskData(PersistentStoreCollection manager,
                               Table table) {
        super(manager, null, table);
    }

    public CachedObject get(CachedObject object, boolean keep) {

        writeLock.lock();

        try {
            currentRow = (RowAVLDiskData) object;
            object     = cache.get(object, this, keep);

            return object;
        } finally {
            currentRow = null;

            writeLock.unlock();
        }
    }

    public void add(CachedObject object) {

        int size = object.getRealSize(cache.rowOut);

        object.setStorageSize(size);

        cache.add(object);
    }

    public CachedObject get(RowInputInterface in) {

        try {
            RowAVLDiskData newRow = new RowAVLDiskData(this, table, in);

            if (currentRow == null) {
                return newRow;
            }

            currentRow.setData(newRow.getData());

            return currentRow;
        } catch (IOException e) {
            throw Error.error(ErrorCode.TEXT_FILE_IO, e);
        }
    }

    public CachedObject getNewCachedObject(Session session, Object object) {

        Row row = new RowAVLDiskData(this, table, (Object[]) object);

        add(row);

        if (session != null) {
            RowAction.addInsertAction(session, table, row);
        }

        return row;
    }

    public void indexRow(Session session, Row row) {

        int i = 0;

        try {
            for (; i < indexList.length; i++) {
                indexList[i].insert(session, this, row);
            }
        } catch (HsqlException e) {

            // unique index violation - rollback insert
            for (--i; i >= 0; i--) {
                indexList[i].delete(session, this, row);
            }

            remove(row.getPos());

            throw e;
        }
    }

    public void set(CachedObject object) {}

    public void removeAll() {
        elementCount = 0;
        ArrayUtil.fillArray(accessorList, null);
    }

    public void remove(int i) {

        cache.remove(i, this);
    }

    public void removePersistence(int i) {
        cache.removePersistence(i, this);
    }

    public void release(int i) {

        cache.release(i);
    }

    public CachedObject getAccessor(Index key) {

        int position = key.getPosition();

        if (position >= accessorList.length) {
            throw Error.runtimeError(ErrorCode.U_S0500, "RowStoreAVL");
        }

        return accessorList[position];
    }

    public void commitPersistence(CachedObject row) {

        try {
            cache.saveRow(row);
        } catch (HsqlException e1) {}
    }

    public void delete(Session session, Row row) {

        for (int j = indexList.length - 1; j >= 0; j--) {
            indexList[j].delete(session, this, row);
        }

        row.delete(this);
    }

    public void commitRow(Session session, Row row, int changeAction,
                          int txModel) {

        switch (changeAction) {

            case RowAction.ACTION_DELETE :
                removePersistence(row.getPos());
                break;

            case RowAction.ACTION_INSERT :
                commitPersistence(row);
                break;

            case RowAction.ACTION_INSERT_DELETE :

                // INSERT + DELETE
                if (txModel == TransactionManager.LOCKS) {
                    remove(row.getPos());
                } else {
                    delete(session, row);
                    remove(row.getPos());
                }
                break;

            case RowAction.ACTION_DELETE_FINAL :
                if (txModel != TransactionManager.LOCKS) {
                    delete(session, row);
                    remove(row.getPos());
                }
                break;
        }
    }

    public void rollbackRow(Session session, Row row, int changeAction,
                            int txModel) {

        switch (changeAction) {

            case RowAction.ACTION_DELETE :
                if (txModel == TransactionManager.LOCKS) {

                    ((RowAVL) row).setNewNodes();
                    indexRow(session, row);
                }
                break;

            case RowAction.ACTION_INSERT :
                if (txModel == TransactionManager.LOCKS) {
                    delete(session, row);
                    remove(row.getPos());
                } else {}
                break;

            case RowAction.ACTION_INSERT_DELETE :

                // INSERT + DELETE
                if (txModel == TransactionManager.LOCKS) {
                    remove(row.getPos());
                } else {
                    delete(session, row);
                    remove(row.getPos());
                }
                break;
        }
    }

    //
    public void release() {

        ArrayUtil.fillArray(accessorList, null);
        table.database.logger.closeTextCache((Table) table);

        cache = null;
    }
}
