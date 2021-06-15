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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.temporal.object;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.IntervalLength;
import static org.junit.Assert.*;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class DefaultIntervalLengthTest {

    private IntervalLength intervalLength1;
    private IntervalLength intervalLength2;

    @Before
    public void setUp() {
        Unit unit1 = SI.SECOND, unit2 = SI.SECOND.times(3600);
        int radix1 = 10, radix2 = 10;
        int factor1 = 3, factor2 = 6;
        int value1 = 7, value2 = 12;
        intervalLength1 = new DefaultIntervalLength(unit1, radix1, factor1, value1);
        intervalLength2 = new DefaultIntervalLength(unit2, radix2, factor2, value2);
    }

    @After
    public void tearDown() {
        intervalLength1 = null;
        intervalLength2 = null;
    }

    /**
     * Test of getUnit method, of class DefaultIntervalLength.
     */
    @Test
    public void testGetUnit() {
        Unit result = intervalLength1.getUnit();
        assertFalse(intervalLength2.getUnit().equals(result));
    }

    /**
     * Test of getRadix method, of class DefaultIntervalLength.
     */
    @Test
    public void testGetRadix() {
        int result = intervalLength1.getRadix();
        assertTrue(intervalLength2.getRadix() == result);
    }

    /**
     * Test of getFactor method, of class DefaultIntervalLength.
     */
    @Test
    public void testGetFactor() {
        int result = intervalLength1.getFactor();
        assertFalse(intervalLength2.getFactor() == result);
    }

    /**
     * Test of getValue method, of class DefaultIntervalLength.
     */
    @Test
    public void testGetValue() {
        int result = intervalLength1.getValue();
        assertFalse(intervalLength2.getValue() == result);
    }

    /**
     * Test of equals method, of class DefaultIntervalLength.
     */
    @Test
    public void testEquals() {
        assertFalse(intervalLength1.equals(null));
        assertEquals(intervalLength1, intervalLength1);
        assertFalse(intervalLength1.equals(intervalLength2));
    }

    /**
     * Test of hashCode method, of class DefaultIntervalLength.
     */
    @Test
    public void testHashCode() {
        int result = intervalLength1.hashCode();
        assertFalse(intervalLength2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultIntervalLength.
     */
    @Test
    public void testToString() {
        String result = intervalLength1.toString();
        assertFalse(intervalLength2.toString().equals(result));
    }
}
