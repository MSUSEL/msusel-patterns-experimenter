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
import org.hsqldb.navigator.RangeIterator;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.navigator.RowSetNavigatorClient;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultConstants;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.types.Type;

/**
 * Implementation of Statement for INSERT statements.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class StatementInsert extends StatementDML {

    int            generatedType;
    ResultMetaData generatedInputMetaData;
    boolean        isSimpleInsert;
    int            overrideUserValue = -1;

    /**
     * Instantiate this as an INSERT_VALUES statement.
     */
    StatementInsert(Session session, Table targetTable, int[] columnMap,
                    Expression insertExpression, boolean[] checkColumns,
                    CompileContext compileContext) {

        super(StatementTypes.INSERT, StatementTypes.X_SQL_DATA_CHANGE,
              session.getCurrentSchemaHsqlName());

        this.targetTable = targetTable;
        this.baseTable   = targetTable.isTriggerInsertable() ? targetTable
                                                             : targetTable
                                                             .getBaseTable();
        this.insertColumnMap    = columnMap;
        this.insertCheckColumns = checkColumns;
        this.insertExpression   = insertExpression;

        setDatabseObjects(session, compileContext);
        checkAccessRights(session);
        setupChecks();

        isSimpleInsert = insertExpression != null
                         && insertExpression.nodes.length == 1
                         && updatableTableCheck == null;
    }

    /**
     * Instantiate this as an INSERT_SELECT statement.
     */
    StatementInsert(Session session, Table targetTable, int[] columnMap,
                    boolean[] checkColumns, QueryExpression queryExpression,
                    CompileContext compileContext, int override) {

        super(StatementTypes.INSERT, StatementTypes.X_SQL_DATA_CHANGE,
              session.getCurrentSchemaHsqlName());

        this.targetTable = targetTable;
        this.baseTable   = targetTable.isTriggerInsertable() ? targetTable
                                                             : targetTable
                                                             .getBaseTable();
        this.insertColumnMap    = columnMap;
        this.insertCheckColumns = checkColumns;
        this.queryExpression    = queryExpression;
        this.overrideUserValue  = override;

        setDatabseObjects(session, compileContext);
        checkAccessRights(session);
        setupChecks();
    }

    /**
     * Executes an INSERT_SELECT or INSERT_VALUESstatement.  It is assumed that
     * the argument is of the correct type.
     *
     * @return the result of executing the statement
     */
    Result getResult(Session session) {

        Result          resultOut          = null;
        RowSetNavigator generatedNavigator = null;
        PersistentStore store              = baseTable.getRowStore(session);

        if (generatedIndexes != null) {
            resultOut = Result.newUpdateCountResult(generatedResultMetaData,
                    0);
            generatedNavigator = resultOut.getChainedResult().getNavigator();
        }

        if (isSimpleInsert) {
            Type[] colTypes = baseTable.getColumnTypes();
            Object[] data = getInsertData(session, colTypes,
                                          insertExpression.nodes[0].nodes);

            return insertSingleRow(session, store, data);
        }

        RowSetNavigator newDataNavigator = queryExpression == null
                                           ? getInsertValuesNavigator(session)
                                           : getInsertSelectNavigator(session);

        if (newDataNavigator.getSize() > 0) {
            insertRowSet(session, generatedNavigator, newDataNavigator);
        }

        if (baseTable.triggerLists[Trigger.INSERT_AFTER].length > 0) {
            baseTable.fireTriggers(session, Trigger.INSERT_AFTER,
                                   newDataNavigator);
        }

        if (resultOut == null) {
            resultOut = new Result(ResultConstants.UPDATECOUNT,
                                   newDataNavigator.getSize());
        } else {
            resultOut.setUpdateCount(newDataNavigator.getSize());
        }

        return resultOut;
    }

    RowSetNavigator getInsertSelectNavigator(Session session) {

        Type[] colTypes  = baseTable.getColumnTypes();
        int[]  columnMap = insertColumnMap;

        //
        Result                result = queryExpression.getResult(session, 0);
        RowSetNavigator       nav         = result.initialiseNavigator();
        Type[]                sourceTypes = result.metaData.columnTypes;
        RowSetNavigatorClient newData     = new RowSetNavigatorClient(2);

        while (nav.hasNext()) {
            Object[] data       = baseTable.getNewRowData(session);
            Object[] sourceData = (Object[]) nav.getNext();

            for (int i = 0; i < columnMap.length; i++) {
                int j = columnMap[i];

                if (j == this.overrideUserValue) {
                    continue;
                }

                Type sourceType = sourceTypes[i];

                data[j] = colTypes[j].convertToType(session, sourceData[i],
                                                    sourceType);
            }

            newData.add(data);
        }

        return newData;
    }

    RowSetNavigator getInsertValuesNavigator(Session session) {

        Type[] colTypes = baseTable.getColumnTypes();

        //
        Expression[]          list    = insertExpression.nodes;
        RowSetNavigatorClient newData = new RowSetNavigatorClient(list.length);

        for (int j = 0; j < list.length; j++) {
            Expression[] rowArgs = list[j].nodes;
            Object[]     data    = getInsertData(session, colTypes, rowArgs);

            newData.add(data);
        }

        return newData;
    }

    /**
     * @todo - fredt - this does not work with different prepare calls
     * with the same SQL statement, but different generated column requests
     * To fix, add comment encapsulating the generated column list to SQL
     * to differentiate between the two invocations
     */
    public void setGeneratedColumnInfo(int generate, ResultMetaData meta) {

        // can support INSERT_SELECT also
        if (type != StatementTypes.INSERT) {
            return;
        }

        int colIndex = baseTable.getIdentityColumnIndex();

        if (colIndex == -1) {
            return;
        }

        generatedType          = generate;
        generatedInputMetaData = meta;

        switch (generate) {

            case ResultConstants.RETURN_NO_GENERATED_KEYS :
                return;

            case ResultConstants.RETURN_GENERATED_KEYS_COL_INDEXES :
                int[] columnIndexes = meta.getGeneratedColumnIndexes();

                if (columnIndexes.length != 1) {
                    return;
                }

                if (columnIndexes[0] != colIndex) {
                    return;
                }

            // fall through
            case ResultConstants.RETURN_GENERATED_KEYS :
                generatedIndexes = new int[]{ colIndex };
                break;

            case ResultConstants.RETURN_GENERATED_KEYS_COL_NAMES :
                String[] columnNames = meta.getGeneratedColumnNames();

                if (columnNames.length != 1) {
                    return;
                }

                if (baseTable.findColumn(columnNames[0]) != colIndex) {
                    return;
                }

                generatedIndexes = new int[]{ colIndex };
                break;
        }

        generatedResultMetaData =
            ResultMetaData.newResultMetaData(generatedIndexes.length);

        for (int i = 0; i < generatedIndexes.length; i++) {
            ColumnSchema column = baseTable.getColumn(generatedIndexes[i]);

            generatedResultMetaData.columns[i] = column;
        }

        generatedResultMetaData.prepareData();

        isSimpleInsert = false;
    }
}
