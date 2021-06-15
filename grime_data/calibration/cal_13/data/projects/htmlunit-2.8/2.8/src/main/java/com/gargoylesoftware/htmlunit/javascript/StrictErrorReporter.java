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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;

import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A JavaScript error reporter that will log all warnings and errors, no matter how trivial.
 *
 * @version $Revision: 5563 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public class StrictErrorReporter implements ErrorReporter, Serializable {

    private static final long serialVersionUID = 2165290829783324770L;
    private static final Log LOG = LogFactory.getLog(StrictErrorReporter.class);

    /**
     * Logs a warning.
     *
     * @param message the message to be displayed
     * @param sourceName the name of the source file
     * @param line the line number
     * @param lineSource the source code that failed
     * @param lineOffset the line offset
     */
    public void warning(
            final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {
        LOG.warn(format("warning", message, sourceName, line, lineSource, lineOffset));
    }

    /**
     * Logs an error.
     *
     * @param message the message to be displayed
     * @param sourceName the name of the source file
     * @param line the line number
     * @param lineSource the source code that failed
     * @param lineOffset the line offset
     */
    public void error(final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {
        LOG.error(format("error", message, sourceName, line, lineSource, lineOffset));
        throw new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    /**
     * Logs a runtime error.
     *
     * @param message the message to be displayed
     * @param sourceName the name of the source file
     * @param line the line number
     * @param lineSource the source code that failed
     * @param lineOffset the line offset
     * @return an evaluator exception
     */
    public EvaluatorException runtimeError(
            final String message, final String sourceName, final int line,
            final String lineSource, final int lineOffset) {
        LOG.error(format("runtimeError", message, sourceName, line, lineSource, lineOffset));
        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    private String format(
            final String prefix, final String message, final String sourceName,
            final int line, final String lineSource, final int lineOffset) {
        return prefix + ": message=[" + message + "] sourceName=[" + sourceName + "] line=[" + line
            + "] lineSource=[" + lineSource + "] lineOffset=[" + lineOffset + "]";
    }
}
