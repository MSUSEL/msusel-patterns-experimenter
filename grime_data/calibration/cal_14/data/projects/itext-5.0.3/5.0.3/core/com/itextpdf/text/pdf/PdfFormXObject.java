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

/**
 * <CODE>PdfFormObject</CODE> is a type of XObject containing a template-object.
 */

public class PdfFormXObject extends PdfStream {
    
    // public static final variables
    
/** This is a PdfNumber representing 0. */
    public static final PdfNumber ZERO = new PdfNumber(0);
    
/** This is a PdfNumber representing 1. */
    public static final PdfNumber ONE = new PdfNumber(1);
    
/** This is the 1 - matrix. */
    public static final PdfLiteral MATRIX = new PdfLiteral("[1 0 0 1 0 0]");
    
/**
 * Constructs a <CODE>PdfFormXObject</CODE>-object.
 *
 * @param	template			the template
 * @param	compressionLevel	the compression level for the stream
 * @since	2.1.3 (Replacing the existing constructor with param compressionLevel)
 */
    
    PdfFormXObject(PdfTemplate template, int compressionLevel) // throws BadPdfFormatException
    {
        super();
        put(PdfName.TYPE, PdfName.XOBJECT);
        put(PdfName.SUBTYPE, PdfName.FORM);
        put(PdfName.RESOURCES, template.getResources());
        put(PdfName.BBOX, new PdfRectangle(template.getBoundingBox()));
        put(PdfName.FORMTYPE, ONE);
        if (template.getLayer() != null)
            put(PdfName.OC, template.getLayer().getRef());
        if (template.getGroup() != null)
            put(PdfName.GROUP, template.getGroup());
        PdfArray matrix = template.getMatrix();
        if (matrix == null)
            put(PdfName.MATRIX, MATRIX);
        else
            put(PdfName.MATRIX, matrix);
        bytes = template.toPdf(null);
        put(PdfName.LENGTH, new PdfNumber(bytes.length));
        flateCompress(compressionLevel);
    }
    
}
