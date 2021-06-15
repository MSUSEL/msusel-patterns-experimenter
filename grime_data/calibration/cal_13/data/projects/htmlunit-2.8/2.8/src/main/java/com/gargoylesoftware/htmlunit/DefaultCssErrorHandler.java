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

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

/**
 * HtmlUnit's default implementation of {@link ErrorHandler}, which logs all CSS problems.
 *
 * @version $Revision: 5563 $
 * @author Daniel Gredler
 * @see SilentCssErrorHandler
 */
public class DefaultCssErrorHandler implements ErrorHandler, Serializable {

    /**Serial version UID. */
    private static final long serialVersionUID = 4655126263007765782L;

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(DefaultCssErrorHandler.class);

    /**
     * {@inheritDoc}
     */
    public void error(final CSSParseException exception) {
        LOG.warn("CSS error: " + buildMessage(exception));
    }

    /**
     * {@inheritDoc}
     */
    public void fatalError(final CSSParseException exception) {
        LOG.warn("CSS fatal error: " + buildMessage(exception));
    }

    /**
     * {@inheritDoc}
     */
    public void warning(final CSSParseException exception) {
        LOG.warn("CSS warning: " + buildMessage(exception));
    }

    /**
     * Builds a message for the specified CSS parsing exception.
     * @param exception the CSS parsing exception to build a message for
     * @return a message for the specified CSS parsing exception
     */
    private String buildMessage(final CSSParseException exception) {
        final String uri = exception.getURI();
        final int line = exception.getLineNumber();
        final int col = exception.getColumnNumber();
        final String msg = exception.getMessage();
        return uri + " [" + line + ":" + col + "] " + msg;
    }

}
