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
package org.geotools.data.spatialite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteJDBCLoader;

/**
 * DataStoreFactory for SpatiaLite database.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 *
 * @source $URL$
 */
public class SpatiaLiteDataStoreFactory extends JDBCDataStoreFactory {

  
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "spatialite");
    
    /** optional user parameter */
    public static final Param USER = new Param(JDBCDataStoreFactory.USER.key, JDBCDataStoreFactory.USER.type, 
            JDBCDataStoreFactory.USER.description, false, JDBCDataStoreFactory.USER.sample);
    
    /**
     * base location to store sqlite database files
     */
    File baseDirectory = null;

    /**
     * Sets the base location to store sqlite database files.
     *
     * @param baseDirectory A directory.
     */
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * The base location to store sqlite database files.
     */
    public File getBaseDirectory() {
        return baseDirectory;
    }

    @Override
    public boolean isAvailable() {
        if (!super.isAvailable()) {
            return false;
        }

        return SQLiteJDBCLoader.isNativeMode();
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new SpatiaLiteDialect( dataStore );
    }

    @Override
    protected String getDatabaseID() {
        return "spatialite";
    }
    
    @Override
    protected String getDriverClassName() {
        return "org.sqlite.JDBC";
    }
    
    public String getDescription() {
        return "SpatiaLite";
    }

    @Override
    protected String getValidationQuery() {
        return null;
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        
        //remove unneccessary parameters
        parameters.remove(HOST.key);
        parameters.remove(PORT.key);
        
        //remove user and password temporarily in order to make username optional
        parameters.remove(JDBCDataStoreFactory.USER.key);
        parameters.put(USER.key, USER);
        
        //add user 
        //add additional parameters
        parameters.put(DBTYPE.key, DBTYPE);

    }
    
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String db = (String) DATABASE.lookUp(params);
        String location = db;
        if (baseDirectory != null) {
            //prepend base directory unless it is an absolute path
            if (!new File(location).isAbsolute()) {
                location = baseDirectory.getAbsolutePath() + File.separator + db;    
            }
        }
        return "jdbc:sqlite:" + location;
    }
    
    @Override
    public BasicDataSource createDataSource(Map params) throws IOException {
        //create a datasource
        BasicDataSource dataSource = new BasicDataSource();

        // driver
        dataSource.setDriverClassName(getDriverClassName());

        // url
        dataSource.setUrl(getJDBCUrl(params));
        
        addConnectionProperties(dataSource);
        initializeDataSource(dataSource);
        
        return dataSource;
    }
    
    static void addConnectionProperties(BasicDataSource dataSource) {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableLoadExtension(true);
        config.enableSpatiaLite(true);
        
        for (Map.Entry e : config.toProperties().entrySet()) {
            dataSource.addConnectionProperty((String)e.getKey(), (String)e.getValue());
        }
    }
    
    static void initializeDataSource(BasicDataSource dataSource) throws IOException {
        //because of the way spatialite is loaded we need to instantiate and close
        // a connection, and the spatialite functions will be registered for all future
        // connections
        try {
            Connection cx = dataSource.getConnection();
            cx.close();
        } 
        catch (SQLException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }
}
