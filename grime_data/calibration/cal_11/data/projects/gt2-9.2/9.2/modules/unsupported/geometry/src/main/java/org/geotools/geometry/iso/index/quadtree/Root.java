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
package org.geotools.geometry.iso.index.quadtree;

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Envelope;
import org.geotools.geometry.iso.util.Assert;

/**
 * QuadRoot is the root of a single Quadtree. It is centred at the origin, and
 * does not have a defined extent.
 * 
 *
 *
 *
 *
 * @source $URL$
 * @version 1.7.2
 */
public class Root extends NodeBase {

	// the singleton root quad is centred at the origin.
	private static final Coordinate origin = new Coordinate(0.0, 0.0);

	public Root() {
	}

	/**
	 * Insert an item into the quadtree this is the root of.
	 */
	public void insert(Envelope itemEnv, Object item) {
		int index = getSubnodeIndex(itemEnv, origin);
		// if index is -1, itemEnv must cross the X or Y axis.
		if (index == -1) {
			add(item);
			return;
		}
		/**
		 * the item must be contained in one quadrant, so insert it into the
		 * tree for that quadrant (which may not yet exist)
		 */
		Node node = subnode[index];
		/**
		 * If the subquad doesn't exist or this item is not contained in it,
		 * have to expand the tree upward to contain the item.
		 */

		if (node == null || !node.getEnvelope().contains(itemEnv)) {
			Node largerNode = Node.createExpanded(node, itemEnv);
			subnode[index] = largerNode;
		}
		/**
		 * At this point we have a subquad which exists and must contain
		 * contains the env for the item. Insert the item into the tree.
		 */
		insertContained(subnode[index], itemEnv, item);
		// System.out.println("depth = " + root.depth() + " size = " +
		// root.size());
		// System.out.println(" size = " + size());
	}

	/**
	 * insert an item which is known to be contained in the tree rooted at the
	 * given QuadNode root. Lower levels of the tree will be created if
	 * necessary to hold the item.
	 */
	private void insertContained(Node tree, Envelope itemEnv, Object item) {
		Assert.isTrue(tree.getEnvelope().contains(itemEnv));
		/**
		 * Do NOT create a new quad for zero-area envelopes - this would lead to
		 * infinite recursion. Instead, use a heuristic of simply returning the
		 * smallest existing quad containing the query
		 */
		boolean isZeroX = IntervalSize.isZeroWidth(itemEnv.getMinX(), itemEnv
				.getMaxX());
		boolean isZeroY = IntervalSize.isZeroWidth(itemEnv.getMinX(), itemEnv
				.getMaxX());
		NodeBase node;
		if (isZeroX || isZeroY)
			node = tree.find(itemEnv);
		else
			node = tree.getNode(itemEnv);
		node.add(item);
	}

	protected boolean isSearchMatch(Envelope searchEnv) {
		return true;
	}

}
