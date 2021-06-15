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
package org.geotools.data.jdbc.ds;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DBCPDataSourceFactory;
import org.geotools.data.jdbc.datasource.DataSourceFinder;

/**
 * 
 *
 * @source $URL$
 */
public class DataSourceFinderTest extends TestCase {
    public void testDbcpFactory() throws IOException {
        assertTrue(new DBCPDataSourceFactory().isAvailable());
        DataSourceFinder.scanForPlugins();
        
        Map map = new HashMap();
        map.put(DBCPDataSourceFactory.DSTYPE.key, "DBCP");
        map.put(DBCPDataSourceFactory.DRIVERCLASS.key, "org.h2.Driver");
        map.put(DBCPDataSourceFactory.JDBC_URL.key, "jdbc:h2:mem:test_mem");
        map.put(DBCPDataSourceFactory.USERNAME.key, "admin");
        map.put(DBCPDataSourceFactory.PASSWORD.key, "");
        map.put(DBCPDataSourceFactory.MAXACTIVE.key, new Integer(10));
        map.put(DBCPDataSourceFactory.MAXIDLE.key, new Integer(0));
        
        DataSource source =  DataSourceFinder.getDataSource(map);
        assertNotNull(source);
        assertTrue(source instanceof BasicDataSource);
    }
    
//    public void testJNDIFactory() throws Exception {
        // can't make this work... there are dependencies from EJBMock to stuff
        // that's not in the maven repos
        
//        EJBMockObjectFactory ejbMock = new EJBMockObjectFactory();
//        ejbMock.initMockContextFactory();
//        Context mockContext = new MockContext();
//        InitialContext context = new InitialContext();
//        DataSource mockDataSource = new MockDataSource();
//        context.rebind("jdbc/pool", mockDataSource);
//        JNDI.init(context);
//        
//        assertTrue(new JNDIDataSourceFactory().isAvailable());
//        DataSourceFinder.scanForPlugins();
//        
//        
//        
//        Map map = new HashMap();
//        map.put(JNDIDataSourceFactory.JNDI_REFNAME.key, "jdbc/pool");
//        
//        DataSource source =  DataSourceFinder.getDataSource(map);
//        assertNotNull(source);
//        assertEquals(mockDataSource, source);
//    }
}
