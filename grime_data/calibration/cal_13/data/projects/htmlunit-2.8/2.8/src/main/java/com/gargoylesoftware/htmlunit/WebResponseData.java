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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Simple data object to simplify WebResponse creation.
 *
 * @version $Revision: 5834 $
 * @author Brad Clarke
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class WebResponseData implements Serializable {

    private static final long serialVersionUID = 2979956380280496543L;

    private final int statusCode_;
    private final String statusMessage_;
    private final List<NameValuePair> responseHeaders_;
    private final DownloadedContent downloadedContent_;

    /**
     * Constructs with a raw byte[] (mostly for testing).
     *
     * @param body              Body of this response
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     * @throws IOException on stream errors
     */
    public WebResponseData(final byte[] body, final int statusCode, final String statusMessage,
            final List<NameValuePair> responseHeaders) throws IOException {
        this(new DownloadedContent.InMemory(body), statusCode, statusMessage, responseHeaders);
    }

    /**
     * Constructs with a data stream to minimize copying of the entire body.
     *
     * @param bodyStream        Stream of this response's body
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     *
     * @throws IOException on stream errors
     * @deprecated As of HtmlUnit-2.8.
     */
    @Deprecated
    public WebResponseData(final InputStream bodyStream, final int statusCode,
            final String statusMessage, final List<NameValuePair> responseHeaders) throws IOException {
        this(HttpWebConnection.downloadContent(bodyStream), statusCode, statusMessage, responseHeaders);
    }

    /**
     * Constructs without data stream for subclasses that override getBody().
     *
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     *
     * @throws IOException on stream errors
     */
    protected WebResponseData(final int statusCode,
            final String statusMessage, final List<NameValuePair> responseHeaders) throws IOException {
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);
        downloadedContent_ = new DownloadedContent.InMemory(ArrayUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * Constructor.
     * @param responseBody the downloaded response body
     * @param statusCode        Status code from the server
     * @param statusMessage     Status message from the server
     * @param responseHeaders   Headers in this response
     * @throws IOException on stream errors
     */
    public WebResponseData(final DownloadedContent responseBody, final int statusCode, final String statusMessage,
            final List<NameValuePair> responseHeaders) throws IOException {
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        responseHeaders_ = Collections.unmodifiableList(responseHeaders);
        downloadedContent_ = responseBody;
    }

    private InputStream getStream(InputStream stream, final List<NameValuePair> headers) throws IOException {
        if (stream == null) {
            return null;
        }
        String encoding = null;
        for (final NameValuePair header : headers) {
            final String headerName = header.getName().trim();
            if (headerName.equalsIgnoreCase("content-encoding")) {
                encoding = header.getValue();
                break;
            }
        }
        if (encoding != null && StringUtils.contains(encoding, "gzip")) {
            stream = new GZIPInputStream(stream);
        }
        else if (encoding != null && StringUtils.contains(encoding, "deflate")) {
            stream = new InflaterInputStream(stream);
        }
        return stream;
    }

    /**
     * Returns the response body.
     * This may cause memory problem for very large responses.
     * @return response body
     */
    public byte[] getBody() {
        try {
            return IOUtils.toByteArray(getInputStream());
        }
        catch (final IOException e) {
            throw new RuntimeException(e); // shouldn't we allow the method to throw IOException?
        }
    }

    /**
     * Returns a new {@link InputStream} allowing to read the downloaded content.
     * @return the associated InputStream
     */
    public InputStream getInputStream() {
        try {
            return getStream(downloadedContent_.getInputStream(), getResponseHeaders());
        }
        catch (final IOException e) {
            throw new RuntimeException(e); // in fact getInputStream should probably have throw declaration
        }
    }

    /**
     * @return response headers
     */
    public List<NameValuePair> getResponseHeaders() {
        return responseHeaders_;
    }

    /**
     * @return response status code
     */
    public int getStatusCode() {
        return statusCode_;
    }

    /**
     * @return response status message
     */
    public String getStatusMessage() {
        return statusMessage_;
    }
}
