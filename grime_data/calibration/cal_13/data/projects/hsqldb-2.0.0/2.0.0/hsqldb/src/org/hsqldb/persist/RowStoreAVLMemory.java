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

import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.TransactionManager;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.index.Index;
import org.hsqldb.index.IndexAVL;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.rowio.RowInputInterface;

/*
 * Implementation of PersistentStore for MEMORY tables.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class RowStoreAVLMemory extends RowStoreAVL implements PersistentStore {

    Database database;
    int      rowIdSequence = 0;

    public RowStoreAVLMemory(PersistentStoreCollection manager, Table table) {

        this.database     = table.database;
        this.manager      = manager;
        this.table        = table;
        this.indexList    = table.getIndexList();
        this.accessorList = new CachedObject[indexList.length];

        manager.setStore(table, this);
    }

    public boolean isMemory() {
        return true;
    }

    public int getAccessCount() {
        return 0;
    }

    public void set(CachedObject object) {}

    public CachedObject get(int i) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowStoreAVMemory");
    }

    public CachedObject getKeep(int i) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowStoreAVLMemory");
    }

    public CachedObject get(int i, boolean keep) {
        throw Error.runtimeError(ErrorCode.U_S0500, "RowStoreAVLMemory");
    }

    public CachedObject get(CachedObject object, boolean keep) {
        return object;
    }

    public int getStorageSize(int i) {
        return 0;
    }

    public void add(CachedObject object) {}

    public CachedObject get(RowInputInterface in) {
        return null;
    }

    public CachedObject getNewInstance(int size) {
        return null;
    }

    public CachedObject getNewCachedObject(Session session, Object object) {

        int id;

        synchronized (this) {
            id = rowIdSequence++;
        }

        Row row = new RowAVL(table, (Object[]) object, id);

        if (session != null) {
            RowAction action = new RowAction(session, table,
                                             RowAction.ACTION_INSERT, row,
                                             null);

            row.rowAction = action;
        }

        return row;
    }

    public void removeAll() {
        elementCount = 0;
        ArrayUtil.fillArray(accessorList, null);
    }

    public void remove(int i) {}

    public void removePersistence(int i) {}

    public void release(int i) {}

    public void commitPersistence(CachedObject row) {}

    public void commitRow(Session session, Row row, int changeAction,
                          int txModel) {

        Object[] data = row.getData();

        switch (changeAction) {

            case RowAction.ACTION_DELETE :
                database.logger.writeDeleteStatement(session, (Table) table,
                                                     data);
                break;

            case RowAction.ACTION_INSERT :
                database.logger.writeInsertStatement(session, (Table) table,
                                                     data);
                break;

            case RowAction.ACTION_INSERT_DELETE :

                // INSERT + DELETE
                break;

            case RowAction.ACTION_DELETE_FINAL :
                delete(session, row);
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
                }
                break;

            case RowAction.ACTION_INSERT_DELETE :

                // INSERT + DELETE
                if (txModel == TransactionManager.LOCKS) {
                    remove(row.getPos());
                }
                break;
        }
    }

    //
    public DataFileCache getCache() {
        return null;
    }

    public void setCache(DataFileCache cache) {}

    public void release() {
        ArrayUtil.fillArray(accessorList, null);
    }

    public void setAccessor(Index key, CachedObject accessor) {

        Index index = (Index) key;

        accessorList[index.getPosition()] = accessor;
    }

    public void setAccessor(Index key, int accessor) {}

    public int elementCount(Session session) {

        Index index = this.indexList[0];

        if (elementCount < 0) {
            if (index == null) {
                elementCount = 0;
            } else {
                elementCount = ((IndexAVL) index).getNodeCount(session, this);
            }
        }

        if (session != null && index != null
                && (table.getTableType() == TableBase.SYSTEM_TABLE
                    || database.txManager.getTransactionControl()
                       != TransactionManager.LOCKS)) {
            return ((IndexAVL) index).getNodeCount(session, this);
        }

        return elementCount;
    }
}
