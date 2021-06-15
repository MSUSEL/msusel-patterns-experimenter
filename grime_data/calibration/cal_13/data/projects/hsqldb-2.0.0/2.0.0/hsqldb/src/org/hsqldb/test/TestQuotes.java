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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *  Test handling of quote characters in strings
 *
 * @author <a href="mailto:david@walend.net">David Walend</a>
 * @author <a href="mailto:jvanzyl@zenplex.com">Jason van Zyl</a>
 */
public class TestQuotes extends TestCase {

    private static final String CREATETABLE =
        "create table quotetest (test varchar)";
    private static final String DELETE = "delete from quotetest";
    private static final String TESTSTRING =
        "insert into quotetest (test) values (?)";
    private static final String NOQUOTES = "the house of the dog of kevin";
    private static final String QUOTES   = "kevin's dog's house";
    private static final String RESULT   = "select * from quotetest";

    public TestQuotes(String testName) {
        super(testName);
    }

    /**
     * Run all related test methods
     */
    public static Test suite() {
        return new TestSuite(org.hsqldb.test.TestQuotes.class);
    }

    public void testSetString() {

        Connection        connection = null;
        Statement         statement  = null;
        PreparedStatement pStatement = null;
        ResultSet         rs1        = null;
        ResultSet         rs2        = null;

        try {
            DriverManager.registerDriver(new org.hsqldb.jdbc.JDBCDriver());

            connection = DriverManager.getConnection("jdbc:hsqldb:mem:.", "sa",
                    "");
            statement = connection.createStatement();

            statement.executeUpdate(CREATETABLE);

            pStatement = connection.prepareStatement(TESTSTRING);

            pStatement.setString(1, NOQUOTES);
            pStatement.executeUpdate();

            rs1 = statement.executeQuery(RESULT);

            rs1.next();

            String result1 = rs1.getString(1);

            assertTrue("result1 is -" + result1 + "- not -" + NOQUOTES + "-",
                       NOQUOTES.equals(result1));
            statement.executeUpdate(DELETE);
            pStatement.setString(1, QUOTES);
            pStatement.executeUpdate();

            rs2 = statement.executeQuery(RESULT);

            rs2.next();

            String result2 = rs2.getString(1);

            assertTrue("result2 is " + result2, QUOTES.equals(result2));
        } catch (SQLException sqle) {
            fail(sqle.getMessage());
        } finally {
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }

            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }

            if (pStatement != null) {
                try {
                    pStatement.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }
}
