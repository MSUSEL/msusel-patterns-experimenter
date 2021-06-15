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
package com.itextpdf.text.xml.xmp;


/**
 * An implementation of an XmpSchema.
 */
public class XmpBasicSchema extends XmpSchema {

	private static final long serialVersionUID = -2416613941622479298L;
	/** default namespace identifier*/
	public static final String DEFAULT_XPATH_ID = "xmp";
	/** default namespace uri*/
	public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/xap/1.0/";
	
	/** An unordered array specifying properties that were edited outside the authoring application. Each item should contain a single namespace and XPath separated by one ASCII space (U+0020). */
	public static final String ADVISORY = "xmp:Advisory";
	/** The base URL for relative URLs in the document content. If this document contains Internet links, and those links are relative, they are relative to this base URL. This property provides a standard way for embedded relative URLs to be interpreted by tools. Web authoring tools should set the value based on their notion of where URLs will be interpreted. */
	public static final String BASEURL = "xmp:BaseURL";
	/** The date and time the resource was originally created. */
	public static final String CREATEDATE = "xmp:CreateDate";
	/** The name of the first known tool used to create the resource. If history is present in the metadata, this value should be equivalent to that of xmpMM:History's softwareAgent property. */
	public static final String CREATORTOOL = "xmp:CreatorTool";
	/** An unordered array of text strings that unambiguously identify the resource within a given context. */
	public static final String IDENTIFIER = "xmp:Identifier";
	/** The date and time that any metadata for this resource was last changed. */
	public static final String METADATADATE = "xmp:MetadataDate";
	/** The date and time the resource was last modified. */
	public static final String MODIFYDATE = "xmp:ModifyDate";
	/** A short informal name for the resource. */
	public static final String NICKNAME = "xmp:Nickname";
	/** An alternative array of thumbnail images for a file, which can differ in characteristics such as size or image encoding. */
	public static final String THUMBNAILS = "xmp:Thumbnails";

	
	public XmpBasicSchema() {
		super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
	}
	
	/**
	 * Adds the creatortool.
	 * @param creator
	 */
	public void addCreatorTool(String creator) {
		setProperty(CREATORTOOL, creator);
	}
	
	/**
	 * Adds the creation date.
	 * @param date
	 */
	public void addCreateDate(String date) {
		setProperty(CREATEDATE, date);
	}
	
	/**
	 * Adds the modification date.
	 * @param date
	 */
	public void addModDate(String date) {
		setProperty(MODIFYDATE, date);
	}

	/**
	 * Adds the meta data date.
	 * @param date
	 */
	public void addMetaDataDate(String date) {
		setProperty(METADATADATE, date);
	}

	/** Adds the identifier.
	 * @param id
	 */
	public void addIdentifiers(String[] id) {
		XmpArray array = new XmpArray(XmpArray.UNORDERED);
		for (int i = 0; i < id.length; i++) {
			array.add(id[i]);
		}
		setProperty(IDENTIFIER, array);
	}

	/** Adds the nickname.
	 * @param name
	 */
	public void addNickname(String name) {
		setProperty(NICKNAME, name);
	}
}
