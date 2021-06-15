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

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Neighbor;
import org.geotools.grid.TestBase;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Unit tests for the OblongGridBuilder class.
 *
 * @author mbedward
 * @since 2.7
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class OblongGridBuilderTest extends TestBase {

    private static final ReferencedEnvelope bounds = new ReferencedEnvelope(0, 100, 0, 100, null);
    private static final double WIDTH = 10;
    private static final double HEIGHT = 5;

    private OblongBuilder gridBuilder;

    @Before
    public void setup() {
        gridBuilder = new OblongBuilder(bounds, WIDTH, HEIGHT);
    }

    @Test
    public void createNeighbor() {
        Oblong neighbor = null;

        class Shift {

            double dx;
            double dy;

            public Shift(double dx, double dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }

        Map<Neighbor, Shift> shifts = new HashMap<Neighbor, Shift>();
        shifts.put(Neighbor.LOWER, new Shift(0.0, -HEIGHT));
        shifts.put(Neighbor.LOWER_LEFT, new Shift(-WIDTH, -HEIGHT));
        shifts.put(Neighbor.LOWER_RIGHT, new Shift(WIDTH, -HEIGHT));
        shifts.put(Neighbor.LEFT, new Shift(-WIDTH, 0.0));
        shifts.put(Neighbor.RIGHT, new Shift(WIDTH, 0.0));
        shifts.put(Neighbor.UPPER, new Shift(0.0, HEIGHT));
        shifts.put(Neighbor.UPPER_LEFT, new Shift(-WIDTH, HEIGHT));
        shifts.put(Neighbor.UPPER_RIGHT, new Shift(WIDTH, HEIGHT));

        Oblong oblong = Oblongs.create(0.0, 0.0, WIDTH, HEIGHT, null);

        for (Neighbor n : Neighbor.values()) {
            neighbor = gridBuilder.createNeighbor(oblong, n);

            Shift shift = shifts.get(n);
            assertNotNull("Error in test code", shift);
            assertNeighbor(oblong, neighbor, shift.dx, shift.dy);
        }
    }

    private void assertNeighbor(Oblong refEl, Oblong neighbor, double dx, double dy) {
        Coordinate[] refCoords = refEl.getVertices();
        Coordinate[] neighborCoords = neighbor.getVertices();

        for (int i = 0; i < refCoords.length; i++) {
            refCoords[i].x += dx;
            refCoords[i].y += dy;
            assertCoordinate(refCoords[i], neighborCoords[i]);
        }
    }
}
