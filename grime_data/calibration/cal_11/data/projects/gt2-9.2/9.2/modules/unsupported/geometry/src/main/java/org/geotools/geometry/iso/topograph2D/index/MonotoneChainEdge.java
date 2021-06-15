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

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Edge;
import org.geotools.geometry.iso.topograph2D.Envelope;

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
public class MonotoneChainEdge {

	Edge e;

	Coordinate[] pts; // cache a reference to the coord array, for efficiency

	// the lists of start/end indexes of the monotone chains.
	// Includes the end point of the edge as a sentinel
	int[] startIndex;

	// these envelopes are created once and reused
	Envelope env1 = new Envelope();

	Envelope env2 = new Envelope();

	public MonotoneChainEdge(Edge e) {
		this.e = e;
		pts = e.getCoordinates();
		MonotoneChainIndexer mcb = new MonotoneChainIndexer();
		startIndex = mcb.getChainStartIndices(pts);
	}

	public Coordinate[] getCoordinates() {
		return pts;
	}

	public int[] getStartIndexes() {
		return startIndex;
	}

	public double getMinX(int chainIndex) {
		double x1 = pts[startIndex[chainIndex]].x;
		double x2 = pts[startIndex[chainIndex + 1]].x;
		return x1 < x2 ? x1 : x2;
	}

	public double getMaxX(int chainIndex) {
		double x1 = pts[startIndex[chainIndex]].x;
		double x2 = pts[startIndex[chainIndex + 1]].x;
		return x1 > x2 ? x1 : x2;
	}

	public void computeIntersects(MonotoneChainEdge mce, SegmentIntersector si) {
		for (int i = 0; i < startIndex.length - 1; i++) {
			for (int j = 0; j < mce.startIndex.length - 1; j++) {
				computeIntersectsForChain(i, mce, j, si);
			}
		}
	}

	public void computeIntersectsForChain(int chainIndex0,
			MonotoneChainEdge mce, int chainIndex1, SegmentIntersector si) {
		computeIntersectsForChain(startIndex[chainIndex0],
				startIndex[chainIndex0 + 1], mce, mce.startIndex[chainIndex1],
				mce.startIndex[chainIndex1 + 1], si);
	}

	private void computeIntersectsForChain(int start0, int end0,
			MonotoneChainEdge mce, int start1, int end1, SegmentIntersector ei) {
		Coordinate p00 = pts[start0];
		Coordinate p01 = pts[end0];
		Coordinate p10 = mce.pts[start1];
		Coordinate p11 = mce.pts[end1];
		// Debug.println("computeIntersectsForChain:" + p00 + p01 + p10 + p11);
		// terminating condition for the recursion
		if (end0 - start0 == 1 && end1 - start1 == 1) {
			ei.addIntersections(e, start0, mce.e, start1);
			return;
		}
		// nothing to do if the envelopes of these chains don't overlap
		env1.init(p00, p01);
		env2.init(p10, p11);
		if (!env1.intersects(env2))
			return;

		// the chains overlap, so split each in half and iterate (binary search)
		int mid0 = (start0 + end0) / 2;
		int mid1 = (start1 + end1) / 2;

		// Assert: mid != start or end (since we checked above for end - start
		// <= 1)
		// check terminating conditions before recursing
		if (start0 < mid0) {
			if (start1 < mid1)
				computeIntersectsForChain(start0, mid0, mce, start1, mid1, ei);
			if (mid1 < end1)
				computeIntersectsForChain(start0, mid0, mce, mid1, end1, ei);
		}
		if (mid0 < end0) {
			if (start1 < mid1)
				computeIntersectsForChain(mid0, end0, mce, start1, mid1, ei);
			if (mid1 < end1)
				computeIntersectsForChain(mid0, end0, mce, mid1, end1, ei);
		}
	}
}
