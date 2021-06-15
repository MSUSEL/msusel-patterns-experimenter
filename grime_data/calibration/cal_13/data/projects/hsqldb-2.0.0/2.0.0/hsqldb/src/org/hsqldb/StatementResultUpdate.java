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
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.navigator.RowSetNavigatorData;
import org.hsqldb.navigator.RowSetNavigatorDataChange;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultConstants;
import org.hsqldb.types.Type;

public class StatementResultUpdate extends StatementDML {

    int    actionType;
    Type[] types;
    Result result;

    StatementResultUpdate() {

        super();

        isTransactionStatement = true;
    }

    public String describe(Session session) {
        return "";
    }

    public Result execute(Session session) {

        try {
            return getResult(session);
        } catch (Throwable e) {
            return Result.newErrorResult(e, null);
        }
    }

    Result getResult(Session session) {

        checkAccessRights(session);

        Object[]        args = session.sessionContext.dynamicArguments;
        Row             row;
        PersistentStore store = baseTable.getRowStore(session);

        switch (actionType) {

            case ResultConstants.UPDATE_CURSOR : {
                row = getRow(session, args);

                /**
                 * @todo - in 2PL mode isDeleted() always returns false.
                 * While write lock prevents delete by other transactions,
                 * same-transaction deletes are not caught
                 */
                if (row == null || row.isDeleted(session, store)) {
                    throw Error.error(ErrorCode.X_24521);
                }

                RowSetNavigatorDataChange list =
                    new RowSetNavigatorDataChange();
                Object[] data =
                    (Object[]) ArrayUtil.duplicateArray(row.getData());
                boolean[] columnCheck = baseTable.getNewColumnCheckList();

                for (int i = 0; i < baseColumnMap.length; i++) {
                    if (types[i] == Type.SQL_ALL_TYPES) {
                        continue;
                    }

                    data[baseColumnMap[i]]        = args[i];
                    columnCheck[baseColumnMap[i]] = true;
                }

                int[] colMap = ArrayUtil.booleanArrayToIntIndexes(columnCheck);

                list.addRow(session, row, data, baseTable.getColumnTypes(),
                            colMap);
                update(session, baseTable, list);

                break;
            }
            case ResultConstants.DELETE_CURSOR : {
                row = getRow(session, args);

                if (row == null || row.isDeleted(session, store)) {
                    throw Error.error(ErrorCode.X_24521);
                }

                RowSetNavigatorDataChange navigator =
                    new RowSetNavigatorDataChange();

                navigator.addRow(row);
                delete(session, baseTable, navigator);

                break;
            }
            case ResultConstants.INSERT_CURSOR : {
                Object[] data = baseTable.getNewRowData(session);

                for (int i = 0; i < data.length; i++) {
                    data[baseColumnMap[i]] = args[i];
                }

                return insertSingleRow(session, store, data);
            }
        }

        return Result.updateOneResult;
    }

    Row getRow(Session session, Object[] args) {

        int             rowIdIndex = result.metaData.getColumnCount();
        Long            rowId      = (Long) args[rowIdIndex];
        PersistentStore store = baseTable.getRowStore(session);
        Row             row        = null;

        if (rowIdIndex + 2 == result.metaData.getExtendedColumnCount()) {
            Object[] data =
                ((RowSetNavigatorData) result.getNavigator()).getData(
                    rowId.longValue());

            if (data != null) {
                row = (Row) data[rowIdIndex + 1];
            }
        } else {
            int id = (int) rowId.longValue();

            row = (Row) store.get(id, false);
        }

        this.result = null;

        return row;
    }

    void setRowActionProperties(Result result, int action, Table table,
                                Type[] types, int[] columnMap) {

        this.result        = result;
        this.actionType    = action;
        this.baseTable     = table;
        this.types         = types;
        this.baseColumnMap = columnMap;
    }

    void checkAccessRights(Session session) {

        switch (type) {

            case StatementTypes.CALL : {
                break;
            }
            case StatementTypes.INSERT : {
                session.getGrantee().checkInsert(targetTable,
                                                 insertCheckColumns);

                break;
            }
            case StatementTypes.SELECT_CURSOR :
                break;

            case StatementTypes.DELETE_WHERE : {
                session.getGrantee().checkDelete(targetTable);

                break;
            }
            case StatementTypes.UPDATE_WHERE : {
                session.getGrantee().checkUpdate(targetTable,
                                                 updateCheckColumns);

                break;
            }
            case StatementTypes.MERGE : {
                session.getGrantee().checkInsert(targetTable,
                                                 insertCheckColumns);
                session.getGrantee().checkUpdate(targetTable,
                                                 updateCheckColumns);

                break;
            }
        }
    }
}
