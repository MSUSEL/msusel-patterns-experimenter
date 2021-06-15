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
package org.geotools.arcsde.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.SdeRow;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public class ArcSDEClobTest {
    private static ClobTestData testData;
    
    private String[] columnNames = { "IntegerField", "ClobField" };

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        testData = new ClobTestData();
        testData.setUp();
        final boolean insertTestData = true;
        testData.createTempTable(insertTestData);
    }

    @AfterClass
    public static void finalTearDown() {
        boolean cleanTestTable = false;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
    }

    /**
     * loads {@code test-data/testparams.properties} into a Properties object, wich is used to
     * obtain test tables names and is used as parameter to find the DataStore
     */
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRead() throws Exception {
        ISession session = null;
        try {
            ArcSDEDataStore dstore = testData.getDataStore();
            session = dstore.getSession(Transaction.AUTO_COMMIT);
            // TODO: This is crap. If data can't be loaded, add another config for the existing
            // table
            String typeName = testData.getTempTableName(session);
            SimpleFeatureType ftype = dstore.getSchema(typeName);
            // The row id column is not returned, but the geometry column is (x+1-1=x)
            assertEquals("Verify attribute count.", columnNames.length, ftype.getAttributeCount());
            ArcSDEQuery query = ArcSDEQuery.createQuery(session, ftype, Query.ALL,
                    FIDReader.NULL_READER, ArcSdeVersionHandler.NONVERSIONED_HANDLER);
            query.execute();
            SdeRow row = query.fetch();
            assertNotNull("Verify first result is returned.", row);
            Object longString = row.getObject(0);
            assertNotNull("Verify the non-nullity of first CLOB.", longString);
            assertEquals("Verify stringiness.", longString.getClass(), String.class);
            row = query.fetch();
            longString = row.getObject(0);
            assertNotNull("Verify the non-nullity of second CLOB.", longString);
            query.close();
        } finally {
            if (session != null) {
                session.dispose();
            }
        }
    }

}
