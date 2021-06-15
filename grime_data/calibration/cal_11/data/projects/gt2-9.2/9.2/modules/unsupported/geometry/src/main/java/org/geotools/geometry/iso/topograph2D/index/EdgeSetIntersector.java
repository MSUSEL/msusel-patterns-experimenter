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

import java.util.List;

/**
 * An EdgeSetIntersector computes all the intersections between the edges in the
 * set. It adds the computed intersections to each edge they are found on. It
 * may be used in two scenarios:
 * <ul>
 * <li>determining the internal intersections between a single set of edges
 * <li>determining the mutual intersections between two different sets of edges
 * </ul>
 * It uses a {@link SegmentIntersector} to compute the intersections between
 * segments and to record statistics about what kinds of intersections were
 * found.
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class EdgeSetIntersector {

	List edges0 = null;

	List edges1 = null;

	public EdgeSetIntersector() {
	}

	/**
	 * Computes all self-intersections between edges in a set of edges, allowing
	 * client to choose whether self-intersections are computed.
	 * 
	 * @param edges
	 *            a list of edges to test for intersections
	 * @param si
	 *            the SegmentIntersector to use
	 * @param testAllSegments
	 *            true if self-intersections are to be tested as well
	 */
	abstract public void computeIntersections(List edges,
			SegmentIntersector si, boolean testAllSegments);

	/**
	 * Computes all mutual intersections between two sets of edges.
	 */
	abstract public void computeIntersections(List edges0, List edges1,
			SegmentIntersector si);

}
