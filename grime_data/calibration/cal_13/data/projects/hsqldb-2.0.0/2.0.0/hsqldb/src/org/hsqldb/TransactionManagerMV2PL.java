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

import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.DoubleIntIndex;
import org.hsqldb.lib.HsqlDeque;
import org.hsqldb.lib.IntKeyHashMapConcurrent;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.persist.CachedObject;

/**
 * Manages rows involved in transactions
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.0
 * @since 2.0.0
 */
public class TransactionManagerMV2PL extends TransactionManagerCommon
implements TransactionManager {

    // functional unit - merged committed transactions
    HsqlDeque committedTransactions          = new HsqlDeque();
    LongDeque committedTransactionTimestamps = new LongDeque();

    public TransactionManagerMV2PL(Database db) {

        database        = db;
        hasPersistence  = database.logger.isLogged();
        lobSession      = database.sessionManager.getSysLobSession();
        rowActionMap    = new IntKeyHashMapConcurrent(10000);
        txModel         = MVLOCKS;
        catalogNameList = new HsqlName[]{ database.getCatalogName() };
    }

    public long getGlobalChangeTimestamp() {
        return globalChangeTimestamp.get();
    }

    public boolean isMVRows() {
        return true;
    }

    public int getTransactionControl() {
        return MVLOCKS;
    }

    public void setTransactionControl(Session session, int mode) {

        writeLock.lock();

        try {

            // statement runs as transaction
            if (liveTransactionTimestamps.size() == 1) {
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
                    case MVLOCKS :
                        break;

                    case LOCKS : {
                        TransactionManager2PL manager =
                            new TransactionManager2PL(database);

                        manager.globalChangeTimestamp.set(
                            globalChangeTimestamp.get());

                        database.txManager = manager;

                        break;
                    }
                }

                return;
            }
        } finally {
            writeLock.unlock();
        }

        throw Error.error(ErrorCode.X_25001);
    }

    public void completeActions(Session session) {
        endActionTPL(session);
    }

    public boolean prepareCommitActions(Session session) {

        Object[] list  = session.rowActionList.getArray();
        int      limit = session.rowActionList.size();

        writeLock.lock();

        try {
            session.actionTimestamp = nextChangeTimestamp();

            for (int i = 0; i < limit; i++) {
                RowAction action = (RowAction) list[i];

                action.prepareCommit(session);
            }

            return true;
        } finally {
            writeLock.unlock();
        }
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

            // session.actionTimestamp is the committed tx timestamp
            if (getFirstLiveTransactionTimestamp() > session.actionTimestamp) {
                mergeTransaction(session, list, 0, limit,
                                 session.actionTimestamp);
                finaliseRows(session, list, 0, limit, true);
            } else {
                list = session.rowActionList.toArray();

                addToCommittedQueue(session, list);
            }

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

        writeLock.lock();

        try {
            session.abortTransaction = false;
            session.actionTimestamp  = nextChangeTimestamp();

            rollbackPartial(session, 0, session.transactionTimestamp);
            endTransaction(session);
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

        for (int i = start; i < limit; i++) {
            RowAction action = (RowAction) list[i];

            if (action != null) {
                action.rollback(session, timestamp);
            } else {
                System.out.println("null action in rollback " + start);
            }
        }

        // rolled back transactions can always be merged as they have never been
        // seen by other sessions
        mergeRolledBackTransaction(session, timestamp, list, start, limit);
        finaliseRows(session, list, start, limit, false);
        session.rowActionList.setSize(start);
    }

    public RowAction addDeleteAction(Session session, Table table, Row row,
                                     int[] colMap) {

        RowAction action;
        boolean   newAction;

        synchronized (row) {
            newAction = row.rowAction == null;
            action    = RowAction.addDeleteAction(session, table, row, colMap);
        }

        session.rowActionList.add(action);

        if (newAction) {
            if (!row.isMemory()) {
                rowActionMap.put(action.getPos(), action);
            }
        }

        return action;
    }

    public void addInsertAction(Session session, Table table, Row row) {

        RowAction action = row.rowAction;

        if (action == null) {
            System.out.println("null insert action " + session + " "
                               + session.actionTimestamp);
        }

        session.rowActionList.add(action);

        if (!row.isMemory()) {
            rowActionMap.put(action.getPos(), action);
        }
    }

// functional unit - accessibility of rows
    public boolean canRead(Session session, Row row, int mode, int[] colMap) {

        RowAction action = row.rowAction;

        if (action == null) {
            return true;
        }

        return action.canRead(session, TransactionManager.ACTION_READ);
    }

    public boolean canRead(Session session, int id, int mode) {

        RowAction action = (RowAction) rowActionMap.get(id);

        return action == null ? true
                              : action.canRead(session,
                                               TransactionManager.ACTION_READ);
    }

    /**
     * add transaction info to a row just loaded from the cache. called only
     * for CACHED tables
     */
    public void setTransactionInfo(CachedObject object) {

        Row       row    = (Row) object;
        RowAction rowact = (RowAction) rowActionMap.get(row.position);

        row.rowAction = rowact;
    }

    /**
     * remove the transaction info
     */
    public void removeTransactionInfo(CachedObject object) {
        rowActionMap.remove(object.getPos());
    }

    /**
     * add a list of actions to the end of queue
     */
    void addToCommittedQueue(Session session, Object[] list) {

        synchronized (committedTransactionTimestamps) {

            // add the txList according to commit timestamp
            committedTransactions.addLast(list);

            // get session commit timestamp
            committedTransactionTimestamps.addLast(session.actionTimestamp);
/* debug 190
            if (committedTransactions.size() > 64) {
                System.out.println("******* excessive transaction queue");
            }
// debug 190 */
        }
    }

    /**
     * expire all committed transactions that are no longer in scope
     */
    void mergeExpiredTransactions(Session session) {

        long timestamp = getFirstLiveTransactionTimestamp();

        while (true) {
            long     commitTimestamp = 0;
            Object[] actions         = null;

            synchronized (committedTransactionTimestamps) {
                if (committedTransactionTimestamps.isEmpty()) {
                    break;
                }

                commitTimestamp = committedTransactionTimestamps.getFirst();

                if (commitTimestamp < timestamp) {
                    committedTransactionTimestamps.removeFirst();

                    actions = (Object[]) committedTransactions.removeFirst();
                } else {
                    break;
                }
            }

            mergeTransaction(session, actions, 0, actions.length,
                             commitTimestamp);
            finaliseRows(session, actions, 0, actions.length, true);
        }
    }

    public void beginTransaction(Session session) {

        writeLock.lock();

        try {
            session.actionTimestamp      = nextChangeTimestamp();
            session.transactionTimestamp = session.actionTimestamp;
            session.isTransaction        = true;

            liveTransactionTimestamps.addLast(session.transactionTimestamp);

            transactionCount++;
        } finally {
            writeLock.unlock();
        }
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

                    // we dont set other sessions that would now be waiting for this one too
                } else {
                    setWaitingSessionTPL(session);
                }
            } else {
                session.abortTransaction = true;
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * add session to the end of queue when a transaction starts
     * (depending on isolation mode)
     */
    public void beginActionResume(Session session) {

        writeLock.lock();

        try {
            session.actionTimestamp = nextChangeTimestamp();

            if (!session.isTransaction) {
                session.transactionTimestamp = session.actionTimestamp;
                session.isTransaction        = true;

                liveTransactionTimestamps.addLast(session.actionTimestamp);

                transactionCount++;
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * remove session from queue when a transaction ends
     * and expire any committed transactions
     * that are no longer required. remove transactions ended before the first
     * timestamp in liveTransactionsSession queue
     */
    void endTransaction(Session session) {

        long timestamp = session.transactionTimestamp;

        session.isTransaction = false;

        int index = liveTransactionTimestamps.indexOf(timestamp);

        if (index >= 0) {
            transactionCount--;
            liveTransactionTimestamps.remove(index);
            mergeExpiredTransactions(session);
        }

    }

// functional unit - list actions and translate id's

    /**
     * Return a lookup of all row ids for cached tables in transactions.
     * For auto-defrag, as currently there will be no RowAction entries
     * at the time of defrag.
     */
    public DoubleIntIndex getTransactionIDList() {
        return super.getTransactionIDList();
    }

    /**
     * Convert row ID's for cached table rows in transactions
     */
    public void convertTransactionIDs(DoubleIntIndex lookup) {
        super.convertTransactionIDs(lookup);
    }
}
