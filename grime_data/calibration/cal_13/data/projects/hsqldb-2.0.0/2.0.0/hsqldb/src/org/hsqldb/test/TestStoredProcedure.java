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
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * Tests for stored procedures.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.7.2
 * @since 1.7.2
 */
public class TestStoredProcedure extends TestBase {

    public TestStoredProcedure(String name) {
        super(name);
    }

    public void testOne() throws Exception {

        Connection conn = newConnection();
        Statement  statement;

        try {
            statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(
                "call \"org.hsqldb.test.TestStoredProcedure.procTest1\"()");

            rs.next();

            int cols = rs.getInt(1);

            assertFalse("test result not correct", false);
        } catch (Exception e) {
        }

        try {
            statement = conn.createStatement();

            statement.execute(
            "create procedure proc1()"
            + "SPECIFIC P2 LANGUAGE JAVA DETERMINISTIC MODIFIES SQL EXTERNAL NAME 'CLASSPATH:org.hsqldb.test.TestStoredProcedure.procTest1'");

        } catch (Exception e) {
            assertTrue("unexpected error", true);
        } finally {
            conn.close();
        }
    }

    public void testTwo() throws Exception {

        Connection conn = newConnection();
        Statement  statement;
        int        updateCount;

        try {
            statement = conn.createStatement();
            statement.execute("create user testuser password 'test'");
            statement.execute("create table testtable(v varchar(20))");
            statement.execute("insert into testtable values ('tennis'), ('tent'), ('television'), ('radio')");
            ResultSet rs = statement.executeQuery(
                "call \"org.hsqldb.test.TestStoredProcedure.funcTest2\"('test')");

            rs.next();

            boolean b = rs.getBoolean(1);

            rs.close();
            assertTrue("test result not correct", b);
            statement.execute(
                "create function func2(varchar(20)) returns boolean "
                + "SPECIFIC F2 LANGUAGE JAVA DETERMINISTIC NO SQL CALLED ON NULL INPUT EXTERNAL NAME 'CLASSPATH:org.hsqldb.test.TestStoredProcedure.funcTest2'");

            rs = statement.executeQuery("call func2('test')");

            rs.next();

            b = rs.getBoolean(1);

            rs.close();
            assertTrue("test result not correct", b);

            rs = statement.executeQuery("select count(*) from testtable where func2(v)");

            rs.next();

            int count = rs.getInt(1);

            assertTrue("test result not correct", count == 3);

            statement.execute("grant execute on specific function public.f2 to testuser");


        } catch (Exception e) {
            assertTrue("unable to execute call to procedure", false);
        } finally {
            conn.close();
        }
    }

    public static int procTest1(Connection conn) throws java.sql.SQLException {

        int                cols;
        java.sql.Statement stmt = conn.createStatement();

        stmt.execute(
            "CREATE temp TABLE MYTABLE(COL1 INTEGER,COL2 VARCHAR(10));");
        stmt.execute("INSERT INTO MYTABLE VALUES    (1,'test1');");
        stmt.execute("INSERT INTO MYTABLE VALUES(2,'test2');");

        java.sql.ResultSet rs = stmt.executeQuery("select * from MYTABLE");
        java.sql.ResultSetMetaData meta = rs.getMetaData();

        cols = meta.getColumnCount();

        rs.close();
        stmt.close();

        return cols;
    }

    public static boolean funcTest2(Connection conn,
                                    String value)
                                    throws java.sql.SQLException {

        if (value != null && value.startsWith("te")) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) throws Exception {

        TestResult            result;
        TestCase              test;
        java.util.Enumeration failures;
        int                   count;

        result = new TestResult();
        test   = new TestStoredProcedure("test");

        test.run(result);

        count = result.failureCount();

        System.out.println("TestStoredProcedure failure count: " + count);

        failures = result.failures();

        while (failures.hasMoreElements()) {
            System.out.println(failures.nextElement());
        }
    }
}
