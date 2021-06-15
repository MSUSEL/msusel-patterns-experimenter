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
package com.jasml.compiler;

public class ParsingException extends Exception {
	int offset = -1, line = -1, column = -1;

	public ParsingException(int offset, int line, int column, String msg) {
		super(msg);
		this.offset = offset;
		this.line = line;
		this.column = column;
	}

	public ParsingException(int line, int column, String msg) {
		super(msg);
		this.line = line;
		this.column = column;
	}

	public ParsingException(int offset, String msg) {
		super(msg);
		this.offset = offset;
	}

	public ParsingException(String msg, Exception e) {
		super(msg+e.getMessage(), e);
	}
	public ParsingException(String msg){
		super(msg);
	}

	public String getMessage() {
		StringBuffer buf = new StringBuffer(super.getMessage());
		if (offset != -1 || line != -1 || column != -1) {
			buf.append(" [");
			if (offset != -1) {
				buf.append("offset:" + offset + ",");
			}
			if (line != -1) {
				buf.append("line:" + (line+1) + ",");
			}
			if (column != -1) {
				buf.append("column:" + (column+1));
			}
			if (buf.charAt(buf.length()-1) == ',') {
				buf.deleteCharAt(buf.length() - 1);
			}
			buf.append(']');
		}
		
		return buf.toString();
	}
}
