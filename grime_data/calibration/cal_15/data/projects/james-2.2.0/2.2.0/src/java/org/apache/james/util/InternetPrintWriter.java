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
package org.apache.james.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Writes to a wrapped Writer class, ensuring that all line separators are '\r\n', regardless
 * of platform.
 */
public class InternetPrintWriter
    extends PrintWriter {

    /**
     * The line separator to use.
     */
    private static String lineSeparator = "\r\n";

    /**
     * Whether the Writer autoflushes on line feeds
     */
    private final boolean autoFlush;

    /**
     * Constructor that takes a writer to wrap.
     *
     * @param out the wrapped Writer
     */
    public InternetPrintWriter (Writer out) {
        super (out);
        autoFlush = false;
    }

    /**
     * Constructor that takes a writer to wrap.
     *
     * @param out the wrapped Writer
     * @param autoFlush whether to flush after each print call
     */
    public InternetPrintWriter (Writer out, boolean autoFlush) {
        super (out, autoFlush);
        this.autoFlush = autoFlush;
    }

    /**
     * Constructor that takes a stream to wrap.
     *
     * @param out the wrapped OutputStream
     */
    public InternetPrintWriter (OutputStream out) {
        super (out);
        autoFlush = false;
    }

    /**
     * Constructor that takes a stream to wrap.
     *
     * @param out the wrapped OutputStream
     * @param autoFlush whether to flush after each print call
     */
    public InternetPrintWriter (OutputStream out, boolean autoFlush) {
        super (out, autoFlush);
        this.autoFlush = autoFlush;
    }

    /**
     * Print a line separator.
     */
    public void println () {
        synchronized (lock) {
            write(lineSeparator);
            if (autoFlush) {
                flush();
            }
        }
    }

    /**
     * Print a boolean followed by a line separator.
     *
     * @param x the boolean to print
     */
    public void println(boolean x) {
        synchronized (lock) {
            print(x);
            println();
        }
    }

    /**
     * Print a char followed by a line separator.
     *
     * @param x the char to print
     */
    public void println(char x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }

    /**
     * Print a int followed by a line separator.
     *
     * @param x the int to print
     */
    public void println (int x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }

    /**
     * Print a long followed by a line separator.
     *
     * @param x the long to print
     */
    public void println (long x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }

    /**
     * Print a float followed by a line separator.
     *
     * @param x the float to print
     */
    public void println (float x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }

    /**
     * Print a double followed by a line separator.
     *
     * @param x the double to print
     */
    public void println (double x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }

    /**
     * Print a character array followed by a line separator.
     *
     * @param x the character array to print
     */
    public void println (char[] x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }

    /**
     * Print a String followed by a line separator.
     *
     * @param x the String to print
     */
    public void println (String x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }

    /**
     * Print an Object followed by a line separator.
     *
     * @param x the Object to print
     */
    public void println (Object x) {
        synchronized (lock) {
            print (x);
            println ();
        }
    }
}
