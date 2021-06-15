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
package org.geotools.feature.simple;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * 
 *
 * @source $URL$
 */
public class SimpleFeatureImplTest extends TestCase {
    
    SimpleFeatureType schema;
    SimpleFeature feature;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        schema = DataUtilities.createType("buildings", "the_geom:MultiPolygon,name:String,ADDRESS:String");
        feature = SimpleFeatureBuilder.build(schema, new Object[] {null, "ABC", "Random Road, 12"}, "building.1");
    }
    
    // see GEOT-2061
    public void testGetProperty() {
        assertEquals("ABC", feature.getProperty("name").getValue());
        assertNull(feature.getProperty("NOWHERE"));
        assertEquals(0, feature.getProperties("NOWHERE").size());
    }
    
    
    public void testGetPropertyNullValue(){
        assertNotNull(feature.getProperty("the_geom"));
        assertNull(feature.getProperty("the_geom").getValue());
    }

    public void testGeometryPropertyType(){
        assertTrue("expected GeometryAttribute, got " + feature.getProperty("the_geom").getClass().getName(),
                feature.getProperty("the_geom") instanceof GeometryAttribute);
    }

    public void testDefaultGeometryProperty(){
        assertTrue("expected GeometryAttribute, got " + feature.getProperty("the_geom").getClass().getName(),
                feature.getProperty("the_geom") instanceof GeometryAttribute);
        GeometryAttribute defaultGeometryProperty = feature.getDefaultGeometryProperty();
        assertNotNull(defaultGeometryProperty);
        assertNull(defaultGeometryProperty.getValue());
        assertNotNull(defaultGeometryProperty.getDescriptor());
        assertTrue(defaultGeometryProperty.getDescriptor() instanceof GeometryDescriptor);
    }
    
    public void testGetName(){
        assertNotNull(feature.getName());
        assertEquals(feature.getFeatureType().getName(), feature.getName());
    }
    
    public void testGetDescriptor() {
        assertNotNull(feature.getDescriptor());
        assertSame(feature.getType(), feature.getDescriptor().getType());
        assertTrue(feature.getDescriptor().isNillable());
        assertEquals(0, feature.getDescriptor().getMinOccurs());
        assertEquals(Integer.MAX_VALUE, feature.getDescriptor().getMaxOccurs());
    }
}
