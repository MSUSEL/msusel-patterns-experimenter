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

import org.geotools.jdbc.JDBCSkipColumnTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class TeradataSkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    public TeradataSkipColumnTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }

    protected void createSkipColumnTable() throws Exception {
        // type dollar allready exists on Teradata 13 linux
        try {
            run("CREATE TYPE dollar AS DECIMAL(8,2) FINAL");
        } catch (Exception e) {
        }
        run("CREATE TABLE \"skipcolumn\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0)  integer, \"id\" integer, \"geom\" ST_GEOMETRY, \"weirdproperty\" dollar,\"name\" varchar(200))");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '"
                + fixture.getProperty("schema") + "', 'skipcolumn', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POINT')");
        // run("CREATE INDEX SKIPCOLUMN_GEOM_INDEX ON \"skipcolumn\" USING GIST (\"geom\") ");

        run("INSERT INTO \"skipcolumn\" VALUES(0, 0, 'POINT(0 0)', null, 'GeoTools')");

    }

    protected void dropSkipColumnTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'skipcolumn'");
        runSafe("DROP TABLE \"skipcolumn\"");
        runSafe("drop type dollar");
    }

}
