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
package org.geotools.metadata;

import java.util.Collection;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.quality.PositionalAccuracyImpl;
import org.opengis.metadata.quality.ConformanceResult;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link Citations} and related constants.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class CitationsTest {
    /**
     * Tests the {@link AbstractMetadata#toString()} method first, since debugging
     * will relying a lot on this method for the remaining of the test suite.
     */
    @Test
    public void testToString() {
        final String text = Citations.EPSG.toString();
        /*
         * Reminder: (?s) allows .* to skip new line characters.
         *           (?m) enable the multi-lines mode for ^ and $.
         *           ^ and $ match the begining and end of a line respectively.
         */
        assertTrue(text.matches("(?s)(?m).*^\\s+Identifiers:\\s+Code:\\s+EPSG$.*"));
        assertTrue(text.matches("(?s)(?m).*^\\s+Linkage:\\s+http://www.epsg.org$.*"));
    }

    /**
     * Makes sure that {@link Citations} constants are immutables.
     */
    @Test
    public void testCitation() {
        assertEquals ("Identity comparaison", Citations.EPSG, Citations.EPSG);
        assertNotSame(Citations.EPSG, Citations.OGC);
        assertTrue(Citations.EPSG instanceof CitationImpl);
        try {
            ((CitationImpl) Citations.EPSG).setISBN("Dummy");
            fail("Pre-defined metadata should be unmodifiable.");
        } catch (UnmodifiableMetadataException e) {
            // This is the expected exception.
        }
        try {
            Citations.EPSG.getIdentifiers().add(null);
            fail("Pre-defined metadata should be unmodifiable.");
        } catch (UnsupportedOperationException e) {
            // This is the expected exception.
        }
    }

    /**
     * Tests {@link PositionalAccuracyImpl} constants.
     */
    @Test
    public void testPositionalAccuracy() {
        assertEquals("Identity comparaison",
                     PositionalAccuracyImpl.DATUM_SHIFT_APPLIED,
                     PositionalAccuracyImpl.DATUM_SHIFT_APPLIED);

        assertEquals("Identity comparaison",
                     PositionalAccuracyImpl.DATUM_SHIFT_OMITTED,
                     PositionalAccuracyImpl.DATUM_SHIFT_OMITTED);

        assertNotSame(PositionalAccuracyImpl.DATUM_SHIFT_APPLIED,
                      PositionalAccuracyImpl.DATUM_SHIFT_OMITTED);

        final Collection appliedResults = PositionalAccuracyImpl.DATUM_SHIFT_APPLIED.getResults();
        final Collection omittedResults = PositionalAccuracyImpl.DATUM_SHIFT_OMITTED.getResults();
        final ConformanceResult applied = (ConformanceResult) appliedResults.iterator().next();
        final ConformanceResult omitted = (ConformanceResult) omittedResults.iterator().next();
        assertNotSame(applied, omitted);
        assertTrue (applied.pass());
        assertFalse(omitted.pass());
        assertFalse(applied.equals(omitted));
        assertFalse(appliedResults.equals(omittedResults));
        assertFalse(PositionalAccuracyImpl.DATUM_SHIFT_APPLIED.equals(
                    PositionalAccuracyImpl.DATUM_SHIFT_OMITTED));
    }
}
