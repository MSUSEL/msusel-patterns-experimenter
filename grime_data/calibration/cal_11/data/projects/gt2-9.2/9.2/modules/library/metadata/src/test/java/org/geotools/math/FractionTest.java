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
package org.geotools.math;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link Fraction} class.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 */
public final class FractionTest {
    /**
     * Tests the {@link Fraction#floor} method.
     */
    @Test
    public void testFloor() {
        final int[] numerators   = { 0,  1,  2,  3,  9, 10, 11, 12};
        final int[] denominators = { 3,  3,  3,  3,  3,  3,  3,  3};
        final int[] positives    = { 0,  0,  0,  1,  3,  3,  3,  4};
        final int[] negatives    = {-0, -1, -1, -1, -3, -4, -4, -4};
        for (int i=0; i<numerators.length; i++) {
            for (int s=0; s<4; s++) {
                int numerator   = numerators  [i];
                int denominator = denominators[i];
                if ((s & 1) != 0) numerator   = -numerator;
                if ((s & 2) != 0) denominator = -denominator;
                final int[] expected = (numerator * denominator >= 0) ? positives : negatives;
                final String label = "floor(" + numerator + '/' + denominator + ')';
                assertEquals(label, expected[i], Fraction.floor(numerator, denominator));
            }
        }
    }

    /**
     * Tests the {@link Fraction#ceil} method.
     */
    @Test
    public void testCeil() {
        final int[] numerators   = { 0,  1,  2,  3,  9, 10, 11, 12};
        final int[] denominators = { 3,  3,  3,  3,  3,  3,  3,  3};
        final int[] positives    = { 0,  1,  1,  1,  3,  4,  4,  4};
        final int[] negatives    = {-0, -0, -0, -1, -3, -3, -3, -4};
        for (int i=0; i<numerators.length; i++) {
            for (int s=0; s<4; s++) {
                int numerator   = numerators  [i];
                int denominator = denominators[i];
                if ((s & 1) != 0) numerator   = -numerator;
                if ((s & 2) != 0) denominator = -denominator;
                final int[] expected = (numerator * denominator >= 0) ? positives : negatives;
                final String label = "ceil(" + numerator + '/' + denominator + ')';
                assertEquals(label, expected[i], Fraction.ceil(numerator, denominator));
            }
        }
    }

    /**
     * Tests the {@link Fraction#round} method.
     */
    @Test
    public void testRoundFraction() {
        final int[] numerators   = { 0,  1,  2,  3,  9, 10, 11, 12, 12, 13, 14, 15, 16, 17, 18, 19};
        final int[] denominators = { 3,  3,  3,  3,  3,  3,  3,  3,  4,  4,  4,  4,  4,  4,  4,  4};
        final int[] results      = { 0,  0,  1,  1,  3,  3,  4,  4,  3,  3,  4,  4,  4,  4,  4,  5};
        for (int i=10; i<numerators.length; i++) {
            for (int s=0; s<4; s++) {
                int numerator   = numerators  [i];
                int denominator = denominators[i];
                int expected    = results     [i];
                if ((s & 1) != 0) numerator   = -numerator;
                if ((s & 2) != 0) denominator = -denominator;
                if (numerator * denominator < 0) expected = -expected;
                final String label = "even(" + numerator + '/' + denominator + ')';
                assertEquals(label, expected, Fraction.round(numerator, denominator));
            }
        }
    }

    /**
     * Tests fraction simplification.
     */
    @Test
    public void testSimplify() {
        Fraction fraction = new Fraction(4, 7);
        assertEquals(4, fraction.numerator());
        assertEquals(7, fraction.denominator());
        fraction.set(4, 8);
        assertEquals(1, fraction.numerator());
        assertEquals(2, fraction.denominator());
        fraction.set(17*21, 31*21);
        assertEquals(17, fraction.numerator());
        assertEquals(31, fraction.denominator());
    }
}
