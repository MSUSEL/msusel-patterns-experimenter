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
 *  HSQLDB TestINPredicate Junit test case. <p>
 *
 * @author  boucherb@users
 * @version 1.9.0
 * @since 1.7.2
 */
public class TestINPredicateParameterizationAndCorrelation extends TestBase {

    public TestINPredicateParameterizationAndCorrelation(String name) {
        super(name);
    }

    /* Implements the TestINPredicate test */
    public void test() throws Exception {

        Connection        conn = newConnection();
        Statement         stmt = conn.createStatement();
        PreparedStatement pstmt;
        ResultSet         rs;
        int               actualCount;
        int               expectedCount;
        String            sql;

        stmt.execute("drop table test if exists");

        sql = "create table test(id int)";

        stmt.execute(sql);

        sql   = "insert into test values(?)";
        pstmt = conn.prepareStatement(sql);

        for (int i = 0; i < 10; i++) {
            pstmt.setInt(1, i);
            pstmt.addBatch();
        }

        pstmt.executeBatch();

        //
        sql   = "select count(*) from test where id in(?,?)";
        pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, 0);
        pstmt.setInt(2, 9);

        rs = pstmt.executeQuery();

        rs.next();

        expectedCount = 2;
        actualCount   = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

        //
        sql = "select count(*) from test a, test b where 0 in(a.id, b.id)";
        rs  = stmt.executeQuery(sql);

        rs.next();

        expectedCount = rs.getInt(1);
        sql = "select count(*) from test a, test b where ? in (a.id, b.id)";
        pstmt         = conn.prepareStatement(sql);

        pstmt.setInt(1, 0);

        rs = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

        //
        expectedCount = 20;
        sql = "select count(*) from test a, test b where a.id in(?, ?)";
        pstmt         = conn.prepareStatement(sql);

        pstmt.setInt(1, 0);
        pstmt.setInt(2, 9);

        rs = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

        //
        expectedCount = 10;
        sql = "select count(*) from test a, test b where ? in(?, b.id)";
        pstmt         = conn.prepareStatement(sql);

        pstmt.setInt(1, 0);
        pstmt.setInt(2, 9);

        rs = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

        //
        expectedCount = 1;
        sql = "select count(*) from test a where ? in(select b.id from test b where a.id = b.id)";
        pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, 0);

        rs = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("\"" + sql + "\"", expectedCount, actualCount);

        //
        sql = "select count(*) from "
              + "(select * from test where id in (1,2)) a,"
              + "(select * from test where id in (3,4)) b "
              + "where a.id < 2 and b.id < 4";
        rs = stmt.executeQuery(sql);

        rs.next();

        expectedCount = rs.getInt(1);
        sql = "select count(*) from "
              + "(select * from test where id in (?,?)) a,"
              + "(select * from test where id in (?,?)) b "
              + "where a.id < ? and b.id < ?";
        pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, 1);
        pstmt.setInt(2, 2);
        pstmt.setInt(3, 3);
        pstmt.setInt(4, 4);
        pstmt.setInt(5, 2);
        pstmt.setInt(6, 4);

        rs = pstmt.executeQuery();

        rs.next();

        actualCount = rs.getInt(1);

        assertEquals("row count: ", expectedCount, actualCount);
    }

    /* Runs TestINPredicate test from the command line*/
    public static void main(String[] args) throws Exception {

        TestResult            result;
        TestCase              test;
        java.util.Enumeration failures;
        int                   count;

        result = new TestResult();
        test   = new TestINPredicateParameterizationAndCorrelation("test");

        test.run(result);

        count = result.failureCount();

        System.out.println(
            "TestINPredicateParameterizationAndCorrelation failure count: "
            + count);

        failures = result.failures();

        while (failures.hasMoreElements()) {
            System.out.println(failures.nextElement());
        }
    }
}
