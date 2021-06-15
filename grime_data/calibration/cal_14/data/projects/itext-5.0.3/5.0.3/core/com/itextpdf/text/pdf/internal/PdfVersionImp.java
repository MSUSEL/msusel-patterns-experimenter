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
package com.itextpdf.text.pdf.internal;

import java.io.IOException;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.pdf.OutputStreamCounter;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.interfaces.PdfVersion;

/**
 * Stores the PDF version information,
 * knows how to write a PDF Header,
 * and how to add the version to the catalog (if necessary).
 */

public class PdfVersionImp implements PdfVersion {
    
    /** Contains different strings that are part of the header. */
    public static final byte[][] HEADER = {
    	DocWriter.getISOBytes("\n"),
    	DocWriter.getISOBytes("%PDF-"),
    	DocWriter.getISOBytes("\n%\u00e2\u00e3\u00cf\u00d3\n")
    };
    
	/** Indicates if the header was already written. */
	protected boolean headerWasWritten = false;
	/** Indicates if we are working in append mode. */
	protected boolean appendmode = false;
	/** The version that was or will be written to the header. */
	protected char header_version = PdfWriter.VERSION_1_4;
	/** The version that will be written to the catalog. */
	protected PdfName catalog_version = null;
	/**
	 * The extensions dictionary.
	 * @since	2.1.6
	 */
	protected PdfDictionary extensions = null;
	
	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfVersion#setPdfVersion(char)
	 */
	public void setPdfVersion(char version) {
		if (headerWasWritten || appendmode) {
			setPdfVersion(getVersionAsName(version));
		}
		else {
			this.header_version = version;
		}
	}
	
	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfVersion#setAtLeastPdfVersion(char)
	 */
	public void setAtLeastPdfVersion(char version) {
		if (version > header_version) {
			setPdfVersion(version);
		}
	}
	
	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfVersion#setPdfVersion(com.itextpdf.text.pdf.PdfName)
	 */
	public void setPdfVersion(PdfName version) {
		if (catalog_version == null || catalog_version.compareTo(version) < 0) {
			this.catalog_version = version;
		}
	}
	
	/**
	 * Sets the append mode.
	 */
	public void setAppendmode(boolean appendmode) {
		this.appendmode = appendmode;
	}
	
	/**
	 * Writes the header to the OutputStreamCounter.
	 * @throws IOException 
	 */
	public void writeHeader(OutputStreamCounter os) throws IOException {
		if (appendmode) {
			os.write(HEADER[0]);
		}
		else {
			os.write(HEADER[1]);
			os.write(getVersionAsByteArray(header_version));
			os.write(HEADER[2]);
			headerWasWritten = true;
		}
	}
	
	/**
	 * Returns the PDF version as a name.
	 * @param version	the version character.
	 */
	public PdfName getVersionAsName(char version) {
		switch(version) {
		case PdfWriter.VERSION_1_2:
			return PdfWriter.PDF_VERSION_1_2;
		case PdfWriter.VERSION_1_3:
			return PdfWriter.PDF_VERSION_1_3;
		case PdfWriter.VERSION_1_4:
			return PdfWriter.PDF_VERSION_1_4;
		case PdfWriter.VERSION_1_5:
			return PdfWriter.PDF_VERSION_1_5;
		case PdfWriter.VERSION_1_6:
			return PdfWriter.PDF_VERSION_1_6;
		case PdfWriter.VERSION_1_7:
			return PdfWriter.PDF_VERSION_1_7;
		default:
			return PdfWriter.PDF_VERSION_1_4;
		}
	}
	
	/**
	 * Returns the version as a byte[].
	 * @param version the version character
	 */
	public byte[] getVersionAsByteArray(char version) {
		return DocWriter.getISOBytes(getVersionAsName(version).toString().substring(1));
	}

	/** Adds the version to the Catalog dictionary. */
	public void addToCatalog(PdfDictionary catalog) {
		if(catalog_version != null) {
			catalog.put(PdfName.VERSION, catalog_version);
		}
		if (extensions != null) {
			catalog.put(PdfName.EXTENSIONS, extensions);
		}
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfVersion#addDeveloperExtension(com.itextpdf.text.pdf.PdfDeveloperExtension)
	 * @since	2.1.6
	 */
	public void addDeveloperExtension(PdfDeveloperExtension de) {
		if (extensions == null) {
			extensions = new PdfDictionary();
		}
		else {
			PdfDictionary extension = extensions.getAsDict(de.getPrefix());
			if (extension != null) {
				int diff = de.getBaseversion().compareTo(extension.getAsName(PdfName.BASEVERSION));
				if (diff < 0)
					return;
				diff = de.getExtensionLevel() - extension.getAsNumber(PdfName.EXTENSIONLEVEL).intValue();
				if (diff <= 0)
					return;
			}
		}
		extensions.put(de.getPrefix(), de.getDeveloperExtensions());
	}
}