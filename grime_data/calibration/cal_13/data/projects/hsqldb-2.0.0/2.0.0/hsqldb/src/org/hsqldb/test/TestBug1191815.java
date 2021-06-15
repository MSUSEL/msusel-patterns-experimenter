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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * Created on Apr 28, 2005
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class TestBug1191815 extends TestBase {

    public TestBug1191815(String name) {
        super(name);
    }

    public void test() throws Exception {

        try {
            Connection conn = newConnection();
            Statement  stmt = conn.createStatement();

            stmt.executeUpdate("drop table testA if exists;");
            stmt.executeUpdate("create table testA(data timestamp);");

            TimeZone pst = TimeZone.getTimeZone("PST");
            Calendar cal = new GregorianCalendar(pst);

            cal.clear();
            cal.set(2005, 0, 1, 0, 0, 0);


            // yyyy-mm-dd hh:mm:ss.fffffffff
            Timestamp ts = new Timestamp(cal.getTimeInMillis());
            ts.setNanos(1000);
            PreparedStatement ps =
                conn.prepareStatement("insert into testA values(?)");

            ps.setTimestamp(1, ts, cal);
            ps.execute();
            ps.setTimestamp(1, ts, null);
            ps.execute();

            String sql = "select * from testA";

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            rs.next();

            Timestamp returned = rs.getTimestamp(1, cal);

            rs.next();

            Timestamp def = rs.getTimestamp(1, null);

            assertEquals(ts, returned);
            assertEquals(ts, def);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public static void main(String[] args) throws Exception {

        TestResult            result;
        TestCase              test;
        java.util.Enumeration exceptions;
        java.util.Enumeration failures;
        int                   count;

        result = new TestResult();
        test   = new TestBug1191815("test");

        test.run(result);

        count = result.failureCount();

        System.out.println("TestBug1192000 failure count: " + count);

        failures = result.failures();

        while (failures.hasMoreElements()) {
            System.out.println(failures.nextElement());
        }
    }
}
