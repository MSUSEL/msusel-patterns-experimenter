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

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;

/**
 * Allows you to add one (or more) existing PDF document(s) to
 * create a new PDF and add the form of another PDF document to
 * this new PDF.
 * @since 2.1.5
 */
public class PdfCopyForms
	implements PdfViewerPreferences, PdfEncryptionSettings {

	/** The class with the actual implementations. */
    private PdfCopyFormsImp fc;

    /**
     * Creates a new instance.
     * @param os the output stream
     * @throws DocumentException on error
     */
    public PdfCopyForms(OutputStream os) throws DocumentException {
        fc = new PdfCopyFormsImp(os);
    }

    /**
     * Concatenates a PDF document.
     * @param reader the PDF document
     * @throws DocumentException on error
     */
    public void addDocument(PdfReader reader) throws DocumentException, IOException {
        fc.addDocument(reader);
    }

    /**
     * Concatenates a PDF document selecting the pages to keep. The pages are described as a
     * <CODE>List</CODE> of <CODE>Integer</CODE>. The page ordering can be changed but
     * no page repetitions are allowed.
     * @param reader the PDF document
     * @param pagesToKeep the pages to keep
     * @throws DocumentException on error
     */
    public void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
        fc.addDocument(reader, pagesToKeep);
    }

    /**
     * Concatenates a PDF document selecting the pages to keep. The pages are described as
     * ranges. The page ordering can be changed but
     * no page repetitions are allowed.
     * @param reader the PDF document
     * @param ranges the comma separated ranges as described in {@link SequenceList}
     * @throws DocumentException on error
     */
    public void addDocument(PdfReader reader, String ranges) throws DocumentException, IOException {
        fc.addDocument(reader, SequenceList.expand(ranges, reader.getNumberOfPages()));
    }

    /**
     *Copies the form fields of this PDFDocument onto the PDF-Document which was added
     * @param reader the PDF document
     * @throws DocumentException on error
     */
    public void copyDocumentFields(PdfReader reader) throws DocumentException{
        fc.copyDocumentFields(reader);
    }

    /** Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @param strength128Bits <code>true</code> for 128 bit key length, <code>false</code> for 40 bit key length
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits) throws DocumentException {
    	fc.setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? PdfWriter.STANDARD_ENCRYPTION_128 : PdfWriter.STANDARD_ENCRYPTION_40);
    }

    /**
     * Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param strength true for 128 bit key length. false for 40 bit key length
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, strength);
    }

    /**
     * Closes the output document.
     */
    public void close() {
        fc.close();
    }

    /**
     * Opens the document. This is usually not needed as addDocument() will do it
     * automatically.
     */
    public void open() {
        fc.openDoc();
    }

    /**
     * Adds JavaScript to the global document
     * @param js the JavaScript
     */
    public void addJavaScript(String js) {
        fc.addJavaScript(js, !PdfEncodings.isPdfDocEncoding(js));
    }

    /**
     * Sets the bookmarks. The list structure is defined in
     * <CODE>SimpleBookmark#</CODE>.
     * @param outlines the bookmarks or <CODE>null</CODE> to remove any
     */
    public void setOutlines(List<HashMap<String, Object>> outlines) {
        fc.setOutlines(outlines);
    }

    /** Gets the underlying PdfWriter.
     * @return the underlying PdfWriter
     */
    public PdfWriter getWriter() {
        return fc;
    }

    /**
     * Gets the 1.5 compression status.
     * @return <code>true</code> if the 1.5 compression is on
     */
    public boolean isFullCompression() {
        return fc.isFullCompression();
    }

    /**
     * Sets the document's compression to the new 1.5 mode with object streams and xref
     * streams. It can be set at any time but once set it can't be unset.
     * <p>
     * If set before opening the document it will also set the pdf version to 1.5.
     */
    public void setFullCompression() {
        fc.setFullCompression();
    }

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings#setEncryption(byte[], byte[], int, int)
	 */
	public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType) throws DocumentException {
		fc.setEncryption(userPassword, ownerPassword, permissions, encryptionType);
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfViewerPreferences#addViewerPreference(com.itextpdf.text.pdf.PdfName, com.itextpdf.text.pdf.PdfObject)
	 */
	public void addViewerPreference(PdfName key, PdfObject value) {
		fc.addViewerPreference(key, value);
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfViewerPreferences#setViewerPreferences(int)
	 */
	public void setViewerPreferences(int preferences) {
		fc.setViewerPreferences(preferences);
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings#setEncryption(java.security.cert.Certificate[], int[], int)
	 */
	public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException {
		fc.setEncryption(certs, permissions, encryptionType);
	}
}