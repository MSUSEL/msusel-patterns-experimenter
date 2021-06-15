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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * An error handler that throws an exception for all problems no matter how minor.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StrictErrorHandler implements ErrorHandler {
    /**
     * A warning occurred.
     *
     * @param rethrow an exception which will be rethrown
     * @throws SAXParseException always
     */
    public void warning(final SAXParseException rethrow) throws SAXParseException {
        throw rethrow;
    }

    /**
     * An error occurred.
     *
     * @param rethrow an exception which will be rethrown
     * @throws SAXParseException always
     */
    public void error(final SAXParseException rethrow) throws SAXParseException {
        throw rethrow;
    }

    /**
     * A fatal error occurred.
     *
     * @param rethrow an exception which will be rethrown
     * @throws SAXParseException always
     */
    public void fatalError(final SAXParseException rethrow) throws SAXParseException {
        throw rethrow;
    }
}
