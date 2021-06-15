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
package org.geotools.xml.impl;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/** A utility class for working with base64 encoding.
 *
 *
 *
 * @source $URL$
 */
public class Base64Binary {
	/** Creates a clone of the byte array <code>pValue</code>.
	 */
	public static byte[] getClone(byte[] pValue) {
		byte[] result = new byte[pValue.length];
		System.arraycopy(pValue, 0, result, 0, pValue.length);
		return result;
	}

	/** Converts the string <code>pValue</code> into a
	 * base64 encoded byte array.
	 */
	public static byte[] decode(String pValue) throws IOException {
		return (new BASE64Decoder()).decodeBuffer(pValue);
	}

	/** Converts the base64 encoded byte array <code>pValue</code>
	 * into a string.
	 */
	public static String encode(byte[] pValue) {
		return (new BASE64Encoder()).encode(pValue);
	}
}
