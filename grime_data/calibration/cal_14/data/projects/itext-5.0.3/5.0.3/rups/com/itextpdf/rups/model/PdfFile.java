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
package com.itextpdf.rups.model;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * Wrapper for both iText's PdfReader (referring to a PDF file to read)
 * and SUN's PDFFile (referring to the same PDF file to render).
 */
public class PdfFile {

	// member variables
	
	/** The directory where the file can be found (if the PDF was passed as a file). */
	protected File directory = null;
	
	/** The original filename. */
	protected String filename = null;
	
	/** The PdfReader object. */
	protected PdfReader reader = null;
	
	/** The file permissions */
	protected Permissions permissions = null;
	
	// constructors
	/**
	 * Constructs a PdfFile object.
	 * @param	file	the File to read
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public PdfFile(File file) throws IOException, DocumentException {
		if (file == null)
			throw new IOException("No file selected.");
		RandomAccessFileOrArray pdf = new RandomAccessFileOrArray(file.getAbsolutePath());
		directory = file.getParentFile();
		filename = file.getName();
		readFile(pdf);
	}
	
	/**
	 * Constructs a PdfFile object.
	 * @param	file	the byte[] to read
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public PdfFile(byte[] file) throws IOException, DocumentException {
		RandomAccessFileOrArray pdf = new RandomAccessFileOrArray(file);
		readFile(pdf);
	}
	
	/**
	 * Does the actual reading of the file into PdfReader and PDFFile.
	 * @param pdf	a Random Access File or Array
	 * @throws IOException
	 * @throws DocumentException
	 */
	protected void readFile(RandomAccessFileOrArray pdf) throws IOException, DocumentException {
		// reading the file into PdfReader
		permissions = new Permissions();
		try {
			reader = new PdfReader(pdf, null);
			permissions.setEncrypted(false);
		} catch(BadPasswordException bpe) {
		    JPasswordField passwordField = new JPasswordField(32);
		    JOptionPane.showConfirmDialog(null, passwordField, "Enter the User or Owner Password of this PDF file", JOptionPane.OK_CANCEL_OPTION);
		    byte[] password = new String(passwordField.getPassword()).getBytes();
		    reader = new PdfReader(pdf, password);
		    permissions.setEncrypted(true);
		    permissions.setCryptoMode(reader.getCryptoMode());
		    permissions.setPermissions(reader.getPermissions());
		    if (reader.isOpenedWithFullPermissions()) {
		    	permissions.setOwnerPassword(password);
		    	permissions.setUserPassword(reader.computeUserPassword());
		    }
		    else {
		    	throw new IOException("You need the owner password of this file to open it in iText Trapeze.");
		    }
		}
	}

	/**
	 * Getter for iText's PdfReader object.
	 * @return	a PdfReader object
	 */
	public PdfReader getPdfReader() {
		return reader;
	}
	
	/**
	 * Getter for the filename
	 * @return the original filename
	 * @since 5.0.3
	 */
	public String getFilename(){
	    return filename;
	}
}