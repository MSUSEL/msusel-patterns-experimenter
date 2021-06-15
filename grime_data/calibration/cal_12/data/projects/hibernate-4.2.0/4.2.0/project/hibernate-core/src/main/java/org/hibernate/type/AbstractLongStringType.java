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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;

/**
 * An abstract type for mapping long string SQL types to a Java String.
 * @author Gavin King, Bertrand Renuart (from TextType)
 *
 * @deprecated Use the {@link AbstractStandardBasicType} approach instead
 */
public abstract class AbstractLongStringType extends ImmutableType {

	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		String str = (String) value;
		st.setCharacterStream( index, new StringReader(str), str.length() );
	}

	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {

			// Retrieve the value of the designated column in the current row of this
			// ResultSet object as a java.io.Reader object
			Reader charReader = rs.getCharacterStream(name);

			// if the corresponding SQL value is NULL, the reader we got is NULL as well
			if (charReader==null) return null;

			// Fetch Reader content up to the end - and put characters in a StringBuilder
			StringBuilder sb = new StringBuilder();
			try {
				char[] buffer = new char[2048];
				while (true) {
					int amountRead = charReader.read(buffer, 0, buffer.length);
					if ( amountRead == -1 ) break;
					sb.append(buffer, 0, amountRead);
				}
			}
			catch (IOException ioe) {
				throw new HibernateException( "IOException occurred reading text", ioe );
			}
			finally {
				try {
					charReader.close();
				}
				catch (IOException e) {
					throw new HibernateException( "IOException occurred closing stream", e );
				}
			}

			// Return StringBuilder content as a large String
			return sb.toString();
	}

	public Class getReturnedClass() {
		return String.class;
	}

	public String toString(Object val) {
		return (String) val;
	}
	public Object fromStringValue(String xml) {
		return xml;
	}

}