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
package org.geotools.coverage.processing;

import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;

import org.opengis.parameter.ParameterValueGroup;

import org.geotools.factory.Hints;
import org.geotools.coverage.grid.Viewer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.resources.coverage.CoverageUtilities;
import static org.geotools.coverage.grid.ViewType.*;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the "filtered subsample" operation.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 *
 * @since 2.3
 */
public final class FilteredSubsampleTest extends GridProcessingTestBase {
    /**
     * The processors to be used for all tests.
     */
    private CoverageProcessor processor;

    /**
     * Set up common objects used for all tests.
     */
    @Before
    public void setUp() {
        processor = CoverageProcessor.getInstance();
    }

    /**
     * Tests the "SubsampleAverage" operation.
     */
    @Test
    public void testSubsampleAverage() {
        final GridCoverage2D originallyIndexedCoverage       = EXAMPLES.get(0);
        final GridCoverage2D indexedCoverage                 = EXAMPLES.get(2);
        final GridCoverage2D indexedCoverageWithTransparency = EXAMPLES.get(3);
        final GridCoverage2D floatCoverage                   = EXAMPLES.get(4);

        // On this one the Subsample average should do an RGB expansion
        float[] filter = new float[] {1};
        Interpolation interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        filteredSubsample(indexedCoverage.view(GEOPHYSICS), interp, filter);

        // On this one the Subsample average should do an RGB expansion preserving alpha.
        filteredSubsample(indexedCoverageWithTransparency.view(GEOPHYSICS), interp, filter);

        // On this one the subsample average should go back to the geophysic
        // view before being applied.
        filteredSubsample(originallyIndexedCoverage.view(PACKED), interp, filter);

        // On this one the Subsample average should do an RGB expansion.
        interp = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        filteredSubsample(indexedCoverage.view(GEOPHYSICS), interp, filter);

        // On this one the Subsample average should do an RGB expansion preserving alpha.
        filteredSubsample(indexedCoverageWithTransparency.view(GEOPHYSICS), interp, filter);

        // On this one the subsample average should go back to the geophysics
        // view before being applied.
        filteredSubsample(originallyIndexedCoverage.view(PACKED), interp, filter);

        // On this one the Subsample average should do an RGB expansion.
        filter = new float[] { 0.5F,  1.0F /  3.0F,
                               0.0F, -1.0F / 12.0F };
        interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        filteredSubsample(indexedCoverage.view(GEOPHYSICS), interp, filter);

        // On this one the Subsample average should do an RGB expansion
        // preserving alpha.
        filteredSubsample(indexedCoverageWithTransparency.view(GEOPHYSICS), interp, filter);

        // On this one the subsample average should go back to the geophysiscs
        // view before being applied.
        filteredSubsample(originallyIndexedCoverage.view(PACKED), interp, filter);

        // On this one the Subsample average should do an RGB expansion.
        interp = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        filteredSubsample(indexedCoverage.view(GEOPHYSICS), interp, filter);

        // On this one the Subsample average should do an RGB expansion
        // preserving alpha.
        filteredSubsample(indexedCoverageWithTransparency.view(GEOPHYSICS), interp, filter);

        // On this one the subsample average should go back to the geophysiscs
        // view before being applied.
        filteredSubsample(originallyIndexedCoverage.view(PACKED), interp, filter);

        // On this one the subsample average should go back to the
        // geophysics view before being applied.
        filteredSubsample(floatCoverage.view(PACKED), interp, new float[]{1},
                new Hints(Hints.COVERAGE_PROCESSING_VIEW, PHOTOGRAPHIC));

        // Play with a rotated coverage.
        filteredSubsample(rotate(floatCoverage.view(GEOPHYSICS), Math.PI/4), interp, filter);
    }

    /**
     * Tests the "FilteredSubsamble" operation.
     *
     * @param coverage The coverage on which to apply the operation.
     * @param interp   The interpolation to use.
     * @param filter   The filter to be used.
     */
    private void filteredSubsample(final GridCoverage2D coverage, final Interpolation interp,
                                   final float[] filter)
    {
        filteredSubsample(coverage, interp, filter, null);
    }

    /**
     * Tests the "FilteredSubsamble" operation using the given hints.
     *
     * @param coverage The coverage on which to apply the operation.
     * @param interp   The interpolation to use.
     * @param filter   The filter to be used.
     * @param hints    An optional set of hints, or {@code null} if none.
     */
    private void filteredSubsample(final GridCoverage2D coverage, final Interpolation interp,
                                   final float[] filter, final Hints hints)
    {
        // Caching initial properties.
        RenderedImage originalImage = coverage.getRenderedImage();
        boolean isIndexed = originalImage.getColorModel() instanceof IndexColorModel;
        int w = originalImage.getWidth();
        int h = originalImage.getHeight();

        // Creating a default processor.
        final CoverageProcessor processor = (hints != null) ? CoverageProcessor.getInstance(hints) : this.processor;

        // Getting parameters for the FilteredSubsample operation.
        final ParameterValueGroup param = processor.getOperation("FilteredSubsample").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("scaleX").setValue(Integer.valueOf(2));
        param.parameter("scaleY").setValue(Integer.valueOf(2));
        param.parameter("qsFilterArray").setValue(filter);
        param.parameter("Interpolation").setValue(interp);

        // Scale a first time by 2.
        GridCoverage2D scaled = (GridCoverage2D) processor.doOperation(param);
        RenderedImage scaledImage = scaled.getRenderedImage();
        assertEquals(w / 2.0, scaledImage.getWidth(),  EPS);
        assertEquals(h / 2.0, scaledImage.getHeight(), EPS);
        assertTrue(!isIndexed
                || (interp instanceof InterpolationNearest)
                || !(scaledImage.getColorModel() instanceof IndexColorModel)
                || CoverageUtilities.preferredViewForOperation(coverage, interp, false, null) == GEOPHYSICS);

        isIndexed = scaledImage.getColorModel() instanceof IndexColorModel;
        w = scaledImage.getWidth();
        h = scaledImage.getHeight();
        assertEnvelopeEquals(coverage, scaled);
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(coverage.getRenderedImage().getData());
            assertNotNull(scaled.getRenderedImage().getData());
        }

        // Scale a second time by 3.
        scaled = (GridCoverage2D) Operations.DEFAULT.filteredSubsample(scaled, 3, 3, filter, interp);
        scaledImage = scaled.getRenderedImage();
        assertEquals(w / 3.0, scaledImage.getWidth(),  1.0/3 + EPS);
        assertEquals(h / 3.0, scaledImage.getHeight(), 1.0/3 + EPS);
        assertTrue(!isIndexed
                || (interp instanceof InterpolationNearest)
                || !(scaledImage.getColorModel() instanceof IndexColorModel)
                ||CoverageUtilities.preferredViewForOperation(coverage, interp, false, null) == GEOPHYSICS);
        assertEnvelopeEquals(coverage, scaled);
        if (SHOW) {
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(scaledImage.getData());
        }
    }
}
