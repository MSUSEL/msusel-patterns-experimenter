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
package org.geotools.coverage.grid;

import java.io.IOException;
import java.net.InetAddress;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link GridCoverage2D} implementation.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class GridCoverageTest extends GridCoverageTestBase {
    
    /** Used to avoid errors if building on a system where hostname is not defined */
    private boolean hostnameDefined;
    
    @Before
    public void setup() {
        try {
            InetAddress.getLocalHost();
            hostnameDefined = true;
        } catch (Exception ex) {
            hostnameDefined = false;
        }
    }

    /**
     * Tests a grid coverage filled with random values.
     */
    @Test
    public void testRandomCoverage() {
        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        final GridCoverage2D coverage = getRandomCoverage(crs);
        assertRasterEquals(coverage, coverage); // Actually a test of assertEqualRasters(...).
        assertSame(coverage.getRenderedImage(), coverage.getRenderableImage(0,1).createDefaultRendering());
        /*
         * Tests the creation of a "geophysics" view. This test make sure that the
         * 'geophysics' method do not creates more grid coverage than needed.
         */
        GridCoverage2D geophysics= coverage.view(ViewType.GEOPHYSICS);
        assertSame(coverage,       coverage.view(ViewType.PACKED));
        assertSame(coverage,     geophysics.view(ViewType.PACKED));
        assertSame(geophysics,   geophysics.view(ViewType.GEOPHYSICS));
        assertFalse( coverage.equals(geophysics));
        assertFalse( coverage.getSampleDimension(0).getSampleToGeophysics().isIdentity());
        assertTrue(geophysics.getSampleDimension(0).getSampleToGeophysics().isIdentity());
    }

    /**
     * Tests the serialization of a grid coverage.
     *
     * @throws IOException if an I/O operation was needed and failed.
     * @throws ClassNotFoundException Should never happen.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        if (hostnameDefined) {
            GridCoverage2D coverage = EXAMPLES.get(0);
            GridCoverage2D serial = serialize(coverage);
            assertNotSame(coverage, serial);
            assertEquals(GridCoverage2D.class, serial.getClass());
            // Compares the geophysics view for working around the
            // conversions of NaN values which may be the expected ones.
            coverage = coverage.view(ViewType.GEOPHYSICS);
            serial = serial.view(ViewType.GEOPHYSICS);
            assertRasterEquals(coverage, serial);
        }
    }

}
