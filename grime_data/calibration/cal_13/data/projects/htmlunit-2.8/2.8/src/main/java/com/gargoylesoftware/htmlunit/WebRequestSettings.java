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

import java.net.URL;

/**
 * Parameter object for making web requests.
 *
 * @version $Revision: 5657 $
 * @author Brad Clarke
 * @author Hans Donner
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Rodney Gitzel
 * @deprecated as of 2.8, please use {@link WebRequest} instead.
 */
@Deprecated
public class WebRequestSettings extends WebRequest {

    private static final long serialVersionUID = -4873887377590383345L;

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL.
     * @param url the target URL
     */
    public WebRequestSettings(final URL url) {
        super(url);
    }

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL using the proxy configuration from the
     * specified original request.
     * @param originalRequest the original request
     * @param url the target URL
     */
    public WebRequestSettings(final WebRequestSettings originalRequest, final URL url) {
        super(originalRequest, url);
    }

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL using the specified HTTP submit method.
     * @param url the target URL
     * @param submitMethod the HTTP submit method to use
     */
    public WebRequestSettings(final URL url, final HttpMethod submitMethod) {
        super(url, submitMethod);
    }
}
