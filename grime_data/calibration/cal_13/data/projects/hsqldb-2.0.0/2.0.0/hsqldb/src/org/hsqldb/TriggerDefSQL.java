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

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.store.ValuePool;
import org.hsqldb.result.Result;

/**
 * Implementation of SQL TRIGGER objects.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class TriggerDefSQL extends TriggerDef {

    OrderedHashSet references;

    public TriggerDefSQL(HsqlNameManager.HsqlName name, int when,
                         int operation, boolean forEachRow, Table table,
                         Table[] transitions, RangeVariable[] rangeVars,
                         Expression condition, String conditionSQL,
                         int[] updateColumns, Routine routine) {

        super(name, when, operation, forEachRow, table, transitions,
              rangeVars, condition, conditionSQL, updateColumns);

        this.routine    = routine;
        this.references = routine.getReferences();
    }

    public OrderedHashSet getReferences() {
        return routine.getReferences();
    }

    public OrderedHashSet getComponents() {
        return null;
    }

    public void compile(Session session, SchemaObject parentObject) {}

    public String getClassName() {
        return null;
    }

    public boolean hasOldTable() {
        return transitions[OLD_TABLE] != null;
    }

    public boolean hasNewTable() {
        return transitions[NEW_TABLE] != null;
    }

    synchronized void pushPair(Session session, Object[] oldData,
                               Object[] newData) {

        Result result = Result.updateZeroResult;

        if (session.sessionContext.depth > 128) {
            throw Error.error(ErrorCode.GENERAL_ERROR);
        }

        session.sessionContext.push();

        if (transitions[OLD_ROW] != null) {
            rangeVars[OLD_ROW].getIterator(session).setCurrent(oldData);
        }

        if (transitions[NEW_ROW] != null) {
            rangeVars[NEW_ROW].getIterator(session).setCurrent(newData);
        }

        if (condition.testCondition(session)) {
            session.sessionContext.routineVariables =
                ValuePool.emptyObjectArray;
            result = routine.statement.execute(session);
        }

        session.sessionContext.pop();

        if (result.isError()) {
            throw result.getException();
        }
    }

    public String getSQL() {

        StringBuffer sb = getSQLMain();

        sb.append(routine.statement.getSQL());

        return sb.toString();
    }
}
