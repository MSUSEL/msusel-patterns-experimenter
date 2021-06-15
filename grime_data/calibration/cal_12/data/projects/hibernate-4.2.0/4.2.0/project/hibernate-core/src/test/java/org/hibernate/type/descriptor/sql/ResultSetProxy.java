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
import java.sql.ResultSet;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class ResultSetProxy<T> implements InvocationHandler {
	public static ResultSet generateProxy(ResultSetProxy handler) {
		return ( ResultSet ) Proxy.newProxyInstance(
				getProxyClassLoader(),
				new Class[] { ResultSet.class },
				handler
		);
	}

	private static ClassLoader getProxyClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if ( cl == null ) {
			cl = ResultSet.class.getClassLoader();
		}
		return cl;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ( method.getName().equals( methodName ) && args.length >= 1 ) {
			return value;
		}
		if ( method.getName().equals( "wasNull" ) ) {
			return value == null;
		}
		throw new UnsupportedOperationException( "Unexpected call ResultSet." + method.getName() );
	}

	private final String methodName;
	private final T value;

	protected ResultSetProxy(String methodName, T value) {
		this.methodName = methodName;
		this.value = value;
	}

	public static ResultSet generateProxy(final String value) {
		return generateProxy(
				new ResultSetProxy<String>( "getString", value )
		);
	}

	public static ResultSet generateProxy(final Clob value) {
		return generateProxy(
				new ResultSetProxy<Clob>( "getClob", value )
		);
	}
}
