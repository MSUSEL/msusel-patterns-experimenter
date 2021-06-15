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

import org.hsqldb.HsqlException;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.LongKeyHashMap;

/**
 * Collection of PersistenceStore itmes currently used by a session.
 * An item is retrieved based on key returned by
 * TableBase.getPersistenceId().
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class PersistentStoreCollectionSession
implements PersistentStoreCollection {

    private final Session        session;
    private final LongKeyHashMap rowStoreMapSession     = new LongKeyHashMap();
    private LongKeyHashMap       rowStoreMapTransaction = new LongKeyHashMap();
    private LongKeyHashMap       rowStoreMapStatement   = new LongKeyHashMap();

    public PersistentStoreCollectionSession(Session session) {
        this.session = session;
    }

    public void setStore(Object key, PersistentStore store) {

        TableBase table = (TableBase) key;

        switch (table.persistenceScope) {

            case TableBase.SCOPE_STATEMENT :
                if (store == null) {
                    rowStoreMapStatement.remove(table.getPersistenceId());
                } else {
                    rowStoreMapStatement.put(table.getPersistenceId(), store);
                }
                break;

            // SYSTEM_TABLE
            case TableBase.SCOPE_FULL :
            case TableBase.SCOPE_TRANSACTION :
                if (store == null) {
                    rowStoreMapTransaction.remove(table.getPersistenceId());
                } else {
                    rowStoreMapTransaction.put(table.getPersistenceId(),
                                               store);
                }
                break;

            case TableBase.SCOPE_SESSION :
                if (store == null) {
                    rowStoreMapSession.remove(table.getPersistenceId());
                } else {
                    rowStoreMapSession.put(table.getPersistenceId(), store);
                }
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500,
                                         "PersistentStoreCollectionSession");
        }
    }

    public PersistentStore getStore(Object key) {

        try {
            TableBase       table = (TableBase) key;
            PersistentStore store;

            switch (table.persistenceScope) {

                case TableBase.SCOPE_STATEMENT :
                    store = (PersistentStore) rowStoreMapStatement.get(
                        table.getPersistenceId());

                    if (store == null) {
                        store = session.database.logger.newStore(session,
                                this, table, true);
                    }

                    return store;

                // SYSTEM_TABLE
                case TableBase.SCOPE_FULL :
                case TableBase.SCOPE_TRANSACTION :
                    store = (PersistentStore) rowStoreMapTransaction.get(
                        table.getPersistenceId());

                    if (store == null) {
                        store = session.database.logger.newStore(session,
                                this, table, true);
                    }

                    return store;

                case TableBase.SCOPE_SESSION :
                    store = (PersistentStore) rowStoreMapSession.get(
                        table.getPersistenceId());

                    if (store == null) {
                        store = session.database.logger.newStore(session,
                                this, table, true);
                    }

                    return store;
            }
        } catch (HsqlException e) {}

        throw Error.runtimeError(ErrorCode.U_S0500,
                                 "PersistentStoreCollectionSession");
    }

    public void clearAllTables() {

        clearSessionTables();
        clearTransactionTables();
        clearStatementTables();
    }

    public void clearResultTables(long actionTimestamp) {

        if (rowStoreMapSession.isEmpty()) {
            return;
        }

        Iterator it = rowStoreMapSession.values().iterator();

        while (it.hasNext()) {
            PersistentStore store = (PersistentStore) it.next();

            if (store.getTimestamp() == actionTimestamp) {
                store.release();
            }
        }
    }

    public void clearSessionTables() {

        if (rowStoreMapSession.isEmpty()) {
            return;
        }

        Iterator it = rowStoreMapSession.values().iterator();

        while (it.hasNext()) {
            PersistentStore store = (PersistentStore) it.next();

            store.release();
        }

        rowStoreMapSession.clear();
    }

    public void clearTransactionTables() {

        if (rowStoreMapTransaction.isEmpty()) {
            return;
        }

        Iterator it = rowStoreMapTransaction.values().iterator();

        while (it.hasNext()) {
            PersistentStore store = (PersistentStore) it.next();

            store.release();
        }

        rowStoreMapTransaction.clear();
    }

    public void clearStatementTables() {

        if (rowStoreMapStatement.isEmpty()) {
            return;
        }

        Iterator it = rowStoreMapStatement.values().iterator();

        while (it.hasNext()) {
            PersistentStore store = (PersistentStore) it.next();

            store.release();
        }

        rowStoreMapStatement.clear();
    }

    public void registerIndex(Table table) {

        PersistentStore store = findStore(table);

        if (store == null) {
            return;
        }

        store.resetAccessorKeys(table.getIndexList());
    }

    public PersistentStore findStore(Table table) {

        PersistentStore store = null;

        switch (table.persistenceScope) {

            case TableBase.SCOPE_STATEMENT :
                store = (PersistentStore) rowStoreMapStatement.get(
                    table.getPersistenceId());
                break;

            // SYSTEM_TABLE
            case TableBase.SCOPE_FULL :
            case TableBase.SCOPE_TRANSACTION :
                store = (PersistentStore) rowStoreMapTransaction.get(
                    table.getPersistenceId());
                break;

            case TableBase.SCOPE_SESSION :
                store = (PersistentStore) rowStoreMapSession.get(
                    table.getPersistenceId());
                break;
        }

        return store;
    }

    public void moveData(Table oldTable, Table newTable, int colIndex,
                         int adjust) {

        PersistentStore oldStore = findStore(oldTable);

        if (oldStore == null) {
            return;
        }

        PersistentStore newStore = getStore(newTable);

        newStore.moveData(session, oldStore, colIndex, adjust);
        setStore(oldTable, null);
    }
}
