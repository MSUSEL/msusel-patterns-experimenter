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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import java.util.Random;
import org.opengis.referencing.operation.TransformException;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link Category} implementation.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class CategoryTest {
    /**
     * Random number generator for this test.
     */
    private static final Random random = new Random(9119969932919929834L);

    /**
     * Checks if a {@link Comparable} is a number identical to the supplied integer value.
     */
    private static void assertValueEquals(String message, Comparable<?> number, int expected) {
        assertTrue("Integer.class", number instanceof Integer);
        assertEquals(message, expected, ((Number) number).intValue());
    }

    /**
     * Checks if a {@link Comparable} is a number identical to the supplied float value.
     */
    private static void assertValueEquals(String message, Comparable<?> number, double expected, double EPS) {
        assertTrue("Double.class", number instanceof Double);
        final double actual = ((Number) number).doubleValue();
        if (Double.isNaN(expected)) {
            assertEquals(message, toHexString(expected), toHexString(actual));
        } else {
            assertEquals(message, expected, actual, EPS);
        }
    }

    /**
     * Returns the specified value as an hexadecimal string. Usefull
     * for comparing NaN values.
     */
    private static String toHexString(final double value) {
        return Integer.toHexString(Float.floatToRawIntBits((float)value));
    }

    /**
     * Make sure that qualitative category produce the expected result.
     *
     * @throws TransformException If an error occured while transforming a value.
     */
    @Test
    public void testQualitativeCategory() throws TransformException {
        for (int pass=0; pass<100; pass++) {
            final int      sample    = random.nextInt(64);
            final Category category1 = new Category("Auto", null, sample);
            final Category category2 = new Category(category1.getName(),
                                                    category1.getColors(),
                                                    category1.getRange(),
                                                    category1.getSampleToGeophysics());

            assertEquals("<init>", category1, category2);
            assertValueEquals("lower",  category1.geophysics(false).getRange().getMinValue().intValue(), sample);
            assertValueEquals("upper",  category1.geophysics(false).getRange().getMaxValue().intValue(), sample);

            assertNull("geophysics(false)", category1.geophysics(false).getSampleToGeophysics());
            assertNull("geophysics(true)",  category1.geophysics(true ).getSampleToGeophysics());
            for (int i=0; i<200; i++) {
                final double x  = 100*random.nextDouble();
                final double y1 = category1.transform.transform(x);
                final double y2 = category2.transform.transform(x);
                assertTrue("toGeophysics(1)", Double.isNaN(y1));
                assertTrue("toGeophysics(2)", Double.isNaN(y2));
                assertEquals("NaN", Double.doubleToRawLongBits(y1), Double.doubleToRawLongBits(y2));
                assertEquals("toSample(1)", sample, category1.inverse.transform.transform(y1), 0);
                assertEquals("toSample(2)", sample, category2.inverse.transform.transform(y2), 0);
            }
        }
    }

    /**
     * Make sure that linear category produce the expected result.
     * This test check also if the default {@link MathTransform1D}
     * for a linear relation is right.
     *
     * @throws TransformException If an error occured while transforming a value.
     */
    @Test
    public void testLinearCategory() throws TransformException {
        final double EPS = 1E-9;
        for (int pass=0; pass<100; pass++) {
            final int     lower = random.nextInt(64);
            final int     upper = random.nextInt(128) + lower+1;
            final double  scale = 10*random.nextDouble() + 0.1; // Must be positive for this test.
            final double offset = 10*random.nextDouble() - 5.0;
            final Category category = new Category("Auto", null, lower, upper, scale, offset);

            assertValueEquals("lower",  category.geophysics(false).getRange().getMinValue().intValue(), lower);
            assertValueEquals("upper",  category.geophysics(false).getRange().getMaxValue().intValue(), upper);
            assertValueEquals("minimum", category.geophysics(true).getRange().getMinValue().doubleValue(), lower*scale+offset, EPS);
            assertValueEquals("maximum", category.geophysics(true).getRange().getMaxValue().doubleValue(), upper*scale+offset, EPS);

            for (int i=0; i<200; i++) {
                final double x = 100*random.nextDouble();
                final double y = x*scale + offset;
                assertEquals("toGeophysics", y,     category.transform.transform(x), EPS);
                assertEquals("toSample", x, category.inverse.transform.transform(y), EPS);
            }
        }
    }
}
