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
//$Id: Document.java 9538 2006-03-04 00:17:57Z steve.ebersole@jboss.com $
package org.hibernate.test.instrument.domain;
import java.util.Date;

/**
 * @author Gavin King
 */
public class Document {
	private Long id;
	private String name;
	private String upperCaseName;
	private String summary;
	private String text;
	private Owner owner;
	private Folder folder;
	private double sizeKb;
	private Date lastTextModification = new Date();
	/**
	 * @return Returns the folder.
	 */
	public Folder getFolder() {
		return folder;
	}
	/**
	 * @param folder The folder to set.
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	/**
	 * @return Returns the owner.
	 */
	public Owner getOwner() {
		return owner;
	}
	/**
	 * @param owner The owner to set.
	 */
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the summary.
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary The summary to set.
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text The text to set.
	 */
	private void setText(String text) {
		this.text = text;
	}
	/**
	 * @return Returns the upperCaseName.
	 */
	public String getUpperCaseName() {
		return upperCaseName;
	}
	/**
	 * @param upperCaseName The upperCaseName to set.
	 */
	public void setUpperCaseName(String upperCaseName) {
		this.upperCaseName = upperCaseName;
	}
	/**
	 * @param sizeKb The size in KBs.
	 */
	public void setSizeKb(double sizeKb) {
		this.sizeKb = sizeKb;
	}
	/**
	 * @return The size in KBs.
	 */
	public double getSizeKb() {
		return sizeKb;
	}	
	
	public void updateText(String newText) {
		if ( !newText.equals(text) ) {
			this.text = newText;
			lastTextModification = new Date();
		}
	}
	
}
