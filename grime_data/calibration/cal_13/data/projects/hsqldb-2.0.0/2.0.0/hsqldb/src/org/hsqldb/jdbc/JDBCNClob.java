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

import java.sql.NClob;

/**
 * The mapping in the Java<sup><font size=-2>TM</font></sup> programming language
 * for the SQL <code>NCLOB</code> type.
 * An SQL <code>NCLOB</code> is a built-in type
 * that stores a Character Large Object using the National Character Set
 *  as a column value in a row of  a database table.
 * <P>The <code>NClob</code> interface extends the <code>Clob</code> interface
 * which provides provides methods for getting the
 * length of an SQL <code>NCLOB</code> value,
 * for materializing a <code>NCLOB</code> value on the client, and for
 * searching for a substring or <code>NCLOB</code> object within a
 * <code>NCLOB</code> value. A <code>NClob</code> object, just like a <code>Clob</code> object, is valid for the duration
 * of the transaction in which it was created.
 * Methods in the interfaces {@link java.sql.ResultSet},
 * {@link java.sql.CallableStatement}, and {@link java.sql.PreparedStatement}, such as
 * <code>getNClob</code> and <code>setNClob</code> allow a programmer to
 * access an SQL <code>NCLOB</code> value.  In addition, this interface
 * has methods for updating a <code>NCLOB</code> value.
 *
 * <!-- start Release-specific documentation -->
 * <div class="ReleaseSpecificDocumentation">
 * <h3>HSQLDB-Specific Information:</h3> <p>
 *
 * First, it should be noted that since HSQLDB represents all character data
 * internally as Java UNICODE (UTF16) String objects, there is not currently any
 * appreciable difference between the HSQLDB XXXCHAR types and the SQL 2003
 * NXXXCHAR and NCLOB types. <p>
 *
 * See {@link org.hsqldb.jdbc.JDBCClob} for further information.
 *
 * </div>
 * <!-- end Release-specific documentation -->
 *
 * @since JDK 1.6, HSQLDB 2.0
 * @author boucherb@users
 * @see JDBCClob
 * @see JDBCClobClient
 */
public class JDBCNClob extends JDBCClob implements NClob {

    protected JDBCNClob() {
        super();
    }

    public JDBCNClob(String data) throws java.sql.SQLException {
        super(data);
    }
}
