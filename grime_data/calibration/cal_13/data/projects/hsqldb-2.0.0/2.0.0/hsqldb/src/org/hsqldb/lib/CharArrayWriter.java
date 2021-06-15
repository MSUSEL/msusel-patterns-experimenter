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

package org.hsqldb.lib;

import java.io.IOException;
import java.io.Reader;
import java.io.EOFException;

/**
 * A writer for char strings.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class CharArrayWriter {

    protected char[] buffer;
    protected int    count;

    public CharArrayWriter(char[] buffer) {
        this.buffer = buffer;
    }

    public CharArrayWriter(Reader reader, int length) throws IOException {

        buffer = new char[length];

        for (int left = length; left > 0; ) {
            int read = reader.read(buffer, count, left);

            if (read == -1) {
                if (left > 0) {
                    reader.close();

                    throw new EOFException();
                }

                break;
            }

            left  -= read;
            count += read;
        }
    }

    public CharArrayWriter(Reader reader) throws IOException {

        buffer = new char[128];

        for (;;) {
            int read = reader.read(buffer, count, buffer.length - count);

            if (read == -1) {
                break;
            }

            count += read;

            if (count == buffer.length) {
                ensureRoom(128);
            }
        }
    }

    public void write(int c) {

        if (count == buffer.length) {
            ensureRoom(count + 1);
        }

        buffer[count++] = (char) c;
    }

    void ensureRoom(int size) {

        if (size <= buffer.length) {
            return;
        }

        int newSize = buffer.length;

        while (newSize < size) {
            newSize *= 2;
        }

        char[] newBuffer = new char[newSize];

        System.arraycopy(buffer, 0, newBuffer, 0, count);

        buffer = newBuffer;
    }

    public void write(String str, int off, int len) {

        ensureRoom(count + len);
        str.getChars(off, off + len, buffer, count);

        count += len;
    }

    public void reset() {
        count = 0;
    }

    public void reset(char[] buffer) {
        count       = 0;
        this.buffer = buffer;
    }

    public char[] toCharArray() {

        char[] newBuffer = new char[count];

        System.arraycopy(buffer, 0, newBuffer, 0, count);

        return (char[]) newBuffer;
    }

    public int size() {
        return count;
    }

    /**
     * Converts input data to a string.
     * @return the string.
     */
    public String toString() {
        return new String(buffer, 0, count);
    }
}
