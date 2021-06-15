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

import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.PeriodDuration;
import static org.junit.Assert.*;
import org.opengis.util.InternationalString;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class DefaultPeriodDurationTest {

    private PeriodDuration periodDuration1;
    private PeriodDuration periodDuration2;

    @Before
    public void setUp() {
        periodDuration1 = new DefaultPeriodDuration(new SimpleInternationalString("5"),
                new SimpleInternationalString("2"),
                new SimpleInternationalString("1"),
                new SimpleInternationalString("12"),
                new SimpleInternationalString("15"),
                new SimpleInternationalString("5"),
                new SimpleInternationalString("23"));
        periodDuration2 = new DefaultPeriodDuration(1535148449548L);
    }

    @After
    public void tearDown() {
        periodDuration1 = null;
        periodDuration2 = null;
    }

    /**
     * Test of getDesignator method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetDesignator() {
        InternationalString result = periodDuration1.getDesignator();
        assertEquals(periodDuration2.getDesignator(), result);
    }

    /**
     * Test of getYears method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetYears() {
        InternationalString result = periodDuration1.getYears();
        assertFalse(periodDuration2.getYears().equals(result));
    }

    /**
     * Test of getMonths method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetMonths() {
        InternationalString result = periodDuration1.getMonths();
        assertFalse(periodDuration2.getMonths().equals(result));
    }

    /**
     * Test of getDays method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetDays() {
        InternationalString result = periodDuration1.getDays();
        assertFalse(periodDuration2.getDays().equals(result));
    }

    /**
     * Test of getTimeIndicator method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetTimeIndicator() {
        InternationalString result = periodDuration1.getTimeIndicator();
        assertEquals(periodDuration2.getTimeIndicator(), result);
    }

    /**
     * Test of getHours method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetHours() {
        InternationalString result = periodDuration1.getHours();
        assertFalse(periodDuration2.getHours().equals(result));
    }

    /**
     * Test of getMinutes method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetMinutes() {
        InternationalString result = periodDuration1.getMinutes();
        assertFalse(periodDuration2.getMinutes().equals(result));
    }

    /**
     * Test of getSeconds method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetSeconds() {
        InternationalString result = periodDuration1.getSeconds();
        assertFalse(periodDuration2.getSeconds().equals(result));
    }

    /**
     * Test of setYears method, of class DefaultPeriodDuration.
     */
    @Test
    public void testSetYears() {
        InternationalString result = periodDuration1.getYears();
        ((DefaultPeriodDuration) periodDuration1).setYears(new SimpleInternationalString("12"));
        assertFalse(periodDuration1.getYears().equals(result));
    }

    /**
     * Test of setMonths method, of class DefaultPeriodDuration.
     */
    @Test
    public void testSetMonths() {
        InternationalString result = periodDuration1.getMonths();
        ((DefaultPeriodDuration) periodDuration1).setMonths(new SimpleInternationalString("13"));
        assertFalse(periodDuration1.getMonths().equals(result));
    }

    /**
     * Test of setDays method, of class DefaultPeriodDuration.
     */
    @Test
    public void testSetDays() {
        InternationalString result = periodDuration1.getDays();
        ((DefaultPeriodDuration) periodDuration1).setDays(new SimpleInternationalString("14"));
        assertFalse(periodDuration1.getDays().equals(result));
    }

    /**
     * Test of setHours method, of class DefaultPeriodDuration.
     */
    @Test
    public void testSetHours() {
        InternationalString result = periodDuration1.getHours();
        ((DefaultPeriodDuration) periodDuration1).setHours(new SimpleInternationalString("1"));
        assertFalse(periodDuration1.getHours().equals(result));
    }

    /**
     * Test of setMinutes method, of class DefaultPeriodDuration.
     */
    @Test
    public void testSetMinutes() {
        InternationalString result = periodDuration1.getMinutes();
        ((DefaultPeriodDuration) periodDuration1).setMinutes(new SimpleInternationalString("4"));
        assertFalse(periodDuration1.getMinutes().equals(result));
    }

    /**
     * Test of setSeconds method, of class DefaultPeriodDuration.
     */
    @Test
    public void testSetSeconds() {
        InternationalString result = periodDuration1.getSeconds();
        ((DefaultPeriodDuration) periodDuration1).setSeconds(new SimpleInternationalString("3"));
        assertFalse(periodDuration1.getSeconds().equals(result));
    }

    /**
     * Test of getTimeInMillis method, of class DefaultPeriodDuration.
     */
    @Test
    public void testGetTimeInMillis() {
        long result = ((DefaultPeriodDuration) periodDuration1).getTimeInMillis();
        assertFalse(((DefaultPeriodDuration) periodDuration2).getTimeInMillis() == result);
    }

    /**
     * Test of equals method, of class DefaultPeriodDuration.
     */
    @Test
    public void testEquals() {
        assertFalse(periodDuration1.equals(null));
        assertEquals(periodDuration1, periodDuration1);
        assertFalse(periodDuration1.equals(periodDuration2));
    }

    /**
     * Test of hashCode method, of class DefaultPeriodDuration.
     */
    @Test
    public void testHashCode() {
        int result = periodDuration1.hashCode();
        assertFalse(periodDuration2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultPeriodDuration.
     */
    @Test
    public void testToString() {
        String result = periodDuration1.toString();
        assertFalse(periodDuration2.toString().equals(result));
    }
}
