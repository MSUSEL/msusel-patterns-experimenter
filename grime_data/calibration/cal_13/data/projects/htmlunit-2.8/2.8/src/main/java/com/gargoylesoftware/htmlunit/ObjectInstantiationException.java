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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Thrown if an object could not be instantiated for some reason.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ObjectInstantiationException extends RuntimeException {
    private static final long serialVersionUID = 8831953284047722098L;
    private final Throwable causeException_;

    /**
     * Creates a new instance.
     * @param message a message explaining the failure
     * @param cause the exception that was thrown
     */
    public ObjectInstantiationException(final String message, final Throwable cause) {
        super(message);
        causeException_ = cause;
    }

    /**
     * Returns the exception that had been thrown during instantiation of the object.
     * @return the cause exception
     */
    public Throwable getCauseException() {
        return causeException_;
    }

    /**
     * Print the stack trace. If this exception contains another exception then
     * the stack traces for both will be printed.
     *
     * @param writer  Where the stack trace will be written
     */
    @Override
    public void printStackTrace(final PrintWriter writer) {
        super.printStackTrace(writer);
        if (causeException_ != null) {
            writer.write("Enclosed exception: ");
            causeException_.printStackTrace(writer);
        }
    }

    /**
     * Print the stack trace. If this exception contains another exception then
     * the stack traces for both will be printed.
     *
     * @param stream Where the stack trace will be written
     */
    @Override
    public void printStackTrace(final PrintStream stream) {
        super.printStackTrace(stream);
        if (causeException_ != null) {
            stream.print("Enclosed exception: ");
            causeException_.printStackTrace(stream);
        }
    }
}
