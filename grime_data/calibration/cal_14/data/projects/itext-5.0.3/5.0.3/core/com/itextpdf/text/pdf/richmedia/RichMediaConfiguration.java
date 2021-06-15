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
 * The RichMediaConfiguration dictionary describes a set of instances
 * that are loaded for a given scene configuration. The configuration
 * to be loaded when an annotation is activated is referenced by the
 * Configuration key in the RichMediaActivation dictionary specified
 * in the RichMediaSettings dictionary.
 * see ExtensionLevel 3 p88
 * @see RichMediaAnnotation
 * @see RichMediaInstance
 * @since	5.0.0
 */
public class RichMediaConfiguration extends PdfDictionary {

	/** An array of indirect object references to RichMediaInstance dictionaries. */
	protected PdfArray instances = new PdfArray();
	
	/**
	 * Creates a RichMediaConfiguration object. Also specifies the primary
	 * content type for the configuration. Valid values are 3D, Flash, Sound,
	 * and Video.
	 * @param	subtype	Possible values are:
	 * PdfName._3D, PdfName.FLASH, PdfName.SOUND, and PdfName.VIDEO.
	 */
	public RichMediaConfiguration(PdfName subtype) {
		super(PdfName.RICHMEDIACONFIGURATION);
		put(PdfName.SUBTYPE, subtype);
		put(PdfName.INSTANCES, instances);
	}
	
	/**
	 * Sets the name of the configuration (must be unique).
	 * @param	name	the name
	 */
	public void setName(PdfString name) {
		put(PdfName.NAME, name);
	}
	
	/**
	 * Adds a RichMediaInstance to the instances array of this
	 * configuration.
	 * @param	instance	a RichMediaInstance
	 */
	public void addInstance(RichMediaInstance instance) {
		instances.add(instance);
	}
}
