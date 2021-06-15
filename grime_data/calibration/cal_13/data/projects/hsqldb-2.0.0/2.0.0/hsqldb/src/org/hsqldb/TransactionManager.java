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

public interface TransactionManager {

    //
    public int LOCKS   = 0;
    public int MVLOCKS = 1;
    public int MVCC    = 2;

    //
    public int ACTION_READ = 0;
    public int ACTION_DUP  = 1;
    public int ACTION_REF  = 2;

    public long getGlobalChangeTimestamp();

    public RowAction addDeleteAction(Session session, Table table, Row row,
                                     int[] colMap);

    public void addInsertAction(Session session, Table table, Row row);

    /**
     * add session to the end of queue when a transaction starts
     * (depending on isolation mode)
     */
    public void beginAction(Session session, Statement cs);

    public void beginActionResume(Session session);

    public void beginTransaction(Session session);

    // functional unit - accessibility of rows
    public boolean canRead(Session session, Row row, int mode, int[] colMap);

    public boolean canRead(Session session, int id, int mode);

    public boolean commitTransaction(Session session);

    public void completeActions(Session session);

    /**
     * Convert row ID's for cached table rows in transactions
     */
    public void convertTransactionIDs(DoubleIntIndex lookup);

    /**
     * Return a lookup of all row ids for cached tables in transactions.
     * For auto-defrag, as currently there will be no RowAction entries
     * at the time of defrag.
     */
    public DoubleIntIndex getTransactionIDList();

    public int getTransactionControl();

    public boolean isMVRows();

    public boolean prepareCommitActions(Session session);

    public void rollback(Session session);

    public void rollbackAction(Session session);

    public void rollbackSavepoint(Session session, int index);

    public void setTransactionControl(Session session, int mode);

    /**
     * add transaction info to a row just loaded from the cache. called only
     * for CACHED tables
     */
    public void setTransactionInfo(CachedObject object);

    /**
     * remove the transaction info
     */
    public void removeTransactionInfo(CachedObject object);
}
