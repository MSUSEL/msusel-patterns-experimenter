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
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import java.io.File;
import java.net.URL;

import org.geotools.data.DataUtilities;

import junit.framework.TestCase;

/**
 * Test the {@link JGrassMapEnvironment} class and the created paths.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 * @source $URL$
 */
@SuppressWarnings("nls")
public class JGrassMapEnvironmentTest extends TestCase {

    public void test() {
        URL pitUrl = this.getClass().getClassLoader().getResource("testlocation/test/cell/pit");
        File mapFile = DataUtilities.urlToFile(pitUrl);
        File mapsetFile = mapFile.getParentFile().getParentFile();

        JGrassMapEnvironment jME = new JGrassMapEnvironment(mapFile);
        checkEnvironment(jME);
        jME = new JGrassMapEnvironment(mapsetFile, "pit");
        checkEnvironment(jME);

    }

    private void checkEnvironment( JGrassMapEnvironment jME ) {
        File cell = jME.getCELL();
        assertTrue(cell.exists());
        assertTrue(cell.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "cell" + File.separator
                        + "pit"));

        File cellFolder = jME.getCellFolder();
        assertTrue(cellFolder.exists() && cellFolder.isDirectory());
        assertTrue(cellFolder.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "cell"));

        File fcell = jME.getFCELL();
        assertTrue(fcell.exists());
        assertTrue(fcell.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "fcell"
                        + File.separator + "pit"));

        File fcellFolder = jME.getFcellFolder();
        assertTrue(fcellFolder.exists() && fcellFolder.isDirectory());
        assertTrue(fcellFolder.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "fcell"));

        File colr = jME.getCOLR();
        assertTrue(colr.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "colr" + File.separator
                        + "pit"));

        File colrFolder = jME.getColrFolder();
        assertTrue(colrFolder.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "colr"));

        File wind = jME.getWIND();
        assertTrue(wind.exists());
        assertTrue(wind.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "WIND"));

    }
}
