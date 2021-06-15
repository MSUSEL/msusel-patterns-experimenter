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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * HSQLDB TestLikePredicate Junit test case. <p>
 *
 * @author  boucherb@users
 * @version 1.9.0
 * @since 1.7.2
 */
public class TestLikePredicateOptimizations extends TestBase {

    public TestLikePredicateOptimizations(String name) {
        super(name);
    }

    /* Implements the TestLikePredicate test */
    public void test() throws Exception {

        Connection        conn = newConnection();
        Statement         stmt = conn.createStatement();
        PreparedStatement pstmt;
        ResultSet         rs;
        String            sql;
        int               expectedCount;
        int               actualCount;

        stmt.execute("drop table test if exists");
        stmt.execute("drop table empty if exists");

        sql = "create table test(name varchar(255))";

        stmt.execute(sql);

        sql = "create table empty(name varchar(255))";

        stmt.execute(sql);

        sql = "insert into empty values 'name10'";

        stmt.execute(sql);

        sql = "insert into empty values 'name11'";

        stmt.execute(sql);

        sql   = "insert into test values(?)";
        pstmt = conn.prepareStatement(sql);

        for (int i = 0; i < 10000; i++) {
            pstmt.setString(1, "name" + i);
            pstmt.addBatch();
        }

        pstmt.executeBatch();

//
        sql = "select count(*) from test where name = (select max(name) from empty)";
        rs = stmt.executeQuery(sql);

        rs.next();

        expectedCount = rs.getInt(1);
        sql = "select count(*) from test where name like (select min(name) from empty)";
        pstmt = conn.prepareStatement(sql);
        rs    = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

        sql = "select count(*) from test where (select max(name) from empty where empty.name = test.name and empty.name > ?) like '%ame11%'";
        pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, "n");

        rs = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

// --
        sql = "select count(*) from test where name = ''";
        rs  = stmt.executeQuery(sql);

        rs.next();

        expectedCount = rs.getInt(1);
        sql           = "select count(*) from test where name like ''";
        pstmt         = conn.prepareStatement(sql);
        rs            = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

        // --
        sql = "SELECT t.name FROM test t WHERE ((SELECT t2.name from test t2 where t2.name=?) like '%name5000%')";
        pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, "name5000");

        rs = pstmt.executeQuery();

        assertEquals(rs.next(), true);
        String actual = rs.getString(1);
        assertEquals(actual, "name0");

// --
// --
        sql = "select count(*) from test where name is not null";
        rs  = stmt.executeQuery(sql);

        rs.next();

        expectedCount = rs.getInt(1);
        sql           = "select count(*) from test where name like '%'";
        pstmt         = conn.prepareStatement(sql);
        rs            = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

// --
        sql = "select count(*) from test where substring(name from 1 for 6) = 'name44'";
        rs = stmt.executeQuery(sql);

        rs.next();

        expectedCount = rs.getInt(1);
        sql           = "select count(*) from test where name like 'name44%'";
        pstmt         = conn.prepareStatement(sql);
        rs            = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

// --
        sql = "select count(*) from test where left(name,5) = 'name4' and right(name,1) = '5'";
        rs = stmt.executeQuery(sql);

        rs.next();

        expectedCount = rs.getInt(1);
        sql           = "select count(*) from test where name like 'name4%5'";
        pstmt         = conn.prepareStatement(sql);
        rs            = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

// --
        String result  = "true";
        String presult = "false";

        stmt.execute("drop table test1 if exists");

        sql   = "CREATE TABLE test1 (col CHAR(30))";
        pstmt = conn.prepareStatement(sql);

        pstmt.execute();

        sql   = "INSERT INTO test1 (col) VALUES ('one')";
        pstmt = conn.prepareStatement(sql);

        pstmt.execute();

        sql   = "SELECT * FROM test1 WHERE ( col LIKE ? )";
        pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, "on%");

        rs = pstmt.executeQuery();

        if (rs.next()) {
            presult = rs.getString("COL");
        }

        sql   = "SELECT * FROM test1 WHERE ( col LIKE 'one' )";
        pstmt = conn.prepareStatement(sql);
        rs    = pstmt.executeQuery();

        if (rs.next()) {
            result = rs.getString("COL");
        }

        assertEquals("\"" + sql + "\"", result, presult);
    }

    /* Runs TestLikePredicate test from the command line*/
    public static void main(String[] args) throws Exception {

        TestResult            result;
        TestCase              test;
        java.util.Enumeration failures;
        int                   count;

        result = new TestResult();
        test   = new TestLikePredicateOptimizations("test");

        test.run(result);

        count = result.failureCount();

        System.out.println("TestLikePredicateOptimizations failure count: "
                           + count);

        failures = result.failures();

        while (failures.hasMoreElements()) {
            System.out.println(failures.nextElement());
        }
    }
}
