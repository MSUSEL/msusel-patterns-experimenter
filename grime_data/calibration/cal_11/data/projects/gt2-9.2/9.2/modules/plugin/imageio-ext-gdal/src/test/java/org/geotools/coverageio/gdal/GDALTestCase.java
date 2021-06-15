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
package org.geotools.coverageio.gdal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *
 * Base testing class initializing JAI properties to be used during tests.
 *
 *
 * @source $URL$
 */
@SuppressWarnings("deprecation")
public class GDALTestCase  {

    protected final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GDALTestCase.class);

	protected static void forceDataLoading(final GridCoverage2D gc) {
    	Assert.assertNotNull(gc);

        if (TestData.isInteractiveTest()) {
           final JFrame frame= new JFrame();
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.getContentPane().add(new ScrollingImagePanel(gc.getRenderedImage(),800,800));
           frame.pack();
           SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				frame.setVisible(true);
				
			}});
        } else {
            PlanarImage.wrapRenderedImage(gc.getRenderedImage()).getTiles();
        }
    }

    /**
     * A String containing the name of the supported format. It will be used to
     * customize the messages.
     */
    private String supportedFormat;

    /**
     * The {@code GridFormatFactorySpi} provided by the specific subclass to
     * handle a specific format.
     */
    private GridFormatFactorySpi factorySpi;

    public GDALTestCase(final String supportedFormat,final GridFormatFactorySpi factorySpi) {
        this.supportedFormat = supportedFormat;
        this.factorySpi = factorySpi;
    }

    @Before
    public void setUp() throws Exception {
        if(!testingEnabled())
            return;
        ImageIO.setUseCache(false);
        JAI.getDefaultInstance().getTileCache().setMemoryCapacity(16 * 1024 * 1024);
        JAI.getDefaultInstance().getTileCache().setMemoryThreshold(1.0f);
        try {
            final File file = TestData.file(this, "test.zip");
            if (file != null && file.exists() && file.canRead())
                // unzip it
                TestData.unzipFile(this, "test.zip");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "can not locate test-data for \"test.zip\"");
        } catch (Exception e1) {
        	LOGGER.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
        }
       
    }

    protected boolean testingEnabled() {
        boolean available = factorySpi.isAvailable();

        if (!available) {
            LOGGER.warning(supportedFormat + " libraries are not available, skipping tests!");
        }

        return available;
    }
}
