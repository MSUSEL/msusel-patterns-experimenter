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

import java.util.ArrayList;

/**
 * StringBuffer to construct an XMP array.
 */
public class XmpArray extends ArrayList<String> {

	private static final long serialVersionUID = 5722854116328732742L;
	/** An array that is unordered. */
	public static final String UNORDERED = "rdf:Bag";
	/** An array that is ordered. */
	public static final String ORDERED = "rdf:Seq";
	/** An array with alternatives. */
	public static final String ALTERNATIVE = "rdf:Alt";

	/** the type of array. */
	protected String type;

	/**
	 * Creates an XmpArray.
	 * @param type the type of array: UNORDERED, ORDERED or ALTERNATIVE.
	 */
	public XmpArray(String type) {
		this.type = type;
	}

	/**
	 * Returns the String representation of the XmpArray.
	 * @return a String representation
	 */
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer("<");
		buf.append(type);
		buf.append('>');
		String s;
		for (String string : this) {
			s = string;
			buf.append("<rdf:li>");
			buf.append(XmpSchema.escape(s));
			buf.append("</rdf:li>");
		}
		buf.append("</");
		buf.append(type);
		buf.append('>');
		return buf.toString();
	}
}