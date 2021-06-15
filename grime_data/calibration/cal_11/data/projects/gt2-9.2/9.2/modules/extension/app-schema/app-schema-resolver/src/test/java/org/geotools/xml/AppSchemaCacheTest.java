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
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;

import org.geotools.data.DataUtilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link AppSchemaCache}.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 *
 *
 * @source $URL$
 */
public class AppSchemaCacheTest {

    /**
     * Test the {@link AppSchemaCache#delete(File) method.
     */
    @Test
    public void delete() throws Exception {
        (new File("target/test/a/b/c")).mkdirs();
        (new File("target/test/a/b/d/e/f")).mkdirs();
        File f = new File("target/test/a/b/d/e/f/temp.txt");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(f);
            printWriter.println("Some text");
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        Assert.assertTrue((new File("target/test/a/b/d/e/f/temp.txt")).exists());
        AppSchemaCache.delete(new File("target/test/a"));
        Assert.assertFalse((new File("target/test/a")).exists());
    }

    /**
     * Test resolution of a schema in an existing cache.
     */
    @Test
    public void resolve() throws Exception {
        // intentionally construct non-canonical cache directory
        File cacheDirectory = new File(DataUtilities.urlToFile(AppSchemaCacheTest.class
                .getResource("/test-data/cache")), "../cache");
        AppSchemaResolver resolver = new AppSchemaResolver(
                new AppSchemaCache(cacheDirectory, false));
        String resolvedLocation = resolver
                .resolve("http://schemas.example.org/cache-test/cache-test.xsd");
        Assert.assertTrue(resolvedLocation.startsWith("file:"));
        Assert.assertTrue(resolvedLocation.endsWith("cache-test.xsd"));
        Assert.assertTrue(DataUtilities.urlToFile((new URI(resolvedLocation)).toURL()).exists());
        // test that cache path is not canonical
        Assert.assertFalse(cacheDirectory.toString().equals(
                cacheDirectory.getCanonicalFile().toString()));
        // test that resolved location is canonical, despite cache directory not being canonical
        Assert.assertEquals(resolvedLocation,
                DataUtilities.urlToFile((new URI(resolvedLocation)).toURL()).getCanonicalFile()
                        .toURI().toString());
    }

}
