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
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface by which an {@link AbstractOpenWebService} retrieves the contents of an HTTP request
 * issued through its {@link HTTPClient}.
 * <p>
 * An HTTPResponse instance shall be {@link #dispose() disposed} as soon as it's
 * {@link #getResponseStream() response stream} has been consumed or is no longer needed. It's up to
 * the implementations to just close the actual {@link InputStream} or return the http connection to
 * the connection pool, or any other resource clean up task that needs to be done.
 * 
 * @author groldan
 * @see HTTPClient
 */
public interface HTTPResponse {

    /**
     * Disposes this HTTP response and cleans up any resource being held.
     * <p>
     * Multiple invocations of this method shall not raise an exception but return silently.
     */
    public void dispose();

    /**
     * Short cut for {@code getResponseHeader("Content-Type")}
     */
    public String getContentType();

    /**
     * Returns the value of the requested HTTP response header, or {@code null} if not set.
     */
    public String getResponseHeader(String headerName);

    /**
     * Returns the HTTP response content byte stream, automatically recognizing gzip encoded
     * responses and returning an uncompressing stream if that's the case.
     * 
     * @throws IOException
     *             if such happens when obtaining the response stream.
     */
    public InputStream getResponseStream() throws IOException;

    /**
     * @return the response charset parsed out of the content type response header, if any, or
     *         {@code null} otherwise.
     */
    public String getResponseCharset();

}