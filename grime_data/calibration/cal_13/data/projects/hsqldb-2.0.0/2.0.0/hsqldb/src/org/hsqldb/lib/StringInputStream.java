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

/**
 * minimal InputStream subclass to fetch bytes form a String
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.7.0
 */
public class StringInputStream extends InputStream {

    protected int    strOffset  = 0;
    protected int    charOffset = 0;
    protected int    available;
    protected String str;

    public StringInputStream(String s) {
        str       = s;
        available = s.length() * 2;
    }

    public int read() throws java.io.IOException {

        if (available == 0) {
            return -1;
        }

        available--;

        char c = str.charAt(strOffset);

        if (charOffset == 0) {
            charOffset = 1;

            return (c & 0x0000ff00) >> 8;
        } else {
            charOffset = 0;

            strOffset++;

            return c & 0x000000ff;
        }
    }

    public int available() throws IOException {
        return available;
    }
}
