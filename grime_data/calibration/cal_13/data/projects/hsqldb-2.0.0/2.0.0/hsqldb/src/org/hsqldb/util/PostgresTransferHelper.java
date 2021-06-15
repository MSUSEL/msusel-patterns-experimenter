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

package org.hsqldb.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

// fredt@users 20020215 - patch 516309 by Nicolas Bazin - transfer PostgresSQL
// sqlbob@users 20020325 - patch 1.7.0 - reengineering

/**
 * Conversions from PostgresSQL databases
 *
 * @author Nichola Bazin
 * @version 1.7.0
 */
class PostgresTransferHelper extends TransferHelper {

    private final int PostgreSQL = 0;
    private final int HSQLDB     = 1;
    String[][]        Funcs      = {
        {
            "now()", "\'now\'"
        }
    };

    PostgresTransferHelper() {
        super();
    }

    PostgresTransferHelper(TransferDb database, Traceable t, String q) {
        super(database, t, q);
    }

    int convertToType(int type) {

        if (type == Types.DECIMAL) {
            type = Types.NUMERIC;

            tracer.trace("Converted DECIMAL to NUMERIC");
        }

        return (type);
    }

    String fixupColumnDefRead(TransferTable t, ResultSetMetaData meta,
                              String columnType, ResultSet columnDesc,
                              int columnIndex) throws SQLException {

        String SeqName   = new String("_" + columnDesc.getString(4) + "_seq");
        int    spaceleft = 31 - SeqName.length();

        if (t.Stmts.sDestTable.length() > spaceleft) {
            SeqName = t.Stmts.sDestTable.substring(0, spaceleft) + SeqName;
        } else {
            SeqName = t.Stmts.sDestTable + SeqName;
        }

        String CompareString = "nextval(\'\"" + SeqName + "\"\'";

        if (columnType.indexOf(CompareString) >= 0) {

            // We just found a increment
            columnType = "SERIAL";
        }

        for (int Idx = 0; Idx < Funcs.length; Idx++) {
            String PostgreSQL_func = Funcs[Idx][PostgreSQL];
            int    iStartPos       = columnType.indexOf(PostgreSQL_func);

            if (iStartPos >= 0) {
                String NewColumnType = columnType.substring(0, iStartPos);

                NewColumnType += Funcs[Idx][HSQLDB];
                NewColumnType +=
                    columnType.substring(iStartPos
                                         + PostgreSQL_func.length());
                columnType = NewColumnType;
            }
        }

        return (columnType);
    }

    String fixupColumnDefWrite(TransferTable t, ResultSetMetaData meta,
                               String columnType, ResultSet columnDesc,
                               int columnIndex) throws SQLException {

        if (columnType.equals("SERIAL")) {
            String SeqName = new String("_" + columnDesc.getString(4)
                                        + "_seq");
            int spaceleft = 31 - SeqName.length();

            if (t.Stmts.sDestTable.length() > spaceleft) {
                SeqName = t.Stmts.sDestTable.substring(0, spaceleft)
                          + SeqName;
            } else {
                SeqName = t.Stmts.sDestTable + SeqName;
            }

            String DropSequence = "DROP SEQUENCE " + SeqName + ";";

            t.Stmts.sDestDrop += DropSequence;
        }

        for (int Idx = 0; Idx < Funcs.length; Idx++) {
            String HSQLDB_func = Funcs[Idx][HSQLDB];
            int    iStartPos   = columnType.indexOf(HSQLDB_func);

            if (iStartPos >= 0) {
                String NewColumnType = columnType.substring(0, iStartPos);

                NewColumnType += Funcs[Idx][PostgreSQL];
                NewColumnType += columnType.substring(iStartPos
                                                      + HSQLDB_func.length());
                columnType = NewColumnType;
            }
        }

        return (columnType);
    }

    void beginDataTransfer() {

        try {
            db.setAutoCommit(false);
        } catch (Exception e) {}
    }

    void endDataTransfer() {

        try {
            db.commit();
            db.execute("VACUUM ANALYZE");
        } catch (Exception e) {}
    }
}
