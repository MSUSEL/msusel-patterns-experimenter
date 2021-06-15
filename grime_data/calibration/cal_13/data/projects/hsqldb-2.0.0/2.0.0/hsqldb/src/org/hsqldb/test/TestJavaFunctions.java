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

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.jdbc.JDBCConnection;
import org.hsqldb.jdbc.JDBCResultSet;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.navigator.RowSetNavigatorClient;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.Type;

public class TestJavaFunctions extends TestBase {

    public TestJavaFunctions() {
        super("TestJavaFunction", "jdbc:hsqldb:file:test3", false, false);
    }

    protected void setUp() {

        super.setUp();

        try {
            prepareDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void prepareDatabase() throws SQLException {

        Connection c = newConnection();
        Statement  s = c.createStatement();

        s.executeUpdate("DROP FUNCTION TEST_QUERY IF EXISTS");
        s.executeUpdate("DROP FUNCTION TEST_CUSTOM_RESULT IF EXISTS");
        s.executeUpdate("DROP TABLE T IF EXISTS");
        s.executeUpdate("CREATE TABLE T(C VARCHAR(20), I INT)");
        s.executeUpdate("INSERT INTO T VALUES 'Thames', 10");
        s.executeUpdate("INSERT INTO T VALUES 'Fleet', 12");
        s.executeUpdate("INSERT INTO T VALUES 'Brent', 14");
        s.executeUpdate("INSERT INTO T VALUES 'Westbourne', 16");
        s.executeUpdate(
            "CREATE FUNCTION TEST_QUERY(INT) RETURNS TABLE(N VARCHAR(20), I INT) "
            + " READS SQL DATA LANGUAGE JAVA EXTERNAL NAME 'CLASSPATH:org.hsqldb.test.TestJavaFunctions.getQueryResult'");
        s.executeUpdate(
            "CREATE FUNCTION TEST_CUSTOM_RESULT(BIGINT, BIGINT) RETURNS TABLE(I BIGINT, N VARBINARY(1000)) "
            + " READS SQL DATA LANGUAGE JAVA EXTERNAL NAME 'CLASSPATH:org.hsqldb.test.TestJavaFunctions.getCustomResult'");
        s.executeUpdate("CHECKPOINT");
        c.close();
    }

    public void testOne() throws SQLException {

        Connection        c = newConnection();
        CallableStatement s = c.prepareCall("CALL TEST_QUERY(16)");

        s.execute();

        ResultSet r = s.getResultSet();

        while (r.next()) {
            String temp = "" + r.getInt(2) + " " + r.getString(1);

            System.out.println(temp);
        }

        s = c.prepareCall("CALL TEST_CUSTOM_RESULT(6, 19)");

        s.execute();

        r = s.getResultSet();

        while (r.next()) {
            String temp =
                "" + r.getLong(1) + " "
                + org.hsqldb.lib.StringConverter.byteArrayToSQLHexString(
                    r.getBytes(2));

            System.out.println(temp);
        }

        s = c.prepareCall("CALL TEST_CUSTOM_RESULT(6, 1900)");

        try {
            s.execute();

            r = s.getResultSet();

            fail("exception not thrown");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        c.close();
    }

    public static void main(String[] args) throws SQLException {}

    public static ResultSet getQueryResult(Connection connection,
                                           int i) throws SQLException {

        Statement st = connection.createStatement();

        return st.executeQuery("SELECT * FROM T WHERE I < " + i);
    }

    public static ResultSet getQueryResult(Connection connection,
                                           String p1) throws SQLException {
        return getQueryResult(connection, 13);
    }

    public static ResultSet getQueryResult(Connection connection, String p1,
                                           String p2) throws SQLException {
        return getQueryResult(connection, 20);
    }

    private static Result newTwoColumnResult() {

        Type[] types = new Type[2];

        types[0] = Type.SQL_BIGINT;
        types[1] = Type.SQL_VARBINARY_DEFAULT;

        ResultMetaData  meta = ResultMetaData.newSimpleResultMetaData(types);
        RowSetNavigator navigator = new RowSetNavigatorClient();
        Result          result    = Result.newDataResult(meta);

        result.setNavigator(navigator);

        return result;
    }

    public static ResultSet getCustomResult(Connection connection, long start,
            long end) throws SQLException {

        Result result = newTwoColumnResult();

        if (end < start) {
            long temp = start;

            start = end;
            end   = temp;
        }

        if (end > 1000) {
            throw org.hsqldb.jdbc.Util.invalidArgument(
                "value larger than 100");
        }

        if (end > start + 100) {
            end = start + 100;
        }

        for (long i = start; i < end; i++) {
            Object[] row = new Object[2];

            row[0] = Long.valueOf(i);
            row[1] = new BinaryData(BigInteger.valueOf(i).toByteArray(),
                                    false);

            result.navigator.add(row);
        }

        result.navigator.reset();

        return new JDBCResultSet((JDBCConnection) connection, null, result,
                                 result.metaData);
    }
}
