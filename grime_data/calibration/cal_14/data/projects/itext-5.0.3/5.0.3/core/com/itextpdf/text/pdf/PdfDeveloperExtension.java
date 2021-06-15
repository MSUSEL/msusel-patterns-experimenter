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

/**
 * Beginning with BaseVersion 1.7, the extensions dictionary lets developers
 * designate that a given document contains extensions to PDF. The presence
 * of the extension dictionary in a document indicates that it may contain
 * developer-specific PDF properties that extend a particular base version
 * of the PDF specification.
 * The extensions dictionary enables developers to identify their own extensions
 * relative to a base version of PDF. Additionally, the convention identifies
 * extension levels relative to that base version. The intent of this dictionary
 * is to enable developers of PDF-producing applications to identify company-specific
 * specifications (such as this one) that PDF-consuming applications use to
 * interpret the extensions.
 * @since	2.1.6
 */
public class PdfDeveloperExtension {

	/** An instance of this class for Adobe 1.7 Extension level 3. */
	public static final PdfDeveloperExtension ADOBE_1_7_EXTENSIONLEVEL3 =
		new PdfDeveloperExtension(PdfName.ADBE, PdfWriter.PDF_VERSION_1_7, 3);
	
	/** The prefix used in the Extensions dictionary added to the Catalog. */
	protected PdfName prefix;
	/** The base version. */
	protected PdfName baseversion;
	/** The extension level within the baseversion. */
	protected int extensionLevel;
	
	/**
	 * Creates a PdfDeveloperExtension object.
	 * @param prefix	the prefix referring to the developer
	 * @param baseversion	the number of the base version
	 * @param extensionLevel	the extension level within the baseverion.
	 */
	public PdfDeveloperExtension(PdfName prefix, PdfName baseversion, int extensionLevel) {
		this.prefix = prefix;
		this.baseversion = baseversion;
		this.extensionLevel = extensionLevel;
	}

	/**
	 * Gets the prefix name.
	 * @return	a PdfName
	 */
	public PdfName getPrefix() {
		return prefix;
	}

	/**
	 * Gets the baseversion name.
	 * @return	a PdfName
	 */
	public PdfName getBaseversion() {
		return baseversion;
	}

	/**
	 * Gets the extension level within the baseversion.
	 * @return	an integer
	 */
	public int getExtensionLevel() {
		return extensionLevel;
	}
	
	/**
	 * Generations the developer extension dictionary corresponding
	 * with the prefix.
	 * @return	a PdfDictionary
	 */
	public PdfDictionary getDeveloperExtensions() {
		PdfDictionary developerextensions = new PdfDictionary();
		developerextensions.put(PdfName.BASEVERSION, baseversion);
		developerextensions.put(PdfName.EXTENSIONLEVEL, new PdfNumber(extensionLevel));
		return developerextensions;
	}
}
