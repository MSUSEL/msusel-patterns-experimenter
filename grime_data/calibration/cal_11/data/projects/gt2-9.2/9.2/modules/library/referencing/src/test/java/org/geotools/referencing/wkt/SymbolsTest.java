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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.wkt;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link Symbols} implementation.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class SymbolsTest {
    /**
     * Tests the {@link Symbols#containsAxis} method.
     */
    @Test
    public void testContainsAxis() {
        final Symbols s = Symbols.DEFAULT;
        assertTrue("AXIS at the begining of a line.",
                s.containsAxis("AXIS[\"Long\", EAST]"));
        assertTrue("AXIS embeded in GEOGCS.",
                s.containsAxis("GEOGCS[\"WGS84\", AXIS[\"Long\", EAST]]"));
        assertTrue("AXIS followed by spaces and different opening brace.",
                s.containsAxis("GEOGCS[\"WGS84\", AXIS (\"Long\", EAST)]"));
        assertTrue("AXIS in mixed cases.",
                s.containsAxis("GEOGCS[\"WGS84\", aXis[\"Long\", EAST]]"));
        assertFalse("AXIS in quoted text.",
                s.containsAxis("GEOGCS[\"AXIS\"]"));
        assertFalse("AXIS without opening bracket.",
                s.containsAxis("GEOGCS[\"WGS84\", AXIS]"));
        assertFalse("No AXIS.",
                s.containsAxis("GEOGCS[\"WGS84\"]"));
    }
}
