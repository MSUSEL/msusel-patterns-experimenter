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
package org.geotools.data.wfs.v1_1_0.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;

import org.geotools.data.DataUtilities;
import org.geotools.data.wfs.protocol.wfs.GetFeatureParser;
import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.protocol.wfs.WFSResponseParser;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A WFS response parser that parses a GetFeature response that did not return an ExceptionReport
 * and is on GML 3.1 format into a {@link GetFeatureParser} in order to stream the features produced
 * by the server.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 *
 *
 *
 * @source $URL$
 * @see Gml31GetFeatureResponseParserFactory
 */
public class FeatureCollectionParser implements WFSResponseParser {

    /**
     * @return a {@link GetFeatureParser} to stream the contents of the GML 3.1 response
     */
    public Object parse(WFS_1_1_0_DataStore wfs, WFSResponse response) throws IOException {

        GetFeatureType request = (GetFeatureType) response.getOriginatingRequest();
        QueryType queryType = (QueryType) request.getQuery().get(0);
        String prefixedTypeName = (String) queryType.getTypeName().get(0);
        SimpleFeatureType schema = wfs.getSchema(prefixedTypeName);
        List<String> propertyNames = queryType.getPropertyName();
        if (propertyNames.size() > 0) {
            // the expected schema may contain less properties than the full schema. Let's say it to
            // the parser so it does not parse unnecessary attributes in case the WFS returns more
            // than requested
            String[] properties = propertyNames.toArray(new String[propertyNames.size()]);
            try {
                schema = DataUtilities.createSubType(schema, properties);
            } catch (SchemaException e) {
                throw (RuntimeException) new RuntimeException().initCause(e);
            }
        }
        QName featureName = wfs.getFeatureTypeName(prefixedTypeName);
        InputStream in = response.getInputStream();

        GetFeatureParser featureReader = new XmlSimpleFeatureParser(in, schema, featureName);
        return featureReader;
    }
}
