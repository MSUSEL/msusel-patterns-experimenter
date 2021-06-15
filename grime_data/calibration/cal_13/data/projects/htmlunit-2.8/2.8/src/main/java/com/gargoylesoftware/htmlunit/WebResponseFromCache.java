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

import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;

/**
 * A {@link WebResponse} implementation to deliver with content from cache. The response
 * is the same but the request may have some variation like an anchor.
 *
 * @version $Revision: 5660 $
 * @author Marc Guillemot
 */
class WebResponseFromCache extends WebResponseWrapper {

    private static final long serialVersionUID = 450330231180187171L;

    private final WebRequest request_;

    /**
     * Wraps the provide response for the given request
     * @param cachedResponse the response from cache
     * @param currentRequest the new request
     */
    WebResponseFromCache(final WebResponse cachedResponse, final WebRequest currentRequest) {
        super(cachedResponse);
        request_ = currentRequest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRequest getRequestSettings() {
        return request_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRequest getWebRequest() {
        return request_;
    }
}
