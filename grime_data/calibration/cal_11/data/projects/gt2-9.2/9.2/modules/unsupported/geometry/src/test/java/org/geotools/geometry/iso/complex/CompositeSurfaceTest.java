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
package org.geotools.geometry.iso.complex;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Surface;

/**
 * 
 *
 * @source $URL$
 */
public class CompositeSurfaceTest extends TestCase {

	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84); 
		
		this._testCompositeSurface(builder);
		
	}
	
	
	private void _testCompositeSurface(GeometryBuilder builder) {
		
		ComplexFactory complf = builder.getComplexFactory();
		PrimitiveFactoryImpl pf = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		GeometryFactory cf = builder.getGeometryFactory();

		List<DirectPosition> directPositionList = new ArrayList<DirectPosition>();
		directPositionList.add(cf.createDirectPosition(new double[] {20, 10}));
		directPositionList.add(cf.createDirectPosition(new double[] {40, 10}));
		directPositionList.add(cf.createDirectPosition(new double[] {50, 40}));
		directPositionList.add(cf.createDirectPosition(new double[] {30, 50}));
		directPositionList.add(cf.createDirectPosition(new double[] {10, 30}));
		directPositionList.add(cf.createDirectPosition(new double[] {20, 10}));

		
		Surface s1 = pf.createSurfaceByDirectPositions(directPositionList);
		
		List<OrientableSurface> surfaceList = new ArrayList<OrientableSurface>();
		surfaceList.add(s1);
		
		CompositeSurface comps1 = complf.createCompositeSurface(surfaceList);
		
		//System.out.println(comps1.getEnvelope());
		double[] dp = comps1.getEnvelope().getLowerCorner().getCoordinate();
		assertTrue(dp[0] == 10);
		assertTrue(dp[1] == 10);
		dp = comps1.getEnvelope().getUpperCorner().getCoordinate();
		assertTrue(dp[0] == 50);
		assertTrue(dp[1] == 50);

		
		// ***** getRepresentativePoint()
		dp = comps1.getRepresentativePoint().getCoordinate();
		assertTrue(dp[0] == 20);
		assertTrue(dp[1] == 10);
		
		// test equals
		assertTrue(comps1.equals(new CompositeSurfaceImpl(surfaceList)));
		
		// Boundary operation of CompositeSurface not implemented yet. Hence isCycle doesn´t work yet.
		//assertTrue(comps1.isCycle() == false);

		
	}
	
}
