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
package org.geotools.coverage.grid;

import java.awt.image.WritableRaster;
import java.util.Random;
import javax.measure.unit.SI;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link ViewsManager} class.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class ViewsManagerTest extends GridCoverageTestBase {
    /**
     * The random number generator to use in this test suite.
     */
    private static final Random random = new Random(7667138224618831007L);

    /**
     * Tests "Piecewise" operation using a simple transform.
     */
    @Test
    public void testPiecewise() {
        // Initialize...
        final GridCoverageBuilder builder = new GridCoverageBuilder();
        final GridCoverageBuilder.Variable variable = builder.newVariable("Elevation", SI.METER);
        variable.addNodataValue("No data", 0);
        builder.setSampleRange(0, 40000);
        builder.setImageSize(360, 180);
        builder.setBufferedImage(random);
        final WritableRaster raster = builder.getBufferedImage().getRaster();
        raster.setSample(0,0,0,0); // For testing NaN value.
        raster.setSample(1,2,0,0);

        // Sanity check...
        assertEquals(360, raster.getWidth());
        assertEquals(180, raster.getHeight());

        // Tests...
        GridCoverage2D packed = builder.getGridCoverage2D();
        GridCoverage2D geophysics = packed.view(ViewType.GEOPHYSICS);
        if (SHOW) {
            show(geophysics);
        }
        // TODO: complete the tests...
    }

    /**
     * Tests "Piecewise" operation using setting found in IFREMER's Coriolis data.
     */
    @Test
    public void testCoriolis() {
        final double scale  = 0.001;
        final double offset = 20.0;

        // Initialize...
        final GridCoverageBuilder builder = new GridCoverageBuilder();
        final GridCoverageBuilder.Variable variable = builder.newVariable("Temperature", SI.CELSIUS);
        variable.addNodataValue("No data", 32767);
        builder.setSampleRange(-20000, 23000);
        builder.setImageSize(360, 180);
        builder.setBufferedImage(random);
        final WritableRaster raster = builder.getBufferedImage().getRaster();
        raster.setSample(0,0,0,32767); // For testing NaN value.
        raster.setSample(1,2,0,32767);

        // Sanity check...
        assertEquals(360, raster.getWidth());
        assertEquals(180, raster.getHeight());

        // Tests without "sample to geophysics" transform...
        GridCoverage2D packed = builder.getGridCoverage2D();
        GridCoverage2D geophysics = packed.view(ViewType.GEOPHYSICS);
        if (SHOW) {
            show(geophysics);
        }
        variable.setLinearTransform(scale, offset);
        packed = builder.getGridCoverage2D();
    }
}
