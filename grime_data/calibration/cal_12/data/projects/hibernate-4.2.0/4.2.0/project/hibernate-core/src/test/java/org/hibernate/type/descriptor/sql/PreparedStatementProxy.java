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
package org.hibernate.type.descriptor.sql;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.Assert;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class PreparedStatementProxy<T> implements InvocationHandler {
	public static PreparedStatement generateProxy(PreparedStatementProxy handler) {
		return (PreparedStatement) Proxy.newProxyInstance(
				getProxyClassLoader(),
				new Class[] { PreparedStatement.class },
				handler
		);
	}

	private static ClassLoader getProxyClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if ( cl == null ) {
			cl = PreparedStatement.class.getClassLoader();
		}
		return cl;
	}

	@SuppressWarnings({ "unchecked" })
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ( value == null ) {
			Assert.assertEquals( "Expecting setNull call", "setNull", method.getName() );
			return null;
		}
		if ( method.getName().equals( methodName ) && args.length >= 1 ) {
			checkValue( (T) args[1] );
			return null;
		}
		throw new UnsupportedOperationException( "Unexpected call PreparedStatement." + method.getName() );
	}

	protected void checkValue(T arg) throws SQLException {
		Assert.assertEquals( value, arg );
	}

	protected final String extractString(Clob clob) throws SQLException {
		if ( StringClobImpl.class.isInstance( clob ) ) {
			return ( (StringClobImpl) clob ).getValue();
		}
		return clob.getSubString( 1, (int)clob.length() );
	}

	private final String methodName;
	private final T value;

	public T getValue() {
		return value;
	}

	protected PreparedStatementProxy(String methodName, T value) {
		this.methodName = methodName;
		this.value = value;
	}

	public static PreparedStatement generateProxy(final String value) {
		return generateProxy(
				new PreparedStatementProxy<String>( "setString", value )
		);
	}

	public static PreparedStatement generateProxy(Clob value) {
		return generateProxy(
				new PreparedStatementProxy<Clob>( "setClob", value ) {
					@Override
					protected void checkValue(Clob arg) throws SQLException {
						Assert.assertEquals( extractString( getValue() ), extractString( arg ) );
					}
				}
		);
	}
}
