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
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;


/**
 * 
 *
 * @source $URL$
 */
public class GridNodeTest extends TestCase {
    GridNode node;
    Region mbr;
    String data = "Sample data : ";
    String data2 = "Sample data 2 : ";

    public static Test suite() {
        return new TestSuite(GridNodeTest.class);
    }

    public void setUp() {
        mbr = new Region(new double[] { 0, 1 }, new double[] { 2, 3 });
        node = new GridNode(new RegionNodeIdentifier(mbr));
    }

    public void testConstructor() {
        assertEquals(mbr, node.getShape());
        assertEquals(new Region(mbr), node.getShape());

        GridNode child = new GridNode(new RegionNodeIdentifier(mbr));
        assertEquals(0, child.getLevel());
        assertEquals(mbr, node.getShape());
        assertEquals(new Region(mbr), node.getShape());
    }

    void populate() {
    	node.data.clear();
        for (int i = 0; i < 10; i++) {
        	GridData gd1 = new GridData(mbr, data+i);
        	GridData gd2 = new GridData(mbr, data2+i);
            node.insertData(gd1);
            node.insertData(gd2);
        }
        
    }

    public void testInsert() {
        populate();
        assertEquals(20, node.getDataCount());
        for (int i = 0; i < 10; i ++){
        	assertEquals(true, node.getData().contains(new GridData(mbr, data + i)));
        	assertEquals(true, node.getData().contains(new GridData(mbr, data2 + i)));
        }
    }

    public void testDelete() {
        populate();
        
        GridData del = new GridData(mbr, data + 5 );
        node.deleteData(del);
        assertEquals(false, node.getData().contains(del));
        
        del = new GridData(mbr, data2 + 9 );
        node.deleteData(del);
        assertEquals(false, node.getData().contains(del));
        
        assertEquals(20-2, node.getDataCount());
    }

    Object getData(GridNode n, int index) {
        if ((index < 0) || (index > (n.getDataCount() - 1))) {
            return null;
        }

        Iterator<GridData> it = n.data.iterator();

        for (int i = 0; i < index; i++) {
            it.next();
        }

        return it.next().getData();
    }
}
