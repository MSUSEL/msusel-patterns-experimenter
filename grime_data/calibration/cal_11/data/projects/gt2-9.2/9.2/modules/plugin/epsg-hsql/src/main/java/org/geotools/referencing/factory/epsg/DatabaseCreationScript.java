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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hsqldb.jdbc.jdbcDataSource;

/**
 * Utility used to create a HSQL zipped version of the official EPSG database 
 *
 *
 *
 * @source $URL$
 */
public class DatabaseCreationScript {

    public static void main(String[] args) throws Exception {
        /**
         * BEFORE USING THIS SCRIPT
         * - make sure you've created modified .sql files following the instructions 
         * - update ThreadedHsqlEpsgFactory.VERSION 
         * - modify the "directory" variable below to point to the folder containing the SQL scripts
         */
        String inputDirectory = "./src/main/resources/org/geotools/referencing/factory/epsg/";

        /**
         * The files we're interested into
         */
        File directory = new File(inputDirectory);
        File propertyFile = new File(directory, ThreadedHsqlEpsgFactory.DATABASE_NAME + ".properties");
        File databaseFile = new File(directory, ThreadedHsqlEpsgFactory.DATABASE_NAME + ".data");
        File backupFile = new File(directory, ThreadedHsqlEpsgFactory.DATABASE_NAME + ".backup");
        File scriptFile = new File(directory, ThreadedHsqlEpsgFactory.DATABASE_NAME + ".script");
        File zipFile =  new File(directory, ThreadedHsqlEpsgFactory.DATABASE_NAME + ".zip");

        /**
         * Preventive cleanup of the files should an old run was broken or stopped in the middle
         */
        propertyFile.delete();
        databaseFile.delete();
        backupFile.delete(); 
        scriptFile.delete();
        zipFile.delete();


        /*
         * Constructs the datasource. Note: we do not use
         * File.toURI() because HSQL doesn't seem to expect an encoded URL
         * (e.g. "%20" instead of spaces).
         */
        final jdbcDataSource source = new jdbcDataSource();
        final StringBuilder url = new StringBuilder(ThreadedHsqlEpsgFactory.PREFIX);
        final String path = directory.getAbsolutePath().replace(File.separatorChar, '/');
        if (path.length()==0 || path.charAt(0)!='/') {
            url.append('/');
        }
        url.append(path);
        if (url.charAt(url.length()-1) != '/') {
            url.append('/');
        }
        url.append(ThreadedHsqlEpsgFactory.DATABASE_NAME);
        source.setDatabase(url.toString());
        source.setUser("SA"); 
        
        Connection connection = source.getConnection();
        /*
         * HSQL has created automatically an empty database. We need to populate it. Executes
         * the SQL scripts bundled in the JAR. In theory, each line contains a full SQL
         * statement. For this plugin however, we have compressed "INSERT INTO" statements using
         * Compactor class in this package.
         */
        System.out.println("Creating the EPSG database");
        final Statement statement = connection.createStatement();
        try {
            // read and execute the scripts that make up the database
            statement.execute("CREATE ALIAS CHR for \"org.hsqldb.Library.character\"");
            executeScript(new File(directory, "EPSG_Tables.sql"), statement);
            executeScript(new File(directory, "EPSG_Data.sql"), statement);
            statement.execute("UPDATE EPSG_DATUM SET REALIZATION_EPOCH = NULL WHERE REALIZATION_EPOCH = ''");
            statement.execute("ALTER TABLE EPSG_DATUM ALTER COLUMN REALIZATION_EPOCH INTEGER");
            executeScript(new File(directory, "EPSG_FKeys.sql"), statement);
            executeScript(new File(directory, "EPSG_Indexes.sql"), statement);
            statement.execute("SHUTDOWN COMPACT");
        } catch (IOException exception) {
            SQLException e = new SQLException("Error occurred while executing "
                    + "the EPSG database creation scripts");
            e.initCause(exception);
            throw e;
        } finally {
            statement.close();
            connection.close();
        }
        System.out.println("EPSG database created");

        /*
         * The database has been fully created. Mark some extra properties in the property
         * file (among others, the version and make it read only)
         * 
         */
        final InputStream propertyIn = new FileInputStream(propertyFile);
        final Properties properties  = new Properties();
        properties.load(propertyIn);
        propertyIn.close();
        properties.put("epsg.version", ThreadedHsqlEpsgFactory.VERSION.toString());
        properties.put("readonly", "true");
        final OutputStream out = new FileOutputStream(propertyFile);
        properties.store(out, "EPSG database on HSQL");
        out.close();

        /*
         * Zip the database
         */
        System.out.println("Creating the zipped database");
        byte[] buf = new byte[1024];
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        File[] files = new File[] {databaseFile, propertyFile, scriptFile};
        for(File file : files) {
            FileInputStream in = new FileInputStream(file);
    
            zos.putNextEntry(new ZipEntry(file.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        }
        zos.close();

        /**
         * Cleanup, delete the database files
         */
        System.out.println("Cleaning up the unzipped database files");
        propertyFile.delete();
        databaseFile.delete();
        backupFile.delete(); 
        scriptFile.delete();
        
        System.out.println("Done. The zipped database file is available at " + zipFile.getAbsolutePath());
    }
    
    static void executeScript(File scriptFile, Statement statement) throws IOException, SQLException {
        System.out.println("Executing script " + scriptFile.getPath());
        SqlScriptReader reader = null;
        try {
            // first read in the tables
            reader = new SqlScriptReader(new InputStreamReader(new FileInputStream(scriptFile), "ISO-8859-15"));
            while(reader.hasNext()) {
                statement.execute(reader.next());
            }
        } finally {
            if(reader != null) 
                reader.dispose();
        }
    }

}
