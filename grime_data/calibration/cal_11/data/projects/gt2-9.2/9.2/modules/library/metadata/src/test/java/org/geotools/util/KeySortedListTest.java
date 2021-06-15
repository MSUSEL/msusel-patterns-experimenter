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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link KeySortedList}.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class KeySortedListTest {
    /**
     * Inserts random floating point numbers into the list. The key is the integer part of the
     * floating point number. This means that the number should be sorted in such a way that
     * their integer part are in increasing order, while the fractional part remains in random
     * order.
     */
    @Test
    public void testAdd() {
        final Random random = new Random(6969483179756527012L);
        final KeySortedList<Integer,Double> list = new KeySortedList<Integer,Double>();
        final Collection<Double> check = new ArrayList<Double>();
        final int maxElements = 1000;
        for (int i=0; i<maxElements; i++) {
            final double  x     = random.nextDouble() * (maxElements/10);
            final Integer key   = (int) x;
            final Double  value = x;
            list.add(key, value);
            check.add(value);
        }
        /*
         * Checks the content.
         */
        assertEquals(maxElements, check.size());
        assertEquals(maxElements, list .size());
        assertEquals(new HashSet<Double>(check), new HashSet<Double>(list));
        /*
         * Checks the iteration.
         */
        int count=0, lastKey=0;
        for (final ListIterator<Double> it=list.listIterator(); it.hasNext(); count++) {
            assertEquals(count, it.nextIndex());
            final Double element = it.next();
            assertEquals(count, it.previousIndex());
            final double value = element.doubleValue();
            final int    key   = (int) value;
            assertTrue(key >= lastKey);
            lastKey = key;
            assertSame(element, list.get(count));
        }
        assertEquals(maxElements, count);
        /*
         * Checks the iteration from a middle point.
         */
        final Integer midKey = (maxElements / 10) / 2;
        final KeySortedList<Integer,Double> head = list.headList(midKey);
        final KeySortedList<Integer,Double> tail = list.tailList(midKey);
        final Collection<Double> rebuild = new ArrayList<Double>(head);
        rebuild.addAll(tail);
        assertEquals(list.size(), head.size() + tail.size());
        assertEquals(list, rebuild);
        assertSame(list.listIterator(midKey).next(), tail.listIterator().next());
    }
}
