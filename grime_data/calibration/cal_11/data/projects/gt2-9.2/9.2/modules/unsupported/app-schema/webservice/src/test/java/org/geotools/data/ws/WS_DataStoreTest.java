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
package org.geotools.data.ws;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import org.geotools.data.ws.WS_DataStore;
import org.geotools.data.ws.XmlDataStore;
import org.geotools.data.ws.WSDataStoreFactory;

import org.junit.Test;


/**
 * Unit test suite for {@link WS_DataStore}
 * 
 * @author Russell Petty
 * @version $Id$
 * @since 2.5.x
 *
 *
 *
 * @source $URL$
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/test/java/org/geotools/data
 *         /wfs/v1_1_0/WFSDataStoreTest.java $
 */
@SuppressWarnings("nls")
public class WS_DataStoreTest {
    private static final String TEST_DIRECTORY = "file:./src/test/resources/test-data/";

    /**
     * Test method for {@link WS_DataStore#getTypeNames()}.
     * 
     * @throws IOException
     */
    @Test
    public void testCreate() throws IOException {
        
        Map<Object, Object> properties = new HashMap<Object, Object>();
        WSDataStoreFactory dsf = new WSDataStoreFactory();
        properties.put("WSDataStoreFactory:GET_CONNECTION_URL", "http://d00109:8080/xaware/XADocSoapServlet");     
        properties.put("WSDataStoreFactory:TIMEOUT", new Integer(30000));
        properties.put("WSDataStoreFactory:TEMPLATE_DIRECTORY", TEST_DIRECTORY);
        properties.put("WSDataStoreFactory:TEMPLATE_NAME", "request.ftl");
        properties.put("WSDataStoreFactory:CAPABILITIES_FILE_LOCATION", TEST_DIRECTORY + "ws_capabilities_equals_removed.xml");

        XmlDataStore ds = dsf.createDataStore(properties); 
        assertNotNull(ds);
    }
    
    
}
