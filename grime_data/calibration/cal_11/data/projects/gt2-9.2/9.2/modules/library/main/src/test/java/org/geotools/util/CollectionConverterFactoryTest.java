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
package org.geotools.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class CollectionConverterFactoryTest extends TestCase {

    public void testCollectionToCollection() throws Exception {
        List source = new ArrayList();
        source.add( 1 );
        source.add( 2 );
        
        Object converted = 
            CollectionConverterFactory.CollectionToCollection.convert(source, Set.class);
        
        assertTrue(converted instanceof Set);
        Set target = (Set) converted;
        assertTrue( target.contains( 1 ) );
        assertTrue( target.contains( 2 ) );
    }
    
    public void testCollectionToArray() throws Exception {
        List source = new ArrayList();
        source.add( 1 );
        source.add( 2 );
        
        Object converted = 
            CollectionConverterFactory.CollectionToArray.convert( source, Integer[].class );
        assertTrue( converted instanceof Integer[] );
        Integer[] target = (Integer[]) converted;
        assertEquals( new Integer(1), target[0] );
        assertEquals( new Integer(2), target[1] );
    }
    
    public void testArrayToCollection() throws Exception {
        Integer[] source = new Integer[]{ 1, 2 };
        
        Object converted = 
            CollectionConverterFactory.ArrayToCollection.convert( source, List.class );
        assertTrue( converted instanceof List );
        List target = (List) converted;
        assertEquals( new Integer( 1 ), target.get( 0 ) );
        assertEquals( new Integer( 2 ), target.get( 1 ) );
        
    }
    
    public void testArrayToArray() throws Exception {
        Integer[] source = new Integer[]{ 1, 2 };
        
        Object converted = 
            CollectionConverterFactory.ArrayToArray.convert( source, Number[].class );
        assertTrue( converted instanceof Number[] );
        Number[] target = (Number[]) converted;
        assertEquals( 1, target[0].intValue() );
        assertEquals( 2, target[1].intValue() );
    }
}
