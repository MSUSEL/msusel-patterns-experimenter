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

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;

/**
 * The RichMediaActivation dictionary specifies the style of presentation,
 * default script behavior, default view information, and animation style
 * when the annotation is activated.
 * See ExtensionLevel 3 p78
 * @since	5.0.0
 */
public class RichMediaActivation extends PdfDictionary {
	
	/**
	 * Creates a RichMediaActivation dictionary.
	 */
	public RichMediaActivation() {
		super(PdfName.RICHMEDIAACTIVATION);
	}
	
	/**
	 * Sets the activation condition.
	 * Set it to XA if the annotation is explicitly activated by a user action
	 * or script (this is the default).
	 * To PO, if the annotation is activated as soon as the page that contains
	 * the annotation receives focus as the current page.
	 * To PV, if the annotation is activated as soon as any part of the page
	 * that contains the annotation becomes visible. One example is in a
	 * multiple-page presentation. Only one page is the current page although
	 * several are visible.
	 * @param	condition	possible values are:
	 * 		PdfName.XA, PdfName.PO, or PdfName.PV
	 */
	public void setCondition(PdfName condition) {
		put(PdfName.CONDITION, condition);
	}
	
	/**
	 * Sets the animation dictionary describing the preferred method
	 * that conforming readers should use to drive keyframe animations
	 * present in this artwork.
	 * @param	animation	a RichMediaAnimation dictionary
	 */
	public void setAnimation(RichMediaAnimation animation) {
		put(PdfName.ANIMATION, animation);
	}
	
	/**
	 * Sets an indirect object reference to a 3D view dictionary
	 * that shall also be referenced by the Views array within the
	 * annotation's RichMediaContent dictionary.
	 * @param	view	an indirect reference
	 */
	public void setView(PdfIndirectReference view) {
		put(PdfName.VIEW, view);
	}
	
	/**
	 * Sets an indirect object reference to a RichMediaConfiguration
	 * dictionary that shall also be referenced by the Configurations
	 * array in the RichMediaContent dictionary (which is part of
	 * the RichMediaAnnotation object).
	 * @param	configuration	an indirect reference
	 */
	public void setConfiguration(PdfIndirectReference configuration) {
		put(PdfName.CONFIGURATION, configuration);
	}
	
	/**
	 * Sets a RichMediaPresentation dictionary that contains information
	 * as to how the annotation and user interface elements will be visually
	 * laid out and drawn.
	 * @param	richMediaPresentation	a RichMediaPresentation object
	 */
	public void setPresentation(RichMediaPresentation richMediaPresentation) {
		put(PdfName.PRESENTATION, richMediaPresentation);
	}
	
	/**
	 * Sets an array of indirect object references to file specification
	 * dictionaries, each of which describe a JavaScript file that shall
	 * be present in the Assets name tree of the RichMediaContent dictionary.
	 * @param	scripts	a PdfArray
	 */
	public void setScripts(PdfArray scripts) {
		put(PdfName.SCRIPTS, scripts);
	}
}
