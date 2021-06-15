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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng;

import static org.geotools.data.shapefile.ng.files.ShpFileType.*;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.geotools.data.shapefile.ng.files.FileWriter;
import org.geotools.data.shapefile.ng.files.Result;
import org.geotools.data.shapefile.ng.files.ShpFiles;
import org.geotools.data.shapefile.ng.files.ShpFiles.State;

/**
 * 
 *
 * @source $URL$
 */
public class ShpFilesLockingTest extends TestCase implements FileWriter {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getClass().getClassLoader().setDefaultAssertionStatus(true);
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        Runtime.getRuntime().runFinalization();
    }

    public void testAcquireReadFile() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        try{
            shpFiles.acquireReadFile(DBF, this);
            fail("Not a file should send exception");
        }catch(IllegalStateException e ){
            // good
        }
        

        String path = "somefile.shp";
        shpFiles = new ShpFiles( new File( path ));

        File file = shpFiles.acquireReadFile(SHP, this);
        assertEquals( new File(path).getCanonicalPath(), file.getPath());
        assertEquals(1, shpFiles.numberOfLocks());
        
        shpFiles.unlockRead(file, this);
        shpFiles.dispose();
    }
    public void testAcquireWriteFile() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        try {
            shpFiles.acquireWriteFile(DBF, this);
            fail("Not a file should send exception");
        } catch (IllegalStateException e) {
            // good
        }
        

        String path = "somefile.shp";
        shpFiles = new ShpFiles( new File( path ));

        File file = shpFiles.acquireWriteFile(SHP, this);
        assertEquals(new File( path ).getCanonicalPath(), file.getPath());
        assertEquals(1, shpFiles.numberOfLocks());
        
        shpFiles.unlockWrite(file, this);
        assertEquals(0, shpFiles.numberOfLocks());
        shpFiles.dispose();
    }

    public void testAcquireRead1() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter = new FileWriter() {

            public String id() {
                return "Other";
            }

        };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireRead(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value
                .toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        // same thread should work
        Result<URL, State> result2 = shpFiles.tryAcquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", result2.value
                .toExternalForm());
        assertEquals(3, shpFiles.numberOfLocks());

        shpFiles.unlockRead(result2.value, this);
        shpFiles.unlockRead(result1.value, testWriter);
        shpFiles.unlockRead(url, this);
        shpFiles.dispose();
    }

    public void testUnlockReadAssertion() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter = new FileWriter() {

            public String id() {
                return "Other";
            }

        };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireRead(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value
                .toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        try {
            shpFiles.unlockRead(result1.value, this);
            throw new RuntimeException(
                    "Unlock should fail because it is in the wrong reader");
        } catch (IllegalArgumentException e) {
            // good
        } catch (RuntimeException e) {
            fail(e.getMessage());
        }

        shpFiles.unlockRead(result1.value, testWriter);
        shpFiles.unlockRead(url, this);
        shpFiles.dispose();
    }

    public void testUnlockWriteAssertion() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireWrite(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter = new FileWriter() {

            public String id() {
                return "Other";
            }

        };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireWrite(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value
                .toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        try {
            shpFiles.unlockRead(result1.value, this);
            throw new RuntimeException(
                    "Unlock should fail because it is in the wrong reader");
        } catch (IllegalArgumentException e) {
            // good
        } catch (RuntimeException e) {
            fail(e.getMessage());
        }

        shpFiles.unlockWrite(url, this);
        shpFiles.unlockWrite(result1.value, testWriter);
        shpFiles.dispose();
    }

    public String id() {
        return getClass().getName();
    }

}
