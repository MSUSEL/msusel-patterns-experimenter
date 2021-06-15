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
package org.hibernate.test.sql.hand;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Currency;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * This is a simple Hibernate custom mapping type for MonetaryAmount value types.
 * <p>
 * 
 * @author Max & Christian 
 */
public class MonetaryAmountUserType
		implements UserType {

	private static final int[] SQL_TYPES = {Types.NUMERIC, Types.VARCHAR };

	public int[] sqlTypes() { return SQL_TYPES; }

	public Class returnedClass() { return MonetaryAmount.class; }

	public boolean isMutable() { return false; }

	public Object deepCopy(Object value) {
		return value; // MonetaryAmount is immutable
	}

	public boolean equals(Object x, Object y) {
		if (x == y) return true;
		if (x == null || y == null) return false;
		return x.equals(y);
	}

	public Object nullSafeGet(ResultSet resultSet,
							  String[] names,
							  SessionImplementor session, Object owner)
			throws HibernateException, SQLException {

		BigDecimal value = resultSet.getBigDecimal(names[0]);
		if (resultSet.wasNull()) return null;
		String cur = resultSet.getString(names[1]);
		Currency userCurrency = Currency.getInstance(cur);
						
		return new MonetaryAmount(value, userCurrency);
	}

	public void nullSafeSet(PreparedStatement statement,
							Object value,
							int index, SessionImplementor session)
			throws HibernateException, SQLException {

		if (value == null) {
			statement.setNull(index, Types.NUMERIC);			
			statement.setNull(index+1, Types.VARCHAR);
		} else {
			MonetaryAmount currency = (MonetaryAmount)value;
			statement.setBigDecimal(index, currency.getValue());
			statement.setString(index+1, currency.getCurrency().getCurrencyCode());
		}
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner)
	throws HibernateException {
		return original;
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}
}
