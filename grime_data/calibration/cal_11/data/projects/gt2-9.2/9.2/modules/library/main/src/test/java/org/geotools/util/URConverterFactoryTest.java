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

import java.io.File;
import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class URConverterFactoryTest extends TestCase {

    URConverterFactory f;
    
    @Override
    protected void setUp() throws Exception {
        f = new URConverterFactory();
    }
    
    public void testStringToURL() throws Exception {
        Converter c = f.createConverter(String.class, URL.class, null) ;
        assertNotNull( c );
        
        assertEquals( new URL( "http://foo.com" ), c.convert( "http://foo.com", URL.class ) );
        assertEquals( new File( "/foo/bar").toURI().toURL() , c.convert( "/foo/bar", URL.class ) );
    }
    
    public void testStringToURI() throws Exception {
        Converter c = f.createConverter(String.class, URI.class, null) ;
        assertNotNull( c );
        
        assertEquals( new URI( "http://foo.com" ), c.convert( "http://foo.com", URI.class ) );
        //assertEquals( new File( "/foo/bar" ).toURI() , c.convert( "/foo/bar", URI.class ) );
    }
    
    public void testURIToURL() throws Exception {
        Converter c = f.createConverter(URL.class, URI.class, null) ;
        assertNotNull( c );
        
        assertEquals( new URI( "http://foo.com" ), c.convert( new URL("http://foo.com"), URI.class ) );
    }
    public void testURLToURI() throws Exception {
        Converter c = f.createConverter(URI.class, URL.class, null) ;
        assertNotNull( c );
        
        assertEquals( new URL( "http://foo.com" ), c.convert( new URI("http://foo.com"), URL.class ) );
    }
//JD: enable when factory registered
//    public void testRegistered() throws Exception {
//        assertEquals( new File( "/foo/bar").toURI().toURL() , Converters.convert( "/foo/bar", URL.class ));
//    }
    
}
