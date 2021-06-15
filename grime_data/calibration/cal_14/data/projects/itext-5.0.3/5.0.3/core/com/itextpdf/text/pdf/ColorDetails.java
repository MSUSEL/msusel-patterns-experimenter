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

/** Each spotcolor in the document will have an instance of this class
 *
 * @author Phillip Pan (phillip@formstar.com)
 */
class ColorDetails {

    /** The indirect reference to this color
     */
    PdfIndirectReference indirectReference;
    /** The color name that appears in the document body stream
     */
    PdfName colorName;
    /** The color
     */
    PdfSpotColor spotcolor;

    /** Each spot color used in a document has an instance of this class.
     * @param colorName the color name
     * @param indirectReference the indirect reference to the font
     * @param scolor the <CODE>PDfSpotColor</CODE>
     */
    ColorDetails(PdfName colorName, PdfIndirectReference indirectReference, PdfSpotColor scolor) {
        this.colorName = colorName;
        this.indirectReference = indirectReference;
        this.spotcolor = scolor;
    }

    /** Gets the indirect reference to this color.
     * @return the indirect reference to this color
     */
    PdfIndirectReference getIndirectReference() {
        return indirectReference;
    }

    /** Gets the color name as it appears in the document body.
     * @return the color name
     */
    PdfName getColorName() {
        return colorName;
    }

    /** Gets the <CODE>SpotColor</CODE> object.
     * @return the <CODE>PdfSpotColor</CODE>
     */
    PdfObject getSpotColor(PdfWriter writer) {
        return spotcolor.getSpotObject(writer);
    }
}
