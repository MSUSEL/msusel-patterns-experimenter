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
package org.geotools.geometry.iso.primitive;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author sanjay
 *
 *
 *
 *
 *
 * @source $URL$
 */
public class PointTest extends TestCase {
	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		
		this._testPoint(builder);
		
	}
	
	public void testGoodPoint() throws Exception {
		CoordinateReferenceSystem crs = CRS.decode( "EPSG:4326");		
		assertNotNull( crs );

		PositionFactory positionFactory = new PositionFactoryImpl(crs, new PrecisionModel());
		PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(crs, positionFactory); // null;
		
		assertEquals( crs, primitiveFactory.getCoordinateReferenceSystem() );
		
		Point point = primitiveFactory.createPoint( new double[]{1,1} );
		
		assertNotNull( point );
		assertEquals( crs, point.getCoordinateReferenceSystem() );
		assertSame( crs, point.getCoordinateReferenceSystem() );		
	}
	
	private void _testPoint(GeometryBuilder builder) {
		
		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		
		double[] coord = new double[]{10, 32000};
		Point p1 = tPrimFactory.createPoint(coord);

		// ***** getRepresentativePoint()
		double[] dp = p1.getRepresentativePoint().getCoordinate();
		assertTrue(dp[0] == 10);
		assertTrue(dp[1] == 32000);
		
		DirectPosition dp1 = tCoordFactory.createDirectPosition(coord);
		
		Point p2 = tPrimFactory.createPoint(dp1);
		
		//System.out.println("P1: " + p1);
		//System.out.println("P2: " + p2);
		assertTrue(p1.equals(p2));
		
		//System.out.println("Dimension is " + p1.getDimension(null));
		assertTrue(p1.getDimension(null) == 0);

		//System.out.println("Coordinate dimension is " + p1.getCoordinateDimension());
		assertTrue(p1.getCoordinateDimension() == 2);
		
		Complex cp1 = p1.getClosure();
		//System.out.println("Class of p1.closure() is " + cp1.getClass());
		assertTrue(cp1 instanceof CompositePoint);
		//System.out.println("p1.closure() is " + cp1);
		
		assertTrue(p1.isCycle() == true);

		
		double[] coord2 = new double[]{5, 20};
		dp1 = tCoordFactory.createDirectPosition(coord2);
		p1.setDirectPosition(dp1);
		//System.out.println("P1: " + p1);
		//System.out.println("P2: " + p2);
		assertTrue(!p1.equals(p2));
		

		
	}

}
