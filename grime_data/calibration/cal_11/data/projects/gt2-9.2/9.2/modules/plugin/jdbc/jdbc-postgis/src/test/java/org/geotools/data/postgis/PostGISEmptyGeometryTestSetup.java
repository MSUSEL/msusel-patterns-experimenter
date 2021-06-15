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

import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/jdbc/jdbc-postgis/src/test/java/org/geotools/data/postgis/PostGISBooleanTestSetup.java $
 */
public class PostGISEmptyGeometryTestSetup extends JDBCEmptyGeometryTestSetup {

    public PostGISEmptyGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

	@Override
	protected void createEmptyGeometryTable() throws Exception {
		//create table schema
		 run("CREATE TABLE \"empty\"(" //
	                + "\"fid\" serial primary key, " //
	                + "\"id\" integer, " //
	                + "\"geom_point\" geometry, " //
	                + "\"geom_linestring\" geometry, " //
	                + "\"geom_polygon\" geometry, " //
                    + "\"geom_multipoint\" geometry, " //	                
                    + "\"geom_multilinestring\" geometry, " //
                    + "\"geom_multipolygon\" geometry, " //                   
	                + "\"name\" varchar," //
	                + "CONSTRAINT enforce_geotype_geom_1 CHECK (geometrytype(geom_point) = 'POINT'::text OR geom_point IS NULL)," //
                    + "CONSTRAINT enforce_geotype_geom_2 CHECK (geometrytype(geom_linestring) = 'LINESTRING'::text OR geom_linestring IS NULL)," //
                    + "CONSTRAINT enforce_geotype_geom_3 CHECK (geometrytype(geom_polygon) = 'POLYGON'::text OR geom_polygon IS NULL)," //
                    + "CONSTRAINT enforce_geotype_geom_4 CHECK (geometrytype(geom_multipoint) = 'MULTIPOINT'::text OR geom_multipoint IS NULL)," //
                    + "CONSTRAINT enforce_geotype_geom_5 CHECK (geometrytype(geom_multilinestring) = 'MULTILINESTRING'::text OR geom_multilinestring IS NULL)," //
                    + "CONSTRAINT enforce_geotype_geom_6 CHECK (geometrytype(geom_multipolygon) = 'MULTIPOLYGON'::text OR geom_multipolygon IS NULL)" //
	                + ")");
        	
	}

	@Override
	protected void dropEmptyGeometryTable() throws Exception {
	     runSafe("DELETE GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'empty'");
		 run( "DROP TABLE \"empty\"");
		
	}
    

}
