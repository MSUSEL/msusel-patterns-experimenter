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

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;

/**
 * <CODE>PdfFont</CODE> is the Pdf Font object.
 * <P>
 * Limitation: in this class only base 14 Type 1 fonts (courier, courier bold, courier oblique,
 * courier boldoblique, helvetica, helvetica bold, helvetica oblique, helvetica boldoblique,
 * symbol, times roman, times bold, times italic, times bolditalic, zapfdingbats) and their
 * standard encoding (standard, MacRoman, (MacExpert,) WinAnsi) are supported.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.7 (page 198-203).
 *
 * @see		PdfName
 * @see		PdfDictionary
 * @see		BadPdfFormatException
 */

class PdfFont implements Comparable<PdfFont> {


    /** the font metrics. */
    private BaseFont font;

    /** the size. */
    private float size;

    /** an image. */
    protected Image image;

    protected float hScale = 1;

    // constructors

    PdfFont(BaseFont bf, float size) {
        this.size = size;
        font = bf;
    }

    // methods

    /**
     * Compares this <CODE>PdfFont</CODE> with another
     *
     * @param	pdfFont	the other <CODE>PdfFont</CODE>
     * @return	a value
     */

    public int compareTo(PdfFont pdfFont) {
        if (image != null)
            return 0;
        if (pdfFont == null) {
            return -1;
        }
        try {
            if (font != pdfFont.font) {
                return 1;
            }
            if (this.size() != pdfFont.size()) {
                return 2;
            }
            return 0;
        }
        catch(ClassCastException cce) {
            return -2;
        }
    }

    /**
     * Returns the size of this font.
     *
     * @return		a size
     */

    float size() {
        if (image == null)
            return size;
        else {
            return image.getScaledHeight();
        }
    }

    /**
     * Returns the approximative width of 1 character of this font.
     *
     * @return		a width in Text Space
     */

    float width() {
        return width(' ');
    }

    /**
     * Returns the width of a certain character of this font.
     *
     * @param		character	a certain character
     * @return		a width in Text Space
     */

    float width(int character) {
        if (image == null)
            return font.getWidthPoint(character, size) * hScale;
        else
            return image.getScaledWidth();
    }

    float width(String s) {
        if (image == null)
            return font.getWidthPoint(s, size) * hScale;
        else
            return image.getScaledWidth();
    }

    BaseFont getFont() {
        return font;
    }

    void setImage(Image image) {
        this.image = image;
    }

    static PdfFont getDefaultFont() {
        try {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);
            return new PdfFont(bf, 12);
        }
        catch (Exception ee) {
            throw new ExceptionConverter(ee);
        }
    }
    void setHorizontalScaling(float hScale) {
        this.hScale = hScale;
    }
}
