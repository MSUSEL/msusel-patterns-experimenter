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

/** <p>Utility class for xs:hexbinary.</p>
 *
 *
 *
 * @source $URL$
 */
public class HexBinary {
	/** Creates a clone of the given byte array.
	 */
	public static byte[] getClone(byte[] pHexBinary) {
		byte[] result = new byte[pHexBinary.length];
		System.arraycopy(pHexBinary, 0, result, 0, pHexBinary.length);
		return result;
	}

	/** Converts the string <code>pValue</code> into an
	 * array of hex bytes.
	 */
	public static byte[] decode(String pValue) {
		if ((pValue.length() % 2) != 0) {
			throw new IllegalArgumentException("A HexBinary string must have even length.");
		}
		byte[] result = new byte[pValue.length() / 2];
		int j = 0;
		for (int i = 0;  i < pValue.length();  ) {
			byte b;
			char c = pValue.charAt(i++);
			char d = pValue.charAt(i++);
			if (c >= '0'  &&  c <= '9') {
				b = (byte) ((c - '0') << 4);
			} else if (c >= 'A'  &&  c <= 'F') {
				b = (byte) ((c - 'A' + 10) << 4);
			} else if (c >= 'a'  &&  c <= 'f') {
				b = (byte) ((c - 'a' + 10) << 4);
			} else {
				throw new IllegalArgumentException("Invalid hex digit: " + c);
			}
			if (d >= '0'  &&  d <= '9') {
				b += (byte) (d - '0');
			} else if (d >= 'A'  &&  d <= 'F') {
				b += (byte) (d - 'A' + 10);
			} else if (d >= 'a'  &&  d <= 'f') {
				b += (byte) (d - 'a' + 10);
			} else {
				throw new IllegalArgumentException("Invalid hex digit: " + d);
			}
			result[j++] = b;
		}
		return result;
	}

	/** Converts the byte array <code>pHexBinary</code> into
	 * a string.
	 */
	public static String encode(byte[] pHexBinary) {
		StringBuffer result = new StringBuffer();
		for (int i = 0;  i < pHexBinary.length;  i++) {
			byte b = pHexBinary[i];
			byte c = (byte) ((b & 0xf0) >> 4);
			if (c <= 9) {
				result.append((char) ('0' + c));
			} else {
				result.append((char) ('A' + c - 10));
			}
			c = (byte) (b & 0x0f);
			if (c <= 9) {
				result.append((char) ('0' + c));
			} else {
				result.append((char) ('A' + c - 10));
			}
		}
		return result.toString();
	}
}
