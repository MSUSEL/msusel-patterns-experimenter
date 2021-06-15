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

import java.sql.Types;

// sqlbob@users 20020325 - patch 1.7.0 - reengineering

/**
 * Conversions from SQLServer7 databases
 *
 * @version 1.7.0
 */
class SqlServerTransferHelper extends TransferHelper {

    private boolean firstTinyintRow;
    private boolean firstSmallintRow;

    SqlServerTransferHelper() {
        super();
    }

    SqlServerTransferHelper(TransferDb database, Traceable t, String q) {
        super(database, t, q);
    }

    String formatTableName(String t) {

        if (t == null) {
            return t;
        }

        if (t.equals("")) {
            return t;
        }

        if (t.indexOf(' ') != -1) {
            return ("[" + t + "]");
        } else {
            return (formatIdentifier(t));
        }
    }

    int convertFromType(int type) {

        // MS SQL 7 specific problems (Northwind database)
        if (type == 11) {
            tracer.trace("Converted DATETIME (type 11) to TIMESTAMP");

            type = Types.TIMESTAMP;
        } else if (type == -9) {
            tracer.trace("Converted NVARCHAR (type -9) to VARCHAR");

            type = Types.VARCHAR;
        } else if (type == -8) {
            tracer.trace("Converted NCHAR (type -8) to VARCHAR");

            type = Types.VARCHAR;
        } else if (type == -10) {
            tracer.trace("Converted NTEXT (type -10) to VARCHAR");

            type = Types.VARCHAR;
        } else if (type == -1) {
            tracer.trace("Converted LONGTEXT (type -1) to LONGVARCHAR");

            type = Types.LONGVARCHAR;
        }

        return (type);
    }

    void beginTransfer() {
        firstSmallintRow = true;
        firstTinyintRow  = true;
    }

    Object convertColumnValue(Object value, int column, int type) {

        // solves a problem for MS SQL 7
        if ((type == Types.SMALLINT) && (value instanceof Integer)) {
            if (firstSmallintRow) {
                firstSmallintRow = false;

                tracer.trace("SMALLINT: Converted column " + column
                             + " Integer to Short");
            }

            value = new Short((short) ((Integer) value).intValue());
        } else if ((type == Types.TINYINT) && (value instanceof Integer)) {
            if (firstTinyintRow) {
                firstTinyintRow = false;

                tracer.trace("TINYINT: Converted column " + column
                             + " Integer to Byte");
            }

            value = new Byte((byte) ((Integer) value).intValue());
        }

        return (value);
    }
}
