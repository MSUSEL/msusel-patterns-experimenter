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
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.util.Version;

/**
 * 
 *
 * @source $URL$
 */
public class PostGISTestSetup extends JDBCTestSetup {

    protected Version postgisVersion, pgsqlVersion;

    @Override
    protected void initializeDatabase() throws Exception {
        DataSource dataSource = getDataSource();
        Connection cx = dataSource.getConnection();
        try {
            PostGISDialect dialect = new PostGISDialect(new JDBCDataStore());
            postgisVersion = dialect.getVersion(cx);
            pgsqlVersion = dialect.getPostgreSQLVersion(cx);
        }
        finally {
            cx.close();
        }
    }

    public boolean isVersion2() {
        return postgisVersion != null && postgisVersion.compareTo(PostGISDialect.V_2_0_0) >= 0;
    }

    public boolean isPgsqlVersionGreaterThanEqualTo(Version v) {
        return pgsqlVersion != null && pgsqlVersion.compareTo(v) >= 0;
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        // the unit tests assume a non loose behaviour
        ((PostGISDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(false);
        
        // the tests assume non estimated extents 
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(false);
        
        // let's work with the most common schema please
        dataStore.setDatabaseSchema("public");
    }

    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "org.postgresql.Driver");
        fixture.put("url", "jdbc:postgresql://localhost/mydbname");
        fixture.put("host", "localhost");
        fixture.put("database", "mydbname");
        fixture.put("port", "5432");
        fixture.put("user", "myuser");
        fixture.put("password", "mypassword");
        return fixture;
    }
    
    @Override
    protected void setUpData() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'ft1'");
        runSafe("DROP TABLE \"ft1\"");
        runSafe("DROP TABLE \"ft2\"");
        
        run("CREATE TABLE \"ft1\"(" //
                + "\"id\" serial primary key, " //
                + "\"geometry\" geometry, " //
                + "\"intProperty\" int," //
                + "\"doubleProperty\" double precision, " // 
                + "\"stringProperty\" varchar)");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'ft1', 'geometry', 2, '4326', 'POINT')");
        if (isVersion2()) {
            run("ALTER TABLE \"ft1\" ALTER COLUMN  \"geometry\" TYPE geometry(Point,4326);");
        }
        run("CREATE INDEX FT1_GEOMETRY_INDEX ON \"ft1\" USING GIST (\"geometry\") ");
        
        run("INSERT INTO \"ft1\" VALUES(0, ST_GeometryFromText('POINT(0 0)', 4326), 0, 0.0, 'zero')"); 
        run("INSERT INTO \"ft1\" VALUES(1, ST_GeometryFromText('POINT(1 1)', 4326), 1, 1.1, 'one')");
        run("INSERT INTO \"ft1\" VALUES(2, ST_GeometryFromText('POINT(2 2)', 4326), 2, 2.2, 'two')");
                // advance the sequence to 2
        run("SELECT nextval(pg_get_serial_sequence('ft1','id'))");
        run("SELECT nextval(pg_get_serial_sequence('ft1','id'))");
        // analyze so that the stats will be up to date
        run("ANALYZE \"ft1\"");
        

    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new PostgisNGDataStoreFactory();
    }

}
