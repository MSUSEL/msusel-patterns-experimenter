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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.NoSuchElementException;

import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Test FilteredFeatureReader for conformance.
 * 
 * @author Jody Garnett, Refractions Research
 *
 *
 * @source $URL$
 */
public class FilteringFeatureReaderTest extends DataTestCase {
     FeatureReader<SimpleFeatureType, SimpleFeature> roadReader;
     FeatureReader<SimpleFeatureType, SimpleFeature> riverReader;
    /**
     * Constructor for FilteringFeatureReaderTest.
     * @param arg0
     */
    public FilteringFeatureReaderTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        roadReader = DataUtilities.reader( roadFeatures );
        riverReader = DataUtilities.reader( riverFeatures );        
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        roadReader.close();
        roadReader = null;
        riverReader.close();
        riverReader = null;
    }

    public void testFilteringFeatureReaderALL() throws IOException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        
        reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(DataUtilities.reader( roadFeatures ), Filter.EXCLUDE );
        try {
            assertFalse( reader.hasNext() );
        }
        finally {
            reader.close();
        }        
        reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(DataUtilities.reader( roadFeatures ), Filter.EXCLUDE );
        assertEquals( 0, count( reader ));
        
        reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(DataUtilities.reader( roadFeatures ), Filter.EXCLUDE );
        assertContents( new SimpleFeature[0], reader );                                                           
    }
    public void testFilteringFeatureReaderNONE() throws IOException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;        
        reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(DataUtilities.reader( roadFeatures ), Filter.INCLUDE );
        try {
            assertTrue( reader.hasNext() );
        }
        finally {
            reader.close();
        }
        reader = DataUtilities.reader( roadFeatures );
        assertEquals( roadFeatures.length, count( reader ));
                
        reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(DataUtilities.reader( roadFeatures ), Filter.INCLUDE );
        assertEquals( roadFeatures.length, count( reader ));
        
        reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(DataUtilities.reader( roadFeatures ), Filter.INCLUDE );
        assertContents( roadFeatures, reader );                                            
    }
    void assertContents( SimpleFeature expected[],  FeatureReader<SimpleFeatureType, SimpleFeature> reader ) throws IOException {
        assertNotNull( reader );
        assertNotNull( expected );
        SimpleFeature feature;
        int count = 0;
        try {
            for( int i=0; i<expected.length;i++){
                assertTrue( reader.hasNext() );
                feature = reader.next();
                assertNotNull( feature );
                assertEquals( expected[i], feature );
                count++;
            }
            assertFalse( reader.hasNext() );
        } catch (NoSuchElementException e) {
            // bad dog!
            throw new DataSourceException("hasNext() lied to me at:"+count, e );
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("next() could not understand feature at:"+count, e );
        }        
        finally {
            reader.close();
        }                
    }
    public void testNext() {
    }

    public void testClose() {
    }

    public void testGetFeatureType() {
    }

    public void testHasNext() {
    }

}
