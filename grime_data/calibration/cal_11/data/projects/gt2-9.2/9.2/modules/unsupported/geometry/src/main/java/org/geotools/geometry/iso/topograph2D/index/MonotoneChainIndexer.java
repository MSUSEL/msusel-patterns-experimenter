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
package org.geotools.geometry.iso.topograph2D.index;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Quadrant;

/**
 * MonotoneChains are a way of partitioning the segments of an edge to allow for
 * fast searching of intersections. They have the following properties:
 * <ol>
 * <li>the segments within a monotone chain will never intersect each other
 * <li>the envelope of any contiguous subset of the segments in a monotone
 * chain is simply the envelope of the endpoints of the subset.
 * </ol>
 * Property 1 means that there is no need to test pairs of segments from within
 * the same monotone chain for intersection. Property 2 allows binary search to
 * be used to find the intersection points of two monotone chains. For many
 * types of real-world data, these properties eliminate a large number of
 * segment comparisons, producing substantial speed gains.
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class MonotoneChainIndexer {

	public static int[] toIntArray(List list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = ((Integer) list.get(i)).intValue();
		}
		return array;
	}

	public MonotoneChainIndexer() {
	}

	public int[] getChainStartIndices(Coordinate[] pts) {
		// find the startpoint (and endpoints) of all monotone chains in this
		// edge
		int start = 0;
		List startIndexList = new ArrayList();
		startIndexList.add(new Integer(start));
		do {
			int last = findChainEnd(pts, start);
			startIndexList.add(new Integer(last));
			start = last;
		} while (start < pts.length - 1);
		// copy list to an array of ints, for efficiency
		int[] startIndex = toIntArray(startIndexList);
		return startIndex;
	}

	/**
	 * @return the index of the last point in the monotone chain
	 */
	private int findChainEnd(Coordinate[] pts, int start) {
		// determine quadrant for chain
		int chainQuad = Quadrant.quadrant(pts[start], pts[start + 1]);
		int last = start + 1;
		while (last < pts.length) {
			// compute quadrant for next possible segment in chain
			int quad = Quadrant.quadrant(pts[last - 1], pts[last]);
			if (quad != chainQuad)
				break;
			last++;
		}
		return last - 1;
	}

}
