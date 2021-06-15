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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.geotools.grid.PolygonElement;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Unit tests for the Hexagon class.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class HexagonTest extends HexagonTestBase {

    @Test
    public void getVerticesFlat() {
        double minx = 1.0;
        double miny = -1.0;
        PolygonElement hexagon = new HexagonImpl(minx, miny, SIDE_LEN, HexagonOrientation.FLAT, null);

        assertVertices(hexagon, minx, miny, SIDE_LEN, HexagonOrientation.FLAT);
    }

    @Test(expected=IllegalArgumentException.class)
    public void badSideLen() throws Exception {
        PolygonElement h = new HexagonImpl(0.0, 0.0, 0.0, HexagonOrientation.FLAT, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void badOrientation() throws Exception {
        PolygonElement h = new HexagonImpl(0.0, 0.0, SIDE_LEN, null, null);
    }

    @Test
    public void getOrientation() {
        Hexagon hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED, null);
        assertEquals(HexagonOrientation.ANGLED, hexagon.getOrientation());

        hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        assertEquals(HexagonOrientation.FLAT, hexagon.getOrientation());
    }

    @Test
    public void getVerticesAngled() {
        double minx = 1.0;
        double miny = -1.0;
        PolygonElement hexagon = new HexagonImpl(minx, miny, SIDE_LEN, HexagonOrientation.ANGLED, null);

        assertVertices(hexagon, minx, miny, SIDE_LEN, HexagonOrientation.ANGLED);
    }

    @Test
    public void getCenterFlat() throws Exception {
        PolygonElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        Coordinate expected = new Coordinate(SIDE_LEN, 0.5 * Math.sqrt(3.0) * SIDE_LEN);
        Coordinate result = hexagon.getCenter();

        assertCoordinate(expected, result);
    }

    @Test
    public void getCenterAngled() {
        PolygonElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED, null);
        Coordinate expected = new Coordinate(0.5 * Math.sqrt(3.0) * SIDE_LEN, SIDE_LEN);
        Coordinate result = hexagon.getCenter();

        assertCoordinate(expected, result);
    }

    @Test
    public void getBoundsFlat() {
        PolygonElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);

        Envelope expected = new Envelope(
                0.0,
                2.0 * SIDE_LEN,
                0.0,
                Math.sqrt(3.0) * SIDE_LEN);

        Envelope result = hexagon.getBounds();

        assertEnvelope(expected, result);
    }

    @Test
    public void getBoundsAngled() {
        PolygonElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.ANGLED, null);

        Envelope expected = new Envelope(
                0.0,
                Math.sqrt(3.0) * SIDE_LEN,
                0.0,
                2.0 * SIDE_LEN);

        Envelope result = hexagon.getBounds();

        assertEnvelope(expected, result);
    }

    @Test
    public void toGeometry() {
        PolygonElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);
        Geometry polygon = hexagon.toGeometry();
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);

        Set<Coordinate> polyCoords = new HashSet<Coordinate>(Arrays.asList(polygon.getCoordinates()));
        for (Coordinate c : hexagon.getVertices()) {
            assertTrue(polyCoords.contains(c));
        }
    }

    @Test
    public void toDenseGeometry() {
        PolygonElement hexagon = new HexagonImpl(0.0, 0.0, SIDE_LEN, HexagonOrientation.FLAT, null);

        final int density = 10;
        final double maxSpacing = SIDE_LEN / density;

        Geometry polygon = hexagon.toDenseGeometry(maxSpacing);
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);
        assertTrue(polygon.getCoordinates().length - 1 >= 6 * density);
    }

}
