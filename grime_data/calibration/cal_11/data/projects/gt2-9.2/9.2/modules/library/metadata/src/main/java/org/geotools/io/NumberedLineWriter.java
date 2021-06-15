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
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.geotools.util.Utilities;
import org.geotools.resources.Arguments;


/**
 * A writer that put line number in front of every line.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class NumberedLineWriter extends IndentedLineWriter {
    /**
     * A default numbered line writer to the {@linkplain System#out standard output stream}.
     * The {@link #close} method on this stream will only flush it without closing it.
     */
    public static final PrintWriter OUT =
            new PrintWriter(new Uncloseable(Arguments.getWriter(System.out)), true);

    /**
     * A stream that can never been closed. Used only for wrapping the
     * {@linkplain System#out standard output stream}.
     */
    private static final class Uncloseable extends NumberedLineWriter {
        /** Constructs a stream. */
        public Uncloseable(final Writer out) {
            super(out);
        }

        /** Flush the stream without closing it. */
        @Override
        public void close() throws IOException {
            flush();
        }
    }

    /**
     * The with reserved for line numbers (not counting the space for "[ ]" brackets).
     */
    private int width = 3;

    /**
     * The current line number.
     */
    private int current = 1;

    /**
     * Constructs a stream which will write line number in front of each line.
     *
     * @param out The underlying stream to write to.
     */
    public NumberedLineWriter(final Writer out) {
        super(out);
    }

    /**
     * Returns the current line number.
     *
     * @return The current line number.
     */
    public int getLineNumber() {
        return current;
    }

    /**
     * Sets the current line number.
     *
     * @param line The current line number.
     */
    public void setLineNumber(final int line) {
        synchronized (lock) {
            this.current = line;
        }
    }

    /**
     * Invoked when a new line is begining. The default implementation writes the
     * current line number.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void beginNewLine() throws IOException {
        final String number = String.valueOf(current++);
        out.write('[');
        out.write(Utilities.spaces(width - number.length()));
        out.write(number);
        out.write("] ");
    }
}
