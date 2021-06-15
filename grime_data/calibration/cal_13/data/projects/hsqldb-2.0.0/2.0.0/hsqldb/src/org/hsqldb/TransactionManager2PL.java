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

import org.hsqldb.lib.DoubleIntIndex;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;

/**
 * Manages rows involved in transactions
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class TransactionManager2PL extends TransactionManagerCommon
implements TransactionManager {

    public TransactionManager2PL(Database db) {

        database       = db;
        hasPersistence = database.logger.isLogged();
        lobSession     = database.sessionManager.getSysLobSession();
        txModel        = LOCKS;
    }

    public long getGlobalChangeTimestamp() {
        return globalChangeTimestamp.get();
    }

    public boolean isMVRows() {
        return false;
    }

    public int getTransactionControl() {
        return LOCKS;
    }

    public void setTransactionControl(Session session, int mode) {

        writeLock.lock();

        try {
            switch (mode) {

                case MVCC : {
                    TransactionManagerMVCC manager =
                        new TransactionManagerMVCC(database);

                    manager.globalChangeTimestamp.set(
                        globalChangeTimestamp.get());
                    manager.liveTransactionTimestamps.addLast(
                        session.transactionTimestamp);

                    database.txManager = manager;

                    break;
                }
                case MVLOCKS : {
                    TransactionManagerMV2PL manager =
                        new TransactionManagerMV2PL(database);

                    manager.globalChangeTimestamp.set(
                        globalChangeTimestamp.get());
                    manager.liveTransactionTimestamps.addLast(
                        session.transactionTimestamp);

                    database.txManager = manager;

                    break;
                }
                case LOCKS :
                    break;
            }

            return;
        } finally {
            writeLock.unlock();
        }
    }

    public void completeActions(Session session) {
        endActionTPL(session);
    }

    public boolean prepareCommitActions(Session session) {

        session.actionTimestamp = nextChangeTimestamp();

        return true;
    }

    public boolean commitTransaction(Session session) {

        if (session.abortTransaction) {
            return false;
        }

        int      limit = session.rowActionList.size();
        Object[] list  = session.rowActionList.getArray();

        writeLock.lock();

        try {
            endTransaction(session);

            // new actionTimestamp used for commitTimestamp
            session.actionTimestamp = nextChangeTimestamp();

            for (int i = 0; i < limit; i++) {
                RowAction action = (RowAction) list[i];

                action.commit(session);
            }

            persistCommit(session, list, limit);
            endTransactionTPL(session);
        } finally {
            writeLock.unlock();
        }

        session.tempSet.clear();

        if (session != lobSession && lobSession.rowActionList.size() > 0) {
            lobSession.isTransaction = true;
            lobSession.actionIndex   = lobSession.rowActionList.size();

            lobSession.commit(false);
        }

        return true;
    }

    public void rollback(Session session) {

        session.abortTransaction = false;
        session.actionTimestamp  = nextChangeTimestamp();

        rollbackPartial(session, 0, session.transactionTimestamp);
        endTransaction(session);
        writeLock.lock();

        try {
            endTransactionTPL(session);
        } finally {
            writeLock.unlock();
        }
    }

    public void rollbackSavepoint(Session session, int index) {

        long timestamp = session.sessionContext.savepointTimestamps.get(index);
        Integer oi = (Integer) session.sessionContext.savepoints.get(index);
        int     start  = oi.intValue();

        while (session.sessionContext.savepoints.size() > index + 1) {
            session.sessionContext.savepoints.remove(
                session.sessionContext.savepoints.size() - 1);
            session.sessionContext.savepointTimestamps.removeLast();
        }

        rollbackPartial(session, start, timestamp);
    }

    public void rollbackAction(Session session) {
        rollbackPartial(session, session.actionIndex, session.actionTimestamp);
        endActionTPL(session);
    }

    /**
     * rollback the row actions from start index in list and
     * the given timestamp
     */
    void rollbackPartial(Session session, int start, long timestamp) {

        Object[] list  = session.rowActionList.getArray();
        int      limit = session.rowActionList.size();

        if (start == limit) {
            return;
        }

        for (int i = limit - 1; i >= start; i--) {
            RowAction action = (RowAction) list[i];

            if (action == null || action.type == RowActionBase.ACTION_NONE
                    || action.type == RowActionBase.ACTION_DELETE_FINAL) {
                continue;
            }

            Row row = action.memoryRow;

            if (row == null) {
                row = (Row) action.store.get(action.getPos(), false);
            }

            if (row == null) {
                continue;
            }

            action.rollback(session, timestamp);

            int type = action.mergeRollback(session, timestamp, row);

            action.store.rollbackRow(session, row, type, txModel);
        }

        session.rowActionList.setSize(start);
    }

    public RowAction addDeleteAction(Session session, Table table, Row row,
                                     int[] colMap) {

        RowAction action;

        synchronized (row) {
            action = RowAction.addDeleteAction(session, table, row, colMap);
        }

        session.rowActionList.add(action);

        PersistentStore store = session.sessionData.getRowStore(table);

        store.delete(session, row);

        return action;
    }

    public void addInsertAction(Session session, Table table, Row row) {

        RowAction action = row.rowAction;

        if (action == null) {
            System.out.println("null insert action " + session + " "
                               + session.actionTimestamp);
        }

        session.rowActionList.add(action);
    }

// functional unit - accessibility of rows
    public boolean canRead(Session session, Row row, int mode, int[] colMap) {
        return true;
    }

    public boolean canRead(Session session, int id, int mode) {
        return true;
    }

    /**
     * add transaction info to a row just loaded from the cache. called only
     * for CACHED tables
     */
    public void setTransactionInfo(CachedObject object) {}

    public void removeTransactionInfo(CachedObject object) {}

    public void beginTransaction(Session session) {

        session.actionTimestamp      = nextChangeTimestamp();
        session.transactionTimestamp = session.actionTimestamp;
        session.isTransaction        = true;

        transactionCount++;
    }

    /**
     * add session to the end of queue when a transaction starts
     * (depending on isolation mode)
     */
    public void beginAction(Session session, Statement cs) {

        if (session.hasLocks(cs)) {
            return;
        }

        writeLock.lock();

        try {
            boolean canProceed = setWaitedSessionsTPL(session, cs);

            if (canProceed) {
                if (session.tempSet.isEmpty()) {
                    lockTablesTPL(session, cs);

                    // we don't set other sessions that would now be waiting for this one too
                    // next lock release will do it
                } else {
                    setWaitingSessionTPL(session);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void beginActionResume(Session session) {

        session.actionTimestamp = nextChangeTimestamp();

        if (!session.isTransaction) {
            session.transactionTimestamp = session.actionTimestamp;
            session.isTransaction        = true;

            transactionCount++;
        }

        return;
    }

    void endTransaction(Session session) {

        session.isTransaction = false;

        transactionCount--;
    }

// functional unit - list actions and translate id's

    /**
     * Return a lookup of all row ids for cached tables in transactions.
     */
    public DoubleIntIndex getTransactionIDList() {

        DoubleIntIndex lookup = new DoubleIntIndex(10, false);

        return lookup;
    }

    /**
     * Convert row ID's for cached table rows in transactions
     */
    public void convertTransactionIDs(DoubleIntIndex lookup) {}
}
