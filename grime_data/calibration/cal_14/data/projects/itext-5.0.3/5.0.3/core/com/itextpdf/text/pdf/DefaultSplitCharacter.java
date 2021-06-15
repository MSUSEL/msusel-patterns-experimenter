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
package com.itextpdf.text.pdf;

import com.itextpdf.text.SplitCharacter;

/**
 * The default class that is used to determine whether or not a character
 * is a split character. You can subclass this class to define your own
 * split characters.
 * @since	2.1.2
 */
public class DefaultSplitCharacter implements SplitCharacter {
	
	/**
	 * An instance of the default SplitCharacter.
	 */
	public static final SplitCharacter DEFAULT = new DefaultSplitCharacter();
	
	/**
	 * Checks if a character can be used to split a <CODE>PdfString</CODE>.
	 * <P>
	 * for the moment every character less than or equal to SPACE, the character '-'
	 * and some specific unicode ranges are 'splitCharacters'.
	 * 
	 * @param start start position in the array
	 * @param current current position in the array
	 * @param end end position in the array
	 * @param	cc		the character array that has to be checked
	 * @param ck chunk array
	 * @return	<CODE>true</CODE> if the character can be used to split a string, <CODE>false</CODE> otherwise
	 */
    public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        char c = getCurrentCharacter(current, cc, ck);
        if (c <= ' ' || c == '-' || c == '\u2010') {
            return true;
        }
        if (c < 0x2002)
            return false;
        return ((c >= 0x2002 && c <= 0x200b)
        || (c >= 0x2e80 && c < 0xd7a0)
        || (c >= 0xf900 && c < 0xfb00)
        || (c >= 0xfe30 && c < 0xfe50)
        || (c >= 0xff61 && c < 0xffa0));
    }

    /**
     * Returns the current character
	 * @param current current position in the array
	 * @param	cc		the character array that has to be checked
	 * @param ck chunk array
     * @return	the current character
     */
    protected char getCurrentCharacter(int current, char[] cc, PdfChunk[] ck) {
    	if (ck == null) {
    		return cc[current];
    	}
    	return (char)ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
    }
}
