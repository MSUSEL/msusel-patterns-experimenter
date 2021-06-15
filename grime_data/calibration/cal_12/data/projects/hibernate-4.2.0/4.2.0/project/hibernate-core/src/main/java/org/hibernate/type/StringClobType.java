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
import java.io.Serializable;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * Map a String to a Clob
 *
 * @author Emmanuel Bernard
 * @deprecated replaced by {@link org.hibernate.type.MaterializedClobType}
 */
@Deprecated
public class StringClobType implements UserType, Serializable {
	public int[] sqlTypes() {
		return new int[]{Types.CLOB};
	}

	public Class returnedClass() {
		return String.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return ( x == y ) || ( x != null && x.equals( y ) );
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		Reader reader = rs.getCharacterStream( names[0] );
		if ( reader == null ) return null;
		StringBuilder result = new StringBuilder( 4096 );
		try {
			char[] charbuf = new char[4096];
			for ( int i = reader.read( charbuf ); i > 0 ; i = reader.read( charbuf ) ) {
				result.append( charbuf, 0, i );
			}
		}
		catch (IOException e) {
			throw new SQLException( e.getMessage() );
		}
		return result.toString();
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		if ( value != null ) {
			String string = (String) value;
			StringReader reader = new StringReader( string );
			st.setCharacterStream( index, reader, string.length() );
		}
		else {
			st.setNull( index, sqlTypes()[0] );
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		//returning value should be OK since String are immutable
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}
