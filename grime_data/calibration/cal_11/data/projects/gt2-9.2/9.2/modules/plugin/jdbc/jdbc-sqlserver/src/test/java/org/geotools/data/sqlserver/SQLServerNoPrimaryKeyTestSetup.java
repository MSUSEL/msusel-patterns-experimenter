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

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected SQLServerNoPrimaryKeyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"id\" int, "
                + "\"geom\" geometry, \"name\" varchar(255) )");
        
        run("INSERT INTO \"lake\" (\"id\",\"geom\",\"name\") VALUES (0,"
                + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
        
        // can't do this, sql server requires a primary key to use spatial indexes
        // run("CREATE SPATIAL INDEX _lake_geometry_index on lake(geom) WITH (BOUNDING_BOX = (-10, -10, 10, 10))");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DROP TABLE \"lake\"");
    }

}
