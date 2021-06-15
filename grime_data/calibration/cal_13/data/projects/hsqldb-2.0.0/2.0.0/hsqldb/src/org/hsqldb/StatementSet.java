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
import org.hsqldb.ParserDQL.CompileContext;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.ArraySort;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.result.Result;
import org.hsqldb.store.ValuePool;
import org.hsqldb.types.Type;

/**
 * Implementation of Statement for PSM and trigger assignment.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class StatementSet extends StatementDMQL {

    Expression expression;

    //
    Expression[] targets;
    int[]        variableIndexes;
    Type[]       sourceTypes;

    //
    final int               operationType;
    public final static int TRIGGER_SET  = 1;
    public final static int SELECT_INTO  = 2;
    public final static int VARIABLE_SET = 3;

    /**
     * Trigger SET statement.
     */
    StatementSet(Session session, Expression[] targets, Table table,
                 RangeVariable rangeVars[], int[] indexes,
                 Expression[] colExpressions, CompileContext compileContext) {

        super(StatementTypes.ASSIGNMENT, StatementTypes.X_SQL_DATA_CHANGE,
              session.getCurrentSchemaHsqlName());

        this.operationType        = TRIGGER_SET;
        this.targets              = targets;
        this.targetTable          = table;
        this.baseTable            = targetTable.getBaseTable();
        this.updateColumnMap      = indexes;
        this.updateExpressions    = colExpressions;
        this.updateCheckColumns   = targetTable.getColumnCheckList(indexes);
        this.targetRangeVariables = rangeVars;
        isTransactionStatement    = false;

        setDatabseObjects(session, compileContext);
        checkAccessRights(session);
    }

    /**
     * PSM and session variable SET
     */
    StatementSet(Session session, Expression[] targets, Expression e,
                 int[] indexes, CompileContext compileContext) {

        super(StatementTypes.ASSIGNMENT, StatementTypes.X_SQL_CONTROL, null);

        this.operationType     = VARIABLE_SET;
        this.targets           = targets;
        this.expression        = e;
        variableIndexes        = indexes;
        sourceTypes            = expression.getNodeDataTypes();
        isTransactionStatement = false;

        setDatabseObjects(session, compileContext);
        checkAccessRights(session);
    }

    /**
     * Single row SELECT INTO
     */
    StatementSet(Session session, Expression[] targets, QueryExpression query,
                 int[] indexes, CompileContext compileContext) {

        super(StatementTypes.ASSIGNMENT, StatementTypes.X_SQL_CONTROL, null);

        this.operationType     = SELECT_INTO;
        this.queryExpression   = query;
        this.targets           = targets;
        variableIndexes        = indexes;
        sourceTypes            = query.getColumnTypes();
        isTransactionStatement = false;

        setDatabseObjects(session, compileContext);
        checkAccessRights(session);
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

    Result getResult(Session session) {

        Result result = null;

        switch (operationType) {

            case StatementSet.TRIGGER_SET :
                result = executeSetStatement(session);
                break;

            case StatementSet.SELECT_INTO : {
                Object[] values = queryExpression.getSingleRowValues(session);

                if (values == null) {
                    result = Result.updateZeroResult;

                    break;
                }

                for (int i = 0; i < values.length; i++) {
                    values[i] =
                        targets[i].getColumn().getDataType().convertToType(
                            session, values[i], sourceTypes[i]);
                }

                result = executeAssignment(session, values);

                break;
            }
            case StatementSet.VARIABLE_SET : {
                Object[] values = getExpressionValues(session);

                if (values == null) {
                    result = Result.updateZeroResult;

                    break;
                }

                for (int i = 0; i < values.length; i++) {
                    Type targetType;

                    if (targets[i].getType() == OpTypes.ARRAY_ACCESS) {
                        targetType =
                            targets[i].getLeftNode().getColumn().getDataType()
                                .collectionBaseType();
                    } else {
                        targetType = targets[i].getColumn().getDataType();
                    }

                    values[i] = targetType.convertToType(session, values[i],
                                                         sourceTypes[i]);
                }

                result = executeAssignment(session, values);

                break;
            }
            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "StatementSet");
        }

        return result;
    }

    public void resolve(Session session) {

        references = new OrderedHashSet();

        switch (operationType) {

            case StatementSet.TRIGGER_SET :
                for (int i = 0; i < updateExpressions.length; i++) {
                    updateExpressions[i].collectObjectNames(references);
                }
                break;

            case StatementSet.SELECT_INTO :
            case StatementSet.VARIABLE_SET : {
                if (expression != null) {
                    expression.collectObjectNames(references);
                }

                if (queryExpression != null) {
                    queryExpression.collectObjectNames(references);
                }

                break;
            }
            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "StatementSet");
        }
    }

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        switch (operationType) {

            case StatementSet.TRIGGER_SET :
                return sql;

            case StatementSet.VARIABLE_SET : {

                /** @todo - cover row assignment */
                sb.append(Tokens.T_SET).append(' ');
                sb.append(targets[0].getColumn().getName().statementName);
                sb.append(' ').append('=').append(' ').append(
                    expression.getSQL());

                break;
            }
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

    public String describe(Session session) {
        return "";
    }

    Result executeSetStatement(Session session) {

        Table        table          = targetTable;
        int[]        colMap         = updateColumnMap;    // column map
        Expression[] colExpressions = updateExpressions;
        Type[]       colTypes       = table.getColumnTypes();
        int index = targetRangeVariables[TriggerDef.NEW_ROW].rangePosition;
        Object[] oldData =
            session.sessionContext.rangeIterators[index].getCurrent();
        Object[] data = StatementDML.getUpdatedData(session, targets, table,
            colMap, colExpressions, colTypes, oldData);

        ArrayUtil.copyArray(data, oldData, data.length);

        return Result.updateOneResult;
    }

    // this fk references -> other  :  other read lock
    void collectTableNamesForRead(OrderedHashSet set) {

        for (int i = 0; i < rangeVariables.length; i++) {
            Table    rangeTable = rangeVariables[i].rangeTable;
            HsqlName name       = rangeTable.getName();

            if (rangeTable.isReadOnly() || rangeTable.isTemp()) {
                continue;
            }

            if (name.schema == SqlInvariants.SYSTEM_SCHEMA_HSQLNAME) {
                continue;
            }

            set.add(name);
        }

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

    Object[] getExpressionValues(Session session) {

        Object[] values;

        if (expression.getType() == OpTypes.ROW) {
            values = expression.getRowValue(session);
        } else if (expression.getType() == OpTypes.ROW_SUBQUERY) {
            values = expression.subQuery.queryExpression.getSingleRowValues(
                session);

            if (values == null) {

                // todo - verify semantics
                return null;
            }
        } else {
            values    = new Object[1];
            values[0] = expression.getValue(session);
        }

        return values;
    }

    Result executeAssignment(Session session, Object[] values) {

        for (int j = 0; j < values.length; j++) {
            Object[] data = ValuePool.emptyObjectArray;

            switch (targets[j].getColumn().getType()) {

                case SchemaObject.PARAMETER :
                    data = session.sessionContext.routineArguments;
                    break;

                case SchemaObject.VARIABLE :
                    data = session.sessionContext.routineVariables;
                    break;
            }

            int colIndex = variableIndexes[j];

            if (targets[j].getType() == OpTypes.ARRAY_ACCESS) {
                data[colIndex] =
                    ((ExpressionAccessor) targets[j]).getUpdatedArray(session,
                        (Object[]) data[colIndex], values[j], true);
            } else {
                data[colIndex] = values[j];
            }
        }

        return Result.updateZeroResult;
    }
}
