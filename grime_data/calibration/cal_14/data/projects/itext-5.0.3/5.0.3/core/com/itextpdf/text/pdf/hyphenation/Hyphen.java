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
package com.itextpdf.text.pdf.hyphenation;

import java.io.Serializable;

/**
 * This class represents a hyphen. A 'full' hyphen is made of 3 parts:
 * the pre-break text, post-break text and no-break. If no line-break
 * is generated at this position, the no-break text is used, otherwise,
 * pre-break and post-break are used. Typically, pre-break is equal to
 * the hyphen character and the others are empty. However, this general
 * scheme allows support for cases in some languages where words change
 * spelling if they're split across lines, like german's 'backen' which
 * hyphenates 'bak-ken'. BTW, this comes from TeX.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */

public class Hyphen implements Serializable {
    private static final long serialVersionUID = -7666138517324763063L;
	public String preBreak;
    public String noBreak;
    public String postBreak;

    Hyphen(String pre, String no, String post) {
        preBreak = pre;
        noBreak = no;
        postBreak = post;
    }

    Hyphen(String pre) {
        preBreak = pre;
        noBreak = null;
        postBreak = null;
    }

    public String toString() {
        if (noBreak == null 
                && postBreak == null 
                && preBreak != null
                && preBreak.equals("-")) {
            return "-";
                }
        StringBuffer res = new StringBuffer("{");
        res.append(preBreak);
        res.append("}{");
        res.append(postBreak);
        res.append("}{");
        res.append(noBreak);
        res.append('}');
        return res.toString();
    }

}
