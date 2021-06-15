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
package org.geotools.geometry.text;

import junit.framework.TestCase;

import org.geotools.geometry.MockGeometryFactory;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.Point;

/**
 * 
 *
 * @source $URL$
 */
public class WKTParserTest extends TestCase {
    // TODO: POLYGON ((280 380, 280 200, 60 200, 60 380, 180 220, 280 380),(40 160, 260 160, 240 60, 20 80, 40 160))
    WKTParser parser;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        MockGeometryFactory mockFactory = new MockGeometryFactory();
        parser = new WKTParser( mockFactory, mockFactory, null, null );
    }
    
    public void testPoint1() throws Exception {
        String WKT = "POINT (80 340)";
        Object geometry = parser.parse( WKT );
        assertNotNull( geometry );
        assertTrue( geometry instanceof Point );
        Point point = (Point) geometry;
        assertNotNull( point.getDirectPosition() );
        assertEquals( 80, point.getDirectPosition().getOrdinate(0), 0.0);
        assertEquals( 340, point.getDirectPosition().getOrdinate(1), 0.0);
    }
    public void testPoint2() throws Exception {
        String WKT = "POINT (320.324 180.234)";
        
        Object geometry = parser.parse( WKT );
        assertNotNull( geometry );
        assertTrue( geometry instanceof Point );
        Point point = (Point) geometry;
        assertEquals( 320.324, point.getDirectPosition().getOrdinate(0), 0.0);
        assertEquals( 180.234, point.getDirectPosition().getOrdinate(1), 0.0);
    }
    public void testPoint3() throws Exception {
        String WKT = "POINT (260.01 -360.55)";
        
        Object geometry = parser.parse( WKT );
        assertNotNull( geometry );
        assertTrue( geometry instanceof Point );
        Point point = (Point) geometry;
        assertEquals( 260.01, point.getDirectPosition().getOrdinate(0), 0.0);
        assertEquals( -360.55, point.getDirectPosition().getOrdinate(1), 0.0);   
    }
    
    public void testLine1() throws Exception {
        String WKT = "LINESTRING (60 380, 60 20, 200 400, 280 20, 360 400, 420 20, 500 400, 580 20, 620 400)";
        
        Object geometry = parser.parse( WKT );
        assertNotNull( geometry );
        assertTrue( geometry instanceof Curve);
        Curve linestring = (Curve) geometry;          
    }
    public void testLine2() throws Exception {
        String WKT = "LINESTRING (80 360, 520 360, 520 40, 120 40, 120 300, 460 300, 460 100, 200 100, 200 240, 400 240, 400 140, 560 0)";
        
        Object geometry = parser.parse( WKT );
        assertNotNull( geometry );
        assertTrue( geometry instanceof Curve);
        Curve linestring = (Curve) geometry;          
    }
}
