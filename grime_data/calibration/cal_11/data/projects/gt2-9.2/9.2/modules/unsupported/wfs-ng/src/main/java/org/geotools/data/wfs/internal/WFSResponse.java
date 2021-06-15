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
package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A handle to a WFS response that contains the input stream to the actual contents and some well
 * known response information derived from the HTTP response headers.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 * 
 * 
 * 
 * @source $URL$
 */
@SuppressWarnings("nls")
public class WFSResponse extends Response {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs.protocol.wfs");

    private Charset charset;

    private String contentType;

    private WFSRequest request;

    private SimpleFeatureType queryType;

    private QName remoteTypeName;

    /**
     * @throws IOException
     * @throws ServiceException
     */
    public WFSResponse(WFSRequest originatingRequest, final HTTPResponse httpResponse)
            throws ServiceException, IOException {

        super(httpResponse);

        this.request = originatingRequest;

        String charset = httpResponse.getResponseHeader("Charset");
        if (charset == null) {
            this.charset = Charset.forName("UTF-8");
        } else {
            try {
                this.charset = Charset.forName(charset);
            } catch (Exception e) {
                // TODO log
                this.charset = Charset.forName("UTF-8");
            }
        }
        this.contentType = httpResponse.getContentType();
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("WFS response: charset=" + charset + ", contentType=" + contentType);
        }
    }

    /**
     * Returns the character encoding if set by the server as an http header, if unknown assumes
     * {@code UTF-8}
     * 
     * @return the character set for the response if set, or {@code null}
     */
    public Charset getCharacterEncoding() {
        return charset;
    }

    /**
     * Returns the WFS response declared content type
     * 
     * @return the content type of the response
     */
    public String getContentType() {
        return contentType;
    }

    public WFSRequest getOriginatingRequest() {
        return request;
    }

    @Override
    public String toString() {
        return new StringBuilder("WFSResponse[charset=").append(charset).append(", contentType=")
                .append(contentType).append("]").toString();
    }

    public SimpleFeatureType getQueryType() {
        return queryType;
    }

    public void setQueryType(SimpleFeatureType queryType) {
        this.queryType = queryType;
    }

    public QName getRemoteTypeName() {
        return remoteTypeName;
    }

    public void setRemoteTypeName(final QName remoteName) {
        remoteTypeName = remoteName;
    }
}
