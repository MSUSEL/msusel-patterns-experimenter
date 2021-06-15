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
import java.sql.SQLException;

import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * HSQLDB TestBug778213 Junit test case. <p>
 *
 * Test to ensure that DDL can be executed through the
 * HSQLDB PreparedStatement interface implementation and
 * that the behaviour of the prepared statement object is
 * nominally correct under "prepared" DDL.
 *
 * @author  boucherb@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class TestBug778213 extends TestBase {

    public TestBug778213(String name) {
        super(name);
    }

    /* Implements the TestBug778213_Part3 test */
    public void test() throws Exception {

        Connection        conn = newConnection();
        PreparedStatement pstmt;
        int               updateCount;

        try {
            pstmt = conn.prepareStatement("drop table test if exists");

            pstmt.executeUpdate();

            pstmt       = conn.prepareStatement("create table test(id int)");
            updateCount = pstmt.executeUpdate();

            assertTrue("expected update count of zero", updateCount == 0);

            pstmt       = conn.prepareStatement("drop table test");
            updateCount = pstmt.executeUpdate();

            assertTrue("expected update count of zero", updateCount == 0);
        } catch (Exception e) {
            assertTrue("unable to prepare or execute DDL", false);
        } finally {
            conn.close();
        }

        conn = newConnection();

        try {
            pstmt = conn.prepareStatement("create table test(id int)");

            assertTrue("got data expecting update count", !pstmt.execute());
        } catch (Exception e) {
            assertTrue("unable to prepare or execute DDL", false);
        } finally {
            conn.close();
        }

        conn = newConnection();

        boolean exception = true;

        try {
            pstmt = conn.prepareStatement("drop table test");

            pstmt.executeQuery();
        } catch (SQLException e) {
            exception = false;
        } finally {
            conn.close();
        }

        if (exception) {
            assertTrue("no exception thrown for executeQuery(DDL)", false);
        }

        conn = newConnection();

        try {
            pstmt = conn.prepareStatement("call identity()");

            pstmt.execute();
        } catch (Exception e) {
            assertTrue("unable to prepare or execute call", false);
        } finally {
            conn.close();
        }

        exception = false;
        conn      = newConnection();

        try {
            pstmt = conn.prepareStatement("create table test(id int)");

            pstmt.addBatch();
        } catch (SQLException e) {
            exception = true;
        } finally {
            conn.close();
        }

        if (exception) {
            assertTrue("not expected exception batching prepared DDL", false);
        }

        conn = newConnection();

        try {
            pstmt = conn.prepareStatement("create table test(id int)");

            assertTrue("expected null ResultSetMetadata for prepared DDL",
                       null == pstmt.getMetaData());
        } finally {
            conn.close();
        }

        conn = newConnection();

//#ifdef JAVA4
        try {
            pstmt = conn.prepareStatement("create table test(id int)");

            assertTrue("expected zero parameter for prepared DDL",
                       0 == pstmt.getParameterMetaData().getParameterCount());
        } finally {
            conn.close();
        }

//#endif JAVA4
    }

    /* Runs TestBug778213_Part3 test from the command line*/
    public static void main(String[] args) throws Exception {

        TestResult            result;
        TestCase              test;
        java.util.Enumeration failures;
        int                   count;

        result = new TestResult();
        test   = new TestBug778213("test");

        test.run(result);

        count = result.failureCount();

        System.out.println("TestBug778213 failure count: " + count);

        failures = result.failures();

        while (failures.hasMoreElements()) {
            System.out.println(failures.nextElement());
        }
    }
}
