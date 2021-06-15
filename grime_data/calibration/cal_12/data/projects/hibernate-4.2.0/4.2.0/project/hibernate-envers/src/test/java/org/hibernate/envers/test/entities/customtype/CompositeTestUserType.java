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
package org.hibernate.envers.test.entities.customtype;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 * @author Andrew DePue
 * @author Adam Warski (adam at warski dot org)
 */
public class CompositeTestUserType implements CompositeUserType {
    public String[] getPropertyNames() {
        return new String[] { "prop1", "prop2" };
    }

    public Type[] getPropertyTypes() {
        return new Type[] { StringType.INSTANCE, IntegerType.INSTANCE };
    }

    public Object getPropertyValue(final Object component, final int property) throws HibernateException {
        Component comp = (Component) component;
        if (property == 0) {
            return comp.getProp1();
        } else {
            return comp.getProp2();
        }
    }

    public void setPropertyValue(final Object component, final int property, final Object value) throws HibernateException {
        Component comp = (Component) component;
        if (property == 0) {
            comp.setProp1((String) value);
        } else {
            comp.setProp2((Integer) value);
        }
    }

    public Class returnedClass() {
        return Component.class;
    }

    public boolean equals(final Object x, final Object y) throws HibernateException {
        //noinspection ObjectEquality
        if (x == y) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        return x.equals(y);
    }

    public int hashCode(final Object x) throws HibernateException {
        return x.hashCode();
    }

    public Object nullSafeGet(final ResultSet rs, final String[] names,
                              final SessionImplementor session,
                              final Object owner) throws HibernateException, SQLException {
        final String prop1 = rs.getString(names[0]);
        if (prop1 == null) {
            return null;
        }
        final int prop2 = rs.getInt(names[1]);
        
        return new Component(prop1, prop2);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value,
                            final int index, final SessionImplementor session)
            throws HibernateException, SQLException
    {
        if (value == null) {
            st.setNull(index, StringType.INSTANCE.sqlType());
            st.setNull(index + 1, IntegerType.INSTANCE.sqlType());
        } else {
            final Component comp = (Component) value;
            st.setString(index, comp.getProp1());
            st.setInt(index + 1, comp.getProp2());
        }
    }

    public Object deepCopy(final Object value) throws HibernateException {
        Component comp = (Component) value;
        return new Component(comp.getProp1(), comp.getProp2());
    }

    public boolean isMutable() {
        return true;
    }

    public Serializable disassemble(final Object value, final SessionImplementor session) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(final Serializable cached, final SessionImplementor session,
                           final Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target,
                          SessionImplementor session, Object owner) throws HibernateException {
        return original;
    }
}