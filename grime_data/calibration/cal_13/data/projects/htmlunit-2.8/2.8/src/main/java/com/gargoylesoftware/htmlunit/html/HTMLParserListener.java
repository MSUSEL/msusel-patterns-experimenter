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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Listener for messages from the HTML parser. <br/>
 * The classification of problems as warnings or errors is the one of the HTML parser
 * used by HtmlUnit. The line and column may indicates the position of the problem detected
 * by the parser. This is only an indication and in some cases the position where
 * the problem has to be solved is located lines before.
 *
 * @version $Revision: 5563 $
 * @author Marc Guillemot
 */
public interface HTMLParserListener {

    /**
     * Simple implementation of {@link HTMLParserListener} logging the received warnings
     * and errors in the "com.gargoylesoftware.htmlunit.html.HTMLParserListener" log.<br/>
     * Errors are logged at the error level and warnings at the warning level.
     */
    HTMLParserListener LOG_REPORTER = new SimpleHTMLParserListener();

    /**
     * Called when the HTML parser reports an error.
     * @param message the description of the problem
     * @param url the URL of the document in which the problem occurs
     * @param line the line of the problem
     * @param column the column of the problem
     * @param key the key identifying the "type" of problem
     */
    void error(final String message, final URL url, final int line, final int column, final String key);

    /**
     * Called when the HTML parser reports a warning.
     * @param message the description of the problem
     * @param url the URL of the document in which the problem occurs
     * @param line the line of the problem
     * @param column the column of the problem
     * @param key the key identifying the "type" of problem
     */
    void warning(final String message, final URL url, final int line, final int column, final String key);
}

/**
 * Simple implementation of {@link HTMLParserListener} logging the received warnings
 * and errors in the "com.gargoylesoftware.htmlunit.html.HTMLParserListener" log.<br/>
 * Errors are logged at the error level and warnings at the warning level.
 */
class SimpleHTMLParserListener implements HTMLParserListener {

    private static final Log LOG = LogFactory.getLog(HTMLParserListener.class);

    public void error(final String message, final URL url, final int line, final int column, final String key) {
        LOG.error(format(message, url, line, column));
    }

    public void warning(final String message, final URL url, final int line, final int column, final String key) {
        LOG.warn(format(message, url, line, column));
    }

    private String format(final String message, final URL url, final int line, final int column) {
        final StringBuilder buffer = new StringBuilder(message);
        buffer.append(" (");
        buffer.append(url.toExternalForm());
        buffer.append(" ");
        buffer.append(line);
        buffer.append(":");
        buffer.append(column);
        buffer.append(")");
        return buffer.toString();
    }

}
