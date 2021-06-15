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
 *
 */
package org.geotools.coverageio.gdal.dted;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *
 * Testing {@link DTEDReader}
 *
 *
 *
 * @source $URL$
 */
public final class DTEDTest extends GDALTestCase {

	/**
     * file name of a valid DTED sample data to be used for tests. 
     */
    private final static String fileName = "n43.dt0";

    public DTEDTest() {
		super( "DTED", new DTEDFormatFactory());
	}
    
    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512).setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final File file = TestData.file(this, fileName);
        final BaseGDALGridCoverage2DReader reader = new DTEDReader(file, hints);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);
        forceDataLoading(gc);
        
        // check band names (check they are not all UNKNOWN)
        System.out.println(Arrays.toString(gc.getSampleDimensions()));

        // /////////////////////////////////////////////////////////////////////
        //
        // read again with subsampling and crop
        //
        // /////////////////////////////////////////////////////////////////////
        final double cropFactor = 2.0;
        final int oldW = gc.getRenderedImage().getWidth();
        final int oldH = gc.getRenderedImage().getHeight();
        final Rectangle range = ((GridEnvelope2D)reader.getOriginalGridRange());
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                    oldEnvelope.getLowerCorner().getOrdinate(0)
                    + (oldEnvelope.getSpan(0) / cropFactor),
                    
                oldEnvelope.getLowerCorner().getOrdinate(1)
                    + (oldEnvelope.getSpan(1) / cropFactor)
                },
                new double[] {
                    oldEnvelope.getUpperCorner().getOrdinate(0),
                    oldEnvelope.getUpperCorner().getOrdinate(1)
                });
        cropEnvelope.setCoordinateReferenceSystem(reader.getCrs());

        final ParameterValue gg = (ParameterValue) ((AbstractGridFormat) reader.getFormat()).READ_GRIDGEOMETRY2D
            .createValue();
        gg.setValue(new GridGeometry2D(
                new GridEnvelope2D(
                    new Rectangle(0, 0, (int) (range.width / 2.0 / cropFactor),
                        (int) (range.height / 2.0 / cropFactor))), cropEnvelope));
        gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg });
        forceDataLoading(gc);
    }
    
    @Test
	public void testService() throws NoSuchAuthorityCodeException, FactoryException {
        if (!testingEnabled()) {
            return;
        }

        GridFormatFinder.scanForPlugins();

        Iterator<GridFormatFactorySpi> list = GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = (GridFormatFactorySpi) list.next();

            if (fac instanceof DTEDFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("DTEDFormatFactory not registered", found);
        Assert.assertTrue("DTEDFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new DTEDFormatFactory().createFormat());
    }    
}
