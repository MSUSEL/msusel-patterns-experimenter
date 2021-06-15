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

public class TestUpdatableResults extends TestBase {

    public TestUpdatableResults(String name) {
        super(name);
    }

    public void testQuery() {

        try {
            Connection c = newConnection();
            Statement st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_UPDATABLE);
            String s = "CREATE TABLE T (I INTEGER, C CHARACTER(10), B BIT(4) DEFAULT B'')";

            st.execute(s);

            s = "INSERT INTO T VALUES(?,?, DEFAULT)";

            PreparedStatement ps = c.prepareStatement(s);

            for (int i = 1; i <= 20; i++) {
                ps.setInt(1, i);
                ps.setString(2, "TEST " + i);
                ps.execute();
            }

            c.setAutoCommit(false);

            s = "SELECT * FROM T";

            ResultSet rs = st.executeQuery(s);


            rs.absolute(10);
            rs.updateString(2, "UPDATE10");
            rs.updateRow();

            rs.absolute(11);
            rs.deleteRow();

            rs.moveToInsertRow();

            rs.updateInt(1, 1011);
            rs.updateString(2, "INSERT1011");
            rs.updateString(3, "0101");

            rs.insertRow();

            rs.close();

            rs = st.executeQuery(s);

            while (rs.next()) {
                System.out.println("" + rs.getInt(1) + "      "
                                   + rs.getString(2) + "      "
                                   + rs.getString(3));
            }

        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
