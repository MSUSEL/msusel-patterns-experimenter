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
package org.hibernate.type;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;

/**
 * Logic to bind stream of char into a VARCHAR
 *
 * @author Emmanuel Bernard
 *
 * @deprecated Use the {@link AbstractStandardBasicType} approach instead
 */
public abstract class AbstractCharArrayType extends MutableType {

	/**
	 * Convert the char[] into the expected object type
	 */
	abstract protected Object toExternalFormat(char[] chars);

	/**
	 * Convert the object into the internal char[] representation
	 */
	abstract protected char[] toInternalFormat(Object chars);

	public Object get(ResultSet rs, String name) throws SQLException {
		Reader stream = rs.getCharacterStream(name);
		if ( stream == null ) return toExternalFormat( null );
		CharArrayWriter writer = new CharArrayWriter();
		for(;;) {
			try {
				int c = stream.read();
				if ( c == -1) return toExternalFormat( writer.toCharArray() );
				writer.write( c );
			}
			catch (IOException e) {
				throw new HibernateException("Unable to read character stream from rs");
			}
		}
	}

	public abstract Class getReturnedClass();

	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		char[] chars = toInternalFormat( value );
		st.setCharacterStream(index, new CharArrayReader(chars), chars.length);
	}

	public int sqlType() {
		return Types.VARCHAR;
	}

	public String objectToSQLString(Object value, Dialect dialect) throws Exception {

		return '\'' + new String( toInternalFormat( value ) ) + '\'';
	}

	public Object stringToObject(String xml) throws Exception {
		if (xml == null) return toExternalFormat( null );
		int length = xml.length();
		char[] chars = new char[length];
		for (int index = 0 ; index < length ; index++ ) {
			chars[index] = xml.charAt( index );
		}
		return toExternalFormat( chars );
	}

	public String toString(Object value) {
		if (value == null) return null;
		return new String( toInternalFormat( value ) );
	}

	public Object fromStringValue(String xml) {
		if (xml == null) return null;
		int length = xml.length();
		char[] chars = new char[length];
		for (int index = 0 ; index < length ; index++ ) {
			chars[index] = xml.charAt( index );
		}
		return toExternalFormat( chars );
	}

	protected Object deepCopyNotNull(Object value) throws HibernateException {
		char[] chars = toInternalFormat(value);
		char[] result = new char[chars.length];
		System.arraycopy(chars, 0, result, 0, chars.length);
		return toExternalFormat(result);
	}
}
