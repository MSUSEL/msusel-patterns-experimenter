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

import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class PositionImplTest extends TestCase {
	
	public void testPositionImpl() {
		
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84_3D;
		DirectPosition dp = new DirectPositionImpl(crs, 1, 2, 0);
		PositionImpl pos = new PositionImpl(dp);
		PointImpl point = new PointImpl(dp);
		PositionImpl pos2 = new PositionImpl(point);
		
		assertTrue(pos2.hasPoint());
		assertTrue(point.equals(pos2.getPoint()));
		pos2.setDirectPosition((DirectPositionImpl) dp);
		assertTrue(pos2.getCoordinateDimension() == 3);
		
		// test toString
		String toS = pos2.toString();
		assertTrue(toS != null);
		assertTrue(toS.length() > 0);	
		
		// test the equals(object)
		assertTrue(pos2.equals((Object) pos));
		assertTrue(pos2.equals((Object) pos2));
		assertFalse(pos2.equals((Object) point));
		assertFalse(pos2.equals((Object) null));
		
		// test bad constructors
		try {
			PositionImpl bad = new PositionImpl((DirectPosition) null);
			fail(); // fail if we get here, this should fail
		}
		catch (IllegalArgumentException e) {
			// good
		}
		try {
			PositionImpl bad = new PositionImpl((PointImpl) null);
			fail(); // fail if we get here, this should fail
		}
		catch (IllegalArgumentException e) {
			// good
		}
	}

}
