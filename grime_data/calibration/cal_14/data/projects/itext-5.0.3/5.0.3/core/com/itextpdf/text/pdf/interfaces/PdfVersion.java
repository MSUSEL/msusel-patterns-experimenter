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
package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfName;

/**
 * The PDF version is described in the PDF Reference 1.7 p92
 * (about the PDF Header) and page 139 (the version entry in
 * the Catalog). You'll also find info about setting the version
 * in the book 'iText in Action' sections 2.1.3 (PDF Header)
 * and 3.3 (Version history).
 */

public interface PdfVersion {
    
    /**
	 * If the PDF Header hasn't been written yet,
	 * this changes the version as it will appear in the PDF Header.
	 * If the PDF header was already written to the OutputStream,
	 * this changes the version as it will appear in the Catalog.
	 * @param version	a character representing the PDF version
	 */
	public void setPdfVersion(char version);
    /**
	 * If the PDF Header hasn't been written yet,
	 * this changes the version as it will appear in the PDF Header,
	 * but only if the parameter refers to a higher version.
	 * If the PDF header was already written to the OutputStream,
	 * this changes the version as it will appear in the Catalog.
	 * @param version	a character representing the PDF version
	 */
	public void setAtLeastPdfVersion(char version);
	/**
	 * Sets the PDF version as it will appear in the Catalog.
	 * Note that this only has effect if you use a later version
	 * than the one that appears in the header; this method
	 * ignores the parameter if you try to set a lower version.
	 * @param version	the PDF name that will be used for the Version key in the catalog
	 */
	public void setPdfVersion(PdfName version);
	/**
	 * Adds a developer extension to the Extensions dictionary
	 * in the Catalog.
	 * @param de	an object that contains the extensions prefix and dictionary
	 * @since	2.1.6
	 */
	public void addDeveloperExtension(PdfDeveloperExtension de);
}