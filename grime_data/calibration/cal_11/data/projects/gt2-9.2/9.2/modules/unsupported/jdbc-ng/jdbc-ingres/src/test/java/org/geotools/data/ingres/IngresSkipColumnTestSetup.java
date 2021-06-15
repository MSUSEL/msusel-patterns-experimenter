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
package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCSkipColumnTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class IngresSkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected IngresSkipColumnTestSetup() {
        super(new IngresTestSetup());
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"skipcolumn\"(" //
                + "\"fid\" int primary key, " //
                + "\"id\" integer, " //
                + "\"geom\" POINT SRID 4326, " //
                + "\"weirdproperty\" NBR," //
                + "\"name\" varchar(256))");
        run("CREATE SEQUENCE skipcolumn_fid_sequence");
        
        run("INSERT INTO \"skipcolumn\" VALUES(NEXT VALUE FOR skipcolumn_fid_sequence, " +
        		"0, GeometryFromText('POINT(0 0)', 4326), null, 'GeoTools')"); 

    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        runSafe("DROP TABLE \"skipcolumn\"");
        runSafe("DROP SEQUENCE skipcolumn_fid_sequence");
    }

}
