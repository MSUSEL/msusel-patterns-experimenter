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

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.internal.util.BytesHelper;
import org.hibernate.type.descriptor.WrapperOptions;

/**
 * Descriptor for {@link UUID} handling.
 *
 * @author Steve Ebersole
 */
public class UUIDTypeDescriptor extends AbstractTypeDescriptor<UUID> {
	public static final UUIDTypeDescriptor INSTANCE = new UUIDTypeDescriptor();

	public UUIDTypeDescriptor() {
		super( UUID.class );
	}

	public String toString(UUID value) {
		return ToStringTransformer.INSTANCE.transform( value );
	}

	public UUID fromString(String string) {
		return ToStringTransformer.INSTANCE.parse( string );
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(UUID value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( UUID.class.isAssignableFrom( type ) ) {
			return (X) PassThroughTransformer.INSTANCE.transform( value );
		}
		if ( String.class.isAssignableFrom( type ) ) {
			return (X) ToStringTransformer.INSTANCE.transform( value );
		}
		if ( byte[].class.isAssignableFrom( type ) ) {
			return (X) ToBytesTransformer.INSTANCE.transform( value );
		}
		throw unknownUnwrap( type );
	}

	public <X> UUID wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( UUID.class.isInstance( value ) ) {
			return PassThroughTransformer.INSTANCE.parse( value );
		}
		if ( String.class.isInstance( value ) ) {
			return ToStringTransformer.INSTANCE.parse( value );
		}
		if ( byte[].class.isInstance( value ) ) {
			return ToBytesTransformer.INSTANCE.parse( value );
		}
		throw unknownWrap( value.getClass() );
	}

	public static interface ValueTransformer {
		public Serializable transform(UUID uuid);
		public UUID parse(Object value);
	}

	public static class PassThroughTransformer implements ValueTransformer {
		public static final PassThroughTransformer INSTANCE = new PassThroughTransformer();

		public UUID transform(UUID uuid) {
			return uuid;
		}

		public UUID parse(Object value) {
			return (UUID)value;
		}
	}

	public static class ToStringTransformer implements ValueTransformer {
		public static final ToStringTransformer INSTANCE = new ToStringTransformer();

		public String transform(UUID uuid) {
			return uuid.toString();
		}

		public UUID parse(Object value) {
			return UUID.fromString( (String) value );
		}
	}

	public static class ToBytesTransformer implements ValueTransformer {
		public static final ToBytesTransformer INSTANCE = new ToBytesTransformer();

		public byte[] transform(UUID uuid) {
			byte[] bytes = new byte[16];
			System.arraycopy( BytesHelper.fromLong( uuid.getMostSignificantBits() ), 0, bytes, 0, 8 );
			System.arraycopy( BytesHelper.fromLong( uuid.getLeastSignificantBits() ), 0, bytes, 8, 8 );
			return bytes;
		}

		public UUID parse(Object value) {
			byte[] msb = new byte[8];
			byte[] lsb = new byte[8];
			System.arraycopy( value, 0, msb, 0, 8 );
			System.arraycopy( value, 8, lsb, 0, 8 );
			return new UUID( BytesHelper.asLong( msb ), BytesHelper.asLong( lsb ) );
		}
	}
}
