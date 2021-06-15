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
public class DublinCoreSchema extends XmpSchema {

	private static final long serialVersionUID = -4551741356374797330L;
	/** default namespace identifier*/
	public static final String DEFAULT_XPATH_ID = "dc";
	/** default namespace uri*/
	public static final String DEFAULT_XPATH_URI = "http://purl.org/dc/elements/1.1/";
	
	/** External Contributors to the resource (other than the authors). */
	public static final String CONTRIBUTOR = "dc:contributor";
	/** The extent or scope of the resource. */
	public static final String COVERAGE = "dc:coverage";
	/** The authors of the resource (listed in order of precedence, if significant). */
	public static final String CREATOR = "dc:creator";
	/** Date(s) that something interesting happened to the resource. */
	public static final String DATE = "dc:date";
	/** A textual description of the content of the resource. Multiple values may be present for different languages. */
	public static final String DESCRIPTION = "dc:description";
	/** The file format used when saving the resource. Tools and applications should set this property to the save format of the data. It may include appropriate qualifiers. */
	public static final String FORMAT = "dc:format";
	/** Unique identifier of the resource. */
	public static final String IDENTIFIER = "dc:identifier";
	/** An unordered array specifying the languages used in the	resource. */
	public static final String LANGUAGE = "dc:language";
	/** Publishers. */
	public static final String PUBLISHER = "dc:publisher";
	/** Relationships to other documents. */
	public static final String RELATION = "dc:relation";
	/** Informal rights statement, selected by language. */
	public static final String RIGHTS = "dc:rights";
	/** Unique identifier of the work from which this resource was derived. */
	public static final String SOURCE = "dc:source";
	/** An unordered array of descriptive phrases or keywords that specify the topic of the content of the resource. */
	public static final String SUBJECT = "dc:subject";
	/** The title of the document, or the name given to the resource. Typically, it will be a name by which the resource is formally known. */
	public static final String TITLE = "dc:title";
	/** A document type; for example, novel, poem, or working paper. */
	public static final String TYPE = "dc:type";

	
	public DublinCoreSchema() {
		super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
		setProperty(FORMAT, "application/pdf");
	}
	
	/**
	 * Adds a title.
	 * @param title
	 */
	public void addTitle(String title) {
		XmpArray array = new XmpArray(XmpArray.ALTERNATIVE);
		array.add(title);
		setProperty(TITLE, array);
	}

	/**
	 * Adds a description.
	 * @param desc
	 */
	public void addDescription(String desc) {
		XmpArray array = new XmpArray(XmpArray.ALTERNATIVE);
		array.add(desc);
		setProperty(DESCRIPTION, array);
	}

	/**
	 * Adds a subject.
	 * @param subject
	 */
	public void addSubject(String subject) {
		XmpArray array = new XmpArray(XmpArray.UNORDERED);
		array.add(subject);
		setProperty(SUBJECT, array);
	}

	
	/**
	 * Adds a subject.
	 * @param subject array of subjects
	 */
	public void addSubject(String[] subject) {
		XmpArray array = new XmpArray(XmpArray.UNORDERED);
		for (int i = 0; i < subject.length; i++) {
			array.add(subject[i]);
		}
		setProperty(SUBJECT, array);
	}
	
	/**
	 * Adds a single author.
	 * @param author
	 */
	public void addAuthor(String author) {
		XmpArray array = new XmpArray(XmpArray.ORDERED);
		array.add(author);
		setProperty(CREATOR, array);
	}

	/**
	 * Adds an array of authors.
	 * @param author
	 */
	public void addAuthor(String[] author) {
		XmpArray array = new XmpArray(XmpArray.ORDERED);
		for (int i = 0; i < author.length; i++) {
			array.add(author[i]);
		}
		setProperty(CREATOR, array);
	}

	/**
	 * Adds a single publisher.
	 * @param publisher
	 */
	public void addPublisher(String publisher) {
		XmpArray array = new XmpArray(XmpArray.ORDERED);
		array.add(publisher);
		setProperty(PUBLISHER, array);
	}

	/**
	 * Adds an array of publishers.
	 * @param publisher
	 */
	public void addPublisher(String[] publisher) {
		XmpArray array = new XmpArray(XmpArray.ORDERED);
		for (int i = 0; i < publisher.length; i++) {
			array.add(publisher[i]);
		}
		setProperty(PUBLISHER, array);
	}
}
