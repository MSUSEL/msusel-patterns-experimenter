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
package com.gargoylesoftware.htmlunit.util;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Provides a convenient implementation of the {@link WebResponse} interface that can be subclassed
 * by developers wishing to adapt a particular WebResponse.
 * This class implements the Wrapper or Decorator pattern. Methods default to calling through to the wrapped
 * web response object.
 *
 * @version $Revision: 5835 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class WebResponseWrapper extends WebResponse {

    private static final long serialVersionUID = -5167730179562144482L;

    private final WebResponse wrappedWebResponse_;

    /**
     * Constructs a WebResponse object wrapping provided WebResponse.
     * @param webResponse the webResponse that does the real work
     * @throws IllegalArgumentException if the webResponse is <code>null</code>
     */
    public WebResponseWrapper(final WebResponse webResponse) throws IllegalArgumentException {
        super(null, null, 0);
        if (webResponse == null) {
            throw new IllegalArgumentException("Wrapped WebResponse can't be null");
        }
        wrappedWebResponse_ = webResponse;
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsStream() on the wrapped webResponse object.
     */
    @Override
    public InputStream getContentAsStream() {
        return wrappedWebResponse_.getContentAsStream();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsString() on the wrapped webResponse object.
     */
    @Override
    public String getContentAsString() {
        return wrappedWebResponse_.getContentAsString();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsString(String) on the wrapped webResponse object.
     */
    @Override
    public String getContentAsString(final String encoding) {
        return wrappedWebResponse_.getContentAsString(encoding);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentAsBytes() on the wrapped webResponse object.
     * @deprecated as of 2.8, use either {@link #getContentAsString()} or {@link #getContentAsStream()}.
     */
    @Override
    @Deprecated
    public byte[] getContentAsBytes() {
        return wrappedWebResponse_.getContentAsBytes();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentCharsetOrNull() on the wrapped webResponse object.
     */
    @Override
    public String getContentCharsetOrNull() {
        return wrappedWebResponse_.getContentCharsetOrNull();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentCharset() on the wrapped webResponse object.
     */
    @Override
    public String getContentCharset() {
        return wrappedWebResponse_.getContentCharset();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getContentType() on the wrapped webResponse object.
     */
    @Override
    public String getContentType() {
        return wrappedWebResponse_.getContentType();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getLoadTime() on the wrapped webResponse object.
     */
    @Override
    public long getLoadTime() {
        return wrappedWebResponse_.getLoadTime();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponseHeaders() on the wrapped webResponse object.
     */
    @Override
    public List<NameValuePair> getResponseHeaders() {
        return wrappedWebResponse_.getResponseHeaders();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponseHeaderValue() on the wrapped webResponse object.
     */
    @Override
    public String getResponseHeaderValue(final String headerName) {
        return wrappedWebResponse_.getResponseHeaderValue(headerName);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getStatusCode() on the wrapped webResponse object.
     */
    @Override
    public int getStatusCode() {
        return wrappedWebResponse_.getStatusCode();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getStatusMessage() on the wrapped webResponse object.
     */
    @Override
    public String getStatusMessage() {
        return wrappedWebResponse_.getStatusMessage();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getRequestSettings() on the wrapped webResponse object.
     * @deprecated as of 2.8, please use {@link #getWebRequest()} instead
     */
    @Override
    @Deprecated
    public WebRequest getRequestSettings() {
        return wrappedWebResponse_.getWebRequest();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getWebRequest() on the wrapped webResponse object.
     */
    @Override
    public WebRequest getWebRequest() {
        return wrappedWebResponse_.getWebRequest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getRequestUrl() {
        return getWebRequest().getUrl();
    }
}
