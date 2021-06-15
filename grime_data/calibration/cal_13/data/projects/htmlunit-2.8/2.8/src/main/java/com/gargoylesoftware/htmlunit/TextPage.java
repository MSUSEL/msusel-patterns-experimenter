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
 * A generic page that will be returned for any text related content.
 * Specifically any content types that start with "text/"
 *
 * @version $Revision: 5764 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 */
public class TextPage implements Page {

    /** Serial version UID. */
    private static final long serialVersionUID = -3338485355330723848L;

    private final WebResponse webResponse_;
    private final String content_;
    private WebWindow enclosingWindow_;

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     */
    public TextPage(final WebResponse webResponse, final WebWindow enclosingWindow) {
        webResponse_ = webResponse;
        content_ = webResponse.getContentAsString();
        enclosingWindow_ = enclosingWindow;
    }

    /**
     * Initializes this page.
     */
    public void initialize() {
    }

    /**
     * Cleans up this page.
     */
    public void cleanUp() {
    }

    /**
     * Returns the content of this page.
     *
     * @return the content of this page
     */
    public String getContent() {
        return content_;
    }

    /**
     * Returns the web response that was originally used to create this page.
     *
     * @return the web response that was originally used to create this page
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * Returns the window that this page is sitting inside.
     *
     * @return the enclosing frame or null if this page isn't inside a frame
     */
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }

    /**
     * Returns the URL of this page.
     * @return the URL of this page
     */
    public URL getUrl() {
        return getWebResponse().getWebRequest().getUrl();
    }
}
