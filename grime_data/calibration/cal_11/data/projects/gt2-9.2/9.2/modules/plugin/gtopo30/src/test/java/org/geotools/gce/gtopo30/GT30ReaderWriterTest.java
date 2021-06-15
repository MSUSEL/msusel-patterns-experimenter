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
package org.geotools.gce.gtopo30;

import java.io.File;
import java.net.URL;

import javax.media.jai.JAI;
import javax.media.jai.RecyclingTileFactory;
import javax.media.jai.TileCache;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.test.TestData;
import org.geotools.resources.coverage.CoverageUtilities;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;

/**
 * Purpose of this class is testing the ability of this plug in to read and
 * write back the in gtopo30 format.
 * 
 * @author Simone Giannecchini
 *
 *
 * @source $URL$
 */
public class GT30ReaderWriterTest extends GT30TestBase {
	/**
	 * Constructor for GT30ReaderTest.
	 * 
	 * @param arg0
	 */
	public GT30ReaderWriterTest(String arg0) {
		super(arg0);
	}

	/**
	 * Testing reader and writer for gtopo. This test first of all read an
	 * existing gtopo tessel into a coverage object, therefore it writes it back
	 * onto the disk. Once the coverage is written back\ it loads it again
	 * building a new coverage which is finally visualized.
	 * 
	 * @throws Exception
	 */
	public void test() throws Exception {

		URL statURL = TestData.url(this, (new StringBuffer(this.fileName)
				.append(".DEM").toString()));
		AbstractGridFormat format = (AbstractGridFormat) new GTopo30FormatFactory()
				.createFormat();

		// using a big tile cache
		final JAI jaiDef = JAI.getDefaultInstance();
		final TileCache cache = jaiDef.getTileCache();
		cache.setMemoryCapacity(64 * 1024 * 1024);
		cache.setMemoryThreshold(1.0f);
		// final TCTool tool= new TCTool();

		// setting JAI wide hints
		jaiDef.setRenderingHint(JAI.KEY_CACHED_TILE_RECYCLING_ENABLED,
				Boolean.TRUE);

		// tile factory and recycler
		final RecyclingTileFactory recyclingFactory = new RecyclingTileFactory();
		jaiDef.setRenderingHint(JAI.KEY_TILE_FACTORY, recyclingFactory);
		jaiDef.setRenderingHint(JAI.KEY_TILE_RECYCLER, recyclingFactory);

		if (format.accepts(statURL)) {

			/**
			 * 
			 * STEP 1 Reading the coverage into memory in order to write it down
			 * again
			 * 
			 */
			// get a reader
			GridCoverageReader reader = format.getReader(statURL);

			// get a grid coverage
			gc = ((GridCoverage2D) reader.read(null));
			assertTrue(CoverageUtilities.hasRenderingCategories(gc));
			if(TestData.isInteractiveTest())
				gc.show();

			// preparing to write it down
			File testDir = TestData.file(this, "");
			newDir = new File(testDir.getAbsolutePath() + "/newDir");
			newDir.mkdir();

			// writing it down
			GridCoverageWriter writer = format.getWriter(newDir);
			writer.write(gc, null);

			/**
			 * 
			 * STEP 2 Reading back into memory the previos coverage.
			 * 
			 */
			// preparing the URL
			statURL = TestData.getResource(this, "newDir/" + this.fileName
					+ ".DEM");

			// read it again
			reader = format.getReader(statURL);
			gc = ((GridCoverage2D) reader.read(null));

			/**
			 * 
			 * STEP 3 Visualizing the lcoverage we just read in order to see if
			 * everything is fine.
			 * 
			 */
			// packed view for this coverage
			GridCoverage2D gc1 = gc.geophysics(false);
			if (TestData.isInteractiveTest()) {
				gc1.show();
				// logging some info
				logger.info(gc.getCoordinateReferenceSystem2D().toWKT() + "\n"
						+ gc.toString());
				logger.info(gc1.getCoordinateReferenceSystem2D().toWKT() + "\n"
						+ gc1.toString());
			} else {
				gc1.getRenderedImage().getData();

			}

		}
	}

	public static final void main(String[] args) throws Exception {
		junit.textui.TestRunner.run(GT30ReaderWriterTest.class);
	}

}
