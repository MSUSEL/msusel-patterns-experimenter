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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.Instant;
import static org.junit.Assert.*;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class DefaultInstantTest {

    private Instant instant1;
    private Instant instant2;
    private Position position1;
    private Position position2;
    private Calendar cal = Calendar.getInstance();

    @Before
    public void setUp() {

        cal.set(2000, 1, 1);
        position1 = new DefaultPosition(cal.getTime());
        cal.set(1998, 1, 1);
        position2 = new DefaultPosition(cal.getTime());
        instant1 = new DefaultInstant(position1);
        instant2 = new DefaultInstant(position2);
    }

    @After
    public void tearDown() {
        instant1 = null;
        instant2 = null;
        position1 = null;
        position2 = null;
    }

    /**
     * Test of getPosition method, of class DefaultInstant.
     */
    @Test
    public void testGetPosition() {
        Position result = instant1.getPosition();
        assertFalse(instant2.getPosition().equals(result));
    }

    /**
     * Test of getBegunBy method, of class DefaultInstant.
     */
    @Test
    public void testGetBegunBy() {
        Collection<Period> result = instant1.getBegunBy();
        assertEquals(instant2.getBegunBy(), result);
    }

    /**
     * Test of getEndedBy method, of class DefaultInstant.
     */
    @Test
    public void testGetEndedBy() {
        Collection<Period> result = instant1.getEndedBy();
        assertEquals(instant2.getEndedBy(), result);
    }

    /**
     * Test of setPosition method, of class DefaultInstant.
     */
    @Test
    public void testSetPosition() {
        Position result = instant1.getPosition();
        Position position = new DefaultPosition(new Date());
        ((DefaultInstant) instant1).setPosition(position);
        assertFalse(instant1.getPosition().equals(result));
    }

    /**
     * Test of setBegunBy method, of class DefaultInstant.
     */
    @Test
    public void testSetBegunBy() {
        Collection<Period> result = instant1.getBegunBy();
        Collection<Period> begunby = null;
        ((DefaultInstant) instant1).setBegunBy(begunby);
        assertEquals(instant1.getBegunBy(), result);
    }
    
    
    /**
     * Test comparison of Instants
     */
    @Test
    public void testCompare() {
       assertEquals(1,((DefaultInstant)instant1).compareTo(instant2));
       assertEquals(0,((DefaultInstant)instant1).compareTo(instant1));
       assertEquals(0,((DefaultInstant)instant2).compareTo(instant2));
    }

    /**
     * Test of setEndBy method, of class DefaultInstant.
     */
    @Test
    public void testSetEndBy() {
        Collection<Period> result = instant1.getEndedBy();
        Collection<Period> endedby = null;
        ((DefaultInstant) instant1).setEndBy(endedby);
        assertEquals(instant1.getEndedBy(), result);
    }

    /**
     * Test of equals method, of class DefaultInstant.
     */
    @Test
    public void testEquals() {
        cal.set(2000, 1, 1);

        assertFalse(instant1.equals(null));
        assertEquals(cal.getTime().getTime(), instant1.getPosition().getDate().getTime());
        assertFalse(instant1.equals(instant2));
    }

    /**
     * Test of hashCode method, of class DefaultInstant.
     */
    @Test
    public void testHashCode() {
        int result = instant1.hashCode();
        assertFalse(instant2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultInstant.
     */
    @Test
    public void testToString() {
        String result = instant1.toString();
        assertFalse(instant2.toString().equals(result));
    }
}
