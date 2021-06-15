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
 * <CODE>PdfNull</CODE> is the Null object represented by the keyword <VAR>null</VAR>.
 * <P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.7'
 * section 3.2.8 (page 63).
 *
 * @see		PdfObject
 */

public class PdfNull extends PdfObject {
    
    // CLASS CONSTANTS
    
    /** An instance of the <CODE>PdfNull</CODE>-object. */
    public static final PdfNull	PDFNULL = new PdfNull();
    
    /** The content of the <CODE>PdfNull</CODE>-object. */
    private static final String CONTENT = "null";
    
    // CONSTRUCTOR
    
    /**
     * Constructs a <CODE>PdfNull</CODE>-object.
     * <P>
     * You never need to do this yourself, you can always use the static final object <VAR>PDFNULL</VAR>.
     */
    public PdfNull() {
        super(NULL, CONTENT);
    }
    
    // CLASS METHOD
    
    public String toString() {
    	return "null";
    }
}