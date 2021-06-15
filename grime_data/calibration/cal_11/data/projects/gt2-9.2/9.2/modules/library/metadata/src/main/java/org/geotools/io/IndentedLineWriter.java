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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

import org.geotools.util.Utilities;


/**
 * A writer that put some spaces in front of every lines. The indentation is initially set
 * to 0 spaces. Users must invoke {@link #setIndentation} in order to set a different value.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @since 2.4
 */
public class IndentedLineWriter extends FilterWriter {
    /**
     * A string with a length equals to the indentation.
     */
    private String margin = "";

    /**
     * {@code true} if we are about to write a new line.
     */
    private boolean newLine = true;

    /**
     * {@code true} if we are waiting for a '\n' character.
     */
    private boolean waitLF;

    /**
     * Constructs a stream which will add spaces in front of each line.
     *
     * @param out The underlying stream to write to.
     */
    public IndentedLineWriter(final Writer out) {
        super(out);
    }

    /**
     * Returns the current indentation.
     *
     * @return The current indentation.
     */
    public int getIdentation() {
        return margin.length();
    }

    /**
     * Sets the indentation to the specified value.
     *
     * @param width The new indentation.
     */
    public void setIndentation(final int width) {
        synchronized (lock) {
            margin = Utilities.spaces(width);
        }
    }

    /**
     * Invoked when a new line is begining. The default implementation writes the
     * amount of spaces specified by the last call to {@link #setIndentation}.
     *
     * @throws IOException If an I/O error occurs
     */
    protected void beginNewLine() throws IOException {
        out.write(margin);
    }

    /**
     * Writes the specified character.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void doWrite(final int c) throws IOException {
        assert Thread.holdsLock(lock);
        if (newLine && (c!='\n' || !waitLF)) {
            beginNewLine();
        }
        out.write(c);
        if ((newLine = (c=='\r' || c=='\n')) == true) {
            waitLF = (c=='\r');
        }
    }

    /**
     * Writes a single character.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final int c) throws IOException {
        synchronized (lock) {
            doWrite(c);
        }
    }

    /**
     * Writes a portion of an array of characters.
     *
     * @param  buffer  Buffer of characters to be written.
     * @param  offset  Offset from which to start reading characters.
     * @param  length  Number of characters to be written.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final char[] buffer, int offset, final int length) throws IOException {
        final int upper = offset + length;
        synchronized (lock) {
check:      while (offset < upper) {
                if (newLine) {
                    doWrite(buffer[offset++]);
                    continue;
                }
                final int lower = offset;
                do {
                    final char c = buffer[offset];
                    if (c=='\r' || c=='\n') {
                        out.write(buffer, lower, offset-lower);
                        doWrite(c);
                        offset++;
                        continue check;
                    }
                } while (++offset < upper);
                out.write(buffer, lower, offset-lower);
                break;
            }
        }
    }

    /**
     * Writes a portion of a string.
     *
     * @param  string  String to be written.
     * @param  offset  Offset from which to start reading characters.
     * @param  length  Number of characters to be written.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(final String string, int offset, final int length) throws IOException {
        final int upper = offset + length;
        synchronized (lock) {
check:      while (offset < upper) {
                if (newLine) {
                    doWrite(string.charAt(offset++));
                    continue;
                }
                final int lower = offset;
                do {
                    final char c = string.charAt(offset);
                    if (c=='\r' || c=='\n') {
                        out.write(string, lower, offset-lower);
                        doWrite(c);
                        offset++;
                        continue check;
                    }
                } while (++offset < upper);
                out.write(string, lower, offset-lower);
                break;
            }
        }
    }
}
