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
 * An exception that is thrown when the server returns a failing status code.
 *
 * @version $Revision: 5658 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public class FailingHttpStatusCodeException extends RuntimeException {

    private static final long serialVersionUID = 4080165207084775250L;

    private final WebResponse response_;

    /**
     * Creates an instance.
     * @param failingResponse the failing response
     */
    public FailingHttpStatusCodeException(final WebResponse failingResponse) {
        this(buildMessage(failingResponse), failingResponse);
    }

    /**
     * Creates an instance.
     * @param message the message
     * @param failingResponse the failing response
     */
    FailingHttpStatusCodeException(final String message, final WebResponse failingResponse) {
        super(message);
        response_ = failingResponse;
    }

    /**
     * Returns the failing status code.
     * @return the code
     */
    public int getStatusCode() {
        return response_.getStatusCode();
    }

    /**
     * Returns the message associated with the failing status code.
     * @return the message
     */
    public String getStatusMessage() {
        return response_.getStatusMessage();
    }

    private static String buildMessage(final WebResponse failingResponse) {
        final int code = failingResponse.getStatusCode();
        final String msg = failingResponse.getStatusMessage();
        final URL url = failingResponse.getWebRequest().getUrl();
        return code + " " + msg + " for " + url;
    }

    /**
     * Gets the failing response.
     * @return the response
     */
    public WebResponse getResponse() {
        return response_;
    }

}
