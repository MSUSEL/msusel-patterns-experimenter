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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Clob;

import org.hibernate.HibernateException;
import org.hibernate.internal.util.ClassLoaderHelper;

/**
 * Manages aspects of proxying {@link Clob Clobs} to add serializability.
 *
 * @author Gavin King
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class SerializableClobProxy implements InvocationHandler, Serializable {
	private static final Class[] PROXY_INTERFACES = new Class[] { Clob.class, WrappedClob.class, Serializable.class };

	private transient final Clob clob;

	/**
	 * Builds a serializable {@link java.sql.Clob} wrapper around the given {@link java.sql.Clob}.
	 *
	 * @param clob The {@link java.sql.Clob} to be wrapped.
	 * @see #generateProxy(java.sql.Clob)
	 */
	protected SerializableClobProxy(Clob clob) {
		this.clob = clob;
	}

	public Clob getWrappedClob() {
		if ( clob == null ) {
			throw new IllegalStateException( "Clobs may not be accessed after serialization" );
		}
		else {
			return clob;
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ( "getWrappedClob".equals( method.getName() ) ) {
			return getWrappedClob();
		}
		try {
			return method.invoke( getWrappedClob(), args );
		}
		catch ( AbstractMethodError e ) {
			throw new HibernateException( "The JDBC driver does not implement the method: " + method, e );
		}
		catch ( InvocationTargetException e ) {
			throw e.getTargetException();
		}
	}

	/**
	 * Generates a SerializableClobProxy proxy wrapping the provided Clob object.
	 *
	 * @param clob The Clob to wrap.
	 * @return The generated proxy.
	 */
	public static Clob generateProxy(Clob clob) {
		return ( Clob ) Proxy.newProxyInstance(
				getProxyClassLoader(),
				PROXY_INTERFACES,
				new SerializableClobProxy( clob )
		);
	}

	/**
	 * Determines the appropriate class loader to which the generated proxy
	 * should be scoped.
	 *
	 * @return The class loader appropriate for proxy construction.
	 */
	public static ClassLoader getProxyClassLoader() {
		ClassLoader cl = ClassLoaderHelper.getContextClassLoader();
		if ( cl == null ) {
			cl = WrappedClob.class.getClassLoader();
		}
		return cl;
	}
}
