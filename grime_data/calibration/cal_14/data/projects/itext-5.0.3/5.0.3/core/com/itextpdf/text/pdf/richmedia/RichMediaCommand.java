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
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

/**
 * the annotation handler specific to the target instance specified
 * by the TI key in the parent rich-media-execute action dictionary.
 * See table 8.45b in the Adobe 1.7 ExtensionLevel 3 document.
 * See RichMediaExecute in the Adobe document.
 * 
 * @since	5.0.0
 */
public class RichMediaCommand extends PdfDictionary {
	
	/**
	 * Creates a RichMediaCommand dictionary.
	 * @param	command	a text string specifying the script command
	 * (a primitive ActionScript or JavaScript function name).
	 */
	public RichMediaCommand(PdfString command) {
		super(PdfName.RICHMEDIACOMMAND);
		put(PdfName.C, command);
	}
	
	/**
	 * Sets the arguments for the command.
	 * @param	args a PdfObject that specifies the arguments to the command.
	 * The object can be a PdfString, PdfNumber or PdfBoolean, or a PdfArray
	 * containing those objects.
	 */
	public void setArguments(PdfObject args) {
		put(PdfName.A, args);
	}
}
