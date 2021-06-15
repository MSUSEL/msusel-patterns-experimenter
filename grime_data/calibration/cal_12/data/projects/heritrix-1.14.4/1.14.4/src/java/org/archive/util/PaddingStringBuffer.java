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
package org.archive.util;

/**
 * StringBuffer-like utility which can add spaces to reach a certain column.  It
 * allows you to append {@link String}, <code>long</code> and <code>int</code>s
 * to the buffer.
 * <p>
 * Note: This class counts from 1, not 0.
 * <p>
 * It uses a StringBuffer behind the scenes.
 * <p>
 * To write a string with multiple lines, it is advisible to use the
 * {@link #newline() newline()} function. Regular appending of strings with
 * newlines (\n) character should be safe though. Right appending of strings
 * with such characters is <i>not</i> safe.
 *
 * @author Gordon Mohr
 */
public final class PaddingStringBuffer {
    // The buffer.
    StringBuffer buffer;
    // Location in current line
    int linePos;

    /** 
     * Create a new PaddingStringBuffer
     *
     */
    public PaddingStringBuffer() {
        buffer = new StringBuffer();
        linePos=0;
    }

    /** append a string directly to the buffer
     * @param string the string to append
     * @return This wrapped buffer w/ the passed string appended.
     */
    public PaddingStringBuffer append(String string) {
        buffer.append(string);
        if ( string.indexOf('\n') == -1 ){
            linePos+=string.length();
        } else {
            while ( string.indexOf('\n') == -1 ){
                string = string.substring(string.indexOf('\n'));
            }
            linePos=string.length();
        }
        return this;
    }

    /**
     * Append a string, right-aligned to the given columm.  If the buffer
     * length is already greater than the column specified, it simply appends
     * the string
     *
     * @param col the column to right-align to
     * @param string the string, must not contain multiple lines.
     * @return This wrapped buffer w/ append string, right-aligned to the
     * given column.
     */
    public PaddingStringBuffer raAppend(int col, String string) {
        padTo(col-string.length());
        append(string);
        return this;
    }

    /** Pad to a given column.  If the buffer size is already greater than the
     * column, nothing is done.
     * @param col
     * @return The buffer padded to <code>i</code>.
     */
    public PaddingStringBuffer padTo(int col) {
        while(linePos<col) {
            buffer.append(" ");
            linePos++;
        }
        return this;
    }

    /** append an <code>int</code> to the buffer.
     * @param i the int to append
     * @return This wrapped buffer with <code>i</code> appended.
     */
    public PaddingStringBuffer append(int i) {
        append(Integer.toString(i));
        return this;
    }


    /**
     * Append an <code>int</code> right-aligned to the given column.  If the
     * buffer length is already greater than the column specified, it simply
     * appends the <code>int</code>.
     *
     * @param col the column to right-align to
     * @param i   the int to append
     * @return This wrapped buffer w/ appended int, right-aligned to the
     *         given column.
     */
    public PaddingStringBuffer raAppend(int col, int i) {
        return raAppend(col,Integer.toString(i));
    }

    /** append a <code>long</code> to the buffer.
     * @param lo the <code>long</code> to append
     * @return This wrapped buffer w/ appended long.
     */
    public PaddingStringBuffer append(long lo) {
        append(Long.toString(lo));
        return this;
    }

    /**Append a <code>long</code>, right-aligned to the given column.  If the
     * buffer length is already greater than the column specified, it simply
     * appends the <code>long</code>.
     * @param col the column to right-align to
     * @param lo the long to append
     * @return This wrapped buffer w/ appended long, right-aligned to the
     * given column.
     */
    public PaddingStringBuffer raAppend(int col, long lo) {
        return raAppend(col,Long.toString(lo));
    }

    /** reset the buffer back to empty */
    public void reset() {
        buffer = new StringBuffer();
        linePos = 0;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return buffer.toString();
    }

    /**
     * Forces a new line in the buffer.
     */
    public PaddingStringBuffer newline() {
        buffer.append("\n");
        linePos = 0;
        return this;
    }

}
