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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

public class HSQLBug {

    public static void main(String[] args) throws Exception {

        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        String     url        = "jdbc:hsqldb:mem:.";
        Properties properties = new Properties();

        properties.setProperty("user", "sa");
        properties.setProperty("password", "");

        Connection con = DriverManager.getConnection(url, properties);
        Statement  s   = con.createStatement();

        s.executeUpdate(
            "CREATE TABLE Table1 (Id INTEGER IDENTITY, Timec DATETIME NOT NULL)");
        s.executeUpdate(
            "CREATE TABLE Table2 (Id INTEGER NOT NULL, Value VARCHAR(100) NOT NULL)");
        s.executeUpdate("CREATE INDEX idx1 ON Table1(timec)");

        // Add test data to the tables
        GregorianCalendar gc = new GregorianCalendar();

        for (int i = 0; i < 1000; i++) {
            gc.add(Calendar.MINUTE, -1);

            String query = "INSERT INTO Table1 (Timec) VALUES ('"
                           + formatter.format(gc.getTime()) + "')";

            s.executeUpdate(query);

            ResultSet r = s.executeQuery("CALL IDENTITY();");

            r.next();

            int id = r.getInt(1);

            s.executeUpdate("INSERT INTO Table2 VALUES (" + id + ", 'ABC')");
            s.executeUpdate("INSERT INTO Table2 VALUES (" + id + ", 'DEF')");
        }

        // Build the query
        StringBuffer query = new StringBuffer();

        query.append("SELECT Timec, Value FROM Table1, Table2 ");
        query.append("WHERE Table1.Id = Table2.Id  ");
        query.append("AND Table1.Timec = ");    // The equals sign causes the CPU to go to 100% for a while

//                      query.append( "AND Table1.Time IN " );  // The work-around is to replace it with IN
        query.append("(");
        query.append(
            "SELECT MAX(Timec) FROM Table1 WHERE Timec <= '2020-01-01 00:00:00'");
        query.append(")");
        System.out.println("Query = " + query);
        System.out.println(
            "Starting Query at: "
            + formatter.format((new GregorianCalendar()).getTime()));

        ResultSet r = s.executeQuery(query.toString());

        r.next();
        System.out.println("Result    : " + r.getTimestamp(1) + "  "
                           + r.getString(2));
        System.out.println(
            "DONE Query at    : "
            + formatter.format((new GregorianCalendar()).getTime()));
    }

    private static SimpleDateFormat formatter =
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
}
