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

import org.hsqldb.ParserDQL.CompileContext;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.ArraySort;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.result.Result;

/**
 * Implementation of Statement for PSM statements with expressions.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class StatementExpression extends StatementDMQL {

    Expression expression;

    /**
     * for RETURN and flow control
     */
    StatementExpression(Session session, CompileContext compileContext,
                        int type, Expression expression) {

        super(type, StatementTypes.X_SQL_CONTROL, null);

        switch (type) {

            case StatementTypes.RETURN :
            case StatementTypes.CONDITION :
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "");
        }

        isTransactionStatement = false;
        this.expression        = expression;

        setDatabseObjects(session, compileContext);
        checkAccessRights(session);
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        switch (type) {

            case StatementTypes.RETURN :
                return sql;

            case StatementTypes.CONDITION :
                sb.append(expression.getSQL());
                break;
        }

        return sb.toString();
    }

    SubQuery[] getSubqueries(Session session) {

        OrderedHashSet subQueries = null;

        if (expression != null) {
            subQueries = expression.collectAllSubqueries(subQueries);
        }

        if (subQueries == null || subQueries.size() == 0) {
            return SubQuery.emptySubqueryArray;
        }

        SubQuery[] subQueryArray = new SubQuery[subQueries.size()];

        subQueries.toArray(subQueryArray);
        ArraySort.sort(subQueryArray, 0, subQueryArray.length,
                       subQueryArray[0]);

        for (int i = 0; i < subqueries.length; i++) {
            subQueryArray[i].prepareTable(session);
        }

        return subQueryArray;
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
            if (subqueries.length > 0) {
                materializeSubQueries(session);
            }

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

            case StatementTypes.RETURN :
            case StatementTypes.CONDITION :
                return expression.getResult(session);

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "");
        }
    }

    public void resolve(Session session) {}

    String describeImpl(Session session) throws Exception {
        return getSQL();
    }

    void collectTableNamesForRead(OrderedHashSet set) {

        for (int i = 0; i < subqueries.length; i++) {
            if (subqueries[i].queryExpression != null) {
                subqueries[i].queryExpression.getBaseTableNames(set);
            }
        }

        for (int i = 0; i < routines.length; i++) {
            set.addAll(routines[i].getTableNamesForRead());
        }
    }

    void collectTableNamesForWrite(OrderedHashSet set) {}
}
