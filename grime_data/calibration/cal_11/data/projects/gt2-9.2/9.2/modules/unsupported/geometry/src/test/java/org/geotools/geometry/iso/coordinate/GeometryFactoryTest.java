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
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.Position;

/**
 * 
 *
 * @source $URL$
 */
public class GeometryFactoryTest extends TestCase {
	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84_3D);
		
		this._testCoordinateObjects(builder);
		
	}	

	private void _testCoordinateObjects(GeometryBuilder builder) {
		
		GeometryFactoryImpl cf = (GeometryFactoryImpl) builder.getGeometryFactory();
		
		// public DirectPositionImpl createDirectPosition();
		DirectPosition dp1 = cf.createDirectPosition();
		assertTrue(Double.compare(dp1.getOrdinate(0), Double.NaN) == 0);
		assertTrue(Double.compare(dp1.getOrdinate(1), Double.NaN) == 0);
		assertTrue(Double.compare(dp1.getOrdinate(2), Double.NaN) == 0);
		
		// public DirectPositionImpl createDirectPosition(double[] coord);
		double[] da = new double[3];
		da[0] = 10.0;
		da[1] = -115000.0;
		da[2] = 0.0000000125;
		DirectPosition dp2 = cf.createDirectPosition(da);
		assertTrue(dp2.getOrdinate(0) == 10.0);
		assertTrue(dp2.getOrdinate(1) == -115000.0);
		assertTrue(dp2.getOrdinate(2) == 0.0000000125);

		// public Envelope createEnvelope(
		//			DirectPosition lowerCorner,
		//			DirectPosition upperCorner)
		Envelope env1 = cf.createEnvelope(dp1, dp2);
		DirectPosition lc = env1.getLowerCorner();
		assertTrue(Double.compare(lc.getOrdinate(0), Double.NaN) == 0);
		assertTrue(Double.compare(lc.getOrdinate(1), Double.NaN) == 0);
		assertTrue(Double.compare(lc.getOrdinate(2), Double.NaN) == 0);
		DirectPosition uc = env1.getUpperCorner();
		assertTrue(uc.getOrdinate(0) == 10.0);
		assertTrue(uc.getOrdinate(1) == -115000.0);
		assertTrue(uc.getOrdinate(2) == 0.0000000125);
		env1 = cf.createEnvelope(dp2, dp1);
		lc = env1.getLowerCorner();
		assertTrue(lc.getOrdinate(0) == 10.0);
		assertTrue(lc.getOrdinate(1) == -115000.0);
		assertTrue(lc.getOrdinate(2) == 0.0000000125);
		uc = env1.getUpperCorner();
		assertTrue(Double.compare(uc.getOrdinate(0), Double.NaN) == 0);
		assertTrue(Double.compare(uc.getOrdinate(1), Double.NaN) == 0);
		assertTrue(Double.compare(uc.getOrdinate(2), Double.NaN) == 0);
		
		// public Position createPosition(DirectPosition dp);
		Position pos1 = cf.createPosition(dp2);
		assertTrue(pos1.getDirectPosition().getOrdinate(0) == 10.0);
		assertTrue(pos1.getDirectPosition().getOrdinate(1) == -115000.0);
		assertTrue(pos1.getDirectPosition().getOrdinate(2) == 0.0000000125);

		// public LineSegment createLineSegment(Position startPoint, Position endPoint);
		Position pos2 = cf.createPosition(dp1);
		LineSegment seg1 = cf.createLineSegment(pos1, pos2);
		assertTrue(Double.compare(seg1.getEndPoint().getOrdinate(0), Double.NaN) == 0.0);
		assertTrue(Double.compare(seg1.getEndPoint().getOrdinate(1), Double.NaN) == 0.0);
		assertTrue(Double.compare(seg1.getEndPoint().getOrdinate(2), Double.NaN) == 0.0);
		assertTrue(seg1.getStartPoint().getOrdinate(0) == 10.0);
		assertTrue(seg1.getStartPoint().getOrdinate(1) == -115000.0);
		assertTrue(seg1.getStartPoint().getOrdinate(2) == 0.0000000125);
		

		
		
	}

}
