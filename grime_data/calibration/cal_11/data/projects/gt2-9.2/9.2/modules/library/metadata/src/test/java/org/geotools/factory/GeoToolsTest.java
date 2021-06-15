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
package org.geotools.factory;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link GeoTools}.
 *
 * @since 2.4
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public final class GeoToolsTest {
    /**
     * Makes sures that J2SE 1.4 assertions are enabled.
     */
    @Test
    public void testAssertionEnabled() {
        assertTrue("Assertions not enabled.", GeoToolsTest.class.desiredAssertionStatus());
    }

    /**
     * Tests the removal of keys from a hashmap. Required for {@link FactoryRegistry} working.
     */
    @Test
    public void testHintsKey() {
        final Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        assertFalse(hints.isEmpty());

        Map<RenderingHints.Key,Object> map = new HashMap<RenderingHints.Key,Object>();
        assertNull(map.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        map = Collections.unmodifiableMap(map);
        assertFalse(map.isEmpty());

        final Hints remaining = new Hints(hints);
        assertTrue(remaining.keySet().removeAll(map.keySet()));
        assertTrue(remaining.isEmpty());
    }

    /**
     * Tests addition of custom hints.
     */
    @Test
    public void testMyHints(){
        Hints hints = GeoTools.getDefaultHints();
        assertTrue(hints.isEmpty());
        assertNull(Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        try {
            hints = GeoTools.getDefaultHints();
            assertNotNull(hints);
            assertFalse(hints.isEmpty());
            assertEquals(1, hints.size());
            final Object value = hints.get(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            assertTrue(value instanceof Boolean);
            assertFalse(((Boolean) value).booleanValue());
            /*
             * Tests the toString() method.
             */
            String text = hints.toString().trim();
            assertTrue(text.matches("Hints:\\s+FORCE_LONGITUDE_FIRST_AXIS_ORDER = false"));

            assertEquals(hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE), Boolean.FALSE);
            text = hints.toString().trim();
            assertTrue(text.matches("Hints:\\s+FORCE_LONGITUDE_FIRST_AXIS_ORDER = true"));

            assertEquals(hints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER), Boolean.TRUE);
            text = hints.toString().trim();
            assertTrue(text.matches("Hints:\\s+System defaults:\\s+FORCE_LONGITUDE_FIRST_AXIS_ORDER = false"));
        } finally {
            assertNotNull(Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        }
        assertTrue(GeoTools.getDefaultHints().isEmpty());
    }

    /**
     * Tests the use of system properties.
     */
    @Test
    public void testSystemHints() {
        Hints hints = GeoTools.getDefaultHints();
        assertNotNull(hints);
        assertTrue(hints.isEmpty());
        System.setProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true");
        Hints.scanSystemProperties();
        try {
            hints = GeoTools.getDefaultHints();
            assertNotNull(hints);
            assertFalse(hints.isEmpty());
            assertEquals(1, hints.size());
            final Object value = hints.get(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            assertTrue(value instanceof Boolean);
            assertTrue(((Boolean) value).booleanValue());
        } finally {
            System.clearProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            assertNotNull(Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        }
        hints = GeoTools.getDefaultHints();
        assertNotNull(hints);
        assertTrue(hints.isEmpty());
    }

    /**
     * Tests {@link GeoTools#fixName} using simpliest name or no context.
     * We avoid the tests that would require a real initial context.
     */
    @Test
    public void testFixName() {
        assertNull  (GeoTools.fixName(null));
        assertEquals("simpleName", GeoTools.fixName("simpleName"));
        assertEquals("jdbc:EPSG",  GeoTools.fixName(null, "jdbc:EPSG"));
        assertEquals("jdbc/EPSG",  GeoTools.fixName(null, "jdbc/EPSG"));
    }
}
