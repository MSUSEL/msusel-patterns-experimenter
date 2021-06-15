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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.internal.BinaryStreamImpl;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.engine.jdbc.BinaryStream;
import org.hibernate.type.descriptor.WrapperOptions;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 * @author Brett meyer
 */
public class SerializableTypeDescriptor<T extends Serializable> extends AbstractTypeDescriptor<T> {

	// unfortunately the param types cannot be the same so use something other than 'T' here to make that obvious
	public static class SerializableMutabilityPlan<S extends Serializable> extends MutableMutabilityPlan<S> {
		private final Class<S> type;

		public static final SerializableMutabilityPlan<Serializable> INSTANCE
				= new SerializableMutabilityPlan<Serializable>( Serializable.class );

		public SerializableMutabilityPlan(Class<S> type) {
			this.type = type;
		}

		@Override
        @SuppressWarnings({ "unchecked" })
		public S deepCopyNotNull(S value) {
			return (S) SerializationHelper.clone( value );
		}

	}

	@SuppressWarnings({ "unchecked" })
	public SerializableTypeDescriptor(Class<T> type) {
		super(
				type,
				Serializable.class.equals( type )
						? (MutabilityPlan<T>) SerializableMutabilityPlan.INSTANCE
						: new SerializableMutabilityPlan<T>( type )
		);
	}

	public String toString(T value) {
		return PrimitiveByteArrayTypeDescriptor.INSTANCE.toString( toBytes( value ) );
	}

	public T fromString(String string) {
		return fromBytes( PrimitiveByteArrayTypeDescriptor.INSTANCE.fromString( string ) );
	}

	@Override
	public boolean areEqual(T one, T another) {
		if ( one == another ) {
			return true;
		}
		if ( one == null || another == null ) {
			return false;
		}
		return one.equals( another )
				|| PrimitiveByteArrayTypeDescriptor.INSTANCE.areEqual( toBytes( one ), toBytes( another ) );
	}

	@Override
	public int extractHashCode(T value) {
		return PrimitiveByteArrayTypeDescriptor.INSTANCE.extractHashCode( toBytes( value ) );
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		} else if ( byte[].class.isAssignableFrom( type ) ) {
			return (X) toBytes( value );
		} else if ( InputStream.class.isAssignableFrom( type ) ) {
			return (X) new ByteArrayInputStream( toBytes( value ) );
		} else if ( BinaryStream.class.isAssignableFrom( type ) ) {
			return (X) new BinaryStreamImpl( toBytes( value ) );
		} else if ( Blob.class.isAssignableFrom( type )) {
			return (X) options.getLobCreator().createBlob( toBytes(value) );
		}
		
		throw unknownUnwrap( type );
	}

	public <X> T wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		} else if ( byte[].class.isInstance( value ) ) {
			return fromBytes( (byte[]) value );
		} else if ( InputStream.class.isInstance( value ) ) {
			return fromBytes( DataHelper.extractBytes( (InputStream) value ) );
		} else if ( Blob.class.isInstance( value )) {
			try {
				return fromBytes( DataHelper.extractBytes( ( (Blob) value ).getBinaryStream() ) );
			} catch ( SQLException e ) {
				throw new HibernateException(e);
			}
		}
		throw unknownWrap( value.getClass() );
	}

	protected byte[] toBytes(T value) {
		return SerializationHelper.serialize( value );
	}

	@SuppressWarnings({ "unchecked" })
	protected T fromBytes(byte[] bytes) {
		return (T) SerializationHelper.deserialize( bytes, getJavaTypeClass().getClassLoader() );
	}
}
