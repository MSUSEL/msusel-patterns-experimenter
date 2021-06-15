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

import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class can tell you more about the permissions that are allowed
 * on the PDF file.
 */
public class Permissions {

	/** Was the file encrypted? */
	protected boolean encrypted = true;
	/** Which owner password was provided to open the file? */
	protected byte[] ownerPassword = null;
	/** What is the user password? */
	protected byte[] userPassword = null;
	/** What are the document permissions? */
	protected int permissions = 0;
	/** How was the document encrypted? */
	protected int cryptoMode = 0;
	
	/**
	 * Tells you if the document was encrypted.
	 * @return true is the document was encrypted
	 */
	public boolean isEncrypted() {
		return encrypted;
	}
	/**
	 * Setter for the encrypted variable.
	 * @param encrypted	set this to true if the document was encrypted
	 */
	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}
	/**
	 * Returns the owner password of the PDF file (if any).
	 * @return	the owner password that was provided upon opening the document
	 */
	public byte[] getOwnerPassword() {
		return ownerPassword;
	}
	/**
	 * Setter for the owner password.
	 * @param ownerPassword	the owner password
	 */
	public void setOwnerPassword(byte[] ownerPassword) {
		this.ownerPassword = ownerPassword;
	}
	/**
	 * Returns the user password (if any).
	 * @return	the user password
	 */
	public byte[] getUserPassword() {
		return userPassword;
	}
	/**
	 * Setter for the user password.
	 * @param userPassword the user password of a PDF file
	 */
	public void setUserPassword(byte[] userPassword) {
		this.userPassword = userPassword;
	}
	/**
	 * Returns the permissions in the form of an int (each bit is a specific permission)
	 * @return the value for the permissions
	 */
	public int getPermissions() {
		return permissions;
	}
	/**
	 * Setter for the permissions.
	 * @param permissions	the permissions in the form of an int
	 */
	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}
	/**
	 * Returns the crypto mode.
	 * @return	the crypto mode
	 */
	public int getCryptoMode() {
		return cryptoMode;
	}
	/**
	 * Setter for the crypto mode
	 * @param cryptoMode	the crypto mode
	 */
	public void setCryptoMode(int cryptoMode) {
		this.cryptoMode = cryptoMode;
	}
	
	/**
	 * Tells you if printing is allowed.
	 * @return	true if printing is allowed
	 */
	public boolean isAllowPrinting() {
		return
			!encrypted
			|| (PdfWriter.ALLOW_PRINTING & permissions) == PdfWriter.ALLOW_PRINTING;
	}
	/**
	 * Tells you if modifying the contents is allowed.
	 * @return true if modifying contents is allowed
	 */
	public boolean isAllowModifyContents(boolean decrypted) {
		return
			!encrypted
			|| (PdfWriter.ALLOW_MODIFY_CONTENTS & permissions) == PdfWriter.ALLOW_MODIFY_CONTENTS;
	}
	/**
	 * Tells you if copying is allowed.
	 * @return true if copying is allowed
	 */
	public boolean isAllowCopy(boolean decrypted) {
		return
			!encrypted
			|| (PdfWriter.ALLOW_COPY & permissions) == PdfWriter.ALLOW_COPY;
	}
	/**
	 * Tells you if modifying annotations is allowed
	 * @return true if modifying annotations is allowed
	 */
	public boolean isAllowModifyAnnotations() {
		return
			!encrypted
			|| (PdfWriter.ALLOW_MODIFY_ANNOTATIONS & permissions) == PdfWriter.ALLOW_MODIFY_ANNOTATIONS;
	}
	/**
	 * Tells you if filling in forms is allowed.
	 * @return true if filling in forms is allowed
	 */
	public boolean isAllowFillIn() {
		return
			!encrypted
			|| (PdfWriter.ALLOW_FILL_IN & permissions) == PdfWriter.ALLOW_FILL_IN;
	}
	/**
	 * Tells you if modifying the layout for screenreaders is allowed.
	 * @return true if modifying the layout for screenreaders is allowed
	 */
	public boolean isAllowScreenReaders() {
		return
			!encrypted
			|| (PdfWriter.ALLOW_SCREENREADERS & permissions) == PdfWriter.ALLOW_SCREENREADERS;
	}
	/**
	 * Tells you if document assembly is allowed.
	 * @return true if document assembly is allowed
	 */
	public boolean isAllowAssembly() {
		return
			!encrypted
			|| (PdfWriter.ALLOW_ASSEMBLY & permissions) == PdfWriter.ALLOW_ASSEMBLY;
	}
	/**
	 * Tells you if degraded printing is allowed.
	 * @return true if degraded printing is allowed
	 */
	public boolean isAllowDegradedPrinting() {
		return
			!encrypted
			|| (PdfWriter.ALLOW_DEGRADED_PRINTING & permissions) == PdfWriter.ALLOW_DEGRADED_PRINTING;
	}
}