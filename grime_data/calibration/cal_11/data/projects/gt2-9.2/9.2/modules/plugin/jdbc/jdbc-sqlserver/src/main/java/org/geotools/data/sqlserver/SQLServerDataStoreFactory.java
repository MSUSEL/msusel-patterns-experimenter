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
package org.geotools.data.sqlserver;

import java.io.IOException;
import java.util.Map;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStore factory for Microsoft SQL Server.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 *
 * @source $URL$
 */
public class SQLServerDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "sqlserver");
    
    /** parameter for using integrated security, only works on windows, ignores the user and password parameters, the current windows user account is used for login*/
    public static final Param INTSEC = new Param("Integrated Security", Boolean.class, "Login as current windows user account. Works only in windows. Ignores user and password settings.", false, new Boolean(false)); 

	/** parameter for using Native Paging */
	public static final Param NATIVE_PAGING = new Param("Use Native Paging", Boolean.class, "Use native paging for sql queries. For some sets of data, native paging can have a performance impact.", false, Boolean.TRUE);	
		
    /** Metadata table providing information about primary keys **/
    public static final Param GEOMETRY_METADATA_TABLE = new Param("Geometry metadata table", String.class,
            "The optional table containing geometry metadata (geometry type and srid). Can be expressed as 'schema.name' or just 'name'", false);

    /** parameter for using WKB or Sql server binary directly. Setting to true will use WKB */
    public static final Param NATIVE_SERIALIZATION = new Param("Use native geometry serialization", Boolean.class,
            "Use native SQL Server serialization, or WKB serialization.", false, Boolean.FALSE);
    
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new SQLServerDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    public String getDescription() {
        return "Microsoft SQL Server";
    }
    
    @Override
    protected String getDriverClassName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    protected String getValidationQuery() {
        return "select 1";
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(INTSEC.key, INTSEC);
        parameters.put(NATIVE_PAGING.key, NATIVE_PAGING);
        parameters.put(NATIVE_SERIALIZATION.key, NATIVE_SERIALIZATION);
        parameters.put(GEOMETRY_METADATA_TABLE.key, GEOMETRY_METADATA_TABLE);
    }
    
    /**
     *  Builds up the JDBC url in a jdbc:<database>://<host>:<port>;DatabaseName=<dbname>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String url = super.getJDBCUrl(params);
        String db = (String) DATABASE.lookUp(params);
        Boolean intsec = (Boolean) INTSEC.lookUp(params);
        if (db != null) {
            url = url.substring(0, url.lastIndexOf("/")) + (db != null ? ";DatabaseName="+db : "");
        }

        if (intsec != null && intsec.booleanValue()) {
        	url = url + ";integratedSecurity=true";
        }
        
        return url;
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
    	
    	// check the geometry metadata table
        String metadataTable = (String) GEOMETRY_METADATA_TABLE.lookUp(params);
        dialect.setGeometryMetadataTable(metadataTable);

    	// check native paging
        Boolean useNativePaging = (Boolean) NATIVE_PAGING.lookUp(params);
        dialect.setUseOffSetLimit(useNativePaging == null || Boolean.TRUE.equals(useNativePaging));

        // check serialization format
        Boolean useNativeSerialization = (Boolean) NATIVE_SERIALIZATION.lookUp(params);
        if (useNativeSerialization != null) {
            dialect.setUseNativeSerialization(useNativeSerialization);
        }

        return dataStore;
    }
    
}
