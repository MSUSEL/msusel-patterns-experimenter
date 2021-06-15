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
 * The RichMediaWindow dictionary stores the dimensions and position of the
 * floating window presented to the user. It is used only if Style is set
 * to Windowed.
 * See ExtensionLevel 3 p84
 * @see	RichMediaPresentation
 * @since	5.0.0
 */
public class RichMediaWindow extends PdfDictionary {

	/**
	 * Creates a RichMediaWindow dictionary.
	 */
	public RichMediaWindow() {
		super(PdfName.RICHMEDIAWINDOW);
	}
	
	/**
	 * Sets a dictionary with keys Default, Max, and Min describing values for
	 * the width of the Window in default user space units.
	 * @param	defaultWidth	the default width
	 * @param	maxWidth		the maximum width
	 * @param	minWidth		the minimum width
	 */
	public void setWidth(float defaultWidth, float maxWidth, float minWidth) {
		put(PdfName.WIDTH, createDimensionDictionary(defaultWidth, maxWidth, minWidth));
	}

	/**
	 * Sets a dictionary with keys Default, Max, and Min describing values for
	 * the height of the Window in default user space units.
	 * @param	defaultHeight	the default height
	 * @param	maxHeight		the maximum height
	 * @param	minHeight		the minimum height
	 */
	public void setHeight(float defaultHeight, float maxHeight, float minHeight) {
		put(PdfName.HEIGHT, createDimensionDictionary(defaultHeight, maxHeight, minHeight));
	}
	
	/**
	 * Creates a dictionary that can be used for the HEIGHT and WIDTH entries
	 * of the RichMediaWindow dictionary.
	 * @param	d		the default
	 * @param	max		the maximum
	 * @param	min		the minimum
	 */
	private PdfDictionary createDimensionDictionary(float d, float max, float min) {
		PdfDictionary dict = new PdfDictionary();
		dict.put(PdfName.DEFAULT, new PdfNumber(d));
		dict.put(PdfName.MAX_CAMEL_CASE, new PdfNumber(max));
		dict.put(PdfName.MIN_CAMEL_CASE, new PdfNumber(min));
		return dict;
	}
	
	/**
	 * Sets a RichMediaPosition dictionary describing the position of the RichMediaWindow.
	 * @param	position	a RichMediaPosition object
	 */
	public void setPosition(RichMediaPosition position) {
		put(PdfName.POSITION, position);
	}
}
