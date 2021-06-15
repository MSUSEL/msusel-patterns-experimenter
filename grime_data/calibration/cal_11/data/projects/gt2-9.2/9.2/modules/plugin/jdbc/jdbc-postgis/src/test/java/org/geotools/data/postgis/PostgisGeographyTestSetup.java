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
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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
import java.sql.ResultSet;
import java.sql.Statement;

import org.geotools.jdbc.JDBCGeographyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.util.Version;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisGeographyTestSetup extends JDBCGeographyTestSetup {

    public PostgisGeographyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    @Override
    protected void createGeoPointTable() throws Exception {
        run("CREATE TABLE geopoint ( id SERIAL PRIMARY KEY, name VARCHAR(64), geo GEOGRAPHY(POINT,4326))");
        run("INSERT INTO geopoint(name, geo) VALUES ('Town', ST_GeographyFromText('SRID=4326;POINT(-110 30)'))");
        run("INSERT INTO geopoint(name, geo) VALUES ('Forest', ST_GeographyFromText('SRID=4326;POINT(-109 29)'))");
        run("INSERT INTO geopoint(name, geo) VALUES ('London', ST_GeographyFromText('SRID=4326;POINT(0 49)') )");
    }

    @Override
    protected void dropGeoPointTable() throws Exception {
        runSafe("DROP TABLE geopoint");
    }

    @Override
    public boolean isGeographySupportAvailable() throws Exception {
        Connection cx = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cx = getDataSource().getConnection(); 
            st = cx.createStatement();
            rs = st.executeQuery("select PostGIS_Lib_Version()");
            if(rs.next()) {
                return new Version(rs.getString(1)).compareTo(new Version("1.5.0")) >= 0;
            } else {
                return true;
            }
        } finally {
            rs.close();
            st.close();
            cx.close();
        }
    }
}
