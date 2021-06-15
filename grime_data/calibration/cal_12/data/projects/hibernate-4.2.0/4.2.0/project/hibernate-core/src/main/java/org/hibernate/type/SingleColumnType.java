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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Provide convenient methods for binding and extracting values for use with {@link BasicType}.  Most of this
 * is copied from the (now deprecated) {@link NullableType}.
 * <p/>
 * Glaring omission are the forms that do not take
 *
 * @author Steve Ebersole
 */
public interface SingleColumnType<T> extends Type {

	public int sqlType();

	public String toString(T value) throws HibernateException;

	public T fromStringValue(String xml) throws HibernateException;

	/**
	 * Get a column value from a result set by name.
	 *
	 * @param rs The result set from which to extract the value.
	 * @param name The name of the value to extract.
	 * @param session The session from which the request originates
	 *
	 * @return The extracted value.
	 *
	 * @throws org.hibernate.HibernateException Generally some form of mismatch error.
	 * @throws java.sql.SQLException Indicates problem making the JDBC call(s).
	 */
	public T nullSafeGet(ResultSet rs, String name, SessionImplementor session) throws HibernateException, SQLException;

	/**
	 * Get a column value from a result set, without worrying about the possibility of null values.
	 *
	 * @param rs The result set from which to extract the value.
	 * @param name The name of the value to extract.
	 * @param session The session from which the request originates
	 *
	 * @return The extracted value.
	 *
	 * @throws org.hibernate.HibernateException Generally some form of mismatch error.
	 * @throws java.sql.SQLException Indicates problem making the JDBC call(s).
	 */
	public Object get(ResultSet rs, String name, SessionImplementor session) throws HibernateException, SQLException;

	/**
	 * Set a parameter value without worrying about the possibility of null
	 * values.  Called from {@link #nullSafeSet} after nullness checks have
	 * been performed.
	 *
	 * @param st The statement into which to bind the parameter value.
	 * @param value The parameter value to bind.
	 * @param index The position or index at which to bind the param value.
	 * @param session The session from which the request originates
	 *
	 * @throws org.hibernate.HibernateException Generally some form of mismatch error.
	 * @throws java.sql.SQLException Indicates problem making the JDBC call(s).
	 */
	public void set(PreparedStatement st, T value, int index, SessionImplementor session) throws HibernateException, SQLException;
}
