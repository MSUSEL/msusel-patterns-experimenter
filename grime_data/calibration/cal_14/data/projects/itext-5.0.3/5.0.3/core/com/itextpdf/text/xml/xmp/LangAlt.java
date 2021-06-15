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

import java.util.Enumeration;
import java.util.Properties;

public class LangAlt extends Properties {

	/** A serial version id. */
	private static final long serialVersionUID = 4396971487200843099L;

	/** Key for the default language. */
	public static final String DEFAULT = "x-default";

	/** Creates a Properties object that stores languages for use in an XmpSchema */
	public LangAlt(String defaultValue) {
		super();
		addLanguage(DEFAULT, defaultValue);
	}

	/** Creates a Properties object that stores languages for use in an XmpSchema */
	public LangAlt() {
		super();
	}

	/**
	 * Add a language.
	 */
	public void addLanguage(String language, String value) {
		setProperty(language, XmpSchema.escape(value));
	}

	/**
	 * Process a property.
	 */
	protected void process(StringBuffer buf, Object lang) {
		buf.append("<rdf:li xml:lang=\"");
		buf.append(lang);
		buf.append("\" >");
		buf.append(get(lang));
		buf.append("</rdf:li>");
	}

	/**
	 * Creates a String that can be used in an XmpSchema.
	 */
	@Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<rdf:Alt>");
		for (Enumeration<?> e = this.propertyNames(); e.hasMoreElements();) {
			process(sb, e.nextElement());
		}
		sb.append("</rdf:Alt>");
		return sb.toString();
	}

}