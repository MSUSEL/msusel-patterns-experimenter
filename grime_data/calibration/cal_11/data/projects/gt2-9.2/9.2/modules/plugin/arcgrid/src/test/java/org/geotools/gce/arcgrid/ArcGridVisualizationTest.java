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
 *
 */
package org.geotools.gce.arcgrid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.Hints;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * <p>
 * Title: TestArcGridClass
 * </p>
 * 
 * <p>
 * Description: Testing ArcGrid ascii grids related classes.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005 Simone Giannecchini
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author <a href="mailto:simboss1@gmil.com">Simone Giannecchini (simboss)</a>
 *
 *
 * @source $URL$
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public final class ArcGridVisualizationTest extends ArcGridTestCaseAdapter {



	/**
	 * Creates a new instance of ArcGridReadWriteTest
	 * 
	 * @param name
	 */
	public ArcGridVisualizationTest(String name) {
		super(name);

	}

	public static final void main(String[] args) throws Exception {
		junit.textui.TestRunner.run(ArcGridVisualizationTest.class);
	}

    public void testFormatFinder() throws Exception {
        // get a gzipped ascii grid
        final File f = TestData.file(this, "arcgrid/arcGrid.asc");
        // Reading the coverage through a file
        AbstractGridFormat format = GridFormatFinder.findFormat(f);
        AbstractGridCoverage2DReader reader = format.getReader(f);

        GridCoverage2D gc = reader.read(null);

        assertNotNull(gc);

    }

	/**
	 * This test tries to read GZipped ascii grids first by supplying the
	 * {@link ArcGridReader} with a {@link File} that points to a gzipped
	 * coverage, second by opening up a {@link GZIPInputStream} and asking
	 * {@link ImageIO} to wrap it with an {@link ImageInputStream}.
	 * 
	 * @throws IOException
	 */
	public void testReadFileGZip() throws IOException {
	    final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM,DefaultGeographicCRS.WGS84);
		LOGGER.info("Reading the coverage through a file");
		// get a gzipped ascii grid
		final File f = TestData.file(this, "arcgrid/spearfish.asc.gz");
		// Reading the coverage through a file
		GridCoverageReader reader = new ArcGridReader(f);
		final GridCoverage2D gc1 = (GridCoverage2D) reader.read(null);

		LOGGER.info("Reading the gzipped coverage through an ImageInputStream");
		// Reading the coverage through an ImageInputStream
		final ImageInputStream iiStream = ImageIO
				.createImageInputStream( new GZIPInputStream(
						new FileInputStream(f)));
		reader = new ArcGridReader(iiStream,hints);
		final GridCoverage2D gc2 = (GridCoverage2D) reader.read(null);

		LOGGER.info(" Reading the gzipped coverage through an InputStream");
		// Reading the coverage through an InputStream
		reader = new ArcGridReader( new GZIPInputStream(
				new FileInputStream(f)),hints);
		final GridCoverage2D gc3 = (GridCoverage2D) reader.read(null);
		
		LOGGER.info("Reading the gzipped coverage through a URL");
		// Reading the coverage through a URL
		reader = new ArcGridReader( f.toURI().toURL(),hints);
		final GridCoverage2D gc4 = (GridCoverage2D) reader.read(null);

		// show the coverage or try to load the data
		if (TestData.isInteractiveTest()) {
			gc1.show();
			gc2.show();
			gc3.show();
			gc4.show();
		} else {
			PlanarImage.wrapRenderedImage(gc1.getRenderedImage()).getTiles();
			PlanarImage.wrapRenderedImage(gc2.getRenderedImage()).getTiles();
			PlanarImage.wrapRenderedImage(gc3.getRenderedImage()).getTiles();
			PlanarImage.wrapRenderedImage(gc4.getRenderedImage()).getTiles();
		}
	}

}
