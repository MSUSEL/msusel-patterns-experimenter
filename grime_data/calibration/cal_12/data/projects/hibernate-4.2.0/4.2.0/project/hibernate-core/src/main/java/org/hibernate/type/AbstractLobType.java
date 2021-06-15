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
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metamodel.relational.Size;

/**
 * @author Emmanuel Bernard
 * @deprecated
 */
@Deprecated
public abstract class AbstractLobType extends AbstractType implements Serializable {
	public boolean isDirty(Object old, Object current, boolean[] checkable, SessionImplementor session)
			throws HibernateException {
		return checkable[0] ? ! isEqual( old, current ) : false;
	}

	@Override
	public Size[] dictatedSizes(Mapping mapping) throws MappingException {
		return new Size[] { LEGACY_DICTATED_SIZE };
	}

	@Override
	public Size[] defaultSizes(Mapping mapping) throws MappingException {
		return new Size[] { LEGACY_DEFAULT_SIZE };
	}

	@Override
	public boolean isEqual(Object x, Object y) {
		return isEqual( x, y, null );
	}

	@Override
	public int getHashCode(Object x) {
		return getHashCode( x, null );
	}

	public String getName() {
		return this.getClass().getName();
	}

	public int getColumnSpan(Mapping mapping) throws MappingException {
		return 1;
	}

	protected abstract Object get(ResultSet rs, String name) throws SQLException;

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		return get( rs, names[0] );
	}

	public Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		return get( rs, name );
	}

	public void nullSafeSet(
			PreparedStatement st, Object value, int index, boolean[] settable, SessionImplementor session
	) throws HibernateException, SQLException {
		if ( settable[0] ) set( st, value, index, session );
	}

	protected abstract void set(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws SQLException;

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		set( st, value, index, session );
	}
}
