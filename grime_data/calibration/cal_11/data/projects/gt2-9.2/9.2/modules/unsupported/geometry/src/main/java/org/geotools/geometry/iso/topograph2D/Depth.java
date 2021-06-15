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
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.topograph2D;


/**
 * A Depth object records the topological depth of the sides of an Edge for up
 * to two Geometries.
 *
 *
 *
 *
 * @source $URL$
 */
public class Depth {

	private final static int NULL_VALUE = -1;

	public static int depthAtLocation(int location) {
		if (location == Location.EXTERIOR)
			return 0;
		if (location == Location.INTERIOR)
			return 1;
		return NULL_VALUE;
	}

	private int[][] depth = new int[2][3];

	public Depth() {
		// initialize depth array to a sentinel value
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				depth[i][j] = NULL_VALUE;
			}
		}
	}

	public int getDepth(int geomIndex, int posIndex) {
		return depth[geomIndex][posIndex];
	}

	public void setDepth(int geomIndex, int posIndex, int depthValue) {
		depth[geomIndex][posIndex] = depthValue;
	}

	public int getLocation(int geomIndex, int posIndex) {
		if (depth[geomIndex][posIndex] <= 0)
			return Location.EXTERIOR;
		return Location.INTERIOR;
	}

	public void add(int geomIndex, int posIndex, int location) {
		if (location == Location.INTERIOR)
			depth[geomIndex][posIndex]++;
	}

	/**
	 * A Depth object is null (has never been initialized) if all depths are
	 * null.
	 */
	public boolean isNull() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				if (depth[i][j] != NULL_VALUE)
					return false;
			}
		}
		return true;
	}

	public boolean isNull(int geomIndex) {
		return depth[geomIndex][1] == NULL_VALUE;
	}

	public boolean isNull(int geomIndex, int posIndex) {
		return depth[geomIndex][posIndex] == NULL_VALUE;
	}

	public void add(Label lbl) {
		for (int i = 0; i < 2; i++) {
			for (int j = 1; j < 3; j++) {
				int loc = lbl.getLocation(i, j);
				if (loc == Location.EXTERIOR || loc == Location.INTERIOR) {
					// initialize depth if it is null, otherwise add this
					// location value
					if (isNull(i, j)) {
						depth[i][j] = depthAtLocation(loc);
					} else
						depth[i][j] += depthAtLocation(loc);
				}
			}
		}
	}

	public int getDelta(int geomIndex) {
		return depth[geomIndex][Position.RIGHT]
				- depth[geomIndex][Position.LEFT];
	}

	/**
	 * Normalize the depths for each geometry, if they are non-null. A
	 * normalized depth has depth values in the set { 0, 1 }. Normalizing the
	 * depths involves reducing the depths by the same amount so that at least
	 * one of them is 0. If the remaining value is > 0, it is set to 1.
	 */
	public void normalize() {
		for (int i = 0; i < 2; i++) {
			if (!isNull(i)) {
				int minDepth = depth[i][1];
				if (depth[i][2] < minDepth)
					minDepth = depth[i][2];

				if (minDepth < 0)
					minDepth = 0;
				for (int j = 1; j < 3; j++) {
					int newValue = 0;
					if (depth[i][j] > minDepth)
						newValue = 1;
					depth[i][j] = newValue;
				}
			}
		}
	}

	public String toString() {
		return "A: " + depth[0][1] + "," + depth[0][2] + " B: " + depth[1][1]
				+ "," + depth[1][2];
	}
}
