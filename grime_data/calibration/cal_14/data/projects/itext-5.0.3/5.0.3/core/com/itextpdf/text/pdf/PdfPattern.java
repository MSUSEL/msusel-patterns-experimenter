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

/**
 * A <CODE>PdfPattern</CODE> defines a ColorSpace
 *
 * @see		PdfStream
 */

public class PdfPattern extends PdfStream {
    
	/**
	 * Creates a PdfPattern object.
	 * @param	painter	a pattern painter instance
	 */
	PdfPattern(PdfPatternPainter painter) {
		this(painter, DEFAULT_COMPRESSION);
	}

	/**
	 * Creates a PdfPattern object.
	 * @param	painter	a pattern painter instance
	 * @param	compressionLevel the compressionLevel for the stream
	 * @since	2.1.3
	 */
    PdfPattern(PdfPatternPainter painter, int compressionLevel) {
        super();
        PdfNumber one = new PdfNumber(1);
        PdfArray matrix = painter.getMatrix();
        if ( matrix != null ) {
            put(PdfName.MATRIX, matrix);
        }
        put(PdfName.TYPE, PdfName.PATTERN);
        put(PdfName.BBOX, new PdfRectangle(painter.getBoundingBox()));
        put(PdfName.RESOURCES, painter.getResources());
        put(PdfName.TILINGTYPE, one);
        put(PdfName.PATTERNTYPE, one);
        if (painter.isStencil())
            put(PdfName.PAINTTYPE, new PdfNumber(2));
        else
            put(PdfName.PAINTTYPE, one);
        put(PdfName.XSTEP, new PdfNumber(painter.getXStep()));
        put(PdfName.YSTEP, new PdfNumber(painter.getYStep()));
        bytes = painter.toPdf(null);
        put(PdfName.LENGTH, new PdfNumber(bytes.length));
        try {
            flateCompress(compressionLevel);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
