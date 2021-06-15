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

/*
 * TestSubQueriesInPreparedStatements.java
 *
 * Created on July 9, 2003, 4:03 PM
 */
package org.hsqldb.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author boucherb@users
 */
public class TestSubQueriesInPreparedStatements {

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {

        Connection        conn;
        Statement         stmnt;
        PreparedStatement pstmnt;
        Driver            driver;

        driver =
            (Driver) Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();

        DriverManager.registerDriver(driver);

        conn = DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "");
        stmnt  = conn.createStatement();
        pstmnt = conn.prepareStatement("drop table t if exists");

        boolean result = pstmnt.execute();

        pstmnt = conn.prepareStatement("create table t(i decimal)");

        int updatecount = pstmnt.executeUpdate();

        pstmnt = conn.prepareStatement("insert into t values(?)");

        for (int i = 0; i < 100; i++) {
            pstmnt.setInt(1, i);
            pstmnt.executeUpdate();
        }

        pstmnt = conn.prepareStatement(
            "select * from (select * from t where i < ?)");

        System.out.println("Expecting: 0..3");
        pstmnt.setInt(1, 4);

        ResultSet rs = pstmnt.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }

        System.out.println("Expecting: 0..4");
        pstmnt.setInt(1, 5);

        rs = pstmnt.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }

        pstmnt = conn.prepareStatement(
            "select sum(i) from (select i from t where i between ? and ?)");

        System.out.println("Expecting: 9");
        pstmnt.setInt(1, 4);
        pstmnt.setInt(2, 5);

        rs = pstmnt.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }

        System.out.println("Expecting: 15");
        pstmnt.setInt(2, 6);

        rs = pstmnt.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }

        pstmnt = conn.prepareStatement(
            "select * from (select i as c1 from t where i < ?) a, (select i as c2 from t where i < ?) b");

        System.out.println("Expecting: (0,0)");
        pstmnt.setInt(1, 1);
        pstmnt.setInt(2, 1);

        rs = pstmnt.executeQuery();

        while (rs.next()) {
            System.out.println("(" + rs.getInt(1) + "," + rs.getInt(2) + ")");
        }

        System.out.println("Expecting: ((0,0), (0,1), (1,0), (1,1)");
        pstmnt.setInt(1, 2);
        pstmnt.setInt(2, 2);

        rs = pstmnt.executeQuery();

        while (rs.next()) {
            System.out.println("(" + rs.getInt(1) + "," + rs.getInt(2) + ")");
        }

        System.out.println("Expecting: ((0,0) .. (3,3)");
        pstmnt.setInt(1, 4);
        pstmnt.setInt(2, 4);

        rs = pstmnt.executeQuery();

        while (rs.next()) {
            System.out.println("(" + rs.getInt(1) + "," + rs.getInt(2) + ")");
        }
    }
}
