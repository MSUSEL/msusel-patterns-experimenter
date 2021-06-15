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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCViewTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class TeradataViewTestSetup extends JDBCViewTestSetup {

    public TeradataViewTestSetup() {
        super(new TeradataTestSetup());
    }
    
    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();

        createLakesView2();
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE lakes (fid NOT NULL PRIMARY KEY INTEGER, id INTEGER, geom ST_Geometry, " +
            "name VARCHAR(100))");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME," +
            " F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + 
                fixture.getProperty("database") + "', 'lakes', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POLYGON')");
        
        run("INSERT INTO lakes VALUES (0, 0, 'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 'muddy')");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakes'");
        runSafe("DROP TABLE lakes");
    }
    
    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW lakesview AS SELECT * FROM lakes");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME," +
            " F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + 
                fixture.getProperty("database") + "', 'lakesview', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POLYGON')");
    }

    protected void createLakesView2() throws Exception {
        runSafe("DROP VIEW lakesview2");
        run("CREATE VIEW lakesview2 AS SELECT * FROM lakes");

        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakesview2'");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME," +
            " F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" +
                fixture.getProperty("database") + "', 'lakesview2', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POLYGON')");

        runSafe("DROP TABLE \"gt_pk_metadata\"");
        run("CREATE TABLE gt_pk_metadata (" + "table_schema VARCHAR(32) NOT NULL, "
                + "table_name VARCHAR(32) NOT NULL, "
                + "pk_column VARCHAR(32) NOT NULL, "
                + "pk_column_idx INTEGER, "
                + "pk_policy VARCHAR(32), "
                + "pk_sequence VARCHAR(64), "
                + "unique (table_schema, table_name, pk_column),"
                + "check (pk_policy in ('sequence', 'assigned', 'autoincrement')))");
        run("INSERT INTO gt_pk_metadata VALUES('" + fixture.getProperty("schema") + "', 'lakesview2', 'fid', 0, 'assigned', NULL)");

        runSafe("DROP TABLE \"lakes_geom_idx\"");
        run("CREATE TABLE \"lakes_geom_idx\""
                    + " (fid INTEGER NOT NULL, cellid INTEGER NOT NULL)");

        runSafe("DROP VIEW \"lakesview2_geom_idx\"");
        run("CREATE VIEW \"lakesview2_geom_idx\" AS SELECT * FROM lakes_geom_idx");

        runSafe("DELETE FROM sysspatial.tessellation WHERE f_table_name = 'lakesview2'");
        run("INSERT INTO sysspatial.tessellation VALUES ("
            + "'geotools',"
            + "'lakesview2',"
            + "'geom',"
            + "-180,-90,180,90,"
            + "1000,1000,3,.01,0"
        + ")");
    }

    @Override
    protected void dropLakesView() throws Exception {
        run("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakesview'");
        runSafe("DROP VIEW lakesview");
    }

    @Override
    protected void createLakesViewPk() throws Exception {
    }

    

    @Override
    protected void dropLakesViewPk() throws Exception {
    }

}
