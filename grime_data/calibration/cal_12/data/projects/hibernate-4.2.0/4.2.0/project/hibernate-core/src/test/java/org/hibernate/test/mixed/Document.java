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
//$Id: Document.java 8670 2005-11-25 17:36:29Z epbernard $

package org.hibernate.test.mixed;
import java.sql.Blob;
import java.util.Calendar;


/**
 * @author Gavin King
 */

public class Document extends Item {

	private Blob content;

	private Calendar modified;

	private Calendar created;

	/**
	 * @return Returns the created.
	 */

	public Calendar getCreated() {

		return created;

	}

	/**
	 * @param created The created to set.
	 */

	public void setCreated(Calendar created) {

		this.created = created;

	}

	/**
	 * @return Returns the modified.
	 */

	public Calendar getModified() {

		return modified;

	}

	/**
	 * @param modified The modified to set.
	 */

	public void setModified(Calendar modified) {

		this.modified = modified;

	}

	/**
	 * @return Returns the content.
	 */

	public Blob getContent() {

		return content;

	}

	/**
	 * @param content The content to set.
	 */

	public void setContent(Blob content) {

		this.content = content;

	}

}

