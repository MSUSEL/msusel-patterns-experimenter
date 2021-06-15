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
import javax.media.jai.TileCache;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.test.TestData;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Purpose of this method is testing the ability of this plugin to write the
 * complete set of files for the GTOPO30 format in a single zip package.
 * 
 * @author Simone Giannecchini
 *
 *
 * @source $URL$
 */
public class GT30ZipWriterTest extends GT30TestBase {
	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 */
	public GT30ZipWriterTest(String name) {
		super(name);
	}

	/**
	 * Testing zipped-package writing capabilites.
	 * 
	 * @throws Exception
	 */
	public void test() throws Exception {
		final URL statURL = TestData.getResource(this, this.fileName + ".DEM");
		final AbstractGridFormat format = (AbstractGridFormat) new GTopo30FormatFactory()
				.createFormat();

		final TileCache defaultInstance = JAI.getDefaultInstance()
				.getTileCache();
		defaultInstance.setMemoryCapacity(1024 * 1024 * 64);
		defaultInstance.setMemoryThreshold(1.0f);

		final GTopo30WriteParams wp = new GTopo30WriteParams();
		wp.setCompressionMode(GTopo30WriteParams.MODE_EXPLICIT);
		wp.setCompressionType("ZIP");
		ParameterValueGroup params = format.getWriteParameters();
		params.parameter(
				AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
				.setValue(wp);

		if (format.accepts(statURL)) {
			// get a reader
			final GridCoverageReader reader = format.getReader(statURL);

			// get a grid coverage
			final GridCoverage2D gc = ((GridCoverage2D) reader.read(null));

			// preparing to write it down
			File testDir = TestData.file(this, "");
			newDir = new File(testDir.getAbsolutePath() + "/newDir");
			newDir.mkdir();


			final GridCoverageWriter writer = format.getWriter(newDir);
			writer.write(gc, (GeneralParameterValue[]) params.values().toArray(
					new GeneralParameterValue[1]));
		
			gc.dispose(false);
		}
	}

	public static final void main(String[] args) throws Exception {
		junit.textui.TestRunner.run((GT30ZipWriterTest.class));
	}
}
