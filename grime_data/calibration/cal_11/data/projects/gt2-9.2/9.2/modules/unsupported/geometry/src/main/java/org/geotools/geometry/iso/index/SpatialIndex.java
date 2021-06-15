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
package org.geotools.geometry.iso.index;

import java.util.List;

import org.geotools.geometry.iso.topograph2D.Envelope;

/**
 * The basic operations supported by classes implementing spatial index
 * algorithms.
 * <p>
 * A spatial index typically provides a primary filter for range rectangle
 * queries. A secondary filter is required to test for exact intersection. The
 * secondary filter may consist of other kinds of tests, such as testing other
 * spatial relationships.
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public interface SpatialIndex {
	/**
	 * Adds a spatial item with an extent specified by the given
	 * {@link Envelope} to the index
	 */
	void insert(Envelope itemEnv, Object item);

	/**
	 * Queries the index for all items whose extents intersect the given search
	 * {@link Envelope} Note that some kinds of indexes may also return objects
	 * which do not in fact intersect the query envelope.
	 * 
	 * @param searchEnv
	 *            the envelope to query for
	 * @return a list of the items found by the query
	 */
	List query(Envelope searchEnv);

	/**
	 * Queries the index for all items whose extents intersect the given search
	 * {@link Envelope}, and applies an {@link ItemVisitor} to them. Note that
	 * some kinds of indexes may also return objects which do not in fact
	 * intersect the query envelope.
	 * 
	 * @param searchEnv
	 *            the envelope to query for
	 * @param visitor
	 *            a visitor object to apply to the items found
	 */
	void query(Envelope searchEnv, ItemVisitor visitor);

	/**
	 * Removes a single item from the tree.
	 * 
	 * @param itemEnv
	 *            the Envelope of the item to remove
	 * @param item
	 *            the item to remove
	 * @return <code>true</code> if the item was found
	 */
	boolean remove(Envelope itemEnv, Object item);

}
