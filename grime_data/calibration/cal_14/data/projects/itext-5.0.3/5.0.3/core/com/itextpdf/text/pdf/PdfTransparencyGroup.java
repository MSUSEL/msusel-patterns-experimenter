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

/** The transparency group dictionary.
 *
 * @author Paulo Soares
 */
public class PdfTransparencyGroup extends PdfDictionary {
    
    /**
     * Constructs a transparencyGroup.
     */
    public PdfTransparencyGroup() {
        super();
        put(PdfName.S, PdfName.TRANSPARENCY);
    }
 
    /**
     * Determining the initial backdrop against which its stack is composited.
     * @param isolated
     */
    public void setIsolated(boolean isolated) {
        if (isolated)
            put(PdfName.I, PdfBoolean.PDFTRUE);
        else
            remove(PdfName.I);
    }
    
    /**
     * Determining whether the objects within the stack are composited with one another or only with the group's backdrop.
     * @param knockout
     */
    public void setKnockout(boolean knockout) {
        if (knockout)
            put(PdfName.K, PdfBoolean.PDFTRUE);
        else
            remove(PdfName.K);
    }

}
