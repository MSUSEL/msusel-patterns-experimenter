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
import java.util.Hashtable;

/**
 * Base class for conversion from a different databases
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @version 1.7.0
 */
class TransferHelper {

    protected TransferDb db;
    protected Traceable  tracer;
    protected String     sSchema;
    protected JDBCTypes  JDBCT;
    private String       quote;

    TransferHelper() {

        db     = null;
        tracer = null;
        quote  = "\'";
        JDBCT  = new JDBCTypes();
    }

    TransferHelper(TransferDb database, Traceable t, String q) {

        db     = database;
        tracer = t;
        quote  = q;
        JDBCT  = new JDBCTypes();
    }

    void set(TransferDb database, Traceable t, String q) {

        db     = database;
        tracer = t;
        quote  = q;
    }

    String formatIdentifier(String id) {

        if (id == null) {
            return id;
        }

        if (id.equals("")) {
            return id;
        }

        if (!id.toLowerCase().equals(id) && !id.toUpperCase().equals(id)) {
            return (quote + id + quote);
        }

        if (!Character.isLetter(id.charAt(0)) || (id.indexOf(' ') != -1)) {
            return (quote + id + quote);
        }

        return id;
    }

    void setSchema(String _Schema) {
        sSchema = _Schema;
    }

    String formatName(String t) {

        String Name = "";

        if ((sSchema != null) && (sSchema.length() > 0)) {
            Name = sSchema + ".";
        }

        Name += formatIdentifier(t);

        return Name;
    }

    int convertFromType(int type) {
        return (type);
    }

    int convertToType(int type) {
        return (type);
    }

    Hashtable getSupportedTypes() {

        Hashtable hTypes = new Hashtable();

        if (db != null) {
            try {
                ResultSet result = db.meta.getTypeInfo();

                while (result.next()) {
                    Integer intobj = new Integer(result.getShort(2));

                    if (hTypes.get(intobj) == null) {
                        try {
                            int typeNumber = result.getShort(2);

                            hTypes.put(intobj, JDBCT.toString(typeNumber));
                        } catch (Exception e) {}
                    }
                }

                result.close();
            } catch (SQLException e) {}
        }

        if (hTypes.isEmpty()) {
            hTypes = JDBCT.getHashtable();
        }

        return hTypes;
    }

    String fixupColumnDefRead(TransferTable t, ResultSetMetaData meta,
                              String columnType, ResultSet columnDesc,
                              int columnIndex) throws SQLException {
        return (columnType);
    }

    String fixupColumnDefWrite(TransferTable t, ResultSetMetaData meta,
                               String columnType, ResultSet columnDesc,
                               int columnIndex) throws SQLException {
        return (columnType);
    }

    boolean needTransferTransaction() {
        return (false);
    }

    Object convertColumnValue(Object value, int column, int type) {
        return (value);
    }

    void beginDataTransfer() {}

    void endDataTransfer() {}

    String fixupColumnDefRead(String aTableName, ResultSetMetaData meta,
                              String columnType, ResultSet columnDesc,
                              int columnIndex) throws SQLException {
        return columnType;
    }

    String fixupColumnDefWrite(String aTableName, ResultSetMetaData meta,
                               String columnType, ResultSet columnDesc,
                               int columnIndex) throws SQLException {
        return columnType;
    }
}
