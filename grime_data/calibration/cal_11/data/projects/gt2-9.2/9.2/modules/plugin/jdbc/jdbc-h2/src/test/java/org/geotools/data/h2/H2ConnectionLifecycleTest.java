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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.jdbc.ConnectionLifecycleListener;
import org.geotools.jdbc.JDBCConnectionLifecycleTest;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.VirtualTable;


public class H2ConnectionLifecycleTest extends JDBCConnectionLifecycleTest {
    
    private class SetVariableListener implements ConnectionLifecycleListener {

        double value;
        
        public void onBorrow(JDBCDataStore store, Connection cx) throws SQLException {
            Statement st = null;
            try {
                st = cx.createStatement();
                st.execute("SET @MYVAR = " + value);
            } finally {
                store.closeSafe(st);
            }
        }

        public void onRelease(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }

        public void onCommit(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }

        public void onRollback(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }

    }
    
    protected JDBCTestSetup createTestSetup() {
        return new H2TestSetup();
    }
    
    public void testVariableListener() throws Exception {
        // setup a virtual table using the user variable
        VirtualTable vt = new VirtualTable("ft1var", "select * from  \"geotools\".\"ft1\" where \"doubleProperty\" > @MYVAR");
        dataStore.addVirtualTable(vt);
        
        // setup a listener that uses said variable
        SetVariableListener listener = new SetVariableListener();
        dataStore.getConnectionLifecycleListeners().add(listener);
        
        // set the value and test
        listener.value = 1.0;
        SimpleFeatureSource fs = dataStore.getFeatureSource("ft1var");
        assertEquals(2, fs.getCount(Query.ALL));
        
        listener.value = 10;
        assertEquals(0, fs.getCount(Query.ALL));
        
    }
}
