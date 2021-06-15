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

/**
 * The RichMediaActivation dictionary specifies the condition
 * that causes deactivation of the annotation.
 * See ExtensionLevel 3 p80
 * @since	5.0.0
 */
public class RichMediaDeactivation extends PdfDictionary {
	
	/**
	 * Creates a RichMediaActivation dictionary.
	 */
	public RichMediaDeactivation() {
		super(PdfName.RICHMEDIADEACTIVATION);
	}
	
	/**
	 * Sets the activation condition.
	 * Set it to XD, and the annotation is explicitly deactivated by a user action
	 * or script.
	 * To PC, and the annotation is deactivated as soon as the page that contains
	 * the annotation loses focus as the current page.
	 * To PI, abd the annotation is deactivated as soon as the entire page that
	 * contains the annotation is no longer visible.
	 * @param	condition	possible values are:
	 * 		PdfName.XD, PdfName.PC, or PdfName.PI
	 */
	public void setCondition(PdfName condition) {
		put(PdfName.CONDITION, condition);
	}
}
