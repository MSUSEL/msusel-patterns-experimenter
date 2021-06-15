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
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing {@link PathType} class.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties like URLs
 *
 *
 *
 * @source $URL$
 */
public class PathTypeTest extends Assert {

        private final static Logger LOGGER = Logger.getLogger(PathTypeTest.class.toString());
	
	public PathTypeTest() {
	}
	
	@Test
	public void relative() throws FileNotFoundException, IOException{
		
		//get some test data
		final File testFile= TestData.file(this, "/rgb/global_mosaic_0.png");
		assertTrue(testFile.exists());
		
		// test it as a relative path to the test-data directory
		final URL temp=PathType.RELATIVE.resolvePath(TestData.url(this, ".").toExternalForm(), "rgb/global_mosaic_0.pgw");
		assertNotNull(temp);
		temp.openStream().close();
		
		// test error checks
		LOGGER.info("Testing invalid path");
		final URL temp1=PathType.RELATIVE.resolvePath(TestData.url(this, ".").toExternalForm(), "rgb/global_mosaic_0.pg");
		assertNull(temp1);
		try {
			temp1.openStream().close();
			fail("The relative URL "+temp1+" is not supposed to exist!");
		} catch (Exception e) {
		}
		
	}
	
	@Test
	public void absolute() throws FileNotFoundException, IOException{
		//get some test data
		final URL testFile= TestData.url(this, "/rgb/global_mosaic_0.png");
		testFile.openStream().close();
		
		// test it as a absolute path to the test-data directory
		final URL temp=PathType.ABSOLUTE.resolvePath(TestData.url(this, ".").toExternalForm(), testFile.toExternalForm());
		assertNotNull(temp);
		temp.openStream().close();

		
		final URL temp1=PathType.ABSOLUTE.resolvePath(null, testFile.toExternalForm());
		assertNotNull(temp1);
		temp1.openStream().close();
		
		// test error checks using relative call
		final URL temp2=PathType.ABSOLUTE.resolvePath(TestData.url(this, ".").toExternalForm(), "rgb/global_mosaic_0.pg");
		assertNull(temp2);
		try {
			temp2.openStream().close();
			fail("The URL "+temp2+" is not supposed to exist!");
		} catch (Exception e) {
		}
		
		
	}

}
