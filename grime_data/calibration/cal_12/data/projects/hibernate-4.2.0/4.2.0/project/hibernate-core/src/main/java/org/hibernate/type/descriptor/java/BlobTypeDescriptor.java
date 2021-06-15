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
package org.hibernate.type.descriptor.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Comparator;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.BlobImplementer;
import org.hibernate.engine.jdbc.BlobProxy;
import org.hibernate.engine.jdbc.WrappedBlob;
import org.hibernate.engine.jdbc.BinaryStream;
import org.hibernate.engine.jdbc.internal.BinaryStreamImpl;
import org.hibernate.type.descriptor.WrapperOptions;

/**
 * Descriptor for {@link Blob} handling.
 * <p/>
 * Note, {@link Blob blobs} really are mutable (their internal state can in fact be mutated).  We simply
 * treat them as immutable because we cannot properly check them for changes nor deep copy them.
 *
 * @author Steve Ebersole
 * @author Brett Meyer
 */
public class BlobTypeDescriptor extends AbstractTypeDescriptor<Blob> {
	public static final BlobTypeDescriptor INSTANCE = new BlobTypeDescriptor();

	public static class BlobMutabilityPlan implements MutabilityPlan<Blob> {
		public static final BlobMutabilityPlan INSTANCE = new BlobMutabilityPlan();

		public boolean isMutable() {
			return false;
		}

		public Blob deepCopy(Blob value) {
			return value;
		}

		public Serializable disassemble(Blob value) {
			throw new UnsupportedOperationException( "Blobs are not cacheable" );
		}

		public Blob assemble(Serializable cached) {
			throw new UnsupportedOperationException( "Blobs are not cacheable" );
		}
	}

	public BlobTypeDescriptor() {
		super( Blob.class, BlobMutabilityPlan.INSTANCE );
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString(Blob value) {
		final byte[] bytes;
		try {
			bytes = DataHelper.extractBytes( value.getBinaryStream() );
		}
		catch ( SQLException e ) {
			throw new HibernateException( "Unable to access blob stream", e );
		}
		return PrimitiveByteArrayTypeDescriptor.INSTANCE.toString( bytes );
	}

	/**
	 * {@inheritDoc}
	 */
	public Blob fromString(String string) {
		return BlobProxy.generateProxy( PrimitiveByteArrayTypeDescriptor.INSTANCE.fromString( string ) );
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Comparator<Blob> getComparator() {
		return IncomparableComparator.INSTANCE;
	}

	@Override
	public int extractHashCode(Blob value) {
		return System.identityHashCode( value );
	}

	@Override
	public boolean areEqual(Blob one, Blob another) {
		return one == another;
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(Blob value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}

		try {
			if ( BinaryStream.class.isAssignableFrom( type ) ) {
				if ( BlobImplementer.class.isInstance( value ) ) {
					// if the incoming Blob is a wrapper, just pass along its BinaryStream
					return (X) ( (BlobImplementer) value ).getUnderlyingStream();
				}
				else {
					// otherwise we need to build a BinaryStream...
					return (X) new BinaryStreamImpl( DataHelper.extractBytes( value.getBinaryStream() ) );
				}
			}
			else if ( byte[].class.isAssignableFrom( type )) {
				if ( BlobImplementer.class.isInstance( value ) ) {
					// if the incoming Blob is a wrapper, just grab the bytes from its BinaryStream
					return (X) ( (BlobImplementer) value ).getUnderlyingStream().getBytes();
				}
				else {
					// otherwise extract the bytes from the stream manually
					return (X) DataHelper.extractBytes( value.getBinaryStream() );
				}
			}
			else if (Blob.class.isAssignableFrom( type )) {
				final Blob blob =  WrappedBlob.class.isInstance( value )
						? ( (WrappedBlob) value ).getWrappedBlob()
						: value;
				return (X) blob;
			}
		}
		catch ( SQLException e ) {
			throw new HibernateException( "Unable to access blob stream", e );
		}
		
		throw unknownUnwrap( type );
	}

	public <X> Blob wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}

		// Support multiple return types from
		// org.hibernate.type.descriptor.sql.BlobTypeDescriptor
		if ( Blob.class.isAssignableFrom( value.getClass() ) ) {
			return options.getLobCreator().wrap( (Blob) value );
		}
		else if ( byte[].class.isAssignableFrom( value.getClass() ) ) {
			return options.getLobCreator().createBlob( ( byte[] ) value);
		}
		else if ( InputStream.class.isAssignableFrom( value.getClass() ) ) {
			InputStream inputStream = ( InputStream ) value;
			try {
				return options.getLobCreator().createBlob( inputStream, inputStream.available() );
			}
			catch ( IOException e ) {
				throw unknownWrap( value.getClass() );
			}
		}

		throw unknownWrap( value.getClass() );
	}
}
