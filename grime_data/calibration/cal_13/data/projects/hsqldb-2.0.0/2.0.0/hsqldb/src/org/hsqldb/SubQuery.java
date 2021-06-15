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

import java.util.Comparator;

import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.HsqlNameManager.SimpleName;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.navigator.RowSetNavigatorData;
import org.hsqldb.navigator.RowSetNavigatorDataTable;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;

/**
 * Represents an SQL view or anonymous subquery (inline virtual table
 * descriptor) nested within an SQL statement. <p>
 *
 * Implements {@link org.hsqldb.lib.ObjectComparator ObjectComparator} to
 * provide the correct order of materialization for nested views / subqueries.
 *
 * @author Campbell Boucher-Burnett (boucherb@users dot sourceforge.net)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 */
class SubQuery implements Comparator {

    int                  level;
    private boolean      isResolved;
    private boolean      isCorrelated;
    private boolean      isExistsPredicate;
    private boolean      uniqueRows;
    private boolean      fullOrder;
    QueryExpression      queryExpression;
    Database             database;
    private TableDerived table;
    View                 view;
    View                 parentView;
    String               sql;

    // IN condition optimisation
    Expression dataExpression;
    boolean    isDataExpression;

    //
    SimpleName[] columnNames;

    //
    int parsePosition;

    //
    public final static SubQuery[] emptySubqueryArray = new SubQuery[]{};

    SubQuery(Database database, int level, QueryExpression queryExpression,
             int mode) {

        this.level           = level;
        this.queryExpression = queryExpression;
        this.database        = database;

        switch (mode) {

            case OpTypes.EXISTS :
                isExistsPredicate = true;
                break;

            case OpTypes.IN :
                uniqueRows = true;

                if (queryExpression != null) {
                    queryExpression.setFullOrder();
                }
                break;

            case OpTypes.UNIQUE :
                fullOrder = true;

                if (queryExpression != null) {
                    queryExpression.setFullOrder();
                }
        }
    }

    SubQuery(Database database, int level, QueryExpression queryExpression,
             View view) {

        this.level           = level;
        this.queryExpression = queryExpression;
        this.database        = database;
        this.view            = view;
    }

    SubQuery(Database database, int level, Expression dataExpression,
             int mode) {

        this.level              = level;
        this.database           = database;
        this.dataExpression     = dataExpression;
        dataExpression.subQuery = this;
        isDataExpression        = true;

        switch (mode) {

            case OpTypes.IN :
                uniqueRows = true;
                break;
        }
    }

    public boolean isResolved() {
        return isResolved;
    }

    public boolean isCorrelated() {
        return isCorrelated;
    }

    public void setCorrelated() {
        isCorrelated = true;
    }

    public void setUniqueRows() {
        uniqueRows = true;
    }

    public TableDerived getTable() {
        return table;
    }

    public void createTable() {

        HsqlName name = database.nameManager.getSubqueryTableName();

        table = new TableDerived(database, name, TableBase.SYSTEM_SUBQUERY,
                                 queryExpression, this);
    }

    public void prepareTable(Session session, HsqlName name,
                             HsqlName[] columns) {

        if (isResolved) {
            return;
        }

        if (table == null) {
            table = new TableDerived(database, name,
                                     TableBase.SYSTEM_SUBQUERY,
                                     queryExpression, this);
        }

        if (columns != null && queryExpression != null) {
            queryExpression.getMainSelect().setColumnAliases(columns);
        }

        table.columnList  = queryExpression.getColumns();
        table.columnCount = queryExpression.getColumnCount();

        TableUtil.setTableIndexesForSubquery(table, uniqueRows || fullOrder,
                                             uniqueRows);

        isResolved = true;
    }

