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
package org.hibernate.test.cut;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 * @author Gavin King
 */
public class MonetoryAmountUserType implements CompositeUserType {

	public String[] getPropertyNames() {
		return new String[] { "amount", "currency" };
	}

	public Type[] getPropertyTypes() {
		return new Type[] { StandardBasicTypes.BIG_DECIMAL, StandardBasicTypes.CURRENCY };
	}

	public Object getPropertyValue(Object component, int property) throws HibernateException {
		MonetoryAmount ma = (MonetoryAmount) component;
		return property==0 ? ma.getAmount() : ma.getCurrency();
	}

	public void setPropertyValue(Object component, int property, Object value)
			throws HibernateException {
		MonetoryAmount ma = (MonetoryAmount) component;
		if ( property==0 ) {
			ma.setAmount( (BigDecimal) value );
		}
		else {
			ma.setCurrency( (Currency) value );
		}
	}

	public Class returnedClass() {
		return MonetoryAmount.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x==y) return true;
		if (x==null || y==null) return false;
		MonetoryAmount mx = (MonetoryAmount) x;
		MonetoryAmount my = (MonetoryAmount) y;
		return mx.getAmount().equals( my.getAmount() ) &&
			mx.getCurrency().equals( my.getCurrency() );
	}

	public int hashCode(Object x) throws HibernateException {
		return ( (MonetoryAmount) x ).getAmount().hashCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		BigDecimal amt = StandardBasicTypes.BIG_DECIMAL.nullSafeGet( rs, names[0], session );
		Currency cur = StandardBasicTypes.CURRENCY.nullSafeGet( rs, names[1], session );
		if (amt==null) return null;
		return new MonetoryAmount(amt, cur);
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		MonetoryAmount ma = (MonetoryAmount) value;
		BigDecimal amt = ma == null ? null : ma.getAmount();
		Currency cur = ma == null ? null : ma.getCurrency();
		StandardBasicTypes.BIG_DECIMAL.nullSafeSet(st, amt, index, session);
		StandardBasicTypes.CURRENCY.nullSafeSet(st, cur, index+1, session);
	}

	public Object deepCopy(Object value) throws HibernateException {
		MonetoryAmount ma = (MonetoryAmount) value;
		return new MonetoryAmount( ma.getAmount(), ma.getCurrency() );
	}

	public boolean isMutable() {
		return true;
	}

	public Serializable disassemble(Object value, SessionImplementor session)
			throws HibernateException {
		return (Serializable) deepCopy(value);
	}

	public Object assemble(Serializable cached, SessionImplementor session, Object owner)
			throws HibernateException {
		return deepCopy(cached);
	}

	public Object replace(Object original, Object target, SessionImplementor session, Object owner)
			throws HibernateException {
		return deepCopy(original); //TODO: improve
	}

}
