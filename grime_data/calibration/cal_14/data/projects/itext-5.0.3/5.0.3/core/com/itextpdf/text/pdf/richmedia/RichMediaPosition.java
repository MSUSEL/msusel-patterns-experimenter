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
package com.itextpdf.text.pdf.richmedia;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;

/**
 * The position of the window in the reader presentation area is described
 * by the RichMediaPosition dictionary. The position of the window remains
 * fixed, regardless of the page translation.
 * See ExtensionLevel 3 p84
 * @since	5.0.0
 */
public class RichMediaPosition extends PdfDictionary {

	/**
	 * Constructs a RichMediaPosition dictionary.
	 */
	public RichMediaPosition() {
		super(PdfName.RICHMEDIAPOSITION);
	}
	
	/**
	 * Set the horizontal alignment.
	 * @param	hAlign possible values are
	 * PdfName.NEAR, PdfName.CENTER, or PdfName.FAR
	 */
	public void setHAlign(PdfName hAlign) {
		put(PdfName.HALIGN, hAlign);
	}
	
	/**
	 * Set the horizontal alignment.
	 * @param	vAlign possible values are
	 * PdfName.NEAR, PdfName.CENTER, or PdfName.FAR
	 */
	public void setVAlign(PdfName vAlign) {
		put(PdfName.VALIGN, vAlign);
	}
	
	/**
	 * Sets the offset from the alignment point specified by the HAlign key.
	 * A positive value for HOffset, when HAlign is either Near or Center,
	 * offsets the position towards the Far direction. A positive value for
	 * HOffset, when HAlign is Far, offsets the position towards the Near
	 * direction.
	 * @param	hOffset	an offset
	 */
	public void setHOffset(float hOffset) {
		put(PdfName.HOFFSET, new PdfNumber(hOffset));
	}
	
	/**
	 * Sets the offset from the alignment point specified by the VAlign key.
	 * A positive value for VOffset, when VAlign is either Near or Center,
	 * offsets the position towards the Far direction. A positive value for
	 * VOffset, when VAlign is Far, offsets the position towards the Near
	 * direction.
	 * @param	vOffset	an offset
	 */
	public void setVOffset(float vOffset) {
		put(PdfName.VOFFSET, new PdfNumber(vOffset));
	}
}