    public void prepareTable(Session session) {

        if (isResolved) {
            return;
        }

        if (view == null) {
            if (table == null) {
                HsqlName name = database.nameManager.getSubqueryTableName();

                table = new TableDerived(database, name,
                                         TableBase.SYSTEM_SUBQUERY,
                                         queryExpression, this);
            }

            if (isDataExpression) {
                TableUtil.addAutoColumns(table, dataExpression.nodeDataTypes);
                TableUtil.setTableIndexesForSubquery(table,
                                                     uniqueRows || fullOrder,
                                                     uniqueRows);
            } else {
                table.columnList  = queryExpression.getColumns();
                table.columnCount = queryExpression.getColumnCount();

                TableUtil.setTableIndexesForSubquery(table,
                                                     uniqueRows || fullOrder,
                                                     uniqueRows);
            }
        } else {
            table = new TableDerived(database, view.getName(),
                                     TableBase.VIEW_TABLE, queryExpression,
                                     this);
            table.columnList  = view.columnList;
            table.columnCount = table.columnList.size();

            table.createPrimaryKey();
        }

        isResolved = true;
    }

    public void setColumnNames(SimpleName[] names) {
        columnNames = names;
    }

    public SimpleName[] gtColumnNames() {
        return columnNames;
    }

    public void materialiseCorrelated(Session session) {

        if (isCorrelated) {
            materialise(session);
        }
    }

    /**
     * Fills the table with a result set
     */
    public void materialise(Session session) {

        PersistentStore store;

        // table constructors
        if (isDataExpression) {
            store = session.sessionData.getSubqueryRowStore(table);

            dataExpression.insertValuesIntoSubqueryTable(session, store);

            return;
        }

        Result result = queryExpression.getResult(session,
            isExistsPredicate ? 1
                              : 0);

        if (uniqueRows) {
            RowSetNavigatorData navigator =
                ((RowSetNavigatorData) result.getNavigator());

            navigator.removeDuplicates();
        }

        store = session.sessionData.getSubqueryRowStore(table);

        table.insertResult(session, store, result);
        result.getNavigator().close();
    }

    public boolean hasUniqueNotNullRows(Session session) {

        RowSetNavigatorData navigator = new RowSetNavigatorDataTable(session,
            table);
        boolean result = navigator.hasUniqueNotNullRows();

        return result;
    }

    public Object[] getValues(Session session) {

        RowIterator it = table.rowIterator(session);

        if (it.hasNext()) {
            Row row = it.getNextRow();

            if (it.hasNext()) {
                throw Error.error(ErrorCode.X_21000);
            }

            return row.getData();
        } else {
            return new Object[table.getColumnCount()];
        }
    }

    public Object getValue(Session session) {

        Object[] data = getValues(session);

        return data[0];
    }

    public RowSetNavigatorData getNavigator(Session session) {

        RowSetNavigatorData navigator = new RowSetNavigatorDataTable(session,
            table);

        return navigator;
    }

    /**
     * This results in the following sort order:
     *
     * view subqueries, then other subqueries
     *
     *    view subqueries:
     *        views sorted by creation order (earlier declaration first)
     *
     *    other subqueries:
     *        subqueries sorted by depth within select query (deep == higher level)
     *
     */
    public int compare(Object a, Object b) {

        SubQuery sqa = (SubQuery) a;
        SubQuery sqb = (SubQuery) b;

        if (sqa.parentView == null && sqb.parentView == null) {
            return sqb.level - sqa.level;
        } else if (sqa.parentView != null && sqb.parentView != null) {
            int ia = database.schemaManager.getTableIndex(sqa.parentView);
            int ib = database.schemaManager.getTableIndex(sqb.parentView);

            if (ia == -1) {
                ia = database.schemaManager.getTables(
                    sqa.parentView.getSchemaName().name).size();
            }

            if (ib == -1) {
                ib = database.schemaManager.getTables(
                    sqb.parentView.getSchemaName().name).size();
            }

            int diff = ia - ib;

            return diff == 0 ? sqb.level - sqa.level
                             : diff;
        } else {
            return sqa.parentView == null ? 1
                                          : -1;
        }
    }
}
