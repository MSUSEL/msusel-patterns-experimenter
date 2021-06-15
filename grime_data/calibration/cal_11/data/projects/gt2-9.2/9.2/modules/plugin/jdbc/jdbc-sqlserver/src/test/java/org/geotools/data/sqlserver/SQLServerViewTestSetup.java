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

import org.geotools.jdbc.JDBCViewTestSetup;

public class SQLServerViewTestSetup extends JDBCViewTestSetup {

    protected SQLServerViewTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE lakes(fid int IDENTITY(0,1) PRIMARY KEY, id int, "
                + "geom geometry, name varchar(255) )");

        run("INSERT INTO lakes (id,geom,name) VALUES ( 0,"
                + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");

        run("CREATE SPATIAL INDEX _lakes_geometry_index on lakes(geom) WITH (BOUNDING_BOX = (-100, -100, 100, 100))");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe("DROP TABLE \"lakes\"");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("create view \"lakesview\" as select * from \"lakes\"");
    }

    @Override
    protected void dropLakesView() throws Exception {
        runSafe("DROP VIEW \"lakesview\"");
    }

    @Override
    protected void createLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void dropLakesViewPk() throws Exception {
        // TODO Auto-generated method stub

    }

}
