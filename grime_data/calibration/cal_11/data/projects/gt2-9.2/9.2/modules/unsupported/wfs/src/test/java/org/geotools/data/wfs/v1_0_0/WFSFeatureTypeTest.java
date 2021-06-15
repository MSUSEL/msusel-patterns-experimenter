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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import junit.framework.TestCase;

import org.geotools.data.wfs.feature.LenientBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class WFSFeatureTypeTest extends TestCase {

    private GeometryFactory fac;

    protected void setUp() throws Exception {
        super.setUp();
        
        fac=new GeometryFactory();
    }

    /**
     * Test that lenient WFSFeature is lenient and somewhat intelligent
     * @throws Exception 
     */
    public void testCreateFeatureInExpectedOrder() throws Exception {
        SimpleFeatureType ft = createFeatureType();
        ft.getUserData().put("lenient", true );
        
        Point point = fac.createPoint(new Coordinate(10,10));
        Integer id = new Integer(2);
        String name = "name1";
        String name2 = "name2";
        Double double1 = new Double(3.0);
        // basic case
        Object[] atts=new Object[]{
                point,
                id,
                name,
                double1,
                name2,                
        };
        SimpleFeature feature = LenientBuilder.build( ft, atts, null );
        
        assertNotNull(feature.getID());
        assertEquals(point, feature.getAttribute(0));
        assertEquals(id, feature.getAttribute(1));
        assertEquals(name, feature.getAttribute(2));
        assertEquals(name2, feature.getAttribute(3));
        assertEquals(double1, feature.getAttribute(4));
        
        atts[1]=null;
        String fid="fid";
        
        feature = LenientBuilder.build(ft, atts, fid );
        assertEquals(fid, feature.getID());
        assertEquals(point, feature.getAttribute(0));
        assertNull(feature.getAttribute(1));
        assertEquals(name, feature.getAttribute(2));
        assertEquals(name2, feature.getAttribute(3));
        assertEquals(double1, feature.getAttribute(4));
        
        atts=new Object[]{
                point,
                name,
                name2,
                double1
        };
        //feature = createFeature(ft, atts, fid );
        //feature = ft.create(atts, fid);
        
        assertEquals(fid, feature.getID());
        assertEquals(point, feature.getAttribute(0));
        assertNull(feature.getAttribute(1));
        assertEquals(name, feature.getAttribute(2));
        assertEquals(name2, feature.getAttribute(3));
        assertEquals(double1, feature.getAttribute(4));
        
        atts=new Object[]{
                point,
                id,
                name2,
                double1
        };
        feature = LenientBuilder.build(ft, atts, fid );
        
        assertEquals(fid, feature.getID());
        assertEquals(point, feature.getAttribute(0));
        assertEquals(id, feature.getAttribute(1));
        assertEquals(name2, feature.getAttribute(2));
        if( false ){
            assertNull(feature.getAttribute(3));
            assertEquals(double1, feature.getAttribute(4));        
        }
        atts=new Object[]{
                point,
                id,
                null,
                name2,
                double1
        };
        feature = LenientBuilder.build(ft, atts, fid );
        //feature = ft.create(atts, fid);        
        assertEquals(fid, feature.getID());
        assertEquals(point, feature.getAttribute(0));
        assertEquals(id, feature.getAttribute(1));
        assertNull(feature.getAttribute(2));
        assertEquals(name2, feature.getAttribute(3));
        assertEquals(double1, feature.getAttribute(4));
    }

    private SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        build.setName("type");
        build.add("geom", Geometry.class );
        build.add("id", Integer.class );
        build.add("name", String.class );
        build.add("name2", String.class );
        build.add("double", Double.class );

//      FeatureTypeBuilder builder=FeatureTypeBuilder.newInstance("type");
//      builder.addType(AttributeTypeFactory.newAttributeType("geom", Geometry.class, false));
//      builder.addType(AttributeTypeFactory.newAttributeType("id", Integer.class, false));
//      builder.addType(AttributeTypeFactory.newAttributeType("name", String.class, false));
//      builder.addType(AttributeTypeFactory.newAttributeType("name2", String.class, false));
//      builder.addType(AttributeTypeFactory.newAttributeType("double", Double.class, false));
//      WFSFeatureType ft = new WFSFeatureType(builder.getFeatureType(),null);

        SimpleFeatureType ft = build.buildFeatureType();
        return ft;
    }

}


