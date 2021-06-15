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
package org.geotools.data.wfs.v1_1_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.data.DataSourceException;
import org.geotools.data.wfs.protocol.wfs.GetFeatureParser;
import org.geotools.gml3.ApplicationSchemaConfiguration;
import org.geotools.wfs.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.StreamingParser;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.SAXException;

/**
 * {@link GetFeatureParser} for {@link WFSFeatureReader} that uses the geotools
 * {@link StreamingParser} to fetch Features out of a WFS GetFeature response.
 * 
 * @author Gabriel Roldan
 * @version $Id: StreamingParserFeatureReader.java 28937 2008-01-25 10:52:22Z
 *          desruisseaux $
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools/wfs/v_1_1_0/data/StreamingParserFeatureReader.java $
 */
class StreamingParserFeatureReader implements GetFeatureParser {

    private StreamingParser parser;

    private InputStream inputStream;

    /**
     * A WFS configuration for unit test support, that resolves schemas to the
     * test data dir.
     * 
     * @author Gabriel Roldan
     * @version $Id: TestWFSConfiguration.java 28989 2008-01-28 21:22:31Z
     *          groldan $
     * @since 2.5.x
     * @source $URL:
     *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/test/java/org/geotools/wfs/v_1_1_0/data/TestWFSConfiguration.java $
     */
    private static class WFSAppSchemaConfiguration extends ApplicationSchemaConfiguration {

        /**
         * 
         * @param wfsConfiguration
         *            the WFS configuration where to grab the bindings from
         * @param namespace
         *            the namespace of the target feature
         * @param schemaLocation
         *            the schema location (a DescribeFeatureType request works)
         */
        public WFSAppSchemaConfiguration(Configuration wfsConfiguration, String namespace,
                String schemaLocation) {
            super(namespace, schemaLocation);
            addDependency(wfsConfiguration);
        }

    }

    /**
     * 
     * @param wfsConfiguration
     *            the configuration where to grab (wfs and gml) bindings from.
     *            Should be an instance of {@link WFSConfiguration}.
     * @param getFeatureResponseStream
     *            the response stream from a GetFeature operation.
     * @param featureName
     *            the name of the Feature (ie, the top level xml element
     *            declaration)
     * @param describeFeatureTypeRequest
     *            provides the location of the GetFeature response schema to be
     *            used by an {@link ApplicationSchemaConfiguration} in order to
     *            resolve imports and includes.
     * @throws DataSourceException
     */
    public StreamingParserFeatureReader(final Configuration wfsConfiguration,
            final InputStream getFeatureResponseStream, final QName featureName,
            final URL describeFeatureTypeRequest) throws DataSourceException {
        this.inputStream = getFeatureResponseStream;
        try {
            Configuration appSchemaConfiguration;
            String namespaceURI = featureName.getNamespaceURI();
            String schemaLocation = describeFeatureTypeRequest.toExternalForm();
            appSchemaConfiguration = new WFSAppSchemaConfiguration(wfsConfiguration, namespaceURI,
                    schemaLocation);

            this.parser = new StreamingParser(appSchemaConfiguration, getFeatureResponseStream,
                    featureName);
        } catch (ParserConfigurationException e) {
            throw new DataSourceException(e);
        } catch (SAXException e) {
            if (e.getCause() == null && e.getException() != null) {
                e.initCause(e.getException());
            }
            throw new DataSourceException(e);
        }
    }

    /**
     * @see GetFeatureParser#close()
     */
    public void close() throws IOException {
        if (inputStream != null) {
            try {
                inputStream.close();
            } finally {
                inputStream = null;
                parser = null;
            }
        }
    }

    /**
     * @see GetFeatureParser#parse()
     */
    public SimpleFeature parse() throws IOException {
        Object parsed = parser.parse();
        SimpleFeature feature = (SimpleFeature) parsed;
        return feature;
    }

    /**
     * @see GetFeatureParser#getNumberOfFeatures()
     */
    public int getNumberOfFeatures() {
        return -1;
    }

}
