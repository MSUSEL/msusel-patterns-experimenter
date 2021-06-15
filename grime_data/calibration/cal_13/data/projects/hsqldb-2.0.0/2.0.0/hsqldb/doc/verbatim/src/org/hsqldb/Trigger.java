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

// fredt@users 20030727 - signature altered to support update triggers
/*

Contents of row1[] and row2[] in each type of trigger.

AFTER INSERT
 - row1[] contains single String object = "Statement-level".

AFTER UPDATE
 - row1[] contains single String object = "Statement-level".

AFTER DELETE
 - row1[] contains single String object = "Statement-level".

BEFORE INSERT FOR EACH ROW
 - row2[] contains data about to be inserted and this can
be modified within the trigger such that modified data gets written to the
database.

AFTER INSERT FOR EACH ROW
 - row2[] contains data just inserted into the table.

BEFORE UPDATE FOR EACH ROW
 - row1[] contains currently stored data and not the data that is about to be
updated.

 - row2[] contains the data that is about to be updated.

AFTER UPDATE FOR EACH ROW
 - row1[] contains old stored data.
 - row2[] contains the new data.

BEFORE DELETE FOR EACH ROW
 - row1[] contains row data about to be deleted.

AFTER DELETE FOR EACH ROW
 - row1[] contains row data that has been deleted.

List compiled by Andrew Knight (quozzbat@users)
*/

/**
 * The interface an HSQLDB TRIGGER must implement. The user-supplied class that
 * implements this must have a default constructor.
 *
 * @author Peter Hudson
 * @version 1.9.0
 * @since 1.7.0
 */
public interface Trigger {

    // type of trigger
    int INSERT_AFTER      = 0;
    int DELETE_AFTER      = 1;
    int UPDATE_AFTER      = 2;
    int INSERT_AFTER_ROW  = 3;
    int DELETE_AFTER_ROW  = 4;
    int UPDATE_AFTER_ROW  = 5;
    int INSERT_BEFORE_ROW = 6;
    int DELETE_BEFORE_ROW = 7;
    int UPDATE_BEFORE_ROW = 8;

    /**
     * The method invoked upon each triggered action.
     *
     * <p> type contains the integer index id for trigger type, e.g.
     * TriggerDef.INSERT_AFTER
     *
     * <p> For all triggers defined as default FOR EACH STATEMENT both
     *  oldRow and newRow are null.
     *
     * <p> For triggers defined as FOR EACH ROW, the following will apply:
     *
     * <p> When UPDATE triggers are fired, oldRow contains the existing values
     * of the table row and newRow contains the new values.
     *
     * <p> For INSERT triggers, oldRow is null and newRow contains the table row
     * to be inserted. For DELETE triggers, newRow is null and oldRow contains
     * the table row to be deleted.
     *
     * <p> For error conditions, users can construct an HsqlException using one
     * of the static methods of org.hsqldb.error.Error with a predefined
     * SQL State from org.hsqldb.error.ErrorCode.
     *
     * @param type int
     * @param trigName the name of the trigger
     * @param tabName the name of the table upon which the triggered action is
     *   occuring
     * @param oldRow the old row
     * @param newRow the new row
     * @throws HsqlException
     */
    void fire(int type, String trigName, String tabName, Object[] oldRow,
              Object[] newRow) throws HsqlException;
}
