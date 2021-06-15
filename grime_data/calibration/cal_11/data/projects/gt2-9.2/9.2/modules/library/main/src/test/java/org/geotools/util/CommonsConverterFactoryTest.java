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

import java.math.BigDecimal;

import java.util.TimeZone;
import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class CommonsConverterFactoryTest extends TestCase {
    
    CommonsConverterFactory factory;
    
    protected void setUp() throws Exception {
        factory = new CommonsConverterFactory();
    }
    
    public void testStringNumberConversion() throws Exception {
        // test with integers
        assertEquals(12, convert("12", Integer.class));
        assertEquals(null, convert("12.0", Integer.class));
        assertEquals(null, convert("12.5", Integer.class));
        assertEquals(null, convert(Long.MAX_VALUE + "", Integer.class));
        
        // test with longs
        assertEquals(Long.MAX_VALUE, convert(Long.MAX_VALUE + "", Long.class));
        assertEquals(null, convert("1e100", Long.class));
        assertEquals(null, convert("12.5", Long.class));
        
        // test with doubles
        assertEquals((double) Long.MAX_VALUE, convert(Long.MAX_VALUE + "", Double.class));
        assertEquals(1e100, convert("1e100", Double.class));
        assertEquals(12.5, convert("12.5", Double.class));
        
        BigDecimal d = new BigDecimal(12345);
        d=d.divide(new BigDecimal(100));
        assertEquals(d,convert("123.45", BigDecimal.class));
    }
    
    public void testTimeZoneConversion() throws Exception {
        assertEquals(TimeZone.getTimeZone("UTC"), convert("UTC", TimeZone.class));
        assertNull(convert("foobar", TimeZone.class));
        assertNull(factory.createConverter(String.class, TimeZone.class, null).convert(null, TimeZone.class));
    }
    
    Object convert( Object source, Class target ) throws Exception {
        return factory.createConverter( source.getClass(), target, null ).convert( source, target );
    }

}
