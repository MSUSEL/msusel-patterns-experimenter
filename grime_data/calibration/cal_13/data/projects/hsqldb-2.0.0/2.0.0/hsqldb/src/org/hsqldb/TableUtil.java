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
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.types.Type;

/*
 * Utility functions to set up special tables.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class TableUtil {

    static Table newTable(Database database, int type,
                          HsqlName tableHsqlName) {

        switch (type) {

            case TableBase.TEMP_TEXT_TABLE :
            case TableBase.TEXT_TABLE : {
                return new TextTable(database, tableHsqlName, type);
            }
            default : {
                return new Table(database, tableHsqlName, type);
            }
        }
    }

    static Table newLookupTable(Database database) {

        TableDerived table;
        HsqlName     name = database.nameManager.getSubqueryTableName();

        table = new TableDerived(database, name, TableBase.SYSTEM_SUBQUERY,
                                 null, null);

        ColumnSchema column =
            new ColumnSchema(HsqlNameManager.getAutoColumnName(0),
                             Type.SQL_INTEGER, false, true, null);

        table.addColumn(column);
        table.createPrimaryKeyConstraint(table.getName(), new int[]{ 0 },
                                         true);

        return table;
    }

    static void setTableIndexesForSubquery(Table table,
                                           boolean fullIndex,
                                           boolean uniqueRows) {

        int[] cols = null;

        if (fullIndex) {
            cols = new int[table.getColumnCount()];

            ArrayUtil.fillSequence(cols);
        }

        table.createPrimaryKey(null, uniqueRows ? cols
                                                : null, false);

        if (uniqueRows) {
            table.fullIndex = table.getPrimaryIndex();
        } else if (fullIndex) {
            table.fullIndex = table.createIndexForColumns(null, cols);
        }
    }

    public static void addAutoColumns(Table table, Type[] colTypes) {

        for (int i = 0; i < colTypes.length; i++) {
            ColumnSchema column =
                new ColumnSchema(HsqlNameManager.getAutoColumnName(i),
                                 colTypes[i], true, false, null);

            table.addColumnNoCheck(column);
        }
    }

    public static void setColumnsInSchemaTable(Table table,
            HsqlName[] columnNames, Type[] columnTypes) {

        for (int i = 0; i < columnNames.length; i++) {
            HsqlName columnName = columnNames[i];

            columnName = table.database.nameManager.newColumnSchemaHsqlName(
                table.getName(), columnName);

            ColumnSchema column = new ColumnSchema(columnName, columnTypes[i],
                                                   true, false, null);

            table.addColumn(column);
        }

        table.setColumnStructures();
    }
}
