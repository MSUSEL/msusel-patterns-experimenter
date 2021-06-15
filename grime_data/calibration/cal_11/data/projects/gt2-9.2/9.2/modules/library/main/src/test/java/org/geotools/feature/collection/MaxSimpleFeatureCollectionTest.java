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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import junit.framework.TestCase;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class MaxSimpleFeatureCollectionTest extends TestCase {

    DefaultFeatureCollection delegate;

    @Override
    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("foo");
        tb.add("geom", Point.class);
        tb.add("name", String.class);
        
        SimpleFeatureType featureType = tb.buildFeatureType();

        delegate = new DefaultFeatureCollection(null, featureType);

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);
        for (int i = 0; i < 10; i++) {
            b.add(new GeometryFactory().createPoint(new Coordinate(i,i)));
            b.add(String.valueOf(i));
            delegate.add(b.buildFeature("fid." + i));
        }
    }

    public void testSize() {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 5);
        assertEquals(5, max.size());
        
        max = new MaxSimpleFeatureCollection(delegate, 7, 5);
        assertEquals(3, max.size());
    }

    public void testIsEmpty() {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 5);
        assertFalse(max.isEmpty());
        
        max = new MaxSimpleFeatureCollection(delegate, 9, 5);
        assertFalse(max.isEmpty());
        
        max = new MaxSimpleFeatureCollection(delegate, 10, 5);
        assertTrue(max.isEmpty());

        max = new MaxSimpleFeatureCollection(delegate, 0, 0);
        assertTrue(max.isEmpty());
    }

    public void testIterator() {
        
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 5);
        SimpleFeatureIterator it = max.features();
        try {
            for (int i = 0; i < 5; i++) {
                assertTrue(it.hasNext());
                assertNotNull(it.next());
            }
            assertFalse(it.hasNext());
        }
        finally {
            it.close();
        }

        max = new MaxSimpleFeatureCollection(delegate, 7, 5);
        it = max.features();
        try {
            for (int i = 0; i < 3; i++) {
                assertTrue(it.hasNext());
                assertNotNull(it.next());
            }
            assertFalse(it.hasNext());
        }
        finally {
            it.close();
        }
    }
}
