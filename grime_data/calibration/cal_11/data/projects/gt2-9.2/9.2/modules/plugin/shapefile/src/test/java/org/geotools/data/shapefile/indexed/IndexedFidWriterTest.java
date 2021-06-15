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
package org.geotools.data.shapefile.indexed;

import java.io.IOException;
import java.net.MalformedURLException;

import org.geotools.data.shapefile.shp.IndexFile;

/**
 * 
 *
 * @source $URL$
 */
public class IndexedFidWriterTest extends FIDTestCase {
    private IndexFile indexFile;
    private IndexedFidWriter writer;

    public IndexedFidWriterTest() throws IOException {
        super("IndexedFidWriterTest");
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }

    private void initWriter() throws IOException, MalformedURLException {
        close();
        indexFile = new IndexFile(shpFiles, false);
        writer = new IndexedFidWriter(shpFiles);
    }

    protected void tearDown() throws Exception {
        close();
        super.tearDown();
    }

    private void close() throws IOException {
        if ((writer != null) && !writer.isClosed()) {
            writer.close();
        }

        try {
            if (indexFile != null) {
                indexFile.close();
            }
        } catch (Exception e) {
            // fine if already closed
        }
    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidWriter.hasNext()'
     */
    public void testHasNext() throws MalformedURLException, IOException {
        FidIndexer.generate(backshp.toURI().toURL());
        initWriter();

        for( int i = 1, j = indexFile.getRecordCount(); i < j; i++ ) {
            assertTrue(i + "th record", writer.hasNext());
            assertEquals((long) i, writer.next());
        }
    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidWriter.remove()'
     */
    public void testRemove() throws MalformedURLException, IOException {
        FidIndexer.generate(backshp.toURI().toURL());
        initWriter();
        writer.next();
        writer.remove();

        for( int i = 2, j = indexFile.getRecordCount(); i < j; i++ ) {
            assertTrue(writer.hasNext());
            assertEquals((long) i, writer.next());
        }

        writer.write();
        close();

        initWriter();

        for( int i = 1, j = indexFile.getRecordCount() - 1; i < j; i++ ) {
            assertTrue(writer.hasNext());
            assertEquals((long) i + 1, writer.next());
        }
    }

    public void testRemoveCounting() throws Exception {
        FidIndexer.generate(backshp.toURI().toURL());
        initWriter();
        writer.next();
        writer.remove();
        writer.next();
        writer.remove();
        writer.next();
        writer.remove();

        while( writer.hasNext() ) {
            writer.next();
            writer.write();
        }

        close();
        IndexedFidReader reader = new IndexedFidReader(shpFiles);
        try {
            assertEquals(3, reader.getRemoves());
        } finally {
            reader.close();
        }

        // remove some more features
        initWriter();
        writer.next();
        writer.next();
        writer.next();
        writer.remove();
        writer.next();
        writer.remove();
        writer.next();
        writer.next();
        writer.next();
        writer.remove();
        while( writer.hasNext() ) {
            writer.next();
            writer.write();
        }

        close();

        reader = new IndexedFidReader(shpFiles);
        try {
            assertEquals(6, reader.getRemoves());
        } finally {
            reader.close();
        }

    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidWriter.write()'
     */
    public void testWrite() throws IOException {
        initWriter();

        for( int i = 0; i < 5; i++ ) {
            writer.next();
            writer.write();
        }

        close();
        initWriter();

        for( int i = 1; i < 5; i++ ) {
            assertTrue(writer.hasNext());
            assertEquals((long) i, writer.next());
        }
    }

}
