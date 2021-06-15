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

import java.util.Hashtable;

/**
 * Base class for conversion from a different databases
 *
 * @author Nicolas BAZIN
 * @version 1.7.0
 */
class JDBCTypes {

    public static final int JAVA_OBJECT = 2000;
    public static final int DISTINCT    = 2001;
    public static final int STRUCT      = 2002;
    public static final int ARRAY       = 2003;
    public static final int BLOB        = 2004;
    public static final int CLOB        = 2005;
    public static final int REF         = 2006;
    private Hashtable       hStringJDBCtypes;
    private Hashtable       hIntJDBCtypes;

    JDBCTypes() {

        hStringJDBCtypes = new Hashtable();
        hIntJDBCtypes    = new Hashtable();

        hStringJDBCtypes.put(new Integer(ARRAY), "ARRAY");
        hStringJDBCtypes.put(new Integer(BLOB), "BLOB");
        hStringJDBCtypes.put(new Integer(CLOB), "CLOB");
        hStringJDBCtypes.put(new Integer(DISTINCT), "DISTINCT");
        hStringJDBCtypes.put(new Integer(JAVA_OBJECT), "JAVA_OBJECT");
        hStringJDBCtypes.put(new Integer(REF), "REF");
        hStringJDBCtypes.put(new Integer(STRUCT), "STRUCT");

        //
        hStringJDBCtypes.put(new Integer(java.sql.Types.BIGINT), "BIGINT");
        hStringJDBCtypes.put(new Integer(java.sql.Types.BINARY), "BINARY");
        hStringJDBCtypes.put(new Integer(java.sql.Types.BIT), "BIT");
        hStringJDBCtypes.put(new Integer(java.sql.Types.CHAR), "CHAR");
        hStringJDBCtypes.put(new Integer(java.sql.Types.DATE), "DATE");
        hStringJDBCtypes.put(new Integer(java.sql.Types.DECIMAL), "DECIMAL");
        hStringJDBCtypes.put(new Integer(java.sql.Types.DOUBLE), "DOUBLE");
        hStringJDBCtypes.put(new Integer(java.sql.Types.FLOAT), "FLOAT");
        hStringJDBCtypes.put(new Integer(java.sql.Types.INTEGER), "INTEGER");
        hStringJDBCtypes.put(new Integer(java.sql.Types.LONGVARBINARY),
                             "LONGVARBINARY");
        hStringJDBCtypes.put(new Integer(java.sql.Types.LONGVARCHAR),
                             "LONGVARCHAR");
        hStringJDBCtypes.put(new Integer(java.sql.Types.NULL), "NULL");
        hStringJDBCtypes.put(new Integer(java.sql.Types.NUMERIC), "NUMERIC");
        hStringJDBCtypes.put(new Integer(java.sql.Types.OTHER), "OTHER");
        hStringJDBCtypes.put(new Integer(java.sql.Types.REAL), "REAL");
        hStringJDBCtypes.put(new Integer(java.sql.Types.SMALLINT),
                             "SMALLINT");
        hStringJDBCtypes.put(new Integer(java.sql.Types.TIME), "TIME");
        hStringJDBCtypes.put(new Integer(java.sql.Types.TIMESTAMP),
                             "TIMESTAMP");
        hStringJDBCtypes.put(new Integer(java.sql.Types.TINYINT), "TINYINT");
        hStringJDBCtypes.put(new Integer(java.sql.Types.VARBINARY),
                             "VARBINARY");
        hStringJDBCtypes.put(new Integer(java.sql.Types.VARCHAR), "VARCHAR");

        hStringJDBCtypes.put(new Integer(java.sql.Types.BOOLEAN), "BOOLEAN");
        //
        hIntJDBCtypes.put("ARRAY", new Integer(ARRAY));
        hIntJDBCtypes.put("BLOB", new Integer(BLOB));
        hIntJDBCtypes.put("CLOB", new Integer(CLOB));
        hIntJDBCtypes.put("DISTINCT", new Integer(DISTINCT));
        hIntJDBCtypes.put("JAVA_OBJECT", new Integer(JAVA_OBJECT));
        hIntJDBCtypes.put("REF", new Integer(REF));
        hIntJDBCtypes.put("STRUCT", new Integer(STRUCT));

        //
        hIntJDBCtypes.put("BIGINT", new Integer(java.sql.Types.BIGINT));
        hIntJDBCtypes.put("BINARY", new Integer(java.sql.Types.BINARY));
        hIntJDBCtypes.put("BIT", new Integer(java.sql.Types.BIT));
        hIntJDBCtypes.put("CHAR", new Integer(java.sql.Types.CHAR));
        hIntJDBCtypes.put("DATE", new Integer(java.sql.Types.DATE));
        hIntJDBCtypes.put("DECIMAL", new Integer(java.sql.Types.DECIMAL));
        hIntJDBCtypes.put("DOUBLE", new Integer(java.sql.Types.DOUBLE));
        hIntJDBCtypes.put("FLOAT", new Integer(java.sql.Types.FLOAT));
        hIntJDBCtypes.put("INTEGER", new Integer(java.sql.Types.INTEGER));
        hIntJDBCtypes.put("LONGVARBINARY",
                          new Integer(java.sql.Types.LONGVARBINARY));
        hIntJDBCtypes.put("LONGVARCHAR",
                          new Integer(java.sql.Types.LONGVARCHAR));
        hIntJDBCtypes.put("NULL", new Integer(java.sql.Types.NULL));
        hIntJDBCtypes.put("NUMERIC", new Integer(java.sql.Types.NUMERIC));
        hIntJDBCtypes.put("OTHER", new Integer(java.sql.Types.OTHER));
        hIntJDBCtypes.put("REAL", new Integer(java.sql.Types.REAL));
        hIntJDBCtypes.put("SMALLINT", new Integer(java.sql.Types.SMALLINT));
        hIntJDBCtypes.put("TIME", new Integer(java.sql.Types.TIME));
        hIntJDBCtypes.put("TIMESTAMP", new Integer(java.sql.Types.TIMESTAMP));
        hIntJDBCtypes.put("TINYINT", new Integer(java.sql.Types.TINYINT));
        hIntJDBCtypes.put("VARBINARY", new Integer(java.sql.Types.VARBINARY));
        hIntJDBCtypes.put("VARCHAR", new Integer(java.sql.Types.VARCHAR));
        hIntJDBCtypes.put("BOOLEAN", new Integer(java.sql.Types.BOOLEAN));
    }

    public Hashtable getHashtable() {
        return hStringJDBCtypes;
    }

    public String toString(int type) {
        return (String) hStringJDBCtypes.get(new Integer(type));
    }

    public int toInt(String type) throws Exception {

        Integer tempInteger = (Integer) hIntJDBCtypes.get(type);

        return tempInteger.intValue();
    }
}
