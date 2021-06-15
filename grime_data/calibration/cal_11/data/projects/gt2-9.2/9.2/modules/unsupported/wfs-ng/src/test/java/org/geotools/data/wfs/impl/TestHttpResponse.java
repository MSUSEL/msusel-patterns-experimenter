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
package org.geotools.data.wfs.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.geotools.data.ows.HTTPResponse;

public class TestHttpResponse implements HTTPResponse {

    private String contentType;

    private String charset;

    private String bodyContent;

    private URL contentUrl;

    public TestHttpResponse(String contentType, String charset, String bodyContent) {
        this.contentType = contentType;
        this.charset = charset;
        this.bodyContent = bodyContent;
    }

    public TestHttpResponse(String contentType, String charset, URL contentUrl) {
        this.contentType = contentType;
        this.charset = charset;
        this.contentUrl = contentUrl;
    }

    public TestHttpResponse(String contentType, String charset, InputStream contentInputStream) {
        this.contentType = contentType;
        this.charset = charset;
        BufferedReader reader = new BufferedReader(new InputStreamReader(contentInputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.bodyContent = sb.toString();
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getResponseHeader(String headerName) {
        if ("charset".equalsIgnoreCase(headerName)) {
            return charset;
        }
        return null;
    }

    @Override
    public InputStream getResponseStream() throws IOException {
        if (bodyContent != null) {
            return new ByteArrayInputStream(this.bodyContent.getBytes());
        }
        if (contentUrl != null) {
            return contentUrl.openStream();
        }
        throw new IllegalStateException();
    }

    @Override
    public String getResponseCharset() {
        return charset;
    }
}