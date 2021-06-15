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

/**
 * Represents a single insert / delete / rollback / commit action on a row.
 *
 * type = type of action
 * actionTimestamp = timestamp of end of SQL action; 0 if action not complete
 * commitTimestamp = timestamp of commit or rollback; 0 if not committed/rolledback
 * rolledBack = flag for rolled back actions
 * next = next action in linked list;
 *
 * timestamps are not in any order
 *
 * @author Fred Toussi (fredt@users dot sourceforge dot net)
 * @version 2.0.0
 * @since 2.0.0
 */
class RowActionBase {

    public static final byte ACTION_NONE          = 0;
    public static final byte ACTION_INSERT        = 1;
    public static final byte ACTION_DELETE        = 2;
    public static final byte ACTION_DELETE_FINAL  = 3;
    public static final byte ACTION_INSERT_DELETE = 4;
    public static final byte ACTION_REF           = 5;
    public static final byte ACTION_CHECK         = 6;
    public static final byte ACTION_DEBUG         = 7;

    //
    RowActionBase            next;
    Session                  session;
    long                     actionTimestamp;
    long                     commitTimestamp;
    byte                     type;
    boolean                  deleteComplete;
    boolean                  rolledback;
    boolean                  prepared;
    int[]                    changeColumnMap;

    RowActionBase() {}

    /**
     * constructor, used for delete actions only
     */
    RowActionBase(Session session, byte type) {

        this.session    = session;
        this.type       = type;
        actionTimestamp = session.actionTimestamp;
    }

    void setAsAction(RowActionBase action) {

        next            = action.next;
        session         = action.session;
        actionTimestamp = action.actionTimestamp;
        commitTimestamp = action.commitTimestamp;
        type            = action.type;
        deleteComplete  = action.deleteComplete;
        rolledback      = action.rolledback;
        prepared        = action.prepared;
        changeColumnMap = action.changeColumnMap;
    }
}
