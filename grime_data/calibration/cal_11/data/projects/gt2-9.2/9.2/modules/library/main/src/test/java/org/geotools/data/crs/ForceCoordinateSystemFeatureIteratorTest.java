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
package org.geotools.data.crs;

import junit.framework.TestCase;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class ForceCoordinateSystemFeatureIteratorTest extends TestCase {

    private static final String FEATURE_TYPE_NAME = "testType";

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * create a datastore with 1 feature in it.
     * @param crs the CRS of the featuretype
     * @param p the point to add, should be same CRS as crs
     * @return
     * @throws Exception
     */
    private MemoryDataStore createDatastore(CoordinateReferenceSystem crs, Point p) throws Exception{
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(FEATURE_TYPE_NAME);
        builder.setCRS(crs);
        builder.add("geom", Point.class );
        
        SimpleFeatureType ft = builder.buildFeatureType();
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(ft);
        b.add( p );
        
        SimpleFeature[] features=new SimpleFeature[]{
           b.buildFeature(null) 
        };
        
        return new MemoryDataStore(features);
    }
    
    public void testSameCRS() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        GeometryFactory fac=new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10,10) );
        
        MemoryDataStore ds = createDatastore(crs, p);
        
        SimpleFeatureCollection collection = ds.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
        SimpleFeatureIterator original = collection.features();
        
        ForceCoordinateSystemIterator modified = new ForceCoordinateSystemIterator(collection.features(), collection.getSchema(), crs);
        
        SimpleFeature f1=original.next();
        SimpleFeature f2=modified.next();
        
        assertEquals(f1,f2);
        
        assertFalse( original.hasNext() );
        assertFalse( modified.hasNext() );
    }
    
    public void testDifferentCRS() throws Exception {
        CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
        GeometryFactory fac=new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10,10) );
        
        MemoryDataStore ds = createDatastore(srcCRS, p);
        
        SimpleFeatureCollection collection = ds.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
        SimpleFeatureIterator original = collection.features();
        CoordinateReferenceSystem destCRS=DefaultEngineeringCRS.CARTESIAN_2D;
        ForceCoordinateSystemIterator modified = new ForceCoordinateSystemIterator(collection.features(), collection.getSchema(), destCRS);
        
        SimpleFeature f1=original.next();
        SimpleFeature f2=modified.next();
        
        assertEquals(((Geometry)f1.getDefaultGeometry()).getCoordinate(),((Geometry)f2.getDefaultGeometry()).getCoordinate());
        assertFalse(f1.getFeatureType().getCoordinateReferenceSystem().equals(f2.getFeatureType().getCoordinateReferenceSystem()));
        assertEquals( srcCRS, f1.getFeatureType().getCoordinateReferenceSystem());
        assertEquals( srcCRS, f1.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());
        assertEquals( destCRS, f2.getFeatureType().getCoordinateReferenceSystem());
        assertEquals( destCRS, f2.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());
        
        assertFalse( original.hasNext() );
        assertFalse( modified.hasNext() );
        
        assertNotNull(modified.builder);
    }
    
    public void testNullDestination() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        GeometryFactory fac=new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10,10) );
        
        MemoryDataStore ds = createDatastore(crs, p);
        
        try{
            SimpleFeatureCollection collection = ds.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
            new ForceCoordinateSystemIterator(collection.features(), collection.getSchema(), (CoordinateReferenceSystem) null);
            fail(); // should throw a nullpointer exception.
        }catch(NullPointerException e){
            // good
        }
        
    }
    
    public void testNullSource() throws Exception {
        CoordinateReferenceSystem srcCRS = null;
        GeometryFactory fac=new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10,10) );
        
        MemoryDataStore ds = createDatastore(srcCRS, p);
        
        SimpleFeatureCollection collection = ds.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
        SimpleFeatureIterator original = collection.features();
        CoordinateReferenceSystem destCRS=DefaultEngineeringCRS.CARTESIAN_2D;
        ForceCoordinateSystemIterator modified = new ForceCoordinateSystemIterator(collection.features(), collection.getSchema(), destCRS);
        
        SimpleFeature f1=original.next();
        SimpleFeature f2=modified.next();
        
        assertEquals(((Geometry)f1.getDefaultGeometry()).getCoordinate(),((Geometry)f2.getDefaultGeometry()).getCoordinate());
        assertFalse( f2.getFeatureType().getCoordinateReferenceSystem().equals(f1.getFeatureType().getCoordinateReferenceSystem()) );
        assertEquals( srcCRS, f1.getFeatureType().getCoordinateReferenceSystem());
        assertEquals( srcCRS, f1.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());
        assertEquals( destCRS, f2.getFeatureType().getCoordinateReferenceSystem());
        assertEquals( destCRS, f2.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());
        
        assertFalse( original.hasNext() );
        assertFalse( modified.hasNext() );
        
        assertNotNull(modified.builder);
    }
}
