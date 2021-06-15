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
package org.hibernate.engine.jdbc.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.hibernate.engine.jdbc.CharacterStream;
import org.hibernate.type.descriptor.java.DataHelper;

/**
 * Implementation of {@link CharacterStream}
 *
 * @author Steve Ebersole
 */
public class CharacterStreamImpl implements CharacterStream {
	private final long length;

	private Reader reader;
	private String string;

	public CharacterStreamImpl(String chars) {
		this.string = chars;
		this.length = chars.length();
	}

	public CharacterStreamImpl(Reader reader, long length) {
		this.reader = reader;
		this.length = length;
	}

	@Override
	public Reader asReader() {
		if ( reader == null ) {
			reader = new StringReader( string );
		}
		return reader;
	}

	@Override
	public String asString() {
		if ( string == null ) {
			string = DataHelper.extractString( reader );
		}
		return string;
	}

	@Override
	public long getLength() {
		return length;
	}

	@Override
	public void release() {
		if ( reader == null ) {
			return;
		}
		try {
			reader.close();
		}
		catch (IOException ignore) {
		}
	}
}
