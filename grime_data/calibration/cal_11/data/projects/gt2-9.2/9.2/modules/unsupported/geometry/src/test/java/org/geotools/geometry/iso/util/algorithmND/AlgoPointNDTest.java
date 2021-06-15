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
package org.geotools.geometry.iso.util.algorithmND;

import junit.framework.TestCase;

import org.geotools.geometry.iso.util.algorithmND.AlgoPointND;

/**
 * 
 *
 * @source $URL$
 */
public class AlgoPointNDTest extends TestCase {

	public void testWorkingAlgos() {
		
		this._test3D();
	}
	
	public void testNotWorkingYetAlgos() {
		
		//this._testNotWorkingAlgorithms();
		
	}

	/**
	 * Test methods for three dimensional space
	 */
	private void _test3D() {
		
		// NOTE:
		// Some asserts must be rounded, because of the floating-point rounding errors

		
		double[] c1 = new double[]{0, 0, 0};
		double[] c2 = new double[]{50, 50, 50};
		double[] c3 = new double[]{100, 100, 100};
		double[] c4 = new double[]{150, 150, 150};
		double[] c5 = new double[]{200, 200, 200};
		double[] c6 = new double[]{150, 150.000000001, 150};
		
		double[] erg = null;
		
		erg = AlgoPointND.add(c6, c6);
		assertTrue(erg[0] == 300);
		assertTrue(erg[1] == 300.000000002);
		assertTrue(erg[2] == 300);

		// add(double[], double[]) : double[]
		erg = AlgoPointND.add(c1, c2);
		assertTrue(erg[0] == 50);
		assertTrue(erg[1] == 50);
		assertTrue(erg[2] == 50);

		erg = AlgoPointND.add(erg, c6);
		assertTrue(erg[0] == 200);
		assertTrue(erg[1] == 200.000000001);
		assertTrue(erg[2] == 200);

		// subtract(double[], double[]) : double[]
		erg = AlgoPointND.subtract(c1, erg);
		assertTrue(erg[0] == 200);
		assertTrue(erg[1] == 200.000000001);
		assertTrue(erg[2] == 200);
		
		double[] erg2 = AlgoPointND.subtract(erg, erg);
		assertTrue(erg2[0] == 0);
		assertTrue(erg2[1] == 0);
		assertTrue(erg2[2] == 0);
		
		erg2 = AlgoPointND.subtract(c4, erg);
		assertTrue(erg2[0] == 50);
		assertTrue(Math.round(erg2[1]*1000000000) == 50000000001.0);
		assertTrue(erg2[2] == 50);

		erg2 = AlgoPointND.subtract(c5, erg);
		assertTrue(erg2[0] == 0);
		assertTrue(Math.round(erg2[1]*1000000000) == 1.0);
		assertTrue(erg2[2] == 0);
		
		// getDistanceToOrigin(double[]) : double
		double d = AlgoPointND.getDistanceToOrigin(c1);
		assertTrue(d == 0.0);

		d = AlgoPointND.getDistanceToOrigin(c2);
		assertTrue(Math.round(d * 10)  == 866.0);
		
		d = AlgoPointND.getDistanceToOrigin(c3);
		assertTrue(Math.round(d * 10)  == 1732.0);

		// normalize(double[]) : double[]
		erg = AlgoPointND.normalize(c1);
		assertTrue(erg == null);

		erg = AlgoPointND.normalize(c2);
		assertTrue(Math.round(erg[0]*1000) == 577.0);
		assertTrue(Math.round(erg[1]*1000) == 577.0);
		assertTrue(Math.round(erg[2]*1000) == 577.0);
		d = AlgoPointND.getDistanceToOrigin(erg);
		assertTrue(d == 1.0);
		
		
		
	}

	
	
	private void _testNotWorkingAlgorithms() {
		
		
	}


}
