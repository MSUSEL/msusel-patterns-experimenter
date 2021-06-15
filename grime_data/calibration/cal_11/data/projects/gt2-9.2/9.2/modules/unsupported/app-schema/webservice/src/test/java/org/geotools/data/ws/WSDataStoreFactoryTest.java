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
package org.geotools.data.ws;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.ws.XmlDataStore;
import org.geotools.data.ws.WSDataStoreFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class WSDataStoreFactoryTest {

    private static final String BASE_DIRECTORY = "./src/test/resources/test-data/";

    private WSDataStoreFactory dsf;

    private Map<String, Serializable> params;

    @Before
    public void setUp() throws Exception {
        dsf = new WSDataStoreFactory();
        params = new HashMap<String, Serializable>();
    }

    @After
    public void tearDown() throws Exception {
        dsf = null;
        params = null;
    }

    @Test
    public void testCanProcess() {
        // URL not set
        assertFalse(dsf.canProcess(params));

        params.put(WSDataStoreFactory.GET_CONNECTION_URL.key,
                "http://someserver.example.org/wfs?request=GetCapabilities");
        assertFalse(dsf.canProcess(params));

        params.put(WSDataStoreFactory.TEMPLATE_NAME.key, "request.ftl");
        assertFalse(dsf.canProcess(params));
        
        try {
            params.put(WSDataStoreFactory.TEMPLATE_DIRECTORY.key, new URL(
                    "file:./src/test/resources/test-data"));
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }
        assertFalse(dsf.canProcess(params));

        try {
            params.put(WSDataStoreFactory.CAPABILITIES_FILE_LOCATION.key, new URL(
                    "file:./src/test/resources/test-data/ws_capabilities.xml"));
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }
        assertTrue(dsf.canProcess(params));

        params.put(WSDataStoreFactory.TIMEOUT.key, "30000");
        assertTrue(dsf.canProcess(params));       
    }

    @Test
    public void testCreateDataStoreWS() throws IOException {
        String capabilitiesFile;
        capabilitiesFile = "ws_capabilities_equals_removed.xml";
        testCreateDataStore_WS(capabilitiesFile);
    }

    private void testCreateDataStore_WS(final String capabilitiesFile) throws IOException {
         final WSDataStoreFactory dsf = new WSDataStoreFactory(); 
        Map<String, Serializable> params = new HashMap<String, Serializable>();
         
            
        File file = new File(BASE_DIRECTORY + capabilitiesFile);
        if (!file.exists()) {
            throw new IllegalArgumentException(capabilitiesFile + " not found");
        }    
        URL url = file.toURL();
       
        params.put(WSDataStoreFactory.GET_CONNECTION_URL.key, url);
        params.put(WSDataStoreFactory.TEMPLATE_DIRECTORY.key, new URL("file:" + BASE_DIRECTORY));
        params.put(WSDataStoreFactory.TEMPLATE_NAME.key, "request.ftl");
        params.put(WSDataStoreFactory.CAPABILITIES_FILE_LOCATION.key, url);

        XmlDataStore dataStore = dsf.createDataStore(params);
        assertTrue(dataStore instanceof WS_DataStore);
    }
}
