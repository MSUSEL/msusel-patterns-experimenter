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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import javax.naming.OperationNotSupportedException;

import junit.framework.TestCase;

import org.geotools.feature.IllegalAttributeException;
import org.xml.sax.SAXException;

/**
 * @author dzwiers
 * @since 0.6.0
 *
 *
 *
 * @source $URL$
 */
public class IonicOnlineTest extends TestCase {

	private URL url = null;

	public IonicOnlineTest() throws MalformedURLException {
		url = new URL(
				"http://webservices.ionicsoft.com/ionicweb/wfs/BOSTON_ORA?version=1.0.0&request=getcapabilities&service=WFS");
	}

	public void testFeatureType() throws NoSuchElementException, IOException,
			SAXException {
		try {
			WFSDataStoreReadTest.doFeatureType(url, true, true, 0);
		} catch (IOException e) {
			skipHttpErrors(e);
		}
	}

	public void testFeatureReader() throws NoSuchElementException, IOException,
			IllegalAttributeException, SAXException {
		// FAILS due to Choice !!!
		try {
			WFSDataStoreReadTest.doFeatureReader(url, true, true, 0);
		} catch (IOException e) {
			skipHttpErrors(e);
		}
	}

	public void testFeatureReaderWithQuery() throws NoSuchElementException,
			OperationNotSupportedException, IllegalAttributeException,
			IOException, SAXException {
		try {
			WFSDataStoreReadTest.doFeatureReaderWithQuery(url, true, true, 0);
		} catch (IOException e) {
			skipHttpErrors(e);
		}
	}

	/**
	 * I the exception is a HTTP failure, skip the test to avoid breaking the build
	 * @param e
	 * @throws IOException
	 */
	private void skipHttpErrors(IOException e) throws IOException {
		if (e.getMessage().indexOf("Server returned HTTP response code:") == -1)
			throw e;
		System.out.println("WARNING, skipping test due to HTTP error: "
				+ e.getMessage());
	}
}
