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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import static org.geotools.data.wfs.protocol.http.HttpMethod.GET;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.ows.OperationType;
import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.data.wfs.protocol.wfs.Version;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.util.logging.Logging;
import org.geotools.wfs.protocol.ConnectionFactory;
import org.geotools.wfs.protocol.WFSProtocolHandler;
import org.geotools.xml.DocumentFactory;
import org.xml.sax.SAXException;

/**
 * 
 *
 * @source $URL$
 */
public class WFS100ProtocolHandler extends WFSProtocolHandler {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    private WFSCapabilities capabilities;

    public WFS100ProtocolHandler(InputStream capabilitiesReader, ConnectionFactory connectionFac) throws IOException {
        super(Version.v1_0_0, connectionFac);
        capabilities = parseCapabilities(capabilitiesReader);
    }

    public WFSCapabilities getCapabilities() {
        return capabilities;
    }

    public ConnectionFactory getConnectionFactory(){
        return super.connectionFac;
    }
    
    protected WFSCapabilities parseCapabilities(InputStream capabilitiesReader) throws IOException {
        // TODO: move to some 1.0.0 specific class
        Map<String,Object> hints = new HashMap<String,Object>();
        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);

        Object parsed;
        try {
            parsed = DocumentFactory.getInstance(capabilitiesReader, hints, LOGGER.getLevel());
        } catch (SAXException e) {
            throw new DataSourceException("Error parsing WFS 1.0.0 capabilities", e);
        }

        if (parsed instanceof WFSCapabilities) {
            return (WFSCapabilities) parsed;
        } else {
            throw new DataSourceException(
                    "The specified URL Should have returned a 'WFSCapabilities' object. Returned a "
                            + ((parsed == null) ? "null value."
                                    : (parsed.getClass().getName() + " instance.")));
        }
    }

    @Override
    public URL getOperationURL(WFSOperationType operation, HttpMethod method)
            throws UnsupportedOperationException {
        OperationType operationType;
        switch (operation) {
        case DESCRIBE_FEATURETYPE:
            operationType = capabilities.getDescribeFeatureType();
            break;
        case GET_CAPABILITIES:
            operationType = capabilities.getGetCapabilities();
            break;
        case GET_FEATURE:
            operationType = capabilities.getGetFeature();
            break;
        case GET_FEATURE_WITH_LOCK:
            operationType = capabilities.getGetFeatureWithLock();
            break;
        case LOCK_FEATURE:
            operationType = capabilities.getLockFeature();
            break;
        case TRANSACTION:
            operationType = capabilities.getTransaction();
            break;
        default:
            throw new IllegalArgumentException("Unknown operation type " + operation);
        }
        if (operationType == null) {
            throw new UnsupportedOperationException(operation + " not supported by the server");
        }
        URL url;
        if (GET == method) {
            url = operationType.getGet();
        } else {
            url = operationType.getPost();
        }
        if (url == null) {
            throw new UnsupportedOperationException("Method " + method + " for " + operation
                    + " is not supported by the server");
        }
        return url;
    }

    @Override
    public boolean supports(WFSOperationType operation, HttpMethod method) {
        try {
            getOperationURL(operation, method);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

}
