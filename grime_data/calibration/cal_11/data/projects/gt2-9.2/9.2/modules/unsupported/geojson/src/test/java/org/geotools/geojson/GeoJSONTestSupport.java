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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson;

import java.io.IOException;
import java.io.StringReader;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class GeoJSONTestSupport extends TestCase {

    protected StringReader reader(String json) throws IOException {
        return new StringReader(json);
    }
    
    protected String strip(String json) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == ' ' || c == '\n') continue;
            if (c == '\'') {
                sb.append("\"");
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    protected void assertEqualsLax(SimpleFeature f1, SimpleFeature f2) {
        assertEquals(f1.getID(), f1.getID());
        assertEquals(f1.getAttributeCount(), f2.getAttributeCount());
        
        for (int i = 0; i < f1.getAttributeCount(); i++) {
            Object o1 = f1.getAttribute(i);
            Object o2 = f2.getAttribute(i);
            
            if (o1 instanceof Geometry) {
                assertTrue(((Geometry) o1).equals((Geometry)o2));
            }
            else {
                if (o1 instanceof Number) {
                    if (o1 instanceof Integer || o1 instanceof Long) {
                        assertTrue(o2 instanceof Integer || o2 instanceof Long);
                        assertEquals(((Number)o1).intValue(), ((Number)o2).intValue());
                    }
                    else if (o1 instanceof Float || o1 instanceof Double) {
                        assertTrue(o2 instanceof Float || o2 instanceof Double);
                        assertEquals(((Number)o1).doubleValue(), ((Number)o2).doubleValue());
                    }
                    else {
                        fail();
                    }
                }
                else {
                    assertEquals(o1, o2);
                }
            }
        }
    }
    
    protected String toString(int val) {
        return val == 0 ? "zero" : 
                val == 1 ? "one" :
                val == 2 ? "two" : 
                val == 3 ? "three" : "four";
    }
}
