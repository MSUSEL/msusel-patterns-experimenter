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
package org.geotools.geometry.iso.coordinate;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.util.Cloneable;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.GeometryFactory;

/**
 * @author Sanjay Jena
 *
 *
 *
 *
 *
 * @source $URL$
 */
public class DirectPositionTest extends TestCase {
	GeometryFactory gf2D;
	GeometryFactory gf3D;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84); 
        gf2D = builder.getGeometryFactory();
        builder.setCoordianteReferenceSystem(DefaultGeographicCRS.WGS84_3D);
        gf3D = builder.getGeometryFactory();
        
    }

	public void testDirectPosition() {
		double x1 = 10000.00;
		double y1 = 10015.50;
		double z1 = 10031.00;
		
		double coords1[] = new double[]{x1, y1, z1};
		double coords2[] = new double[]{x1, y1};
		double resultCoords[];
		
		// Creating a DP
		DirectPosition dp1 = gf3D.createDirectPosition(coords1);
		
		// getCoordinate()
		resultCoords = dp1.getCoordinate();
		assertTrue(coords1[0] == resultCoords[0]);
		assertTrue(coords1[1] == resultCoords[1]);
		assertTrue(coords1[2] == resultCoords[2]);
		
		// getOrdinate(dim)
		assertTrue(coords1[0] == dp1.getOrdinate(0));
		assertTrue(coords1[1] == dp1.getOrdinate(1));
		assertTrue(coords1[2] == dp1.getOrdinate(2));
		
		// Cloning a DP
		DirectPosition dp2 = (DirectPosition) ((Cloneable) dp1).clone();
		
		// setOrdinate(dim, value)
		dp1.setOrdinate(0, 10.5);
		dp1.setOrdinate(1, 20.7);
		dp1.setOrdinate(2, -30.666);
		resultCoords = dp1.getCoordinate();
		assertTrue(resultCoords[0] == 10.5);
		assertTrue(resultCoords[1] == 20.7);
		assertTrue(resultCoords[2] == -30.666);
		
		// Test if clone() returned a copy, and not a reference
		// The values of dp2 should not be modified by the previous setOrdinate call in dp1
		resultCoords = dp2.getCoordinate();
		assertTrue(x1 == resultCoords[0]);
		assertTrue(y1 == resultCoords[1]);
		assertTrue(z1 == resultCoords[2]);

		//DirectPosition dp3 = aCoordFactory.createDirectPosition(coords2);
		
		assertTrue(dp1.getDimension() == 3);
		
		
	}

}
