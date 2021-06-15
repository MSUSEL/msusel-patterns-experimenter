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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCViewTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class TeradataViewTestSetup extends JDBCViewTestSetup {

    protected TeradataViewTestSetup() {
        super(new TeradataTestSetup());
    }


    protected void createLakesTable() throws Exception {
        run("CREATE TABLE \"lakes\"(\"fid\" generated always as identity int primary key NOT NULL, \"id\" int, \"geom\" ST_GEOMETRY, \"name\" varchar(200) )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + fixture.getProperty("schema") + "', 'lakes', 'geom', 2, 1619, 'POLYGON')");
//         run("CREATE INDEX LAKES_GEOM_INDEX ON \"lakes\" USING GIST (\"geom\") ");

        run("INSERT INTO \"lakes\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))','muddy')");
    }


    protected void dropLakesTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakes'");
        runSafe("DROP TABLE \"lakes\"");
    }


    protected void createLakesView() throws Exception {
        run("create view \"lakesview\" ( \"fid\", id, \"geom\",\"name\" )  as select * from \"lakes\"");
        // disabled insert to make sure views work even without geom column declarations
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + fixture.getProperty("schema") + "', 'lakesview', 'geom', 2, 1619, 'POLYGON')");
    }


    protected void dropLakesView() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakesview'");
        runSafe("DROP VIEW \"lakesview\"");
    }


    protected void createLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }


    protected void dropLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }

}
