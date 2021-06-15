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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import org.opengis.referencing.NoSuchAuthorityCodeException;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link URN_Parser} class.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class URN_ParserTest {

    /**
     * Parses a valid URN.
     */
    @Test
    public void testParse() throws NoSuchAuthorityCodeException {
        final URN_Parser parser = URN_Parser.buildParser("urn:ogc:def:CRS:EPSG:6.11.2:4326");
        assertEquals("crs",       parser.type.name);
        assertEquals("EPSG",      parser.authority);
        assertEquals("6.11.2",    parser.version.toString());
        assertEquals("4326",      parser.code);
        assertEquals("EPSG:4326", parser.getAuthorityCode());
    }

    /**
     * Parses a valid URN without version.
     */
    @Test
    public void testParseWithoutVersion() throws NoSuchAuthorityCodeException {
        final URN_Parser parser = URN_Parser.buildParser("urn:ogc:def:CRS:EPSG:4326");
        assertEquals("crs",       parser.type.name);
        assertEquals("EPSG",      parser.authority);
        assertNull  (             parser.version);
        assertEquals("4326",      parser.code);
        assertEquals("EPSG:4326", parser.getAuthorityCode());
    }

    /**
     * Parses an invalid URN.
     */
    @Test
    public void testInvalidParse() {
        final String urn = "urn:ogcx:def:CRS:EPSG:6.8:4326";
        try {
            URN_Parser.buildParser(urn);
            fail();
        } catch (NoSuchAuthorityCodeException e) {
            // This is the expected exception.
            assertEquals(urn, e.getAuthorityCode());
        }
    }

    /**
     * Parses a URN with an unknow type.
     */
    @Test
    public void testInvalidType() {
        final String urn = "urn:ogc:def:dummy:EPSG:6.8:4326";
        try {
            URN_Parser.buildParser(urn);
            fail();
        } catch (NoSuchAuthorityCodeException e) {
            // This is the expected exception.
            assertEquals("dummy", e.getAuthorityCode());
        }
    }
}
