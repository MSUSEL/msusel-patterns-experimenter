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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class H2SkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected H2SkipColumnTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }
    
    @Override
    protected void createSkipColumnTable() throws Exception {
        run("CREATE TABLE \"skipcolumn\" (\"fid\" int AUTO_INCREMENT(1) PRIMARY KEY, "
        + "\"id\" int, \"geom\" POINT, \"weird\" array, \"name\" varchar)");
        run("CALL AddGeometryColumn(NULL, 'skipcolumn', 'geom', 4326, 'POINT', 2)");
        run("INSERT INTO \"skipcolumn\" VALUES ("
            + "0, 0, ST_GeomFromText('POINT(0 0)',4326), null, 'GeoTools')");
    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'skipcolumn'");
        runSafe("DROP TABLE \"skipcolumn\"");
    }

}
