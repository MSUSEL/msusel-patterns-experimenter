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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.geotools.ows.ServiceException;
import org.geotools.xml.Configuration;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class DescribeFeatureTypeResponse extends WFSResponse {

    private FeatureType parsed;

    public DescribeFeatureTypeResponse(final DescribeFeatureTypeRequest request,
            final HTTPResponse httpResponse) throws ServiceException, IOException {

        super(request, httpResponse);

        final WFSStrategy strategy = request.getStrategy();
        final Configuration wfsConfiguration = strategy.getWfsConfiguration();
        final QName remoteTypeName = request.getTypeName();
        final FeatureTypeInfo featureTypeInfo = strategy.getFeatureTypeInfo(remoteTypeName);
        final CoordinateReferenceSystem defaultCrs = featureTypeInfo.getCRS();

        InputStream responseStream = httpResponse.getResponseStream();
        try {
            File tmp = File.createTempFile(remoteTypeName.getLocalPart(), ".xsd");
            OutputStream output = new BufferedOutputStream(new FileOutputStream(tmp));
            output = new TeeOutputStream(output, System.out);
            try {
                IOUtils.copy(responseStream, output);
            } finally {
                output.flush();
                IOUtils.closeQuietly(output);
            }
            try {
                URL schemaLocation = tmp.toURI().toURL();
                this.parsed = EmfAppSchemaParser.parse(wfsConfiguration, remoteTypeName,
                        schemaLocation, defaultCrs);
            } finally {
                tmp.delete();
            }
        } finally {
            responseStream.close();
            httpResponse.dispose();
        }

    }

    public FeatureType getFeatureType() {
        return parsed;
    }
}
