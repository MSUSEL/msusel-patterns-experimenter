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
package org.hibernate.engine.jdbc;

import java.lang.reflect.Proxy;
import java.sql.Clob;
import java.sql.NClob;

/**
 * Manages aspects of proxying java.sql.NClobs to add serializability.
 *
 * @author Steve Ebersole
 */
public class SerializableNClobProxy extends SerializableClobProxy {
	private static final Class[] PROXY_INTERFACES = new Class[] { NClob.class, WrappedNClob.class };

	public static boolean isNClob(Clob clob) {
		return NClob.class.isInstance( clob );
	}

	/**
	 * Builds a serializable {@link java.sql.Clob} wrapper around the given {@link java.sql.Clob}.
	 *
	 * @param clob The {@link java.sql.Clob} to be wrapped.
	 *
	 * @see #generateProxy(java.sql.Clob)
	 */
	protected SerializableNClobProxy(Clob clob) {
		super( clob );
	}

	/**
	 * Generates a SerializableNClobProxy proxy wrapping the provided NClob object.
	 *
	 * @param nclob The NClob to wrap.
	 * @return The generated proxy.
	 */
	public static NClob generateProxy(NClob nclob) {
		return ( NClob ) Proxy.newProxyInstance(
				getProxyClassLoader(),
				PROXY_INTERFACES,
				new SerializableNClobProxy( nclob )
		);
	}

	/**
	 * Determines the appropriate class loader to which the generated proxy
	 * should be scoped.
	 *
	 * @return The class loader appropriate for proxy construction.
	 */
	public static ClassLoader getProxyClassLoader() {
		return SerializableClobProxy.getProxyClassLoader();
	}
}
