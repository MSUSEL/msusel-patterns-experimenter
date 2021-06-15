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
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.store.ValuePool;
import org.hsqldb.types.Type;

/**
 * Table with data derived from a query expression.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class TableDerived extends Table {

    QueryExpression queryExpression;
    View            view;
    SubQuery        subQuery;

    public TableDerived(Database database, HsqlName name, int type) {

        super(database, name, type);

        switch (type) {

            case TableBase.FUNCTION_TABLE :
            case TableBase.VIEW_TABLE :
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "Table");
        }
    }

    public TableDerived(Database database, HsqlName name, int type,
                        QueryExpression queryExpression, SubQuery subQuery) {

        super(database, name, type);

        switch (type) {

            case TableBase.SYSTEM_SUBQUERY :
            case TableBase.VIEW_TABLE :
            case TableBase.RESULT_TABLE :
                break;

            default :
                throw Error.runtimeError(ErrorCode.U_S0500, "Table");
        }

        this.queryExpression = queryExpression;
        this.subQuery        = subQuery;
    }

    public TableDerived(Database database, HsqlName name, int type,
                        Type[] columnTypes, HashMappedList columnList,
                        QueryExpression queryExpression, SubQuery subQuery) {

        this(database, name, type, queryExpression, subQuery);

        this.colTypes          = columnTypes;
        this.columnList        = columnList;
        columnCount            = columnList.size();
        primaryKeyCols         = ValuePool.emptyIntArray;
        primaryKeyTypes        = Type.emptyArray;
        primaryKeyColsSequence = ValuePool.emptyIntArray;
        colDefaults            = new Expression[columnCount];
        colNotNull             = new boolean[columnCount];
        defaultColumnMap       = new int[columnCount];

        ArrayUtil.fillSequence(defaultColumnMap);

        bestIndexForColumn = new int[colTypes.length];

        ArrayUtil.fillArray(bestIndexForColumn, -1);
        createPrimaryIndex(primaryKeyCols, primaryKeyTypes, null);
    }

    public int getId() {
        return 0;
    }

    public boolean isWritable() {
        return true;
    }

    public boolean isInsertable() {
        return queryExpression == null ? false
                                       : queryExpression.isInsertable();
    }

    public boolean isUpdatable() {
        return queryExpression == null ? false
                                       : queryExpression.isUpdatable();
    }

    public int[] getUpdatableColumns() {
        return defaultColumnMap;
    }

    public Table getBaseTable() {
        return queryExpression == null ? this
                                       : queryExpression.getBaseTable();
    }

    public int[] getBaseTableColumnMap() {

        return queryExpression == null ? null
                                       : queryExpression
                                           .getBaseTableColumnMap();
    }

    public SubQuery getSubQuery() {
        return subQuery;
    }

    public QueryExpression getQueryExpression() {
        return queryExpression;
    }
}
