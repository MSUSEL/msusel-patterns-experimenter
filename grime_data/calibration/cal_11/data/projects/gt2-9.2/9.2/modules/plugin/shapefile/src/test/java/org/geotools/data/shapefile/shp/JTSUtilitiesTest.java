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
package org.geotools.data.shapefile.shp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * 
 *
 * @source $URL$
 */
public class JTSUtilitiesTest {
    GeometryFactory geomFactory;

    @Before
    public void setUp() {
        geomFactory = new GeometryFactory();
    }

    @After
    public void tearDown() {
        geomFactory = null;
    }

    @Test
    public void testReverseRing() {
        Coordinate[] coordinates = new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1),
                new Coordinate(0, 2), new Coordinate(0, 0) };
        LinearRing before = geomFactory.createLinearRing(coordinates);
        assertEquals(before.getCoordinateN(0), coordinates[0]);
        assertEquals(before.getCoordinateN(1), coordinates[1]);
        assertEquals(before.getCoordinateN(2), coordinates[2]);
        assertEquals(before.getCoordinateN(3), coordinates[3]);

        LinearRing after = JTSUtilities.reverseRing(before);

        assertTrue( after.equalsTopo(before.reverse()) );

        assertEquals(after.getCoordinateN(0), coordinates[3]);
        assertEquals(after.getCoordinateN(1), coordinates[2]);
        assertEquals(after.getCoordinateN(2), coordinates[1]);
        assertEquals(after.getCoordinateN(3), coordinates[0]);
    }
}
