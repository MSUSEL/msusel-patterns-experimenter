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
package org.archive.io;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A decorator on IOException for IOEs that are likely not fatal or at least
 * merit retry.
 * @author stack
 * @version $Date: 2006-08-23 18:39:37 +0000 (Wed, 23 Aug 2006) $, $Revision: 4530 $
 */
public class RecoverableIOException extends IOException {
    private static final long serialVersionUID = 6194776587381865451L;
    private final IOException decoratedIOException;

    public RecoverableIOException(final String message) {
        this(new IOException(message));
    }

    public RecoverableIOException(final IOException ioe) {
        super();
        this.decoratedIOException = ioe;
    }

    public Throwable getCause() {
        return this.decoratedIOException.getCause();
    }

    public String getLocalizedMessage() {
        return this.decoratedIOException.getLocalizedMessage();
    }

    public String getMessage() {
        return this.decoratedIOException.getMessage();
    }

    public StackTraceElement[] getStackTrace() {
        return this.decoratedIOException.getStackTrace();
    }

    public synchronized Throwable initCause(Throwable cause) {
        return this.decoratedIOException.initCause(cause);
    }

    public void printStackTrace() {
        this.decoratedIOException.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        this.decoratedIOException.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s) {
        this.decoratedIOException.printStackTrace(s);
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.decoratedIOException.setStackTrace(stackTrace);
    }

    public String toString() {
        return this.decoratedIOException.toString();
    }
}