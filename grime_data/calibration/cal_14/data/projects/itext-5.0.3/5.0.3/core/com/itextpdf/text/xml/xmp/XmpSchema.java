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

/**
 * Abstract superclass of the XmpSchemas supported by iText.
 */
public abstract class XmpSchema extends Properties {

	private static final long serialVersionUID = -176374295948945272L;

	/** the namesspace */
	protected String xmlns;

	/** Constructs an XMP schema.
	 * @param xmlns
	 */
	public XmpSchema(String xmlns) {
		super();
		this.xmlns = xmlns;
	}
	/**
	 * The String representation of the contents.
	 * @return a String representation.
	 */
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Enumeration<?> e = this.propertyNames(); e.hasMoreElements(); ) {
			process(buf, e.nextElement());
		}
		return buf.toString();
	}
	/**
	 * Processes a property
	 * @param buf
	 * @param p
	 */
	protected void process(StringBuffer buf, Object p) {
		buf.append('<');
		buf.append(p);
		buf.append('>');
		buf.append(this.get(p));
		buf.append("</");
		buf.append(p);
		buf.append('>');
	}
	/**
	 * @return Returns the xmlns.
	 */
	public String getXmlns() {
		return xmlns;
	}

	/**
	 * @param key
	 * @param value
	 * @return the previous property (null if there wasn't one)
	 */
	public Object addProperty(String key, String value) {
		return this.setProperty(key, value);
	}

	/**
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
    public Object setProperty(String key, String value) {
		return super.setProperty(key, escape(value));
	}

	/**
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 *
	 * @param key
	 * @param value
	 * @return the previous property (null if there wasn't one)
	 */
	public Object setProperty(String key, XmpArray value) {
		return super.setProperty(key, value.toString());
	}

	/**
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 *
	 * @param key
	 * @param value
	 * @return the previous property (null if there wasn't one)
	 */
	public Object setProperty(String key, LangAlt value) {
		return super.setProperty(key, value.toString());
	 }

	/**
	 * @param content
	 * @return an escaped string
	 */
	public static String escape(String content) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < content.length(); i++) {
			switch(content.charAt(i)) {
			case '<':
				buf.append("&lt;");
				break;
			case '>':
				buf.append("&gt;");
				break;
			case '\'':
				buf.append("&apos;");
				break;
			case '\"':
				buf.append("&quot;");
				break;
			case '&':
				buf.append("&amp;");
				break;
			default:
				buf.append(content.charAt(i));
			}
		}
		return buf.toString();
	}
}
