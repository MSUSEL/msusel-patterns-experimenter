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
package org.geotools.data.ogr;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public abstract class OGRDataStoreFactoryTest extends TestCaseSupport {

    protected OGRDataStoreFactoryTest(Class<? extends OGRDataStoreFactory> dataStoreFactoryClass) {
        super(dataStoreFactoryClass);
    }

    public void testLookup() throws Exception {
        Map<String, Serializable> map = new HashMap<String, Serializable>();
        map.put(OGRDataStoreFactory.OGR_NAME.key, getAbsolutePath(STATE_POP));
        DataStore ds = null;
        try {
            ds = DataStoreFinder.getDataStore(map);
            assertNotNull(ds);
            assertTrue(ds instanceof OGRDataStore);
        } finally {
            disposeQuietly(ds);
        }
    }

    private void disposeQuietly(DataStore ds) {
        if (ds != null) {
            ds.dispose();
        }
    }

    public void testNamespace() throws Exception {
        Map<String, Serializable> map = new HashMap<String, Serializable>();
        URI namespace = new URI("http://jesse.com");
        map.put(OGRDataStoreFactory.NAMESPACEP.key, namespace);
        map.put(OGRDataStoreFactory.OGR_NAME.key, getAbsolutePath(STATE_POP));
        DataStore store = null;
        try {
            store = dataStoreFactory.createDataStore(map);
            SimpleFeatureType schema = store.getSchema(
                    STATE_POP.substring(STATE_POP.lastIndexOf('/') + 1,
                            STATE_POP.lastIndexOf('.')));
            assertEquals(namespace.toString(), schema.getName().getNamespaceURI());
        } finally {
            disposeQuietly(store);
        }
    }
    
    public void testNames() throws Exception {
        Set<String> drivers = dataStoreFactory.getAvailableDrivers();
        assertTrue(drivers.size() > 0);
        assertTrue(drivers.contains("ESRI Shapefile"));
    }

}
