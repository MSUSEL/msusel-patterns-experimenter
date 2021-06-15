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
package org.geotools.coverageio.gdal.jp2kak;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
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
 * Testing {@link JP2KReader}
 *
 *
 *
 * @source $URL$
 */
public final class JP2KTest extends GDALTestCase {
    protected final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
    "org.geotools.coverageio.gdal.jp2kak");
    
    /**
     * file name of a valid JP2K sample data to be used for tests.
     */
    private final static String fileName = "sample.jp2";

    /**
     * Creates a new instance of JP2KTest
     *
     * @param name
     */
    public JP2KTest() {
        super("JP2K", new JP2KFormatFactory());
    }


    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        // read in the grid coverage
        if (fileName.equalsIgnoreCase("")) {
            LOGGER.info("===================================================================\n"
                + " Warning! No valid test File has been yet specified.\n"
                + " Please provide a valid sample in the source code and repeat this test!\n"
                + "========================================================================");

            return;
        }
        File file = null;
        try{
            file = TestData.file(this, fileName);
        }catch (FileNotFoundException fnfe){
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        }

        final BaseGDALGridCoverage2DReader reader = new JP2KReader(file);
        final ParameterValue gg = (ParameterValue) ((AbstractGridFormat) reader.getFormat()).READ_GRIDGEOMETRY2D
            .createValue();
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), oldEnvelope));

        final GridCoverage2D gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg });
        forceDataLoading(gc);
        
        if (TestData.isInteractiveTest()) {
            // printing CRS information
            LOGGER.info(gc.getCoordinateReferenceSystem().toWKT());
            LOGGER.info(gc.getEnvelope().toString());
        }
    }

    @Test
	public void testIsAvailable() throws NoSuchAuthorityCodeException, FactoryException {
	    if (!testingEnabled()) {
	        return;
	    }
	
	    GridFormatFinder.scanForPlugins();
	
	    Iterator list = GridFormatFinder.getAvailableFormats().iterator();
	    boolean found = false;
	    GridFormatFactorySpi fac = null;
	
	    while (list.hasNext()) {
	        fac = (GridFormatFactorySpi) list.next();
	
	        if (fac instanceof JP2KFormatFactory) {
	            found = true;
	
	            break;
	        }
	    }
	
	    Assert.assertTrue("JP2KFormatFactory not registered", found);
	    Assert.assertTrue("JP2KFormatFactory not available", fac.isAvailable());
	    Assert.assertNotNull(new JP2KFormatFactory().createFormat());
	}
}
