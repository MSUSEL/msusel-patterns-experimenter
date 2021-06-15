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

import com.itextpdf.text.Document;

/**
 * An implementation of an XmpSchema.
 */
public class PdfSchema extends XmpSchema {

	private static final long serialVersionUID = -1541148669123992185L;
	/** default namespace identifier*/
	public static final String DEFAULT_XPATH_ID = "pdf";
	/** default namespace uri*/
	public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/pdf/1.3/";
	
	/** Keywords. */
	public static final String KEYWORDS = "pdf:Keywords";
	/** The PDF file version (for example: 1.0, 1.3, and so on). */
	public static final String VERSION = "pdf:PDFVersion";
	/** The Producer. */
	public static final String PRODUCER = "pdf:Producer";


	public PdfSchema() {
		super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
		addProducer(Document.getVersion());
	}
	
	/**
	 * Adds keywords.
	 * @param keywords
	 */
	public void addKeywords(String keywords) {
		setProperty(KEYWORDS, keywords);
	}
	
	/**
	 * Adds the producer.
	 * @param producer
	 */
	public void addProducer(String producer) {
		setProperty(PRODUCER, producer);
	}

	/**
	 * Adds the version.
	 * @param version
	 */
	public void addVersion(String version) {
		setProperty(VERSION, version);
	}
}
