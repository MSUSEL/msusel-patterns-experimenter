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

/**
 * @author kloska@users
 */
public class TestPreparedSubQueries extends TestCase {

    private Connection con = null;

    private class sqlStmt {

        boolean prepare;
        boolean update;
        String  command;

        sqlStmt(String c, boolean p, boolean u) {

            prepare = p;
            command = c;
            update  = u;
        }
    }
    ;

    private sqlStmt[] stmtArray = {
        new sqlStmt("drop table a if exists cascade", false, false),
        new sqlStmt("create cached table a (a int identity,b int)", false,
                    false),
        new sqlStmt("create index bIdx on a(b)", false, false),
        new sqlStmt("insert into a(b) values(1)", true, true),
        new sqlStmt("insert into a(b) values(2)", true, true),
        new sqlStmt("insert into a(b) values(3)", true, true),
        new sqlStmt("insert into a(b) values(4)", true, true),
        new sqlStmt("insert into a(b) values(5)", true, true),
        new sqlStmt("insert into a(b) values(6)", true, true),
        new sqlStmt(
            "update a set b=100 where b>(select b from a X where X.a=2)",
            true, true),
        new sqlStmt("update a set b=200 where b>(select b from a where a=?)",
                    true, true),
        new sqlStmt(
            "update a set b=300 where b>(select b from a X where X.a=?)",
            true, true)
    };
    private Object[][] stmtArgs = {
        {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, { new Integer(2) },
        { new Integer(2) }
    };

    public TestPreparedSubQueries(String name) {
        super(name);
    }

    protected void setUp() {

        String url = "jdbc:hsqldb:test";

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");

            con = java.sql.DriverManager.getConnection(url, "sa", "");
        } catch (Exception e) {}
    }

    public void testA() {

        try {
            int i = 0;

            for (i = 0; i < stmtArray.length; i++) {
                int j;

                System.out.println(" -- #" + i + " ----------------------- ");

                if (stmtArray[i].prepare) {
                    PreparedStatement ps = null;

                    System.out.println(" -- preparing\n<<<\n"
                                       + stmtArray[i].command + "\n>>>\n");

                    ps = con.prepareStatement(stmtArray[i].command);

                    System.out.print(" -- setting " + stmtArgs[i].length
                                     + " Args [");

                    for (j = 0; j < stmtArgs[i].length; j++) {
                        System.out.print((j > 0 ? "; "
                                                : "") + stmtArgs[i][j]);
                        ps.setObject(j + 1, stmtArgs[i][j]);
                    }

                    System.out.println("]");
                    System.out.println(" -- executing ");

                    if (stmtArray[i].update) {
                        int r = ps.executeUpdate();

                        System.out.println(" ***** ps.executeUpdate gave me "
                                           + r);
                    } else {
                        boolean b = ps.execute();

                        System.out.print(" ***** ps.execute gave me " + b);
                    }
                } else {
                    System.out.println(" -- executing directly\n<<<\n"
                                       + stmtArray[i].command + "\n>>>\n");

                    Statement s = con.createStatement();
                    boolean   b = s.execute(stmtArray[i].command);

                    System.out.println(" ***** st.execute gave me " + b);
                }
            }
        } catch (Exception e) {
            System.out.println(" ?? Caught Exception " + e);
            assertTrue(false);
        }

        assertTrue(true);
    }

    public void testGenerated() {
        boolean valid = false;
        try {
            Statement s = con.createStatement();
            s.execute("drop table a if exists");
            s.execute("create cached table a (a int identity,b int)");
            s.execute("insert into a(b) values(1)", Statement.RETURN_GENERATED_KEYS);
            ResultSet r = s.getGeneratedKeys();
            while(r.next()) {
                r.getInt(1);
                valid = true;
            }
            r.close();
            assertTrue(valid);

            s.execute("insert into a(b) values(2)",new int[]{1});
            r = s.getGeneratedKeys();
            while(r.next()) {
                r.getInt(1);
                valid = true;
            }
            assertTrue(valid);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testIdentity() {
        boolean valid = false;
        try {
            Statement s = con.createStatement();
            s.execute("drop table a if exists");
            s.execute("create cached table a (a int identity, b int)");
            PreparedStatement p1 = con.prepareStatement("insert into a(b) values ?");
            p1.setInt(1, 10);
            p1.executeUpdate();
            PreparedStatement p2 = con.prepareStatement("call identity()");
            ResultSet r = p2.executeQuery();
            while(r.next()) {
                r.getInt(1);
                valid = true;
            }
            p1.setInt(1, 11);
            p1.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement("select count(*) from a where a in ((select a from a where b = ?) union (select ? from a))");
            ps3.setInt(1, 10);
            ps3.setInt(2, 1);

            r = ps3.executeQuery();

            while(r.next()) {
                int value = r.getInt(1);
                valid = value == 2;
            }

            assertTrue(valid);
        } catch (Exception e) {
        assertTrue(false);
    }
    }

}
