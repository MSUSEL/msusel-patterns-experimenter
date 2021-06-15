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
import java.text.DateFormat;
import java.util.Calendar;

import junit.framework.Assert;

/**
 * Tests for normalisation of Time and Date values.
 * Base on the original test submission.
 * @author Miro Halas
 */
public class TestDateTime extends TestBase {

    public TestDateTime(String s) {
        super(s);
    }

    protected void setUp() {

        super.setUp();

        try {
            Connection connection = super.newConnection();
            Statement  statement  = connection.createStatement();

            statement.execute("drop table time_test if exists");
            statement.execute("drop table date_test if exists");
            statement.execute("create table time_test(time_test time)");
            statement.execute("create table date_test(date_test date)");
            connection.close();
        } catch (Exception e) {}
    }

    /**
     * Test the database support for Date objects. Date object ignores the time
     * portion of the Java Date.
     *
     * This class inserts date into database, then retrieve it back using
     * different java time
     *
     * @throws Throwable - an error has occured during test
     */
    public void testBasicDateSupport() throws Throwable {

        final String INSERT_DATE =
            "insert into date_test(date_test) values (?)";

        // See OracleTests class why we need to select tablename.*
        final String SELECT_DATE =
            "select date_test.* from date_test where date_test = ?";
        final String DELETE_DATE =
            "delete from date_test where date_test = ?";
        Calendar          calGenerate = Calendar.getInstance();
        java.sql.Date     insertDate;
        Connection        connection = super.newConnection();
        PreparedStatement insertStatement;
        int               iUpdateCount = 0;

        // Set date of my birthday ;-)
        calGenerate.set(1995, 9, 15, 1, 2, 3);

        insertDate      = new java.sql.Date(calGenerate.getTimeInMillis());
        insertStatement = connection.prepareStatement(INSERT_DATE);

        insertStatement.setDate(1, insertDate);

        iUpdateCount = insertStatement.executeUpdate();

        insertStatement.close();
        Assert.assertEquals(
            "Exactly one record with date data shoud have been inserted.",
            iUpdateCount, 1);

        // Now select it back to be sure it is there
        PreparedStatement selectStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet         results         = null;
        java.sql.Date     retrievedDate   = null;
        boolean           bHasMoreThanOne;
        int               iDeletedCount = 0;

        // Set different time, since when we are dealing with just dates it
        // shouldn't matter
        calGenerate.set(1995, 9, 15, 2, 3, 4);

        java.sql.Date selectDate =
            new java.sql.Date(calGenerate.getTimeInMillis());

        selectStatement = connection.prepareStatement(SELECT_DATE);

        selectStatement.setDate(1, selectDate);

        results = selectStatement.executeQuery();

        // Get the date from the database
        Assert.assertTrue("The inserted date is not in the database.",
                          results.next());

        retrievedDate   = results.getDate(1);
        deleteStatement = connection.prepareStatement(DELETE_DATE);

        deleteStatement.setDate(1, insertDate);

        iDeletedCount = deleteStatement.executeUpdate();

        deleteStatement.close();
        Assert.assertEquals(
            "Exactly one record with date data shoud have been deleted.",
            iDeletedCount, 1);

        boolean result = retrievedDate.toString().startsWith(
            insertDate.toString().substring(0, 10));

        Assert.assertTrue(
            "The date retrieved from database "
            + DateFormat.getDateTimeInstance().format(retrievedDate)
            + " is not the same as the inserted one "
            + DateFormat.getDateTimeInstance().format(insertDate), result);
    }

