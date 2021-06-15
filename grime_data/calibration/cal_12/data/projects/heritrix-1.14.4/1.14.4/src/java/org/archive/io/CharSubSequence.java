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


/**
 * Provides a subsequence view onto a CharSequence.
 *
 * @author gojomo
 * @version $Revision: 3288 $, $Date: 2005-03-31 17:43:23 +0000 (Thu, 31 Mar 2005) $
 */
public class CharSubSequence implements CharSequence {

    CharSequence inner;
    int start;
    int end;

    public CharSubSequence(CharSequence inner, int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("Start " + start + " is > " +
                " than end " + end);
        }

        if (end < 0 || start < 0) {
            throw new IllegalArgumentException("Start " + start + " or end " +
                end + " is < 0.");
        }

        if (inner ==  null) {
            throw new NullPointerException("Passed charsequence is null.");
        }

        this.inner = inner;
        this.start = start;
        this.end = end;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.CharSequence#length()
     */
    public int length() {
        return this.end - this.start;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.CharSequence#charAt(int)
     */
    public char charAt(int index) {
        return this.inner.charAt(this.start + index);
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.CharSequence#subSequence(int, int)
     */
    public CharSequence subSequence(int begin, int finish) {
        return new CharSubSequence(this, begin, finish);
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.CharSequence#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(length());
        // could use StringBuffer.append(CharSequence) if willing to do 1.5 & up
        for (int i = 0;i<length();i++) {
            sb.append(charAt(i)); 
        }
        return sb.toString();
    }
}
