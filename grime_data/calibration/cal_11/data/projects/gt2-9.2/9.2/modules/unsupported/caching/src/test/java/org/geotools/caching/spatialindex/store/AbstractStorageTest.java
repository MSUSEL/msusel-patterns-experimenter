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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.spatialindex.store;

import junit.framework.TestCase;

import org.geotools.caching.grid.spatialindex.GridSpatialIndex;
import org.geotools.caching.grid.spatialindex.store.MemoryStorage;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;
import org.geotools.caching.spatialindex.Storage;


/**
 * 
 *
 * @source $URL$
 */
public abstract class AbstractStorageTest extends TestCase {
    Storage store;
    TestNode n;
    RegionNodeIdentifier id;
    GridSpatialIndex grid;

    protected void setUp() {
        grid = new GridSpatialIndex(new Region(new double[] { 0, 0 }, new double[] { 1, 1 }), 10,
                MemoryStorage.createInstance(), 200);
        n = new TestNode(grid, new Region(new double[] { 0, 0 }, new double[] { 1, 1 }));
        id = new RegionNodeIdentifier(n);
        store = createStorage();
    }

    abstract Storage createStorage();

    public void testPut() {
        store.put(n);
        store.put(n);
    }

    public void testGet() {
        store.put(n);

        Node g = store.get(id);
        assertEquals(n.getIdentifier(), g.getIdentifier());
    }

    public void testRemove() {
        store.put(n);

        Node g = store.get(id);
        assertEquals(n.getIdentifier(), g.getIdentifier());
        store.remove(id);
        assertNull(store.get(id));
        store.put(n);
        g = store.get(id);
        assertEquals(n.getIdentifier(), g.getIdentifier());
    }
}
