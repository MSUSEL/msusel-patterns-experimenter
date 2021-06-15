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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.TestCaseSupport;

/**
 *
 *
 * @source $URL$
 */
public class IndexedShapefileDataStoreFactoryTest extends TestCaseSupport {
    private ShapefileDataStoreFactory factory;

    public IndexedShapefileDataStoreFactoryTest() throws IOException {
        super("IndexedShapefileDataStoreFactoryTest");
    }

    protected void setUp() throws Exception {
        factory = new ShapefileDataStoreFactory();
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.canProcess(Map)'
     */
    public void testCanProcessMap() throws Exception {
        Map map = new HashMap();
        map.put(ShapefileDataStoreFactory.URLP.key, TestData
                .url(IndexedShapefileDataStoreTest.STATE_POP));
        assertTrue(factory.canProcess(map));
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.createDataStore(Map)'
     */
    public void testCreateDataStoreMap() throws Exception {
        ShapefileDataStore ds = testCreateDataStore(true);
        ds.dispose();

        ShapefileDataStore ds1 = testCreateDataStore(true, true);
        ShapefileDataStore ds2 = testCreateDataStore(true, true);

        assertNotSame(ds1, ds2);
        ds2.dispose();

        ds2 = testCreateDataStore(true, false);
        assertNotSame(ds1, ds2);
        ds1.dispose();
        ds2.dispose();
    }

    private ShapefileDataStore testCreateDataStore(boolean createIndex)
            throws Exception {
        return testCreateDataStore(true, createIndex);
    }

    public void testNamespace() throws Exception {
        ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
        Map map = new HashMap();
        URI namespace = new URI("http://jesse.com");
        map.put(ShapefileDataStoreFactory.NAMESPACEP.key, namespace);
        map.put(ShapefileDataStoreFactory.URLP.key, TestData
                .url(IndexedShapefileDataStoreTest.STATE_POP));

        DataStore store = factory.createDataStore(map);
        String typeName = IndexedShapefileDataStoreTest.STATE_POP.substring(
                IndexedShapefileDataStoreTest.STATE_POP.indexOf('/') + 1,
                IndexedShapefileDataStoreTest.STATE_POP.lastIndexOf('.'));
        assertEquals("http://jesse.com", store.getSchema(typeName).getName()
                .getNamespaceURI());
        store.dispose();
    }

    private ShapefileDataStore testCreateDataStore(boolean newDS,
            boolean createIndex) throws Exception {
        File f = copyShapefiles(IndexedShapefileDataStoreTest.STATE_POP);
        Map map = new HashMap();
        map.put(ShapefileDataStoreFactory.URLP.key, f.toURI().toURL());
        map.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key,
                createIndex ? Boolean.TRUE : Boolean.FALSE);

        ShapefileDataStore ds;

        if (newDS) {
            // This may provided a warning if the file already is created
            ds = (ShapefileDataStore) factory.createNewDataStore(map);
        } else {
            ds = (ShapefileDataStore) factory.createDataStore(map);
        }

        if (ds instanceof IndexedShapefileDataStore) {
            IndexedShapefileDataStore indexed = (IndexedShapefileDataStore) ds;
            testDataStore(IndexType.QIX, createIndex, indexed);
        }
        return ds;
    }

    private void testDataStore(IndexType treeType, boolean createIndex,
            IndexedShapefileDataStore ds) {
        assertNotNull(ds);
        assertEquals(treeType, ds.treeType);
        assertEquals(treeType != IndexType.NONE, ds.useIndex);
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.createNewDataStore(Map)'
     */
    public void testCreateNewDataStore() throws Exception {
        ShapefileDataStore ds1 = testCreateDataStore(true, false);
        ShapefileDataStore ds2 = testCreateDataStore(true, true);

        assertNotSame(ds1, ds2);
        ds1.dispose();
        ds2.dispose();
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.isAvailable()'
     */
    public void testIsAvailable() {
        assertTrue(factory.isAvailable());
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.getParametersInfo()'
     */
    public void testGetParametersInfo() {
        List infos = Arrays.asList(factory.getParametersInfo());
        assertTrue(infos
                .contains(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX));
        assertTrue(infos.contains(ShapefileDataStoreFactory.URLP));
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.getFileExtensions()'
     */
    public void testGetFileExtensions() {
        List ext = Arrays.asList(factory.getFileExtensions());
        assertTrue(ext.contains(".shp"));
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.canProcess(URL)'
     */
    public void testCanProcessURL() throws FileNotFoundException {
        factory.canProcess(TestData.url(IndexedShapefileDataStoreTest.STATE_POP));
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.createDataStore(URL)'
     */
    public void testCreateDataStoreURL() throws IOException {
        copyShapefiles(IndexedShapefileDataStoreTest.STATE_POP);
        DataStore ds = factory.createDataStore(TestData.url(TestCaseSupport.class,
                IndexedShapefileDataStoreTest.STATE_POP));
        testDataStore(IndexType.QIX, true, (IndexedShapefileDataStore) ds);
        ds.dispose();
    }

    /*
     * Test method for
     * 'org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory.getTypeName(URL)'
     */
    public void testGetTypeName() throws IOException {
        factory.getTypeName(TestData.url(IndexedShapefileDataStoreTest.STATE_POP));
    }
}
