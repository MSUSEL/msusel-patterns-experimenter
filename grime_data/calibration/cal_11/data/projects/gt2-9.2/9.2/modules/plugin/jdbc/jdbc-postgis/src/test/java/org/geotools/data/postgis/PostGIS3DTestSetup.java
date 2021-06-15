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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBC3DTestSetup;

/**
 * 
 * 
 * @source $URL$
 */
public class PostGIS3DTestSetup extends JDBC3DTestSetup {

    protected PostGIS3DTestSetup() {
        super(new PostGISTestSetup());
    
    }

    @Override
    protected void createLine3DTable() throws Exception {
        // setup table
        run("CREATE TABLE \"line3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                + "\"geom\" geometry, \"name\" varchar )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'line3d', 'geom', 3, '4326', 'LINESTRING')");
        run("CREATE INDEX line3d_GEOM_IDX ON \"line3d\" USING GIST (\"geom\") ");
    
        // insert data
        run("INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                + "ST_GeomFromText('LINESTRING(1 1 0, 2 2 0, 4 2 1, 5 1 1)', 4326),"
                + "'l1')");
        run("INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                + "ST_GeomFromText('LINESTRING(3 0 1, 3 2 2, 3 3 3, 3 4 5)', 4326),"
                + "'l2')");
    }

    @Override
    protected void createPoint3DTable() throws Exception {
        // setup table
        run("CREATE TABLE \"point3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                + "\"geom\" geometry, \"name\" varchar )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'point3d', 'geom', 3, '4326', 'POINT')");
        run("CREATE INDEX POINT3D_GEOM_IDX ON \"point3d\" USING GIST (\"geom\") ");
    
        // insert data
        run("INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                + "ST_GeomFromText('POINT(1 1 1)', 4326)," + "'p1')");
        run("INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                + "ST_GeomFromText('POINT(3 0 1)', 4326)," + "'p2')");
    }

    @Override
    protected void dropLine3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'line3d'");
        run("DROP TABLE \"line3d\"");
    }

    @Override
    protected void dropPoly3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'poly3d'");
        run("DROP TABLE \"poly3d\"");
    }

    @Override
    protected void dropPoint3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'point3d'");
        run("DROP TABLE \"point3d\"");
    }

}
