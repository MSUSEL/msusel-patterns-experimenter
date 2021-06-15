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
package org.geotools.jdbc;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Tests that geometry types can be properly created and retrieved back from the
 * database. You might need to override some of the tests method to fix the expectations
 * for specific geometry class types.
 *
 *
 *
 * @source $URL$
 */
public abstract class JDBCGeometryTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCGeometryTestSetup createTestSetup();

    public void testPoint() throws Exception {
        assertEquals(Point.class, checkGeometryType(Point.class));
    }
    
    public void testLineString() throws Exception {
        assertEquals(LineString.class, checkGeometryType(LineString.class));
    }
    
    public void testLinearRing() throws Exception {
        assertEquals(LinearRing.class, checkGeometryType(LinearRing.class));
    }
    
    public void testPolygon() throws Exception {
        assertEquals(Polygon.class, checkGeometryType(Polygon.class));
    }
    
    public void testMultiPoint() throws Exception {
        assertEquals(MultiPoint.class, checkGeometryType(MultiPoint.class));
    }
    
    public void testMultiLineString() throws Exception {
        assertEquals(MultiLineString.class, checkGeometryType(MultiLineString.class));
    }
    
    public void testMultiPolygon() throws Exception {
        assertEquals(MultiPolygon.class, checkGeometryType(MultiPolygon.class));
    }
    
    /**
     * Sometimes the source cannot anticipate the geometry type, can we cope with this?
     * @throws Exception
     */
    public void testGeometry() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(Geometry.class));
    }
    
    /**
     * Same goes for heterogeneous collections 
     * @throws Exception
     */
    public void testGeometryCollection() throws Exception {
        assertEquals(GeometryCollection.class, checkGeometryType(GeometryCollection.class));
    }

    protected Class checkGeometryType(Class geomClass) throws Exception {
        // we just prefix the table name with "t" otherwise we go beyond
        // the Oracle identifier max length limit (oh my my...)
        String featureTypeName = tname("t" + geomClass.getSimpleName());

        // create a featureType and write it to PostGIS
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(featureTypeName);
        ftb.add(aname("id"), Integer.class);
        ftb.add(aname("name"), String.class);
        ftb.add(aname("geom"), geomClass, crs);

        SimpleFeatureType newFT = ftb.buildFeatureType();
        dataStore.createSchema(newFT);

        SimpleFeatureType newSchema = dataStore.getSchema(featureTypeName);
        assertNotNull(newSchema);
        assertEquals(3, newSchema.getAttributeCount());
        return newSchema.getGeometryDescriptor().getType().getBinding();
    }

}
