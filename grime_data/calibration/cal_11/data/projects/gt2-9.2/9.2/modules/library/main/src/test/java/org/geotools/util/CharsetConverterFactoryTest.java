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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.nio.charset.Charset;
import java.util.Set;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class CharsetConverterFactoryTest extends TestCase {

    CharsetConverterFactory factory;
    
    protected void setUp() throws Exception {
        factory = new CharsetConverterFactory();
    }
    
    public void testLookupStringToCharset() {
        Set<ConverterFactory> s = Converters.getConverterFactories(String.class,Charset.class);
        for ( ConverterFactory cf : s ) {
            if ( cf instanceof CharsetConverterFactory ) {
                return;
            }
        }
        
        fail( "CharsetConverterFactory not found" );
    }
    
    public void testLookupCharsetToString() {
        Set<ConverterFactory> s = Converters.getConverterFactories(Charset.class,String.class);
        for ( ConverterFactory cf : s ) {
            if ( cf instanceof CharsetConverterFactory ) {
                return;
            }
        }
        
        fail( "CharsetConverterFactory not found" );
    }
    
    public void testStringToCharset() throws Exception {
        Converter c = factory.createConverter( String.class, Charset.class, null );
        assertNotNull( c );
        
        Charset charset = c.convert( "UTF-8", Charset.class );
        assertNotNull( charset );
        assertEquals( "UTF-8", charset.name() );
        
        assertNull( c.convert( "FOO", Charset.class ) );
    }
    
    public void testCharsetToString() throws Exception {
        Converter c = factory.createConverter( Charset.class, String.class, null );
        assertNotNull( c );
        
        String charset = c.convert( Charset.forName( "UTF-8"), String.class );
        assertEquals( "UTF-8", charset );
    }
}
