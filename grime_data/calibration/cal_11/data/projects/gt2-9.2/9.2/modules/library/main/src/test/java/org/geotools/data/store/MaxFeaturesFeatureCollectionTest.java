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
package org.geotools.data.store;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.MaxSimpleFeatureCollection;

/**
 * 
 *
 * @source $URL$
 */
public class MaxFeaturesFeatureCollectionTest extends
		FeatureCollectionWrapperTestSupport {

    public void testSize() throws Exception {
        // in the common case it's as big as the max
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 2);
        assertEquals(2, max.size());

        // however if we skip much it's going to be just as big as the remainder
        max = new MaxSimpleFeatureCollection(delegate, delegate.size() - 1, 10);
        assertEquals(1, max.size());
        
        // and if we skip more than the size
        max = new MaxSimpleFeatureCollection(delegate, delegate.size() + 1, 10);
        assertEquals(0, max.size());
    }

    public void testIteratorMax() throws Exception {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 2);
        SimpleFeatureIterator i = max.features();
        try {
            for (int x = 0; x < 2; x++) {
                assertTrue(i.hasNext());
                i.next();
            }
            assertFalse(i.hasNext());
        } finally {
            i.close();
        }
    }
    
    public void testIteratorSkipMax() throws Exception {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, delegate.size() - 1, 2);
        SimpleFeatureIterator i = max.features();
        try {
            assertTrue(i.hasNext());
            i.next();
            assertFalse(i.hasNext());
        } finally {
            i.close();
        }
    }
    
    public void testIteratorSkipMoreSize() throws Exception {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, delegate.size() + 1, 2);
        SimpleFeatureIterator i = max.features();
        try {
            assertFalse(i.hasNext());
        } finally {
            i.close();            
        }
        
    }
}
