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
package org.geotools.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * Tests the ArrayFeatureReader class
 *
 * @author jones
 *
 *
 * @source $URL$
 */
public class ArrayFeatureReaderTest extends TestCase {
    private CollectionFeatureReader arrayReader;
    private CollectionFeatureReader collectionReader;
    private CollectionFeatureReader featureCollectionReader;
    private SimpleFeatureType type;
    private SimpleFeature[] features;

    protected void setUp() throws Exception {
        type = DataUtilities.createType("TestType", "geom:Geometry");
        features = new SimpleFeature[] {
            SimpleFeatureBuilder.build( type, new Object[] { null }, "f1" ), 
            SimpleFeatureBuilder.build( type, new Object[] { null }, "f2" ),
            SimpleFeatureBuilder.build( type, new Object[] { null }, "f3" ),
            SimpleFeatureBuilder.build( type, new Object[] { null }, "f4" ),
            SimpleFeatureBuilder.build( type, new Object[] { null }, "f5" ),
            SimpleFeatureBuilder.build( type, new Object[] { null }, "f6" )
        };

        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        List<SimpleFeature> list = Arrays.asList(features);
        collection.addAll(list);
        arrayReader = new CollectionFeatureReader(features);
        collectionReader = new CollectionFeatureReader(list, type);
        featureCollectionReader = new CollectionFeatureReader((SimpleFeatureCollection)collection, type);
    }

    /**
     * Test method for 'org.geotools.data.ArrayFeatureReader.getFeatureType()'
     */
    public void testGetFeatureType() {
        assertEquals(type, arrayReader.getFeatureType());
        assertEquals(type, collectionReader.getFeatureType());
        assertEquals(type, featureCollectionReader.getFeatureType());
    }

    /**
     * Test method for 'org.geotools.data.ArrayFeatureReader.next()'
     *
     * @throws Exception
     */
    public void testNext() throws Exception {
        assertEquals(features[0], arrayReader.next());
        assertEquals(features[1], arrayReader.next());
        assertEquals(features[2], arrayReader.next());
        assertEquals(features[3], arrayReader.next());
        assertEquals(features[4], arrayReader.next());
        assertEquals(features[5], arrayReader.next());

        assertEquals(features[0], collectionReader.next());
        assertEquals(features[1], collectionReader.next());
        assertEquals(features[2], collectionReader.next());
        assertEquals(features[3], collectionReader.next());
        assertEquals(features[4], collectionReader.next());
        assertEquals(features[5], collectionReader.next());

        assertEquals(features[0], featureCollectionReader.next());
        assertEquals(features[1], featureCollectionReader.next());
        assertEquals(features[2], featureCollectionReader.next());
        assertEquals(features[3], featureCollectionReader.next());
        assertEquals(features[4], featureCollectionReader.next());
        assertEquals(features[5], featureCollectionReader.next());
    }

    /**
     * Test method for 'org.geotools.data.ArrayFeatureReader.hasNext()'
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testHasNext() throws Exception {
        testHasNext(arrayReader);
        testHasNext(collectionReader);
        testHasNext(featureCollectionReader);
    }

    private void testHasNext(FeatureReader <SimpleFeatureType, SimpleFeature> arrayReader)
        throws IOException, IllegalAttributeException {
        assertTrue(arrayReader.hasNext());
        arrayReader.next();
        assertTrue(arrayReader.hasNext());
        arrayReader.next();
        assertTrue(arrayReader.hasNext());
        arrayReader.next();
        assertTrue(arrayReader.hasNext());
        arrayReader.next();
        assertTrue(arrayReader.hasNext());
        arrayReader.next();
        assertTrue(arrayReader.hasNext());
        arrayReader.next();
        assertFalse(arrayReader.hasNext());
    }

    /**
     * Test method for 'org.geotools.data.ArrayFeatureReader.close()'
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testClose() throws Exception {
        arrayReader.close();
        assertFalse(arrayReader.hasNext());

        try {
            arrayReader.next();
            fail();
        } catch (Exception e) {
            // good
        }

        collectionReader.close();
        assertFalse(collectionReader.hasNext());

        try {
            collectionReader.next();
            fail();
        } catch (Exception e) {
            // good
        }

        featureCollectionReader.close();
        assertFalse(featureCollectionReader.hasNext());

        try {
            featureCollectionReader.next();
            fail();
        } catch (Exception e) {
            // good
        }
    }
}
