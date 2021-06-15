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
package org.geotools.data.directory;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import org.geotools.TestData;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.junit.After;

/**
 * 
 *
 * @source $URL$
 */
public class DirectoryTestSupport {
    
    static final URI NSURI;
    static {
        try {
            NSURI = new URI("http://www.geotools.org");
        } catch(Exception e) {
            throw new RuntimeException("Impossible...");
        }
    }
    File tempDir = null;
    
    @After
    public void tearDown() throws IOException {
        if(tempDir != null)
            deleteDirectory(tempDir);
    }
    
    FileStoreFactory getFileStoreFactory() {
        return new ShapefileDataStoreFactory.ShpFileStoreFactory(new ShapefileDataStoreFactory(),
                Collections.singletonMap(ShapefileDataStoreFactory.NAMESPACEP.key, NSURI));
    }
    
    /**
     * Recursively deletes the contents of the specified directory 
     */
    public static void deleteDirectory(File directory) throws IOException {
        for (File f : directory.listFiles()) {
            if (f.isDirectory()) {
                deleteDirectory(f);
            } else {
                if(!f.delete()) {
                	System.out.println("Couldn't delete " + f.getAbsolutePath());
                }
            }
        }
        directory.delete();
    }

    /**
     * Copies the specified shape file into the {@code test-data} directory, together with its
     * sibling ({@code .dbf}, {@code .shp}, {@code .shx} and {@code .prj} files).
     */
    protected File copyShapefiles(final String name) throws IOException {
        return copyShapefiles(name, null);
    }
    
    /**
     * Copies the specified shape file into the {@code test-data} directory, together with its
     * sibling ({@code .dbf}, {@code .shp}, {@code .shx} and {@code .prj} files).
     */
    protected File copyShapefiles(final String name, final String directoryName) throws IOException {
        assertTrue(TestData.copy(this, sibling(name, "dbf"), directoryName).canRead());
        assertTrue(TestData.copy(this, sibling(name, "shp"), directoryName).canRead());
        assertTrue(TestData.copy(this, sibling(name, "shx"), directoryName).canRead());
        try {
            assertTrue(TestData.copy(this, sibling(name, "prj"), directoryName).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        return TestData.copy(this, name, directoryName);
    }
    
    /**
     * Helper method for {@link #copyShapefiles}.
     */
    private static String sibling(String name, final String ext) {
        final int s = name.lastIndexOf('.');
        if (s >= 0) {
            name = name.substring(0, s);
        }
        return name + '.' + ext;
    }
}
