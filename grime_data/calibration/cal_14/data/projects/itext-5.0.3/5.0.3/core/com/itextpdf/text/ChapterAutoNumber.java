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

import com.itextpdf.text.error_messages.MessageLocalization;
/**
 * Chapter with auto numbering.
 *
 * @author Michael Niedermair
 */
public class ChapterAutoNumber extends Chapter {

    // constant
    private static final long serialVersionUID = -9217457637987854167L;

    /**
     * Is the chapter number already set?
     * @since	2.1.4
     */
    protected boolean numberSet = false;
    
    /**
     * Create a new object.
     *
     * @param para     the Chapter title (as a <CODE>Paragraph</CODE>)
     */
    public ChapterAutoNumber(final Paragraph para) {
        super(para, 0);
    }

    /**
     * Create a new object.
     * 
     * @param title	    the Chapter title (as a <CODE>String</CODE>)
     */
    public ChapterAutoNumber(final String title) {
        super(title, 0);
    }

    /**
     * Create a new section for this chapter and ad it.
     *
     * @param title  the Section title (as a <CODE>String</CODE>)
     * @return Returns the new section.
     */
    public Section addSection(final String title) {
    	if (isAddedCompletely()) {
    		throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document"));
    	}
        return addSection(title, 2);
    }

    /**
     * Create a new section for this chapter and add it.
     *
     * @param title  the Section title (as a <CODE>Paragraph</CODE>)
     * @return Returns the new section.
     */
    public Section addSection(final Paragraph title) {
    	if (isAddedCompletely()) {
    		throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document"));
    	}
        return addSection(title, 2);
    }
    
    /**
     * Changes the Chapter number.
     * @param	number	the new chapter number
     * @since 2.1.4
     */
    public int setAutomaticNumber(int number) {
    	if (!numberSet) {
        	number++;
        	super.setChapterNumber(number);
        	numberSet = true;
    	}
		return number;
    }

}
