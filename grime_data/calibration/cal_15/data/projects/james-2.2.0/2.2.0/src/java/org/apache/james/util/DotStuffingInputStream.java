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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Removes the dot-stuffing happening during the NNTP and SMTP message
 * transfer
 */
public class DotStuffingInputStream extends FilterInputStream {
    /**
     * An array to hold the last two bytes read off the stream.
     * This allows the stream to detect '\r\n' sequences even
     * when they occur across read boundaries.
     */
    protected int last[] = new int[2];

    public DotStuffingInputStream(InputStream in) {
        super(in);
        last[0] = -1;
        last[1] = -1;
    }

    /**
     * Read through the stream, checking for '\r\n.'
     *
     * @return the byte read from the stream
     */
    public int read() throws IOException {
        int b = in.read();
        if (b == '.' && last[0] == '\r' && last[1] == '\n') {
            //skip this '.' because it should have been stuffed
            b = in.read();
        }
        last[0] = last[1];
        last[1] = b;
        return b;
    }

    /**
     * Read through the stream, checking for '\r\n.'
     *
     * @param b the byte array into which the bytes will be read
     * @param off the offset into the byte array where the bytes will be inserted
     * @param len the maximum number of bytes to be read off the stream
     * @return the number of bytes read
     */
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
               ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;

        for (; i < len ; i++) {
            c = read();
            if (c == -1) {
                break;
            }
            if (b != null) {
                b[off + i] = (byte)c;
            }
        }

        return i;
    }
}
