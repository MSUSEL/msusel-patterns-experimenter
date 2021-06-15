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
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.Trigger;
import org.hsqldb.lib.ArrayUtil;

/**
 *
 * @author fredt
 */
public class TestTriggers extends TestBase {

    Connection conn;

    public TestTriggers(String testName) {
        super(testName, "jdbc:hsqldb:file:trigs", false, false);
    }

    public void setUp() {

        super.setUp();

        try {
            openConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void tearDown() {

        try {
            conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        super.tearDown();
    }

    public void testTriggerAction() {

        runScript();

        try {
            runStatements();
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        try {
            shutdownDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        try {
            runStatements();
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private void openConnection() throws SQLException {
        conn = newConnection();
    }

    private void runScript() {
        TestUtil.testScript(conn, "TestTriggers.txt");
    }

    private void shutdownDatabase() throws SQLException {

        Statement st = conn.createStatement();

        st.execute("shutdown");
        st.close();
    }

    private void runStatements() throws SQLException {

        Statement st = conn.createStatement();

        st.execute("delete from testtrig");
        st.execute("alter table testtrig alter column c1 restart with 0");
        clearCalls();
        st.execute(
            "insert into testtrig values (default, 'inserted val 1', 100)");
        checkCallCount(3);
        checkCalls(Trigger.INSERT_AFTER, 1);
        checkCalls(Trigger.INSERT_BEFORE_ROW, 1);
        checkCalls(Trigger.INSERT_AFTER_ROW, 1);
        clearCalls();
        st.execute(
            "insert into testtrig (c2, c3) select c2, c3 from testtrig where c1 < 0");
        checkCallCount(1);
        checkCalls(Trigger.INSERT_AFTER, 1);
        checkCalls(Trigger.INSERT_BEFORE_ROW, 0);
        checkCalls(Trigger.INSERT_AFTER_ROW, 0);
        clearCalls();
        st.execute("update testtrig set c2 = c2 || ' updated' where c1 = 0");
        checkCallCount(3);
        checkCalls(Trigger.UPDATE_AFTER, 1);
        checkCalls(Trigger.UPDATE_BEFORE_ROW, 1);
        checkCalls(Trigger.UPDATE_AFTER_ROW, 1);
        clearCalls();
        st.execute("update testtrig set c2 = c2 || ' updated' where c1 < 0");
        checkCallCount(1);
        checkCalls(Trigger.UPDATE_AFTER, 1);
        checkCalls(Trigger.UPDATE_BEFORE_ROW, 0);
        checkCalls(Trigger.UPDATE_AFTER_ROW, 0);
        st.close();
    }

    void checkCalls(int trigType, int callCount) {
        assertEquals("call count mismatch", TriggerClass.callCounts[trigType],
                     callCount);
    }

    void clearCalls() {

        TriggerClass.callCount = 0;

        ArrayUtil.fillArray(TriggerClass.callCounts, 0);
    }

    void checkCallCount(int count) {
        assertEquals("trigger call mismatch", count, TriggerClass.callCount);
    }
}
