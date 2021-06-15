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

package org.geotools.grid.oblong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.geotools.grid.PolygonElement;
import org.geotools.grid.TestBase;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Unit tests for the Oblong class.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class OblongTest extends TestBase {
    private static final double MINX = -10;
    private static final double MINY = -5;
    private static final double WIDTH = 2.0;
    private static final double HEIGHT = 1.0;

    @Test
    public void createValid() {
        PolygonElement oblong = new OblongImpl(0, 0, WIDTH, HEIGHT, null);
        assertNotNull(oblong);
    }

    @Test(expected=IllegalArgumentException.class)
    public void negativeWidth() {
        PolygonElement oblong = new OblongImpl(0, 0, -1, HEIGHT, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void zeroWidth() {
        PolygonElement oblong = new OblongImpl(0, 0, 0, HEIGHT, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void negativeHeight() {
        PolygonElement oblong = new OblongImpl(0, 0, WIDTH, -1, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void zeroHeight() {
        PolygonElement oblong = new OblongImpl(0, 0, WIDTH, 0, null);
    }

    @Test
    public void getArea() {
        PolygonElement oblong = new OblongImpl(MINX, MINY, WIDTH, HEIGHT, null);
        double expected = WIDTH * HEIGHT;
        assertEquals(expected, oblong.getArea(), TOL);
    }

    @Test
    public void getBounds() {
        PolygonElement oblong = new OblongImpl(MINX, MINY, WIDTH, HEIGHT, null);
        assertEnvelope(new Envelope(-10, WIDTH - 10, -5, HEIGHT - 5), oblong.getBounds());
    }

    @Test
    public void getCenter() {
        PolygonElement oblong = new OblongImpl(MINX, MINY, WIDTH, HEIGHT, null);
        Coordinate expected = new Coordinate(WIDTH/2 + MINX, HEIGHT/2 + MINY);
        assertCoordinate(expected, oblong.getCenter());
    }

    @Test
    public void getVertices() {
        PolygonElement oblong = new OblongImpl(MINX, MINY, WIDTH, HEIGHT, null);
        Coordinate[] expected = {
            new Coordinate(MINX, MINY),
            new Coordinate(MINX, MINY + HEIGHT),
            new Coordinate(MINX + WIDTH, MINY + HEIGHT),
            new Coordinate(MINX + WIDTH, MINY),
        };

        Coordinate[] actual = oblong.getVertices();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertCoordinate(expected[i], actual[i]);
        }
    }

    @Test
    public void toGeometry() {
        PolygonElement oblong = new OblongImpl(MINX, MINY, WIDTH, HEIGHT, null);
        Geometry polygon = oblong.toGeometry();
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);

        Set<Coordinate> polyCoords = new HashSet<Coordinate>(Arrays.asList(polygon.getCoordinates()));
        for (Coordinate c : oblong.getVertices()) {
            assertTrue(polyCoords.contains(c));
        }
    }

    @Test
    public void toDenseGeometry() {
        PolygonElement oblong = new OblongImpl(0, 0, WIDTH, HEIGHT, null);

        final int density = 10;
        final double maxSpacing = Math.min(WIDTH, HEIGHT) / density;

        Geometry polygon = oblong.toDenseGeometry(maxSpacing);
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);
        assertTrue(polygon.getCoordinates().length - 1 >= 2 * (WIDTH + HEIGHT) * density);
    }

}
