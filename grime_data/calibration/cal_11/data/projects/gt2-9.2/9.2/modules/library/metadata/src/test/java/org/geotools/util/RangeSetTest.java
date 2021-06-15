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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link RangeSet} implementation.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Andrea Aime
 */
public final class RangeSetTest {
    /**
     * Tests {@link RangeSet#remove}.
     */
    @Test
    public void testRangeRemoval() {
        RangeSet<Double> rs = new RangeSet<Double>(Double.class);
        rs.add(10.0, 22.0);
        rs.remove(8.0, 12.0);
        RangeSet<Double> rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 22.0);
        assertEquals("Lower removal:", rs, rsResult);

        rs.remove(20.0, 30.0);
        rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 20.0);
        assertEquals("Upper removal:", rs, rsResult);

        rs.remove(8.0, 10.0);
        assertEquals("Inferior null removal:", rs, rsResult);
        rs.remove(8.0, 12.0);
        assertEquals("Inferior touch removal:", rs, rsResult);

        rs.remove(22.0, 40.0);
        assertEquals("Upper null removal:", rs, rsResult);
        rs.remove(20.0, 40.0);
        assertEquals("Upper touch removal:", rs, rsResult);

        rs.remove(14.0, 16.0);
        rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 14.0);
        rsResult.add(16.0, 20.0);
        assertEquals("Central removal:", rs, rsResult);

        rs.remove(15.0, 15.5);
        assertEquals("Central null removal:", rs, rsResult);

        rs.remove(14.0, 16.0);
        assertEquals("Central touch null removal:", rs, rsResult);

        rs.remove(15.0, 17.0);
        rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 14.0);
        rsResult.add(17.0, 20.0);
        assertEquals("Central right removal:", rs, rsResult);

        rs.remove(13.0, 15.0);
        rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 13.0);
        rsResult.add(17.0, 20.0);
        assertEquals("Central left removal:", rs, rsResult);

        rs.remove(12.5, 18.0);
        rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 12.5);
        rsResult.add(18.0, 20.0);
        assertEquals("Central both removal:", rs, rsResult);

        rs.remove(18.5, 19.0);
        rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 12.5);
        rsResult.add(18.0, 18.5);
        rsResult.add(19.0, 20.0);
        assertEquals("Central removal 2:", rs, rsResult);

        rs.remove(17.0, 19.0);
        rsResult = new RangeSet<Double>(Double.class);
        rsResult.add(12.0, 12.5);
        rsResult.add(19.0, 20.0);
        assertEquals("Central wipeout:", rs, rsResult);

        rs.remove(0.0, 25.0);
        assertEquals("Full wipeout:", 0, rs.size());
    }
}
