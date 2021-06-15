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
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;

/**
 * Dictionary containing parameters related to an active Flash subtype
 * in a RichMediaInstance dictionary.
 * See ExtensionLevel 3 p90
 * @since	5.0.0
 */
public class RichMediaParams extends PdfDictionary {

	/**
	 * Creates a RichMediaParams object.
	 */
	public RichMediaParams() {
		super(PdfName.RICHMEDIAPARAMS);
	}
	
	/**
	 * Sets a text string containing formatted name value pairs passed
	 * to the Flash Player context when activated.
	 * @param	flashVars	a String with the Flash variables
	 */
	public void setFlashVars(String flashVars) {
		put(PdfName.FLASHVARS, new PdfString(flashVars));
	}
	
	/**
	 * Sets the binding.
	 * @param	binding	possible values:
	 * PdfName.NONE, PdfName.FOREGROUND, PdfName.BACKGROUND, PdfName.MATERIAL
	 */
	public void setBinding(PdfName binding) {
		put(PdfName.BINDING, binding);
	}
	
	/**
	 * Stores the material name that content is to be bound to.
	 * Required if Binding value is Material.
	 * @param	bindingMaterialName	a material name
	 */
	public void setBindingMaterialName(PdfString bindingMaterialName) {
		put(PdfName.BINDINGMATERIALNAME, bindingMaterialName);
	}
	
	/**
	 * Sets an array of CuePoint dictionaries containing points
	 * in time within a Flash animation.
	 * @param	cuePoints	a PdfArray with CuePoint objects
	 */
	public void setCuePoints(PdfArray cuePoints) {
		put(PdfName.CUEPOINTS, cuePoints);
	}
	
	/**
	 * A text string used to store settings information associated
	 * with a Flash RichMediaInstance. It is to be stored and loaded
	 * by the scripting run time.
	 * @param	settings	a PdfString
	 */
	public void setSettings(PdfString settings) {
		put(PdfName.SETTINGS, settings);
	}
}
