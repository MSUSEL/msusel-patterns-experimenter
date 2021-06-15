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
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.result.Result;

/**
 * Implementation of Statement for simple PSM control statements.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class StatementSimple extends Statement {

    String   sqlState;
    HsqlName label;

    //
    ColumnSchema[] variables;
    int[]          variableIndexes;

    StatementSimple(int type, HsqlName label) {

        super(type, StatementTypes.X_SQL_CONTROL);

        references             = new OrderedHashSet();
        isTransactionStatement = false;
        this.label             = label;
    }

    StatementSimple(int type, String sqlState) {

        super(type, StatementTypes.X_SQL_CONTROL);

        references             = new OrderedHashSet();
        isTransactionStatement = false;
        this.sqlState          = sqlState;
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        switch (type) {

            /** @todo 1.9.0 - add the exception */
            case StatementTypes.SIGNAL :
                sb.append(Tokens.T_SIGNAL);
                break;

            case StatementTypes.RESIGNAL :
                sb.append(Tokens.T_RESIGNAL);
                break;

            case StatementTypes.ITERATE :
                sb.append(Tokens.T_ITERATE).append(' ').append(label);
                break;

            case StatementTypes.LEAVE :
                sb.append(Tokens.T_LEAVE).append(' ').append(label);
                break;
        }

        return sb.toString();
    }

    protected String describe(Session session, int blanks) {

        StringBuffer sb = new StringBuffer();

        sb.append('\n');

        for (int i = 0; i < blanks; i++) {
            sb.append(' ');
        }

        sb.append(Tokens.T_STATEMENT);

        return sb.toString();
    }

    public Result execute(Session session) {

        Result result;

        try {
            result = getResult(session);
        } catch (Throwable t) {
            result = Result.newErrorResult(t, null);
        }

        if (result.isError()) {
            result.getException().setStatementType(group, type);
        }

        return result;
    }

    Result getResult(Session session) {

        switch (type) {

            /** @todo - check sqlState against allowed values */
            case StatementTypes.SIGNAL :
            case StatementTypes.RESIGNAL :
                HsqlException ex = Error.error("sql routine error", sqlState,
                                               -1);

                return Result.newErrorResult(ex);

            case StatementTypes.ITERATE :
            case StatementTypes.LEAVE :
                return Result.newPSMResult(type, label.name, null);

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "");
        }
    }

    public void resolve(Session session) {

        boolean resolved = false;

        switch (type) {

            case StatementTypes.SIGNAL :
            case StatementTypes.RESIGNAL :
                resolved = true;
                break;

            case StatementTypes.ITERATE : {
                StatementCompound statement = parent;

                while (statement != null) {
                    if (statement.isLoop) {
                        if (label == null) {
                            resolved = true;

                            break;
                        }

                        if (statement.label != null
                                && label.name.equals(statement.label.name)) {
                            resolved = true;

                            break;
                        }
                    }

                    statement = statement.parent;
                }

                break;
            }
            case StatementTypes.LEAVE :
                resolved = true;
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "");
        }

        if (!resolved) {
            throw Error.error(ErrorCode.X_42602);
        }
    }

    public String describe(Session session) {
        return "";
    }

    public boolean isCatalogChange() {
        return false;
    }
}
