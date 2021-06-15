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
package org.geotools.caching.grid.spatialindex;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.caching.grid.spatialindex.store.MemoryStorage;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;


/**
 * 
 *
 * @source $URL$
 */
public class GridRootNodeTest extends GridNodeTest {
    GridRootNode node;
    GridRootNode node3D;
    Region mbr3D;
    int size = 100;
    int size3D = 1000;
    GridSpatialIndex index ;
    
    public static Test suite() {
        return new TestSuite(GridRootNodeTest.class);
    }

    public void setUp() {
        mbr = new Region(new double[] { 0, 20 }, new double[] { 10, 30 });
        mbr3D = new Region(new double[] { 0, 20, 40 }, new double[] { 10, 30, 50 });

        index = new GridSpatialIndex(mbr, 100, MemoryStorage.createInstance(), 2000);
        node = new GridRootNode(size, new RegionNodeIdentifier( mbr));
        node3D = new GridRootNode(size3D, new RegionNodeIdentifier(mbr3D) );
        super.node = node;
    }

    public void testContructor() {
        assertTrue(node.getCapacity() >= size);
    }

    public void testConstructor3D() {
        assertTrue(node3D.getCapacity() >= size3D);
    }

    public void testIncrement() {
        int dims = node.tiles_number.length;
        double[] pos = new double[dims];
        double[] nextpos = new double[dims];
        double[] posback = new double[dims];
        double[] nextposback = new double[dims];

        for (int i = 0; i < dims; i++) {
            pos[i] = ((Region)node.getShape()).getLow(i);
            posback[i] = pos[i];
            nextpos[i] = pos[i] + node.tiles_size;
            nextposback[i] = nextpos[i];
        }

        node.increment(pos, nextpos);
        assertEquals(nextposback[0], pos[0], 0);

        for (int i = 1; i < dims; i++) {
            assertEquals(posback[i], pos[i], 0);
        }

        int count = 1;

        do {
            count++;
        } while (node.increment(pos, nextpos));

        assertEquals(node.getCapacity(), count);

        for (int i = 0; i < dims; i++) {
            assertEquals(posback[i], pos[i], 0);
        }
    }

    public void testGridIndexToNodeId() {
        int[] index = new int[] { 1, 2, 3 };
        int id = node3D.gridIndexToNodeId(index);
        int of = ((int) Math.pow(size3D, 1d / 3)) + 1;
        assertEquals((of * of * 3) + (of * 2) + 1, id);
    }

    public void testSplit() {
        node.split(index);
        assertEquals(node.getCapacity(), node.children.size());

        double size = node.getShape().getArea() / node.getCapacity();

        for (Iterator<NodeIdentifier> it = node.children.iterator(); it.hasNext();) {
            NodeIdentifier next = (NodeIdentifier) it.next();
            assertTrue(node.getShape().intersects(next.getShape()));
            assertEquals(size, next.getShape().getArea(), 1e-2);
        }
    }

    public void testSplit3D() {
        node3D.split(index);
        assertEquals(node3D.getCapacity(), node3D.children.size());

        double size = node3D.getShape().getArea() / node3D.getCapacity();

        for (Iterator<NodeIdentifier> it = node3D.children.iterator(); it.hasNext();) {
            NodeIdentifier next = (NodeIdentifier) it.next();
            assertTrue(node3D.getShape().intersects(next.getShape()));
            assertEquals(size, next.getShape().getArea(), 1e-2);
        }
    }
}
