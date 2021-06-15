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
import java.sql.Blob;

import org.hibernate.HibernateException;
import org.hibernate.internal.util.ClassLoaderHelper;

/**
 * Manages aspects of proxying {@link Blob Blobs} to add serializability.
 *
 * @author Gavin King
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class SerializableBlobProxy implements InvocationHandler, Serializable {
	private static final Class[] PROXY_INTERFACES = new Class[] { Blob.class, WrappedBlob.class, Serializable.class };

	private transient final Blob blob;

	/**
	 * Builds a serializable {@link Blob} wrapper around the given {@link Blob}.
	 *
	 * @param blob The {@link Blob} to be wrapped.
	 * @see
	 */
	private SerializableBlobProxy(Blob blob) {
		this.blob = blob;
	}

	public Blob getWrappedBlob() {
		if ( blob == null ) {
			throw new IllegalStateException( "Blobs may not be accessed after serialization" );
		}
		else {
			return blob;
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ( "getWrappedBlob".equals( method.getName() ) ) {
			return getWrappedBlob();
		}
		try {
			return method.invoke( getWrappedBlob(), args );
		}
		catch ( AbstractMethodError e ) {
			throw new HibernateException( "The JDBC driver does not implement the method: " + method, e );
		}
		catch ( InvocationTargetException e ) {
			throw e.getTargetException();
		}
	}

	/**
	 * Generates a SerializableBlob proxy wrapping the provided Blob object.
	 *
	 * @param blob The Blob to wrap.
	 *
	 * @return The generated proxy.
	 */
	public static Blob generateProxy(Blob blob) {
		return ( Blob ) Proxy.newProxyInstance(
				getProxyClassLoader(),
				PROXY_INTERFACES,
				new SerializableBlobProxy( blob )
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
			cl = WrappedBlob.class.getClassLoader();
		}
		return cl;
	}
}
