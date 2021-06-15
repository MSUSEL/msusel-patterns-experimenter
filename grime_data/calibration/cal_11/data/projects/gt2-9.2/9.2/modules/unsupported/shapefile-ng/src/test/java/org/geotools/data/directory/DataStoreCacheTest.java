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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.geotools.data.directory.DirectoryTypeCache;
import org.junit.Test;


/**
 * 
 *
 * @source $URL$
 */
public class DataStoreCacheTest extends DirectoryTestSupport {
    
    private static final String DESTDIR = "shapes";
    // we need a long delay for builds under UNIX, the timestap is coarse
    // (on windows it worked with 100ms)
    private static final int DELAY = 1000;

    @Test
    public void testInitialization() throws Exception {
        copyShapefiles("shapes/archsites.shp");
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        System.out.println(cache.getTypeNames());
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("archsites"));
        assertTrue(cache.getTypeNames().contains("bugsites"));
        cache.dispose();
    }
    
    @Test
    public void testAddNewDataStore() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        copyShapefiles("shapes/archsites.shp");
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("archsites"));
        cache.dispose();
    }
    
    @Test
    public void testRemoveDataStore() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        copyShapefiles("shapes/archsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("archsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        assertTrue(new File(tempDir, "archsites.shp").delete());
        assertTrue(new File(tempDir, "archsites.dbf").delete());
        assertTrue(new File(tempDir, "archsites.shx").delete());
        System.out.println(Arrays.asList(tempDir.listFiles()));
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        cache.dispose();
    }
    
    @Test
    public void testRemoveType() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        copyShapefiles("shapes/archsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("archsites"));
        assertTrue(cache.getTypeNames().contains("bugsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        assertTrue(new File(tempDir, "archsites.shp").delete());
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        cache.dispose();
    }
    
    @Test
    public void testAddType() throws Exception {
        File f = copyShapefiles("shapes/bugsites.shp");
        tempDir = f.getParentFile();
        DirectoryTypeCache cache = new DirectoryTypeCache(tempDir, getFileStoreFactory());
        assertEquals(1, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        
        // give the os some time, the directory last modification
        // time has a os specific time resolution
        Thread.sleep(DELAY);
        copyShapefiles("shapes/archsites.shp");
        assertEquals(2, cache.getTypeNames().size());
        assertTrue(cache.getTypeNames().contains("bugsites"));
        assertTrue(cache.getTypeNames().contains("archsites"));
        cache.dispose();
    }

}

