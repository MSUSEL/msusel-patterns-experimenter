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

import java.io.PrintStream;

/**
 * Represents a point on an edge which intersects with another edge.
 * <p>
 * The intersection may either be a single point, or a line segment (in which
 * case this point is the start of the line segment) The intersection point must
 * be precise.
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class EdgeIntersection implements Comparable {

	public Coordinate coord; // the point of intersection

	public int segmentIndex; // the index of the containing line segment in
								// the parent edge

	public double dist; // the edge distance of this point along the containing
						// line segment

	public EdgeIntersection(Coordinate coord, int segmentIndex, double dist) {
		this.coord = new Coordinate(coord);
		this.segmentIndex = segmentIndex;
		this.dist = dist;
	}

	public int compareTo(Object obj) {
		EdgeIntersection other = (EdgeIntersection) obj;
		return compare(other.segmentIndex, other.dist);
	}

	/**
	 * @return -1 this EdgeIntersection is located before the argument location
	 * @return 0 this EdgeIntersection is at the argument location
	 * @return 1 this EdgeIntersection is located after the argument location
	 */
	public int compare(int segmentIndex, double dist) {
		if (this.segmentIndex < segmentIndex)
			return -1;
		if (this.segmentIndex > segmentIndex)
			return 1;
		if (this.dist < dist)
			return -1;
		if (this.dist > dist)
			return 1;
		return 0;
	}

	public boolean isEndPoint(int maxSegmentIndex) {
		if (segmentIndex == 0 && dist == 0.0)
			return true;
		if (segmentIndex == maxSegmentIndex)
			return true;
		return false;
	}

	public void print(PrintStream out) {
		out.print(coord);
		out.print(" seg # = " + segmentIndex);
		out.println(" dist = " + dist);
	}
}
