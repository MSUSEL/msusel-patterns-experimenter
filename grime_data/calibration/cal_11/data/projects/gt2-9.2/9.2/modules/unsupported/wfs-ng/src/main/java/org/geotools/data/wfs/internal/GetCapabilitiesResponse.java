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
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_1_0_CAPABILITIES_CONFIGURATION;
import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_1_1_CONFIGURATION;
import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_2_0_CONFIGURATION;
import static org.geotools.data.wfs.internal.Loggers.MODULE;
import static org.geotools.data.wfs.internal.Loggers.RESPONSES;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.opengis.wfs.WFSCapabilitiesType;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.DataSourceException;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.util.Version;
import org.geotools.xml.Configuration;
import org.geotools.xml.DOMParser;
import org.w3c.dom.Document;

public class GetCapabilitiesResponse extends org.geotools.data.ows.GetCapabilitiesResponse {

    private WFSGetCapabilities capabilities;

    public GetCapabilitiesResponse(HTTPResponse response) throws IOException, ServiceException {
        super(response);
        MODULE.finer("Parsing GetCapabilities response");
        try {
            final Document rawDocument;
            final byte[] rawResponse;
            {
                ByteArrayOutputStream buff = new ByteArrayOutputStream();
                InputStream inputStream = response.getResponseStream();
                try {
                    IOUtils.copy(inputStream, buff);
                } finally {
                    inputStream.close();
                }
                rawResponse = buff.toByteArray();
            }
            if (RESPONSES.isLoggable(Level.FINE)) {
                RESPONSES.fine("Full GetCapabilities response: " + new String(rawResponse));
            }
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                builderFactory.setNamespaceAware(true);
                builderFactory.setValidating(false);
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                rawDocument = documentBuilder.parse(new ByteArrayInputStream(rawResponse));
            } catch (Exception e) {
                throw new IOException("Error parsing capabilities document: " + e.getMessage(), e);
            }

            List<Configuration> tryConfigs = Arrays.asList(WFS_2_0_CONFIGURATION,
                    WFS_1_1_CONFIGURATION, WFS_1_0_CAPABILITIES_CONFIGURATION);

            final String versionAtt = rawDocument.getDocumentElement().getAttribute("version");
            Version version = null;
            if (null != versionAtt) {
                version = new Version(versionAtt);
                if (Versions.v1_0_0.equals(version)) {
                    tryConfigs = Collections.singletonList(WFS_1_0_CAPABILITIES_CONFIGURATION);
                } else if (Versions.v1_1_0.equals(version)) {
                    tryConfigs = Collections.singletonList(WFS_1_1_CONFIGURATION);
                } else if (Versions.v2_0_0.equals(version)) {
                    tryConfigs = Collections.singletonList(WFS_2_0_CONFIGURATION);
                }
            }
            EObject parsedCapabilities = null;

            for (Configuration wfsConfig : tryConfigs) {
                try {
                    parsedCapabilities = parseCapabilities(rawDocument, wfsConfig);
                    if (parsedCapabilities != null) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (null == parsedCapabilities) {
                throw new IllegalStateException("Unable to parse GetCapabilities document");
            }
            // if (object instanceof ServiceException) {
            // throw (ServiceException) object;
            // }

            this.capabilities = WFSGetCapabilities.create(parsedCapabilities, rawDocument);
        } finally {
            response.dispose();
        }
    }

    private EObject parseCapabilities(final Document document, final Configuration wfsConfig)
            throws IOException {

        // final Parser parser = new Parser(wfsConfig);
        DOMParser parser = new DOMParser(wfsConfig, document);
        final Object parsed;
        try {
            parsed = parser.parse();
        } catch (Exception e) {
            throw new DataSourceException("Exception parsing WFS capabilities", e);
        }
        if (parsed == null) {
            throw new DataSourceException("WFS capabilities was not parsed");
        }
        if (!(parsed instanceof WFSCapabilitiesType)) {
            throw new DataSourceException("Expected WFS Capabilities, got " + parsed);
        }
        EObject object = (EObject) parsed;
        return object;
    }

    @Override
    public WFSGetCapabilities getCapabilities() {
        return capabilities;
    }
}
