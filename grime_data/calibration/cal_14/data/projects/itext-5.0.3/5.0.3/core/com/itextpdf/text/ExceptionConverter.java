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
/*
 * The original version of this class was published in an article by professor Heinz Kabutz.
 * Read http://www.javaspecialists.co.za/archive/newsletter.do?issue=033&print=yes&locale=en_US
 * "This material from The Java(tm) Specialists' Newsletter by Maximum Solutions (South Africa).
 * Please contact Maximum Solutions for more information."
 * 
 * Copyright (C) 2001 Dr. Heinz M. Kabutz
 * Permission was granted by Dr. Kabutz to use this source code in iText.
 */
package com.itextpdf.text;

/**
 * The ExceptionConverter changes a checked exception into an
 * unchecked exception.
 */
public class ExceptionConverter extends RuntimeException {
    private static final long serialVersionUID = 8657630363395849399L;
	/** we keep a handle to the wrapped exception */
    private Exception ex;
    /** prefix for the exception */
    private String prefix;

    /**
     * Construct a RuntimeException based on another Exception
     * @param ex the exception that has to be turned into a RuntimeException
     */
    public ExceptionConverter(Exception ex) {
        this.ex = ex;
        prefix = (ex instanceof RuntimeException) ? "" : "ExceptionConverter: ";
    }

    /**
     * Convert an Exception into an unchecked exception. Return the exception if it is
     * already an unchecked exception or return an ExceptionConverter wrapper otherwise
     *
     * @param ex the exception to convert
     * @return an unchecked exception 
     * @since 2.1.6
     */
    public static final RuntimeException convertException(Exception ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        }
        return new ExceptionConverter(ex);
    }

    /**
     * and allow the user of ExceptionConverter to get a handle to it. 
     * @return the original exception
     */
    public Exception getException() {
        return ex;
    }

    /**
     * We print the message of the checked exception 
     * @return message of the original exception
     */
    public String getMessage() {
        return ex.getMessage();
    }

    /**
     * and make sure we also produce a localized version
     * @return localized version of the message
     */
    public String getLocalizedMessage() {
        return ex.getLocalizedMessage();
    }

    /**
     * The toString() is changed to be prefixed with ExceptionConverter 
     * @return String version of the exception
     */
    public String toString() {
        return prefix + ex;
    }

    /** we have to override this as well */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * here we prefix, with s.print(), not s.println(), the stack
     * trace with "ExceptionConverter:" 
     * @param s
     */
    public void printStackTrace(java.io.PrintStream s) {
        synchronized (s) {
            s.print(prefix);
            ex.printStackTrace(s);
        }
    }

    /**
     * Again, we prefix the stack trace with "ExceptionConverter:" 
     * @param s
     */
    public void printStackTrace(java.io.PrintWriter s) {
        synchronized (s) {
            s.print(prefix);
            ex.printStackTrace(s);
        }
    }

    /**
     * requests to fill in the stack trace we will have to ignore.
     * We can't throw an exception here, because this method
     * is called by the constructor of Throwable 
     * @return a Throwable
     */
    public Throwable fillInStackTrace() {
        return this;
    }
}