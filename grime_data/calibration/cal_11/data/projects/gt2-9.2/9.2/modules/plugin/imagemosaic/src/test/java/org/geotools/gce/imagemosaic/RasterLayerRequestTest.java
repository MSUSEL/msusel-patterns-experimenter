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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

public class RasterLayerRequestTest {

    @Test
    public void testResolutions() throws Exception {
        // get some test data
        final File testMosaic = TestData.file(this, "/overview/0");
        assertTrue(testMosaic.exists());

        // build the objects we need to get to build a raster layer request
        final ImageMosaicReader reader = (ImageMosaicReader) new ImageMosaicFormat().getReader(
                testMosaic, null);
        final RasterManager manager = new RasterManager(reader);

        GeneralEnvelope oe = reader.getOriginalEnvelope();
        System.out.println(oe);
        double offset = oe.getSpan(0) * 0.9;
        GeneralEnvelope reNative = new GeneralEnvelope(oe);
        reNative.setRange(0, oe.getMinimum(0) - offset, oe.getMaximum(0) - offset);
        Envelope reTransformed = CRS.transform(reNative, CRS.decode("EPSG:3857", true));
        // System.out.println(reader.getOriginalGridRange());
        // the raster has bands like this, let's ask for the coarser grained overview
        // Band 1 Block=140x58 Type=Byte, ColorInterp=Gray
        // Overviews: 70x94, 35x47
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 35, 47), reTransformed);

        ParameterValue<GridGeometry2D> ggParam = AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        ggParam.setValue(gg);

        // Creating a request
        final RasterLayerRequest request = new RasterLayerRequest(
                new GeneralParameterValue[] { ggParam }, manager);
        double[] rr = request.getRequestedResolution();
        // System.out.println(Arrays.toString(rr));
        double resolution = Math.min(rr[0], rr[1]);
        // System.out.println(resolution);
        // native resolution is ~16, overviews are at 32 and 64, the request really needs 64
        assertTrue(resolution > 60);
    }
}
