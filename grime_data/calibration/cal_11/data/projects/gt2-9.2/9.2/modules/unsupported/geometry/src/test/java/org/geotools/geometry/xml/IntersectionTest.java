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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.xml;

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.Surface;
import org.opengis.referencing.FactoryException;

/**
 * 
 *
 * @source $URL$
 */
public class IntersectionTest extends AbstractGeometryTest {

    /**
     * Prepare the test environment
     */
    public void setUp() throws FactoryException {
        super.setUp();
    }
    public void testNotYet(){        
    }
    
    /**
     * test the simple intersection of two polygons
     */
    public void XtestSimpleIntersection() {
        DirectPosition[] pointsA = new DirectPosition[4];
        pointsA[0] = createDirectPosition(0.0, 0.0);
        pointsA[1] = createDirectPosition(1.0, 0.0);
        pointsA[2] = createDirectPosition(0.0, 1.0);
        pointsA[3] = createDirectPosition(0.0, 0.0);

        DirectPosition[] pointsB = new DirectPosition[4];
        pointsB[0] = createDirectPosition(0.0, 0.0);
        pointsB[1] = createDirectPosition(1.0, 0.0);
        pointsB[2] = createDirectPosition(1.0, 1.0);
        pointsB[3] = createDirectPosition(0.0, 0.0);

        Surface sA = createSurface(pointsA);
        assertEquals(0.5, sA.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sA.getPerimeter());

        Surface sB = createSurface(pointsB);
        assertEquals(0.5, sB.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sB.getPerimeter());

        TransfiniteSet result = sA.intersection(sB);
        assertTrue(result instanceof Surface);
        Surface surfaceResult = (Surface)result;
        assertEquals(1.0 + Math.sqrt(2.0), surfaceResult.getPerimeter());
    }

    public void XtestEdgeIntersection() {
        DirectPosition[] pointsA = new DirectPosition[4];
        pointsA[0] = createDirectPosition(0.0, 0.0);
        pointsA[1] = createDirectPosition(1.0, 0.0);
        pointsA[2] = createDirectPosition(0.0, 1.0);
        pointsA[3] = createDirectPosition(0.0, 0.0);

        DirectPosition[] pointsB = new DirectPosition[4];
        pointsB[0] = createDirectPosition(1.0, 0.0);
        pointsB[1] = createDirectPosition(1.0, 1.0);
        pointsB[2] = createDirectPosition(0.0, 1.0);
        pointsB[3] = createDirectPosition(1.0, 0.0);

        Surface sA = createSurface(pointsA);
        assertEquals(0.5, sA.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sA.getPerimeter());

        Surface sB = createSurface(pointsB);
        assertEquals(0.5, sB.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sB.getPerimeter());

        TransfiniteSet result = sA.intersection(sB);
        assertTrue(result instanceof Curve);
        Curve curveResult = (Curve)result;
        assertEquals(0.0, curveResult.getStartParam(), 1.0e-8);
        assertEquals(Math.sqrt(2.0), curveResult.getEndParam(), 1.0e-8);
    }

}
