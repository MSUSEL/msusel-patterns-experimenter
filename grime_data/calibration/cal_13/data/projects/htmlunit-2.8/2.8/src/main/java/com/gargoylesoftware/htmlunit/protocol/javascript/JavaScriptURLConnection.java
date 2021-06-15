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
package com.gargoylesoftware.htmlunit.protocol.javascript;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.gargoylesoftware.htmlunit.TextUtil;

/**
 * A URLConnection for supporting JavaScript URLs.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class JavaScriptURLConnection extends URLConnection {

    /** The JavaScript "URL" prefix. */
    public static final String JAVASCRIPT_PREFIX = "javascript:";

    /** The JavaScript code. */
    private final String content_;

    /**
     * Creates an instance.
     * @param newUrl the JavaScript URL
     */
    public JavaScriptURLConnection(final URL newUrl) {
        super(newUrl);
        content_ = newUrl.toExternalForm().substring(JAVASCRIPT_PREFIX.length());
    }

    /**
     * This method does nothing in this implementation but is required to be implemented.
     */
    @Override
    public void connect() {
        // Empty.
    }

    /**
     * Returns the input stream - in this case the content of the URL.
     * @return the input stream
     */
    @Override
    public InputStream getInputStream() {
        return TextUtil.toInputStream(content_);
    }

}
