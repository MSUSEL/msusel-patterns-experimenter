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

import org.hsqldb.lib.StopWatch;

public class TestAnother {

    // fixed
    protected String url = "jdbc:hsqldb:";

//    protected String  filepath = "hsql://localhost/mytest";
//    protected String filepath = "mem:test";
    protected String filepath = "/hsql/testtime/test";

    public TestAnother() {}

    public void setUp() {

        String user     = "sa";
        String password = "";

        try {
            Connection conn = null;

            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            conn = DriverManager.getConnection(url + filepath, user,
                                               password);

            Statement stmnt = conn.createStatement();
            Statement st    = conn.createStatement();

            st.executeUpdate("CREATE TABLE TT(D DATE)");
            st.executeUpdate("INSERT INTO TT VALUES ('2004-01-02')");
            st.executeUpdate("INSERT INTO TT VALUES ('2004-02-02')");

            ResultSet rs = st.executeQuery("SELECT * FROM TT");

            while (rs.next()) {
                System.out.println(rs.getDate(1));
            }

            st.executeUpdate("DROP TABLE TT");
            rs.close();

            Statement stm = conn.createStatement();

            stm.executeUpdate(
                "create table test (id int,atime timestamp default current_timestamp)");

            stm = conn.createStatement();

            int count = stm.executeUpdate("insert into test (id) values (1)");

            System.out.println(count);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("TestSql.setUp() error: " + e.getMessage());
        }
    }

    public static void main(String[] argv) {

        StopWatch   sw   = new StopWatch();
        TestAnother test = new TestAnother();

        test.setUp();
        System.out.println("Total Test Time: " + sw.elapsedTime());
    }
}
