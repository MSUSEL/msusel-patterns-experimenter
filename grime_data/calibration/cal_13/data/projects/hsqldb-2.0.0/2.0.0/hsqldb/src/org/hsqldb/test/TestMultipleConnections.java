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

package org.hsqldb.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestMultipleConnections {

    public TestMultipleConnections() {}

    public static void main(String[] args) throws Exception {

        // test for bug itme 500105 commit does not work with multiple con. FIXED
        TestMultipleConnections hs   = new TestMultipleConnections();
        Connection              con1 = hs.createObject();
        Connection              con2 = hs.createObject();
        Connection              con3 = hs.createObject();

        con1.setAutoCommit(false);

        //connection1.commit();
        con2.setAutoCommit(false);

        //connection1.commit();
        con3.setAutoCommit(false);

        //connection1.commit();
        Statement st = con3.createStatement();

        st.execute("DROP TABLE T IF EXISTS");
        st.execute("CREATE TABLE T (I INT)");
        st.execute("INSERT INTO T VALUES (2)");

        ResultSet rs = st.executeQuery("SELECT * FROM T");

        rs.next();

        int value = rs.getInt(1);

        con2.commit();
        con3.commit();
        con1.commit();

        rs = st.executeQuery("SELECT * FROM T");

        rs.next();

        if (value != rs.getInt(1)) {
            throw new Exception("value doesn't exist");
        }
    }

    /**
     * create a connection and wait
     */
    protected Connection createObject() {

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            return DriverManager.getConnection("jdbc:hsqldb:/hsql/test/test",
                                               "sa", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
