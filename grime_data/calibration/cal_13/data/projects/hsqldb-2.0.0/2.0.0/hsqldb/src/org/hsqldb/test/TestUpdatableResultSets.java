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
import java.sql.SQLException;
import java.sql.Statement;

public class TestUpdatableResultSets extends TestBase {

    //
    Connection connection;
    Statement  statement;

    public TestUpdatableResultSets(String name) {
        super(name);
    }

    protected void setUp() {

        super.setUp();

        try {
            connection = super.newConnection();
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                                                   ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void testUpdatable() {

        try {
            statement.execute("drop table t1 if exists");
            statement.execute(
                "create table t1 (i int primary key, c varchar(10), t varbinary(3))");

            String            insert = "insert into t1 values(?,?,?)";
            String            select = "select i, c, t from t1";
            PreparedStatement ps     = connection.prepareStatement(insert);

            for (int i = 0; i < 10; i++) {
                ps.setInt(1, i);
                ps.setString(2, String.valueOf(i) + " s");
                ps.setBytes(3, new byte[] {
                    (byte) i, ' ', (byte) i
                });
                ps.execute();
            }

            connection.setAutoCommit(false);

            ResultSet rs = statement.executeQuery(select);

            while (rs.next()) {
                String s = rs.getString(2);

                rs.updateString(2, s + s);
                rs.updateRow();
            }

            rs.close();

            rs = statement.executeQuery(select);

            while (rs.next()) {
                String s = rs.getString(2);

                System.out.println(s);
            }

            connection.rollback();

            rs = statement.executeQuery(select);

            while (rs.next()) {
                String s = rs.getString(2);

                System.out.println(s);
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testScrollable() {

        try {
            statement.execute("drop table t1 if exists");
            statement.execute(
                "create table t1 (i int primary key, c varchar(10), t varbinary(3))");
            statement.close();

            String            insert = "insert into t1 values(?,?,?)";
            String            select = "select i, c, t from t1";
            PreparedStatement ps     = connection.prepareStatement(insert);

            for (int i = 0; i < 10; i++) {
                ps.setInt(1, i);
                ps.setString(2, String.valueOf(i) + " s");
                ps.setBytes(3, new byte[] {
                    (byte) i, ' ', (byte) i
                });
                ps.execute();
            }

            connection.setAutoCommit(false);

            statement =
                connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                           ResultSet.CONCUR_READ_ONLY);

            ResultSet srs = statement.executeQuery("select * from t1 limit 2");

            srs.afterLast();

            while (srs.previous()) {
                String name = srs.getString(2);
                float  id   = srs.getFloat(1);

                System.out.println(name + "   " + id);
            }

            srs.close();

            srs = statement.executeQuery("select * from t1 limit 2");

            srs.absolute(3);

            while (srs.previous()) {
                String name = srs.getString(2);
                float  id   = srs.getFloat(1);

                System.out.println(name + "   " + id);
            }

            srs.absolute(2);

            while (srs.previous()) {
                String name = srs.getString(2);
                float  id   = srs.getFloat(1);

                System.out.println(name + "   " + id);
            }

            srs.absolute(-1);

            while (srs.previous()) {
                String name = srs.getString(2);
                float  id   = srs.getFloat(1);

                System.out.println(name + "   " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
