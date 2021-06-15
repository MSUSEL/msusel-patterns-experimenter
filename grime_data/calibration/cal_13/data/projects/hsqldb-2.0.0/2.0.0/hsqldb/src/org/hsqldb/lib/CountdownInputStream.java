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
import java.io.InputStream;

// fredt@users - 1.9.0 corrected read(byte[], int, int)

/**
 * Counts down from a specified value the number of bytes actually read
 * from the wrapped InputStream. <p>
 *
 * Returns minus one (-1) early from readXXX methods if the count
 * down reaches zero (0) before the end of the wrapped InputStream
 * is encountered. <p>
 *
 * This class is especially useful when a fixed number of bytes is to be read
 * from an InputStream that is in turn to be used as the source for an
 * {@link java.io.InputStreamReader InputStreamReader}.
 *
 * @author boucherb@users
 * @version 1.9.0
 * @since 1.9.0
 */
public final class CountdownInputStream extends InputStream {

    private long        count;
    private InputStream input;

    public CountdownInputStream(final InputStream is) {
        this.input = is;
    }

    public int read() throws IOException {

        if (this.count <= 0) {
            return -1;
        }

        final int b = this.input.read();

        if (b >= 0) {
            this.count--;
        }

        return b;
    }

    public int read(final byte[] buf) throws IOException {

        if (this.count <= 0) {
            return -1;
        }

        int len = buf.length;

        if (len > this.count) {
            len = (int) this.count;
        }

        final int r = this.input.read(buf, 0, len);

        if (r > 0) {
            this.count -= r;
        }

        return r;
    }

    public int read(final byte[] buf, final int off,
                    int len) throws IOException {

        if (this.count <= 0) {
            return -1;
        }

        if (len > this.count) {
            len = (int) this.count;
        }

        final int r = this.input.read(buf, off, len);

        if (r > 0) {
            this.count -= r;
        }

        return r;
    }

    public void close() throws IOException {
        this.input.close();
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
