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

public class TestScript extends TestBase {

      String path = "TestSelf01TempTables.txt";
//       String path = "TestSelfTriggers2.txt";
//    String path = "TestAny.txt";
//    String path = "TestSelf.txt";
//    String path = "TestSelf3PartNames.txt";
//    String path = "TestSelfArithmetic.txt";
//    String path = "TestSelfAlterColumn.txt";
//    String path = "TestSelfCaseWhen.txt";
//    String path = "TestSelfCheckConstraints.txt";
//    String path = "TestSelfColGrant.txt";
//    String path = "TestSelfCreate.txt";
//    String path = "TestSelfConstraints.txt";
//    String path = "TestSelfGrantees.txt";
//    String path = "TestSelfGroupBy.txt";
//    String path = "TestSelfInsertDeleteQueries.txt";
//    String path = "TestSelfInterval.txt";
//    String path = "TestSelfInternalFunctions.txt";
//    String path = "TestSelfFieldLimits.txt";
//    String path = "TestSelfFKModes.txt";
//    String path = "TestSelfInPredicateReferencing.txt";
//    String path = "TestSelfIssues.txt";
//    String path = "TestSelfJoins.txt";
//    String path = "TestSelfLeftJoin.txt";
//    String path = "TestSelfNameResolution.txt";
//    String path = "TestSelfImmediateShutdown.txt";
//    String path = "TestSelfInsertDeleteQueries.txt";
//    String path = "TestSelfInPredicateReferencing.txt";
//    String path = "TestSelfMultiGrants.txt";
//    String path = "TestSelfNot.txt";
//    String path = "TestSelfOrderLimits.txt";
//    String path = "TestSelfRoleNesting.txt";
//    String path = "TestSelfQueries.txt";
//    String path = "TestSelfSchemaPersistB1.txt";
//    String path = "TestSelfSeqRightsA.txt";
//      String path = "TestSelfStoredProcedure.txt";
//      String path = "TestSelfStoredProcedureTypes.txt";
//    String path = "TestSelfSysTables.txt";
//    String path = "TestSelfTempTable1.txt";
//    String path = "TestSelfTransaction.txt";
//    String path = "TestSelfTriggers.txt";
//    String path = "TestSelfUnions.txt";
//    String path = "TestSelfUserFunction.txt";
//    String path = "TestSelfViews.txt";
//    String path = "TestSelfViewGrants.txt";
//    String path = "TestSelfSeqRightsA.txt";
//      String path = "TestSelfSysTables.txt";
//    String path = "TestTemp.txt";
    public TestScript(String name) {
        super(name, null, false, false);
    }

    public void test() throws java.lang.Exception {

        TestUtil.deleteDatabase("test");

        Connection conn = newConnection();
        String fullPath = "testrun/hsqldb/" + path;
        TestUtil.testScript(conn, fullPath);
        conn.createStatement().execute("SHUTDOWN IMMEDIATELY");
    }

    public static void main(String[] Args) throws Exception {

        TestScript ts = new TestScript("test");

        ts.test();
    }
}
