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

import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class PrimitiveGeometryBuilderTest extends TestCase {

	CoordinateReferenceSystem crs_WGS84;
	GeometryBuilder builder;
	
	public void setUp() {
		crs_WGS84 = DefaultGeographicCRS.WGS84;
		builder = new GeometryBuilder(crs_WGS84); 
	}
	
	public void testBuildPoint() {
		
		// test positionfactory
		PositionFactory posFactory = builder.getPositionFactory();
		DirectPosition position = posFactory.createDirectPosition(new double[] { 48.44, -123.37 });
		
		// test primitivefactory
		PrimitiveFactory primitiveFactory = builder.getPrimitiveFactory();
		Point point = primitiveFactory.createPoint(new double[] { 48.44, -123.37 });
		
		assertTrue(position.equals(point.getCentroid()));
		
		// change CRS and test
		builder.setCoordianteReferenceSystem(DefaultGeographicCRS.WGS84_3D);
		primitiveFactory = builder.getPrimitiveFactory();
		Point point3D = primitiveFactory.createPoint(new double[] { 48.44, -123.37, 1.0 });
		
		assertFalse(point.getCoordinateReferenceSystem().equals(point3D.getCoordinateReferenceSystem()));
		assertFalse(point.equals(point3D));
	}
}
