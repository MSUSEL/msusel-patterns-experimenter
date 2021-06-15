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
/* SubElement
*
* $Id: SubElement.java 4465 2006-08-08 18:25:42Z stack-sf $
*
* Created on July 26, 2006.
*
* Copyright (C) 2006 Internet Archive.
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.archive.util.anvl;

/**
 * Abstract ANVL 'data element' sub-part.
 * Subclass to make a Comment, a Label, or a Value.
 * @author stack
 */
abstract class SubElement {
    private final String e;

    protected SubElement() {
        this(null);
    }

    public SubElement(final String s) {
        this.e = baseCheck(s);
    }

    protected String baseCheck(final String s) {
        // Check for null.
        if (s == null || s.length() <= 0) {
            throw new IllegalArgumentException("Can't be null or empty");
        }
        // Check for CRLF.
        for (int i = 0; i < s.length(); i++) {
            checkCharacter(s.charAt(i), s, i);
        }
        return s;
    }
    
    protected void checkCharacter(final char c, final String srcStr,
    		final int index) {
        checkControlCharacter(c, srcStr, index);
        checkCRLF(c, srcStr, index);
    }
    
    protected void checkControlCharacter(final char c, final String srcStr,
            final int index) {
        if (Character.isISOControl(c) && !Character.isWhitespace(c) ||
                !Character.isValidCodePoint(c)) {
            throw new IllegalArgumentException(srcStr +
                " contains a control character(s) or invalid code point: 0x" +
                Integer.toHexString(c));
        }
    }
    
    protected void checkCRLF(final char c, final String srcStr,
            final int index) {
        if (ANVLRecord.isCROrLF(c)) {
            throw new IllegalArgumentException(srcStr +
                " contains disallowed CRLF control character(s): 0x" +
                Integer.toHexString(c));
        }
    }
    
    @Override
    public String toString() {
        return e;
    }
}