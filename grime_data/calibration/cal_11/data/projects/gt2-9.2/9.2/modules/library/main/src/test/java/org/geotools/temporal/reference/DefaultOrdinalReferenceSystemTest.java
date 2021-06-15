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
package org.geotools.temporal.reference;

import java.util.Collection;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opengis.temporal.OrdinalEra;
import org.opengis.temporal.OrdinalReferenceSystem;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class DefaultOrdinalReferenceSystemTest {

    private OrdinalReferenceSystem ordinalReferenceSystem1;
    private OrdinalReferenceSystem ordinalReferenceSystem2;

    @Before
    public void setUp() {
        NamedIdentifier name1 = new NamedIdentifier(Citations.CRS, "Ordinal1");
        NamedIdentifier name2 = new NamedIdentifier(Citations.CRS, "Ordinal2");
        ordinalReferenceSystem1 = new DefaultOrdinalReferenceSystem(name1, null, null);
        ordinalReferenceSystem2 = new DefaultOrdinalReferenceSystem(name2, null, null);
    }

    @After
    public void tearDown() {
        ordinalReferenceSystem1 = null;
        ordinalReferenceSystem2 = null;
    }

    /**
     * Test of getOrdinalEraSequence method, of class DefaultOrdinalReferenceSystem.
     */
    @Test
    public void testGetOrdinalEraSequence() {
        Collection<OrdinalEra> result = ordinalReferenceSystem1.getOrdinalEraSequence();
        assertEquals(ordinalReferenceSystem2.getOrdinalEraSequence(), result);
    }

    /**
     * Test of toWKT method, of class DefaultOrdinalReferenceSystem.
     */
    @Test
    public void testToWKT() {
        //@TODO this method is not implemented yet!
    }

    /**
     * Test of equals method, of class DefaultOrdinalReferenceSystem.
     */
    @Test
    public void testEquals() {
        assertFalse(ordinalReferenceSystem1.equals(null));
        assertEquals(ordinalReferenceSystem1, ordinalReferenceSystem1);
        assertFalse(ordinalReferenceSystem1.equals(ordinalReferenceSystem2));
    }

    /**
     * Test of hashCode method, of class DefaultOrdinalReferenceSystem.
     */
    @Test
    public void testHashCode() {
        int result = ordinalReferenceSystem1.hashCode();
        assertFalse(ordinalReferenceSystem2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultOrdinalReferenceSystem.
     */
    @Test
    public void testToString() {
        String result = ordinalReferenceSystem1.toString();
        assertFalse(ordinalReferenceSystem2.toString().equals(result));
    }
}
