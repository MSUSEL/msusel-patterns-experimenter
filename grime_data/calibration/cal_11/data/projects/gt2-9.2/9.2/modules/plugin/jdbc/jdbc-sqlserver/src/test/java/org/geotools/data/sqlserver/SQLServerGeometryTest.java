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

import org.geotools.jdbc.JDBCGeometryTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.geotools.referencing.CRS;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author DamianoG
 * 
 */
public class SQLServerGeometryTest extends JDBCGeometryTest {

    private SQLServerGeometryTestSetup testSetup;

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        testSetup = new SQLServerGeometryTestSetup();
        return testSetup;
    }

    @Override
    public void testPoint() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(Point.class, checkGeometryType(Point.class));
    }

    @Override
    public void testLineString() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(LineString.class, checkGeometryType(LineString.class));
    }

    @Override
    public void testLinearRing() throws Exception {
        // LinearRing is not supported by SQLServer
    }

    @Override
    public void testPolygon() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(Polygon.class, checkGeometryType(Polygon.class));
    }

    @Override
    public void testMultiPoint() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(MultiPoint.class, checkGeometryType(MultiPoint.class));
    }

    @Override
    public void testMultiLineString() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(MultiLineString.class, checkGeometryType(MultiLineString.class));
    }

    @Override
    public void testMultiPolygon() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(MultiPolygon.class, checkGeometryType(MultiPolygon.class));
    }

    @Override
    public void testGeometry() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(Geometry.class, checkGeometryType(Geometry.class));
    }

    @Override
    public void testGeometryCollection() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(GeometryCollection.class, checkGeometryType(GeometryCollection.class));
    }
    
    public void testGeometryMetadataTable() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        
        GeometryDescriptor gd = dataStore.getFeatureSource(tname("gtmeta")).getSchema().getGeometryDescriptor();
        assertEquals(Point.class, gd.getType().getBinding());
        assertEquals(4326, (int) CRS.lookupEpsgCode(gd.getCoordinateReferenceSystem(), false));
    }

}
