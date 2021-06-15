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
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Polygon;

/**
 * Unit tests for the Grids class.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class GridsHexagonalTest {

    private final ReferencedEnvelope BOUNDS = new ReferencedEnvelope(0, 90, 0, 100, null);
    private final double SIDE_LEN = 5.0;
    
    private final int expectedCols = (int) ((BOUNDS.getWidth() - 2 * SIDE_LEN) / (1.5 * SIDE_LEN)) + 1;
    private final int expectedRows = (int) (BOUNDS.getHeight() / (Math.sqrt(3.0) * SIDE_LEN));
    private final int expectedNumElements = expectedRows * expectedCols;

    @Test
    public void createGrid() throws Exception {
        SimpleFeatureSource gridSource = Grids.createHexagonalGrid(BOUNDS, SIDE_LEN);
        assertGridSizeAndIds(gridSource);

        SimpleFeatureIterator iter = gridSource.getFeatures().features();
        try {
            SimpleFeature f = iter.next();
            Polygon poly = (Polygon) f.getDefaultGeometry();
            assertEquals(6, poly.getCoordinates().length - 1);
        } finally {
            iter.close();
        }
    }

    @Test
    public void createDensifiedGrid() throws Exception {
        final int vertexDensity = 10;
        SimpleFeatureSource gridSource = Grids.createHexagonalGrid(BOUNDS, SIDE_LEN, SIDE_LEN / vertexDensity);
        assertGridSizeAndIds(gridSource);

        SimpleFeatureIterator iter = gridSource.getFeatures().features();
        try {
            SimpleFeature f = iter.next();
            Polygon poly = (Polygon) f.getDefaultGeometry();
            assertTrue(poly.getCoordinates().length - 1 >= 6 * vertexDensity);
        } finally {
            iter.close();
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void createGrid_InvalidBounds() {
        Grids.createHexagonalGrid(ReferencedEnvelope.EVERYTHING, SIDE_LEN);
    }

    @Test(expected=IllegalArgumentException.class)
    public void createGrid_NullBounds() {
        Grids.createHexagonalGrid(null, SIDE_LEN);
    }

    @Test(expected=IllegalArgumentException.class)
    public void createGrid_InvalidSideLength() {
        Grids.createHexagonalGrid(BOUNDS, 0);
    }

    private void assertGridSizeAndIds(SimpleFeatureSource gridSource) throws Exception {
        SimpleFeatureCollection grid = gridSource.getFeatures();
        assertEquals(expectedNumElements, grid.size());

        boolean[] flag = new boolean[expectedNumElements + 1];
        int count = 0;

        SimpleFeatureIterator iter = grid.features();
        try {
            while (iter.hasNext()) {
                SimpleFeature f = iter.next();
                int id = (Integer) f.getAttribute("id");
                assertFalse(id == 0);
                assertFalse(flag[id]);
                flag[id] = true;
                count++ ;
            }

        } finally {
            iter.close();
        }

        assertEquals(expectedNumElements, count);
    }
}
