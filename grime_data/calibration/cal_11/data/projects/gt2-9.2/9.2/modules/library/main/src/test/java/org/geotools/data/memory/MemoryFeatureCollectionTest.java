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
package org.geotools.data.memory;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Iterator;

import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.FilteredIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;

/**
 * 
 *
 * @source $URL$
 */
public class MemoryFeatureCollectionTest extends DataTestCase {
    private MemoryFeatureCollection roads;
    
    public MemoryFeatureCollectionTest( String test) {
        super(test);        
    }
    protected void setUp() throws Exception {
        super.setUp();
        roads = new MemoryFeatureCollection( roadType );
        roads.addAll( Arrays.asList( roadFeatures ) );        
    }
    public void testAdd(){
        MemoryFeatureCollection rivers = new MemoryFeatureCollection( riverType );
        for( int i=0; i<riverFeatures.length;i++){
            rivers.add( riverFeatures[i] );
        }
        assertEquals( riverFeatures.length, rivers.size() );
    }
    public void testAddAll(){
        MemoryFeatureCollection rivers = new MemoryFeatureCollection( riverType );
        rivers.addAll( Arrays.asList( riverFeatures ) );
    }
    public void testSize(){
        assertEquals( roadFeatures.length, roads.size() );    
    }
    public void testResources(){
        Object[] array = roads.toArray();
        assertEquals( roads.size(), array.length );
        
        SimpleFeatureIterator i = roads.features();
        try {
            assertTrue( i.hasNext() );
        }
        finally {
            i.close();
        }
        try {
            assertFalse( i.hasNext() );
            fail("should be closed");
        }
        catch( IllegalStateException closed ){            
        }
        i=roads.features();
        try {
            assertTrue( i.hasNext() );
        }
        finally {
            i.close();
        }
        try {
            assertFalse( i.hasNext() );
            fail("should be closed");
        }
        catch( IllegalStateException closed ){            
        }        
    }
    
    public void testBounds() {
        MemoryFeatureCollection rivers = new MemoryFeatureCollection(riverType);
        ReferencedEnvelope expected = new ReferencedEnvelope();
        for (int i = 0; i < riverFeatures.length; i++) {
            rivers.add(riverFeatures[i]);
            expected.include(riverFeatures[i].getBounds());
        }
        assertEquals(riverFeatures.length, rivers.size());

        // Should not throw an UnsupportedOperationException
        assertNotNull(rivers.getBounds());
        assertEquals( expected, rivers.getBounds() );
    }

    /**
     * This feature collection is still implementing Collection so we best check it works
     */
    public void testIterator() throws Exception {
        int count=0;
        Iterator<SimpleFeature> it = roads.iterator();
        try {
            while( it.hasNext() ){
                @SuppressWarnings("unused")
                SimpleFeature feature = it.next();
                count++;
            }
        } finally {
            DataUtilities.close( it );
        }
        assertEquals( roads.size(), count );
        
        count=0;
        FilteredIterator<SimpleFeature> filteredIterator = new FilteredIterator<SimpleFeature>( roads, rd12Filter );
        try {
            while( filteredIterator.hasNext() ){
                @SuppressWarnings("unused")
                SimpleFeature feature = filteredIterator.next();
                count++;
            }
        } finally {
            filteredIterator.close();
        }
        assertEquals( expected( rd12Filter) , count );
    }
    
    public void testSubCollection(){
        int count = 0;
        SimpleFeatureIterator it = roads.features();
        try {
            while( it.hasNext() ){
                SimpleFeature feature = it.next();
                if( rd12Filter.evaluate( feature )){
                    count++;
                }
            }
        } finally {
            it.close();
        }
        SimpleFeatureCollection sub = roads.subCollection( rd12Filter );
        assertEquals( count, sub.size() );
    }
    public void testSubSubCollection(){
        SimpleFeatureCollection sub = roads.subCollection( rd12Filter );        
        SimpleFeatureCollection subsub = sub.subCollection( rd1Filter );
        assertEquals( 1, subsub.size() );        
    }
    public void XtestSort(){
//        FeatureList fList = roads.sort(SortBy.NATURAL_ORDER);
//        for (Object obj : fList) {
//            System.out.println(obj);
//        }
    }
}
