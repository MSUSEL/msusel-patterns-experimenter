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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.internal.BinaryStreamImpl;
import org.hibernate.internal.util.ClassLoaderHelper;
import org.hibernate.type.descriptor.java.DataHelper;

/**
 * Manages aspects of proxying {@link Blob} references for non-contextual creation, including proxy creation and
 * handling proxy invocations.  We use proxies here solely to avoid JDBC version incompatibilities.
 *
 * @author Gavin King
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class BlobProxy implements InvocationHandler {
	private static final Class[] PROXY_INTERFACES = new Class[] { Blob.class, BlobImplementer.class };

	private BinaryStream binaryStream;
	private boolean needsReset = false;

	/**
	 * Constructor used to build {@link Blob} from byte array.
	 *
	 * @param bytes The byte array
	 * @see #generateProxy(byte[])
	 */
	private BlobProxy(byte[] bytes) {
		binaryStream = new BinaryStreamImpl( bytes );
	}

	/**
	 * Constructor used to build {@link Blob} from a stream.
	 *
	 * @param stream The binary stream
	 * @param length The length of the stream
	 * @see #generateProxy(java.io.InputStream, long)
	 */
	private BlobProxy(InputStream stream, long length) {
		this.binaryStream = new StreamBackedBinaryStream( stream, length );
	}

	private long getLength() {
		return binaryStream.getLength();
	}

	private InputStream getStream() throws SQLException {
		InputStream stream = binaryStream.getInputStream();
		try {
			if ( needsReset ) {
				stream.reset();
			}
		}
		catch ( IOException ioe) {
			throw new SQLException("could not reset reader");
		}
		needsReset = true;
		return stream;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UnsupportedOperationException if any methods other than
	 * {@link Blob#length}, {@link Blob#getUnderlyingStream},
	 * {@link Blob#getBinaryStream}, {@link Blob#getBytes}, {@link Blob#free},
	 * or toString/equals/hashCode are invoked.
	 */
	@Override
	@SuppressWarnings({ "UnnecessaryBoxing" })
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final String methodName = method.getName();
		final int argCount = method.getParameterTypes().length;

		if ( "length".equals( methodName ) && argCount == 0 ) {
			return Long.valueOf( getLength() );
		}
		if ( "getUnderlyingStream".equals( methodName ) ) {
			return binaryStream;
		}
		if ( "getBinaryStream".equals( methodName ) ) {
			if ( argCount == 0 ) {
				return getStream();
			}
			else if ( argCount == 2 ) {
				long start = (Long) args[0];
				if ( start < 1 ) {
					throw new SQLException( "Start position 1-based; must be 1 or more." );
				}
				if ( start > getLength() ) {
					throw new SQLException( "Start position [" + start + "] cannot exceed overall CLOB length [" + getLength() + "]" );
				}
				int length = (Integer) args[1];
				if ( length < 0 ) {
					// java docs specifically say for getBinaryStream(long,int) that the start+length must not exceed the
					// total length, however that is at odds with the getBytes(long,int) behavior.
					throw new SQLException( "Length must be great-than-or-equal to zero." );
				}
				return DataHelper.subStream( getStream(), start-1, length );
			}
		}
		if ( "getBytes".equals( methodName ) ) {
			if ( argCount == 2 ) {
				long start = (Long) args[0];
				if ( start < 1 ) {
					throw new SQLException( "Start position 1-based; must be 1 or more." );
				}
				int length = (Integer) args[1];
				if ( length < 0 ) {
					throw new SQLException( "Length must be great-than-or-equal to zero." );
				}
				return DataHelper.extractBytes( getStream(), start-1, length );
			}
		}
		if ( "free".equals( methodName ) && argCount == 0 ) {
			binaryStream.release();
			return null;
		}
		if ( "toString".equals( methodName ) && argCount == 0 ) {
			return this.toString();
		}
		if ( "equals".equals( methodName ) && argCount == 1 ) {
			return Boolean.valueOf( proxy == args[0] );
		}
		if ( "hashCode".equals( methodName ) && argCount == 0 ) {
			return this.hashCode();
		}

		throw new UnsupportedOperationException( "Blob may not be manipulated from creating session" );
	}

	/**
	 * Generates a BlobImpl proxy using byte data.
	 *
	 * @param bytes The data to be created as a Blob.
	 *
	 * @return The generated proxy.
	 */
	public static Blob generateProxy(byte[] bytes) {
		return ( Blob ) Proxy.newProxyInstance(
				getProxyClassLoader(),
				PROXY_INTERFACES,
				new BlobProxy( bytes )
		);
	}

	/**
	 * Generates a BlobImpl proxy using a given number of bytes from an InputStream.
	 *
	 * @param stream The input stream of bytes to be created as a Blob.
	 * @param length The number of bytes from stream to be written to the Blob.
	 *
	 * @return The generated proxy.
	 */
	public static Blob generateProxy(InputStream stream, long length) {
		return ( Blob ) Proxy.newProxyInstance(
				getProxyClassLoader(),
				PROXY_INTERFACES,
				new BlobProxy( stream, length )
		);
	}

	/**
	 * Determines the appropriate class loader to which the generated proxy
	 * should be scoped.
	 *
	 * @return The class loader appropriate for proxy construction.
	 */
	private static ClassLoader getProxyClassLoader() {
		ClassLoader cl = ClassLoaderHelper.getContextClassLoader();
		if ( cl == null ) {
			cl = BlobImplementer.class.getClassLoader();
		}
		return cl;
	}

	private static class StreamBackedBinaryStream implements BinaryStream {
		private final InputStream stream;
		private final long length;

		private byte[] bytes;

		private StreamBackedBinaryStream(InputStream stream, long length) {
			this.stream = stream;
			this.length = length;
		}

		@Override
		public InputStream getInputStream() {
			return stream;
		}

		@Override
		public byte[] getBytes() {
			if ( bytes == null ) {
				bytes = DataHelper.extractBytes( stream );
			}
			return bytes;
		}

		@Override
		public long getLength() {
			return (int) length;
		}

		@Override
		public void release() {
			try {
				stream.close();
			}
			catch (IOException ignore) {
			}
		}
	}
}
