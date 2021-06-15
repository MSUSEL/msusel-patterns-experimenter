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

package org.hsqldb.jdbc;

import java.lang.reflect.Field;

// fredt@users - 1.9.0 rewritten as simple structure derived from JDBCResultSetMetaData

/**
 * Provides a site for holding the ResultSetMetaData for individual ResultSet
 * columns. In 2.0 it is implemented as a simple data structure derived
 * from calls to JDBCResultSetMetaData methods.
 * purposes.<p>
 *
 * @author Campbell Boucher-Burnett (boucherb@users dot sourceforge.net)
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0
 * @since HSQLDB 1.7.2
 */
public final class JDBCColumnMetaData {

    /** The column's table's catalog name. */
    public String catalogName;

    /**
     * The fully-qualified name of the Java class whose instances are
     * manufactured if the method ResultSet.getObject is called to retrieve
     * a value from the column.
     */
    public String columnClassName;

    /** The column's normal max width in chars. */
    public int columnDisplaySize;

    /** The suggested column title for use in printouts and displays. */
    public String columnLabel;

    /** The column's name. */
    public String columnName;

    /** The column's SQL type. */
    public int columnType;

    /** The column's value's number of decimal digits. */
    public int precision;

    /** The column's value's number of digits to right of the decimal point. */
    public int scale;

    /** The column's table's schema. */
    public String schemaName;

    /** The column's table's name. */
    public String tableName;

    /** Whether the value of the column are automatically numbered. */
    public boolean isAutoIncrement;

    /** Whether the column's value's case matters. */
    public boolean isCaseSensitive;

    /** Whether the values in the column are cash values. */
    public boolean isCurrency;

    /** Whether a write on the column will definitely succeed. */
    public boolean isDefinitelyWritable;

    /** The nullability of values in the column. */
    public int isNullable;

    /** Whether the column's values are definitely not writable. */
    public boolean isReadOnly;

    /** Whether the column's values can be used in a where clause. */
    public boolean isSearchable;

    /** Whether values in the column are signed numbers. */
    public boolean isSigned;

    /** Whether it is possible for a write on the column to succeed. */
    public boolean isWritable;

    /**
     * Retrieves a String representation of this object.
     *
     * @return a Sring representation of this object
     */
    public String toString() {

        try {
            return toStringImpl();
        } catch (Exception e) {
            return super.toString() + "[" + e + "]";
        }
    }

    /**
     * Provides the implementation of the toString() method.
     *
     * @return a Sring representation of this object
     */
    private String toStringImpl() throws Exception {

        StringBuffer sb;
        Field[]      fields;
        Field        field;

        sb = new StringBuffer();

        sb.append('[');

        fields = getClass().getFields();

        int len = fields.length;

        for (int i = 0; i < len; i++) {
            field = fields[i];

            sb.append(field.getName());
            sb.append('=');
            sb.append(field.get(this));

            if (i + 1 < len) {
                sb.append(',');
                sb.append(' ');
            }
        }
        sb.append(']');

        return sb.toString();
    }
}
