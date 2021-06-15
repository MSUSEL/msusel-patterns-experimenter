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
package org.hibernate.type.descriptor.sql;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class StringClobImpl implements Clob {
	private final String value;

	public StringClobImpl(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public long length() throws SQLException {
		return value.length();
	}

	public String getSubString(long pos, int length) throws SQLException {
		return value.substring( (int)pos, (int)(pos+length) );
	}

	public Reader getCharacterStream() throws SQLException {
		return new StringReader( value );
	}

	public Reader getCharacterStream(long pos, long length) throws SQLException {
		return new StringReader( getSubString( pos, (int)length ) );
	}

	public InputStream getAsciiStream() throws SQLException {
		throw new UnsupportedOperationException( "not supported" );
	}

	public long position(String searchstr, long start) throws SQLException {
		return value.indexOf( searchstr, (int)start );
	}

	public long position(Clob searchstr, long start) throws SQLException {
		throw new UnsupportedOperationException( "not supported" );
	}

	public int setString(long pos, String str) throws SQLException {
		throw new UnsupportedOperationException( "not supported" );
	}

	public int setString(long pos, String str, int offset, int len) throws SQLException {
		throw new UnsupportedOperationException( "not supported" );
	}

	public OutputStream setAsciiStream(long pos) throws SQLException {
		throw new UnsupportedOperationException( "not supported" );
	}

	public Writer setCharacterStream(long pos) throws SQLException {
		throw new UnsupportedOperationException( "not supported" );
	}

	public void truncate(long len) throws SQLException {
	}

	public void free() throws SQLException {
	}
}
