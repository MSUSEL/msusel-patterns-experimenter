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

/**
 * DB Out of memory test
 * cached tables in non-nio mode
 * @author Nitin Chauhan
 */
public class TestNitin {

    public static void main(String[] args) {

        java.sql.Connection    c  = null;
        java.sql.Statement     s  = null;
        java.io.BufferedReader br = null;

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            c = java.sql.DriverManager.getConnection(
                "jdbc:hsqldb:c:/ft/hsqldb_w_1_8_0/oom/my.db", "SA", "");
            s = c.createStatement();
            br = new java.io.BufferedReader(
                new java.io.FileReader("c:/ft/hsqldb_w_1_8_0//oom//my.sql"));

            String line;
            int    lineNo = 0;

            while ((line = br.readLine()) != null) {
                if (line.length() > 0 && line.charAt(0) != '#') {
                    s.execute(line);

                    if (lineNo++ % 100 == 0) {
                        System.out.println(lineNo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (java.io.IOException ioe) {}

            try {
                if (s != null) {
                    s.close();
                }
            } catch (java.sql.SQLException se) {}

            try {
                if (c != null) {
                    c.close();
                }
            } catch (java.sql.SQLException se) {}
        }

        System.exit(0);
    }
}
