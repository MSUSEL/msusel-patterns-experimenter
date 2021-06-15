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
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Base class for unit tests of the JTS utility class.
 * 
 * @author Michael Bedward
 *
 *
 * @source $URL$
 * @version $Id$
 * @since 2.8
 */
public class JTSTestBase {

    protected static final double TOL = 1.0E-6;
    
    protected static final int[] XPOINTS = 
        {0, 15, 30, 45, 60, 75, 90, 75, 60, 45, 30, 15};
    
    protected static final int[] YPOINTS = 
        {0, 20, 10, 20, 10, 20, 0, -20, -10, -20, -10, -20};
    
    protected static final int NPOINTS = XPOINTS.length;

    protected Coordinate[] getLineCoords() {
        return getCoords(0);
    }
    
    protected Coordinate[] getLineCoords(int offset) {
        return getCoords(offset);
    }

    protected Coordinate[] getPolyCoords() {
        return getPolyCoords(0);
    }
    
    protected Coordinate[] getPolyCoords(int offset) {
        Coordinate[] coords = new Coordinate[JTSTest.NPOINTS + 1];
        System.arraycopy(getCoords(offset), 0, coords, 0, NPOINTS);
        coords[JTSTest.NPOINTS] = new Coordinate(coords[0]);
        return coords;
    }
    
    private Coordinate[] getCoords(int offset) {
        Coordinate[] coords = new Coordinate[JTSTest.NPOINTS];
        for (int i = 0; i < JTSTest.NPOINTS; i++) {
            coords[i] = new Coordinate(JTSTest.XPOINTS[i] + offset, JTSTest.YPOINTS[i] + offset);
        }
        return coords;
    }
    
    protected static class CoordList {
        private static final double TOL = 1.0e-4d;
        private List<Coordinate> coords;
        
        CoordList(Coordinate[] coordArray) {
            coords = Arrays.asList(coordArray);
        }
        
        public boolean contains(Coordinate coord) {
            for (Coordinate c : coords) {
                if (equal2D(c, coord)) return true;
            }
            return false;
        }
        
        public boolean containsAll(Coordinate[] coordArray) {
            for (Coordinate c : coordArray) {
                if (!contains(c)) return false;
            }
            return true;
        }

        private boolean equal2D(Coordinate c0, Coordinate c1) {
            if (c0 == null || c1 == null) {
                throw new IllegalArgumentException("arguments must not be null");
            }
            
            return (Math.abs(c0.x - c1.x) < TOL && Math.abs(c0.y - c1.y) < TOL);
        }
    }
    
}
