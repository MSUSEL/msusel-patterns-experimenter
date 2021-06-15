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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public AllTests(String s) {
        super(s);
    }

    public static Test suite() {

        TestSuite suite = new TestSuite();

        suite.addTestSuite(org.hsqldb.test.TestBatchExecution.class);
        suite.addTestSuite(org.hsqldb.test.TestBug1191815.class);
        suite.addTestSuite(org.hsqldb.test.TestBug778213.class);
        suite.addTestSuite(org.hsqldb.test.TestBug785429.class);
        suite.addTestSuite(org.hsqldb.test.TestBug808460.class);
        suite.addTestSuite(org.hsqldb.test.TestCollation.class);
        suite.addTestSuite(org.hsqldb.test.TestDatabaseMetaData.class);
        suite.addTestSuite(org.hsqldb.test.TestDateTime.class);
        suite.addTestSuite(org.hsqldb.test.TestINPredicateParameterizationAndCorrelation.class);
        suite.addTestSuite(org.hsqldb.test.TestJDBCGeneratedColumns.class);
        suite.addTestSuite(org.hsqldb.test.TestJDBCSavepoints.class);
        suite.addTestSuite(org.hsqldb.test.TestLikePredicateOptimizations.class);
        suite.addTestSuite(org.hsqldb.test.TestLobs.class);
        suite.addTestSuite(org.hsqldb.test.TestMerge.class);
        suite.addTestSuite(org.hsqldb.test.TestMultiInsert.class);
        suite.addTestSuite(org.hsqldb.test.TestPreparedSubQueries.class);
        suite.addTestSuite(org.hsqldb.test.TestSql.class);
        suite.addTestSuite(org.hsqldb.test.TestStoredProcedure.class);
        suite.addTestSuite(org.hsqldb.test.TestSubselect.class);
        suite.addTestSuite(org.hsqldb.test.TestTextTable.class);
        suite.addTestSuite(org.hsqldb.test.TestTextTables.class);
        suite.addTestSuite(org.hsqldb.test.TestViewAsterisks.class);

        //
        suite.addTestSuite(org.hsqldb.test.TestCascade.class);
//        suite.addTestSuite(org.hsqldb.test.TestDataStructures.class);
        suite.addTestSuite(org.hsqldb.test.TestGroupByHaving.class);
        suite.addTestSuite(org.hsqldb.test.TestSqlPersistent.class);
        suite.addTestSuite(org.hsqldb.test.TestUpdatableResults.class);
        suite.addTestSuite(org.hsqldb.test.TestUpdatableResultSets.class);
        return suite;
    }
}
