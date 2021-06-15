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

import com.itextpdf.text.exceptions.IllegalPdfSyntaxException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;

/**
 * The RichMediaInstance dictionary, referenced by the Instances entry
 * of the RichMediaConfiguration, describes a single instance of an asset
 * with settings to populate the artwork of an annotation.
 * See ExtensionLevel 3 p88
 * @see	RichMediaConfiguration
 * @since	5.0.0
 */
public class RichMediaInstance extends PdfDictionary {

	/** True if the instance is a flash animation. */
	protected boolean flash;
	
	/**
	 * Creates a RichMediaInstance. Also specifies the content type
	 * for the instance. Valid values are 3D, Flash, Sound, and Video.
	 * The subtype must match the asset file type of the instance.
	 * @param	subtype	possible values are:
	 * PdfName._3D, PdfName.FLASH, PdfName.SOUND, and PdfName.VIDEO.
	 */
	public RichMediaInstance(PdfName subtype) {
		super(PdfName.RICHMEDIAINSTANCE);
		put(PdfName.SUBTYPE, subtype);
		flash = PdfName.FLASH.equals(subtype);
	}
	
	/**
	 * Sets the parameters. This will only work for Flash.
	 * @param params	a RichMediaParams object
	 */
	public void setParams(RichMediaParams params) {
		if (flash) {
			put(PdfName.PARAMS, params);
		}
		else {
			throw new IllegalPdfSyntaxException("Parameters can only be set for Flash instances.");
		}
	}
	
	/**
	 * Sets a dictionary that shall be an indirect object reference
	 * to a file specification dictionary that is also referenced
	 * in the Assets name tree of the content of the annotation.
	 * @param	asset	a reference to a dictionary present in the Assets name tree
	 */
	public void setAsset(PdfIndirectReference asset) {
		put(PdfName.ASSET, asset);
	}
}
