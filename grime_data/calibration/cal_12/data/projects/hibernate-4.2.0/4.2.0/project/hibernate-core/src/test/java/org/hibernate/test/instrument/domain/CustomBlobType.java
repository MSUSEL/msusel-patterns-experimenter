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
package org.hibernate.test.instrument.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

/**
 * A simple byte[]-based custom type.
 */
public class CustomBlobType implements UserType {
	/**
	 * {@inheritDoc}
	 */
	public Object nullSafeGet(ResultSet rs, String names[], SessionImplementor session, Object owner) throws SQLException {
		// cast just to make sure...
		return StandardBasicTypes.BINARY.nullSafeGet( rs, names[0], session );
	}

	/**
	 * {@inheritDoc}
	 */
	public void nullSafeSet(PreparedStatement ps, Object value, int index, SessionImplementor session) throws SQLException, HibernateException {
		// cast just to make sure...
		StandardBasicTypes.BINARY.nullSafeSet( ps, value, index, session );
	}

	/**
	 * {@inheritDoc}
	 */
	public Object deepCopy(Object value) {
		byte result[] = null;

		if ( value != null ) {
			byte bytes[] = ( byte[] ) value;

			result = new byte[bytes.length];
			System.arraycopy( bytes, 0, result, 0, bytes.length );
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMutable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public int[] sqlTypes() {
		return new int[] { Types.VARBINARY };
	}

	/**
	 * {@inheritDoc}
	 */
	public Class returnedClass() {
		return byte[].class;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object x, Object y) {
		return Arrays.equals( ( byte[] ) x, ( byte[] ) y );
	}

	/**
	 * {@inheritDoc}
	 */
	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Serializable disassemble(Object arg0)
			throws HibernateException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode(Object arg0)
			throws HibernateException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object replace(Object arg0, Object arg1, Object arg2)
			throws HibernateException {
		return null;
	}
}