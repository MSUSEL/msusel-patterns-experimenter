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
package org.geotools.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Allows reuse of JDBCTestSetup for a different set of tests.
 * <p>
 * For example see JDBC3DTestSetup which provides a different
 * test dataset, while still using the provided delegate
 * to access a test fixture and establish a connection.
 * 
 * @see JDBC3DTestSetup
 * @source $URL$
 */
public class JDBCDelegatingTestSetup extends JDBCTestSetup {

    protected JDBCTestSetup delegate;
    
    protected JDBCDelegatingTestSetup( JDBCTestSetup delegate ) {
        this.delegate = delegate;
    }

    @Override
    public void setFixture(Properties fixture) {
        super.setFixture(fixture);
        delegate.setFixture(fixture);
    }
    
    @Override
    protected Properties createOfflineFixture() {
        return delegate.createOfflineFixture();
    }
    
    @Override
    protected Properties createExampleFixture() {
        return delegate.createExampleFixture();
    }
    
    public void setUp() throws Exception {
        // make sure we don't forget to run eventual extra stuff
        delegate.setUp();
    }
    
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        delegate.tearDown();
    }
    
    @Override
    protected void setUpData() throws Exception {
        delegate.setUpData();
    }
    
    protected final void initializeDatabase() throws Exception {
        delegate.initializeDatabase();
    }

    protected void initializeDataSource(BasicDataSource ds, Properties db) {
        delegate.initializeDataSource(ds, db);
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return delegate.createDataStoreFactory();
    }
    
    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        delegate.setUpDataStore(dataStore);
    }

    @Override
    protected String typeName(String raw) {
        return delegate.typeName(raw);
    }
    
    @Override
    protected String attributeName(String raw) {
        return delegate.attributeName(raw);
    }
    
    @Override
    public boolean shouldRunTests(Connection cx) throws SQLException {
        return delegate.shouldRunTests(cx);
    }
}
