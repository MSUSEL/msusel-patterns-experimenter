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

import java.util.ArrayList;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;

/**
 * @author sanjay
 *
 *
 *
 *
 *
 * @source $URL$
 */
public class RingTest extends TestCase {
	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		
		//this._testTriangle1(tGeomFactory);
		this._testRing1(builder);
		
	}

	private void _testRing1(GeometryBuilder builder) {

		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		
		/* Defining Positions for LineStrings */
		ArrayList<Position> line1 = new ArrayList<Position>();
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50, 20})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30, 30})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 50})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 70})));

		ArrayList<Position> line2 = new ArrayList<Position>();
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 70})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{40, 80})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{70, 80})));

		ArrayList<Position> line3 = new ArrayList<Position>();
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{70, 80})));
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{90, 70})));
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 60})));
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 40})));

		ArrayList<Position> line4 = new ArrayList<Position>();
		line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 40})));
		line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{80, 30})));
		line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50, 20})));
		
		/* Setting up Array of these LineStrings */
		ArrayList<CurveSegment> tLineList1 = new ArrayList<CurveSegment>();
		tLineList1.add(tCoordFactory.createLineString(line1));
		tLineList1.add(tCoordFactory.createLineString(line2)); 

		ArrayList<CurveSegment> tLineList2 = new ArrayList<CurveSegment>();
		tLineList2.add(tCoordFactory.createLineString(line3)); 
		tLineList2.add(tCoordFactory.createLineString(line4)); 

		/* Build Curve */
		CurveImpl curve1 = tPrimFactory.createCurve(tLineList1);
		CurveImpl curve2 = tPrimFactory.createCurve(tLineList2);

		
		/* Build Ring */
		ArrayList<OrientableCurve> curveList = new ArrayList<OrientableCurve>();
		curveList.add(curve1);
		curveList.add(curve2);
		
		Ring ring1 = tPrimFactory.createRing(curveList);

		//System.out.println(ring1);

		//System.out.println(ring1.getEnvelope());
		
		// Rings should be simple
		assertTrue(ring1.isSimple());
		
		// Rings should be cyclic (=closed)
		assertTrue(ring1.isCycle());
		
		// ***** getRepresentativePoint()
		double[] dp = ring1.getRepresentativePoint().getCoordinate();
		assertTrue(dp[0] == 50);
		assertTrue(dp[1] == 20);

		assertTrue(ring1.isCycle() == true);
		
		//PaintGMObject.paint(curve1);

		

	}


}
