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

import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;

/**
 * A rich-media-execute action identifies a rich media annotation and
 * specifies a command to be sent to that annotation's handler.
 * See table 8.45a in the Adobe 1.7 ExtensionLevel 3 document
 * @see	RichMediaAnnotation
 * @see	RichMediaCommand
 * @since	5.0.0
 */
public class RichMediaExecuteAction extends PdfAction {

	/**
	 * Creates a RichMediaExecute action dictionary.
	 * @param	ref	a reference to rich media annotation dictionary for
	 * an annotation for which to execute the script command.
	 * @param	command	the command name and arguments to be
	 * executed when the rich-media-execute action is invoked.
	 */
	public RichMediaExecuteAction(PdfIndirectReference ref,
			RichMediaCommand command) {
		super();
		put(PdfName.S, PdfName.RICHMEDIAEXECUTE);
		put(PdfName.TA, ref);
		put(PdfName.CMD, command);
	}
	
	/**
	 * Sets the target instance for this action.
	 * @param	ref	a reference to a RichMediaInstance
	 */
	public void setRichMediaInstance(PdfIndirectReference ref) {
		put(PdfName.TI, ref);
	}
}
