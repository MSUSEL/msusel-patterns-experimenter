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

package org.geotools.grid.hexagon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.GridElement;
import org.geotools.grid.GridFeatureBuilder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Unit tests for the Hexagons class.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class HexagonsTest extends HexagonTestBase {

    @Test
    public void calculateArea() {
        assertEquals(AREA, Hexagons.sideLengthToArea(SIDE_LEN), TOL);
    }

    @Test(expected=IllegalArgumentException.class)
    public void calculateAreaInvalidArg() {
        Hexagons.sideLengthToArea(0.0);
    }

    @Test
    public void calculateSideLen() {
        assertEquals(SIDE_LEN, Hexagons.areaToSideLength(AREA), TOL);
    }

    @Test(expected=IllegalArgumentException.class)
    public void calculateSideLenInvalidArg() {
        Hexagons.areaToSideLength(0.0);
    }

    @Test
    public void createHexagon() {
        Hexagon hexagon = Hexagons.create(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        assertNotNull(hexagon);
    }

    @Test
    public void getVerticesFlat() {
        Hexagon hexagon = Hexagons.create(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        assertVertices(hexagon, 0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT);
    }
    
    @Test
    public void getVerticesAngled() {
        Hexagon hexagon = Hexagons.create(0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED, null);
        assertVertices(hexagon, 0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED);
    }

    @Test
    public void createGrid() throws Exception {
        final SimpleFeatureType TYPE = DataUtilities.createType("hextype", "hexagon:Polygon,id:Integer");

        final double SPAN = 100;
        final ReferencedEnvelope bounds = new ReferencedEnvelope(0, SPAN, 0, SPAN, null);

        class Setter extends GridFeatureBuilder {
            int id = 0;

            public Setter(SimpleFeatureType type) {
                super(type);
            }

            @Override
            public void setAttributes(GridElement el, Map<String, Object> attributes) {
                attributes.put("id", ++id);
            }
        }

        Setter setter = new Setter(TYPE);

        SimpleFeatureSource gridSource = Hexagons.createGrid(bounds, SIDE_LEN, HexagonOrientation.FLAT, setter);
        assertNotNull(gridSource);

        int expectedCols = (int) ((SPAN - 2 * SIDE_LEN) / (1.5 * SIDE_LEN)) + 1;
        int expectedRows = (int) (SPAN / (Math.sqrt(3.0) * SIDE_LEN));
        
        assertEquals(expectedCols * expectedRows, setter.id);
        assertEquals(setter.id, gridSource.getFeatures().size());
    }

}
