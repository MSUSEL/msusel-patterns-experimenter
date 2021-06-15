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
package com.itextpdf.rups.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * Everything writing to this OutputStream will be shown in a JTextArea.
 */
public class TextAreaOutputStream extends OutputStream {
	/** The text area to which we want to write. */
	protected JTextArea text;
	/** Keeps track of the offset of the text in the text area. */
	protected int offset;
	
	/**
	 * Constructs a TextAreaOutputStream.
	 * @param text	the text area to which we want to write.
	 * @throws IOException
	 */
	public TextAreaOutputStream(JTextArea text) throws IOException {
		this.text = text;
		clear();
	}

	/**
	 * Clear the text area.
	 */
	public void clear() {
		text.setText(null);
		offset = 0;
	}
	
	/**
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int i) throws IOException {
		byte[] b = { (byte)i };
		write(b, 0, 1);
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		String snippet = new String(b, off, len);
		text.insert(snippet, offset);
		offset += len - off;
	}

	/**
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		byte[] snippet = new byte[1024];
		int bytesread;
		while ((bytesread = bais.read(snippet)) > 0) {
			write(snippet, 0, bytesread);
		}
	}
	
}
