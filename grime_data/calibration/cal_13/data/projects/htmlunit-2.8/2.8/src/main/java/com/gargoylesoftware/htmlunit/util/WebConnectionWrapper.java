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

import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * <p>Provides a convenient implementation of the {@link WebConnection} interface that can be subclassed by developers
 * wishing to adapt a particular WebConnection.</p>
 *
 * <p>This class implements the Wrapper or Decorator pattern. Methods default to calling through to the wrapped
 * web connection object.</p>
 *
 * @version $Revision: 5660 $
 * @author Marc Guillemot
 */
public class WebConnectionWrapper implements WebConnection {
    private final WebConnection wrappedWebConnection_;

    /**
     * Constructs a WebConnection object wrapping provided WebConnection.
     * @param webConnection the webConnection that does the real work
     * @throws IllegalArgumentException if the connection is <code>null</code>
     */
    public WebConnectionWrapper(final WebConnection webConnection) throws IllegalArgumentException {
        if (webConnection == null) {
            throw new IllegalArgumentException("Wrapped connection can't be null");
        }
        wrappedWebConnection_ = webConnection;
    }

    /**
     * Constructs a WebConnection object wrapping the connection of the WebClient and places itself as
     * connection of the WebClient.
     * @param webClient the WebClient which WebConnection should be wrapped
     * @throws IllegalArgumentException if the WebClient is <code>null</code>
     */
    public WebConnectionWrapper(final WebClient webClient) throws IllegalArgumentException {
        if (webClient == null) {
            throw new IllegalArgumentException("WebClient can't be null");
        }
        wrappedWebConnection_ = webClient.getWebConnection();
        webClient.setWebConnection(this);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponse() on the wrapped connection object.
     */
    public WebResponse getResponse(final WebRequest request) throws IOException {
        return wrappedWebConnection_.getResponse(request);
    }

}
