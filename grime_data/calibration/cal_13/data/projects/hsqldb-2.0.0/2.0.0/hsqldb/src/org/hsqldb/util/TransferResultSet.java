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
import java.sql.SQLException;
import java.util.Vector;

/**
 * Helper class for transferring a result set
 *
 * @author Nicolas BAZIN
 * @version 1.7.0
 */
class TransferResultSet {

    Vector   vRows = null;
    int      iRowIdx;
    int      iMaxRowIdx;
    int      iColumnCount;
    String[] sColumnNames = null;
    int[]    iColumnTypes = null;

    TransferResultSet(ResultSet r) {

        iRowIdx      = 0;
        iMaxRowIdx   = 0;
        iColumnCount = 0;
        vRows        = new Vector();

        try {
            while (r.next()) {
                if (sColumnNames == null) {
                    iColumnCount = r.getMetaData().getColumnCount();
                    sColumnNames = new String[iColumnCount + 1];
                    iColumnTypes = new int[iColumnCount + 1];

                    for (int Idx = 0; Idx < iColumnCount; Idx++) {
                        sColumnNames[Idx + 1] =
                            r.getMetaData().getColumnName(Idx + 1);
                        iColumnTypes[Idx + 1] =
                            r.getMetaData().getColumnType(Idx + 1);
                    }

                    vRows.addElement(null);
                }

                iMaxRowIdx++;

                Object[] Values = new Object[iColumnCount + 1];

                for (int Idx = 0; Idx < iColumnCount; Idx++) {
                    Values[Idx + 1] = r.getObject(Idx + 1);
                }

                vRows.addElement(Values);
            }
        } catch (SQLException SQLE) {
            iRowIdx      = 0;
            iMaxRowIdx   = 0;
            iColumnCount = 0;
            vRows        = new Vector();
        }
    }

    TransferResultSet() {

        iRowIdx      = 0;
        iMaxRowIdx   = 0;
        iColumnCount = 0;
        vRows        = new Vector();
    }

    void addRow(String[] Name, int[] type, Object[] Values,
                int nbColumns) throws Exception {

        if ((Name.length != type.length) || (Name.length != Values.length)
                || (Name.length != (nbColumns + 1))) {
            throw new Exception("Size of parameter incoherent");
        }

        if (sColumnNames == null) {
            iColumnCount = nbColumns;
            sColumnNames = Name;
            iColumnTypes = type;

            vRows.addElement(null);
        }

        if ((iMaxRowIdx > 0) && (this.getColumnCount() != nbColumns)) {
            throw new Exception("Wrong number of columns: "
                                + this.getColumnCount()
                                + " column is expected");
        }

        iMaxRowIdx++;

        vRows.addElement(Values);
    }

    boolean next() {

        iRowIdx++;

        return ((iRowIdx <= iMaxRowIdx) && (iMaxRowIdx > 0));
    }

    String getColumnName(int columnIdx) {

        if ((iMaxRowIdx <= 0) || (iMaxRowIdx < iRowIdx)) {
            return null;
        }

        return sColumnNames[columnIdx];
    }

    int getColumnCount() {

        if ((iMaxRowIdx <= 0) || (iMaxRowIdx < iRowIdx)) {
            return 0;
        }

        return iColumnCount;
    }

    int getColumnType(int columnIdx) {

        if ((iMaxRowIdx <= 0) || (iMaxRowIdx < iRowIdx)) {
            return 0;
        }

        return iColumnTypes[columnIdx];
    }

    Object getObject(int columnIdx) {

        if ((iMaxRowIdx <= 0) || (iMaxRowIdx < iRowIdx)) {
            return null;
        }

        return ((Object[]) vRows.elementAt(iRowIdx))[columnIdx];
    }
}
