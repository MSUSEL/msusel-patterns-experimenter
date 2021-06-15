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
package com.itextpdf.text;

import com.itextpdf.text.pdf.PdfChunk;

/** Interface for customizing the split character.
 *
 * @author Paulo Soares
 */

public interface SplitCharacter {
    
    /**
     * Returns <CODE>true</CODE> if the character can split a line. The splitting implementation
     * is free to look ahead or look behind characters to make a decision.
     * <p>
     * The default implementation is:
     * <p>
     * <pre>
     * public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
     *    char c;
     *    if (ck == null)
     *        c = cc[current];
     *    else
     *        c = (char) ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
     *    if (c <= ' ' || c == '-') {
     *        return true;
     *    }
     *    if (c < 0x2e80)
     *        return false;
     *    return ((c >= 0x2e80 && c < 0xd7a0)
     *    || (c >= 0xf900 && c < 0xfb00)
     *    || (c >= 0xfe30 && c < 0xfe50)
     *    || (c >= 0xff61 && c < 0xffa0));
     * }
     * </pre>
     * @param start the lower limit of <CODE>cc</CODE> inclusive
     * @param current the pointer to the character in <CODE>cc</CODE>
     * @param end the upper limit of <CODE>cc</CODE> exclusive
     * @param cc an array of characters at least <CODE>end</CODE> sized
     * @param ck an array of <CODE>PdfChunk</CODE>. The main use is to be able to call
     * {@link PdfChunk#getUnicodeEquivalent(int)}. It may be <CODE>null</CODE>
     * or shorter than <CODE>end</CODE>. If <CODE>null</CODE> no conversion takes place.
     * If shorter than <CODE>end</CODE> the last element is used
     * @return <CODE>true</CODE> if the character(s) can split a line
     */
    public boolean isSplitCharacter(int start, int current, int end, char cc[], PdfChunk ck[]);
}
