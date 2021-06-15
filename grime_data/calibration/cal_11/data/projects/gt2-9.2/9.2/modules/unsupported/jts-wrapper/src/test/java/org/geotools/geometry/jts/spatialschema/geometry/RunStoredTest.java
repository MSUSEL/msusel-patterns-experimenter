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
 */ 
package org.geotools.geometry.jts.spatialschema.geometry;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @author Jody Garnett
 * @author Joel Skelton
 *
 *
 *
 *
 * @source $URL$
 */
public class RunStoredTest extends TestCase {
    private static final Log LOG = LogFactory.getLog(RunStoredTest.class);
    private static String TEST_DIRECTORY = "src/main/resources/org/geotools/test-data/xml/geometry";

    private FilenameFilter xmlFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".xml");
        }
    };

    /**
     * Load and run all test files.
     * @throws IOException 
     */
    public void testGeometriesFromXML() throws IOException {
        GeometryTestParser parser = new GeometryTestParser();
        File dir = new File(TEST_DIRECTORY);
        File[] listFiles = dir.listFiles(xmlFilter);
        if (dir.isDirectory()) {
            for (int i=0; i<listFiles.length; i++) {
                File testFile  = (File) listFiles[i];
                LOG.info("Loading test description file:" + testFile);
                FileInputStream inputStream = new FileInputStream(testFile);
                InputSource inputSource = new InputSource(inputStream);
                GeometryTestContainer tests = parser.parseTestDefinition(inputSource);
                assertTrue("Failed test(s) in: " + testFile.getName(), tests.runAllTestCases());
            }
        }
    }
}
