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
//$Id: DoubleStringType.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

public class DoubleStringType implements CompositeUserType {

	private static final int[] TYPES = { Types.VARCHAR, Types.VARCHAR };

	public int[] sqlTypes() {
		return TYPES;
	}

	public Class returnedClass() {
		return String[].class;
	}

	public boolean equals(Object x, Object y) {
		if (x==y) return true;
		if (x==null || y==null) return false;
		return ( (String[]) x )[0].equals( ( (String[]) y )[0] ) && ( (String[]) x )[1].equals( ( (String[]) y )[1] );
	}

	public int hashCode(Object x) throws HibernateException {
		String[] a = (String[]) x;
		return a[0].hashCode() + 31 * a[1].hashCode(); 
	}

	public Object deepCopy(Object x) {
		if (x==null) return null;
		String[] result = new String[2];
		String[] input = (String[]) x;
		result[0] = input[0];
		result[1] = input[1];
		return result;
	}

	public boolean isMutable() { return true; }

	public Object nullSafeGet(ResultSet rs,	String[] names, SessionImplementor session,	Object owner)
	throws HibernateException, SQLException {

		String first = StringType.INSTANCE.nullSafeGet( rs, names[0], session );
		String second = StringType.INSTANCE.nullSafeGet( rs, names[1], session );

		return ( first==null && second==null ) ? null : new String[] { first, second };
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
	throws HibernateException, SQLException {

		String[] strings = (value==null) ? new String[2] : (String[]) value;

		StringType.INSTANCE.nullSafeSet( st, strings[0], index, session );
		StringType.INSTANCE.nullSafeSet( st, strings[1], index+1, session );
	}

	public String[] getPropertyNames() {
		return new String[] { "s1", "s2" };
	}

	public Type[] getPropertyTypes() {
		return new Type[] { StringType.INSTANCE, StringType.INSTANCE };
	}

	public Object getPropertyValue(Object component, int property) {
		return ( (String[]) component )[property];
	}

	public void setPropertyValue(
		Object component,
		int property,
		Object value) {

		( (String[]) component )[property] = (String) value;
	}

	public Object assemble(
		Serializable cached,
		SessionImplementor session,
		Object owner) {

		return deepCopy(cached);
	}

	public Serializable disassemble(Object value, SessionImplementor session) {
		return (Serializable) deepCopy(value);
	}
	
	public Object replace(Object original, Object target, SessionImplementor session, Object owner) 
	throws HibernateException {
		return original;
	}

}