    public void testBasicDefaultTimeSupport() throws Throwable {

        final String INSERT_TIME =
            "insert into time_test(time_test) values (?)";

        // See OracleTests class why we need to select tablename.*
        final String SELECT_TIME =
            "select time_test.* from time_test where time_test = ?";
        final String DELETE_TIME =
            "delete from time_test where time_test = ?";
        java.sql.Time     insertTime;
        Connection        connection = super.newConnection();
        PreparedStatement insertStatement;
        int               iUpdateCount = 0;

        insertTime      = new java.sql.Time(3600000);
        insertStatement = connection.prepareStatement(INSERT_TIME);

        insertStatement.setTime(1, insertTime);

        iUpdateCount = insertStatement.executeUpdate();

        insertStatement.close();
        Assert.assertEquals(
            "Exactly one record with time data shoud have been inserted.",
            iUpdateCount, 1);

        // Now select it back to be sure it is there
        PreparedStatement selectStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet         results         = null;
        java.sql.Time     retrievedTime;
        int               iDeletedCount = 0;
        java.sql.Time     selectTime;

        selectStatement = connection.prepareStatement(SELECT_TIME);

        selectTime = new java.sql.Time(3600000);

        selectStatement.setTime(1, selectTime);

        results = selectStatement.executeQuery();

        // Get the date from the database
        Assert.assertTrue("The inserted time is not in the database.",
                          results.next());

        retrievedTime = results.getTime(1);

        //
        deleteStatement = connection.prepareStatement(DELETE_TIME);

        deleteStatement.setTime(1, insertTime);

        iDeletedCount = deleteStatement.executeUpdate();

        Assert.assertEquals(
            "Exactly one record with time data shoud have been deleted.",
            iDeletedCount, 1);

        // And now test the date
        Assert.assertNotNull(
            "The inserted time shouldn't be retrieved as null from the database",
            retrievedTime);

        // Ignore milliseconds when comparing dates
        String selectString = selectTime.toString();
        String retrievedString = retrievedTime.toString();

        boolean result =
            retrievedString.equals(selectString);

        Assert.assertTrue(
            "The time retrieved from database "
            + DateFormat.getDateTimeInstance().format(retrievedTime)
            + " is not the same as the inserted one "
            + DateFormat.getDateTimeInstance().format(insertTime), result);
    }

    /**
     * Test the database support for Time objects. Time object ignores the date
     * portion of the Java Date.
     *
     * This class inserts time into database, then retrieve it back using
     * different java date and deletes it using cursor.
     *
     * Uses the already setup connection and transaction.
     * No need to close the connection since base class is doing it for us.
     *
     * @throws Throwable - an error has occured during test
     */
    public void testBasicTimeSupport() throws Throwable {

        final String INSERT_TIME =
            "insert into time_test(time_test) values (?)";

        // See OracleTests class why we need to select tablename.*
        final String SELECT_TIME =
            "select time_test.* from time_test where time_test = ?";
        final String DELETE_TIME =
            "delete from time_test where time_test = ?";
        Calendar          calGenerate = Calendar.getInstance();
        java.sql.Time     insertTime;
        Connection        connection = super.newConnection();
        PreparedStatement insertStatement;
        int               iUpdateCount = 0;

        // Set date of my birthday ;-)
        calGenerate.set(1995, 9, 15, 1, 2, 3);

        insertTime      = new java.sql.Time(calGenerate.getTime().getTime());
        insertStatement = connection.prepareStatement(INSERT_TIME);

        insertStatement.setTime(1, insertTime, calGenerate);

        iUpdateCount = insertStatement.executeUpdate();

        insertStatement.close();
        Assert.assertEquals(
            "Exactly one record with time data shoud have been inserted.",
            iUpdateCount, 1);

        // Now select it back to be sure it is there
        PreparedStatement selectStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet         results         = null;
        java.sql.Time     retrievedTime;
        int               iDeletedCount = 0;
        java.sql.Time     selectTime;

        selectStatement = connection.prepareStatement(SELECT_TIME);

        // Set different date, since when we are dealing with just time it
        // shouldn't matter
        // fredt - but make sure the date is in the same daylight saving range as today !
        calGenerate.set(1975, 9, 16, 1, 2, 3);

        selectTime = new java.sql.Time(calGenerate.getTime().getTime());

        selectStatement.setTime(1, selectTime, calGenerate);

        results = selectStatement.executeQuery();

        // Get the date from the database
        Assert.assertTrue("The inserted time is not in the database.",
                          results.next());

        retrievedTime = results.getTime(1, calGenerate);

        //
        deleteStatement = connection.prepareStatement(DELETE_TIME);

        deleteStatement.setTime(1, insertTime, calGenerate);

        iDeletedCount = deleteStatement.executeUpdate();

        Assert.assertEquals(
            "Exactly one record with time data shoud have been deleted.",
            iDeletedCount, 1);

        // And now test the date
        Assert.assertNotNull(
            "The inserted time shouldn't be retrieved as null from the database",
            retrievedTime);

        // Ignore milliseconds when comparing dates
        String selectString = selectTime.toString();
        String retrievedString = retrievedTime.toString();

        boolean result =
            retrievedString.equals(selectString);

        Assert.assertTrue(
            "The time retrieved from database "
            + DateFormat.getDateTimeInstance().format(retrievedTime)
            + " is not the same as the inserted one "
            + DateFormat.getDateTimeInstance().format(insertTime), result);
    }
}
