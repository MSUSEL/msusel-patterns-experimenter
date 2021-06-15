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
package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCGeometryTestSetup;

/**
 * @author DamianoG
 * 
 */
public class SQLServerGeometryTestSetup extends JDBCGeometryTestSetup {

    protected SQLServerGeometryTestSetup() {
        super(new SQLServerTestSetup());
    }

    public void setUp() throws Exception {
        super.setUp();
        runSafe("DROP TABLE GEOMETRY_COLUMNS");
        runSafe("DROP TABLE gtmeta");

        // create the geometry columns
        run("CREATE TABLE GEOMETRY_COLUMNS(" + "F_TABLE_SCHEMA VARCHAR(30) NOT NULL,"
                + "F_TABLE_NAME VARCHAR(30) NOT NULL," + "F_GEOMETRY_COLUMN VARCHAR(30) NOT NULL,"
                + "COORD_DIMENSION INTEGER," + "SRID INTEGER NOT NULL,"
                + "TYPE VARCHAR(30) NOT NULL," + ");");

        String sql = "CREATE TABLE gtmeta (" 
                + "id int IDENTITY(0,1) PRIMARY KEY, geom geometry, intProperty int , "
                + "doubleProperty float, stringProperty varchar(255))";
        run(sql);
        
        sql = "CREATE SPATIAL INDEX _gtmeta_geom_index on gtmeta(geom) WITH (BOUNDING_BOX = (-10, -10, 10, 10))"; 
        run(sql);
        
        sql = "INSERT INTO GEOMETRY_COLUMNS (F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, TYPE) " + 
                "VALUES ('', 'gtmeta','geom', 2, 4326, 'POINT')";
        run(sql);
    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '" + tableName + "'");
        runSafe("DROP TABLE " + tableName);
    }

    public void setupMetadataTable(JDBCDataStore dataStore) {
        ((SQLServerDialect) dataStore.getSQLDialect()).setGeometryMetadataTable("GEOMETRY_COLUMNS");
    }

}
