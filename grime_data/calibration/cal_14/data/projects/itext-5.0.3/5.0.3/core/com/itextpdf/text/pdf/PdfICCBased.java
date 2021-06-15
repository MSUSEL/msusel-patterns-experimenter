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

import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.ExceptionConverter;

/**
 * A <CODE>PdfICCBased</CODE> defines a ColorSpace
 *
 * @see		PdfStream
 */

public class PdfICCBased extends PdfStream {

    /**
     * Creates an ICC stream.
     * @param	profile an ICC profile
     */
    public PdfICCBased(ICC_Profile profile) {
    	this(profile, DEFAULT_COMPRESSION);
    }
    
    /**
     * Creates an ICC stream.
     *
     * @param	compressionLevel	the compressionLevel
     *
     * @param	profile an ICC profile
     * @since	2.1.3	(replacing the constructor without param compressionLevel)
     */
    public PdfICCBased(ICC_Profile profile, int compressionLevel) {
        super();
        try {
            int numberOfComponents = profile.getNumComponents();
            switch (numberOfComponents) {
                case 1:
                    put(PdfName.ALTERNATE, PdfName.DEVICEGRAY);
                    break;
                case 3:
                    put(PdfName.ALTERNATE, PdfName.DEVICERGB);
                    break;
                case 4:
                    put(PdfName.ALTERNATE, PdfName.DEVICECMYK);
                    break;
                default:
                    throw new PdfException(MessageLocalization.getComposedMessage("1.component.s.is.not.supported", numberOfComponents));
            }
            put(PdfName.N, new PdfNumber(numberOfComponents));
            bytes = profile.getData();
            put(PdfName.LENGTH, new PdfNumber(bytes.length));
            flateCompress(compressionLevel);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
