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
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.OrderedIntHashSet;
import org.hsqldb.result.Result;

/**
 * Implementation of Statement for condition handler objects.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class StatementHandler extends Statement {

    public static final int NONE          = 0;
    public static final int SQL_EXCEPTION = 1;
    public static final int SQL_WARNING   = 2;
    public static final int SQL_NOT_FOUND = 3;
    public static final int SQL_STATE     = 4;

    //
    public static final int CONTINUE = 5;
    public static final int EXIT     = 6;
    public static final int UNDO     = 7;

    //
    public final int handlerType;

    //
    OrderedIntHashSet conditionGroups = new OrderedIntHashSet();
    OrderedHashSet    conditionStates = new OrderedHashSet();
    Statement         statement;

    //
    public static final StatementHandler[] emptyExceptionHandlerArray =
        new StatementHandler[]{};

    StatementHandler(int handlerType) {

        super(StatementTypes.HANDLER, StatementTypes.X_SQL_CONTROL);

        this.handlerType = handlerType;
    }

    public void addConditionState(String sqlState) {

        boolean result = conditionStates.add(sqlState);

        result &= conditionGroups.isEmpty();

        if (!result) {
            throw Error.error(ErrorCode.X_42604);
        }
    }

    public void addConditionType(int conditionType) {

        boolean result = conditionGroups.add(conditionType);

        result &= conditionStates.isEmpty();

        if (!result) {
            throw Error.error(ErrorCode.X_42604);
        }
    }

    public void addStatement(Statement s) {
        statement = s;
    }

    public boolean handlesConditionType(int type) {
        return conditionGroups.contains(type);
    }

    public boolean handlesCondition(String sqlState) {

        if (conditionStates.contains(sqlState)) {
            return true;
        }

        String conditionClass = sqlState.substring(0, 2);

        if (conditionStates.contains(conditionClass)) {
            return true;
        }

        if (conditionClass.equals("01")) {
            return conditionGroups.contains(SQL_WARNING);
        }

        if (conditionClass.equals("02")) {
            return conditionGroups.contains(SQL_NOT_FOUND);
        }

        return conditionGroups.contains(SQL_EXCEPTION);
    }

    public int[] getConditionTypes() {
        return conditionGroups.toArray();
    }

    public String[] getConditionStates() {
        return (String[]) conditionStates.toArray(
            new String[conditionStates.size()]);
    }

    public void resolve(Session session) {

        if (statement != null) {
            statement.resolve(session);

            readTableNames  = statement.getTableNamesForRead();
            writeTableNames = statement.getTableNamesForWrite();
        }
    }

    public Result execute(Session session) {

        if (statement != null) {
            return statement.execute(session);
        } else {
            return Result.updateZeroResult;
        }
    }

    public String describe(Session session) {
        return "";
    }

    public OrderedHashSet getReferences() {

        if (statement == null) {
            return new OrderedHashSet();
        }

        return statement.getReferences();
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer(64);
        String       s;

        s = handlerType == CONTINUE ? Tokens.T_CONTINUE
                                    : handlerType == EXIT ? Tokens.T_EXIT
                                                          : Tokens.T_UNDO;

        sb.append(Tokens.T_DECLARE).append(' ').append(s).append(' ');
        sb.append(Tokens.T_HANDLER).append(' ').append(Tokens.T_FOR);
        sb.append(' ');

        for (int i = 0; i < conditionStates.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }

            sb.append(Tokens.T_SQLSTATE).append(' ');
            sb.append('\'').append(conditionStates.get(i)).append('\'');
        }

        for (int i = 0; i < conditionGroups.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }

            switch (conditionGroups.get(i)) {

                case SQL_EXCEPTION :
                    sb.append(Tokens.T_SQLEXCEPTION);
                    break;

                case SQL_WARNING :
                    sb.append(Tokens.T_SQLWARNING);
                    break;

                case SQL_NOT_FOUND :
                    sb.append(Tokens.T_NOT).append(' ').append(Tokens.FOUND);
                    break;
            }
        }

        if (statement != null) {
            sb.append(' ').append(statement.getSQL());
        }

        return sb.toString();
    }

    public boolean isCatalogChange() {
        return false;
    }
}
