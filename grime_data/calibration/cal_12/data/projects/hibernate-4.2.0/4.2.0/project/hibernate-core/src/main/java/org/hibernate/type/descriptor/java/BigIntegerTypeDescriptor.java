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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.hibernate.type.descriptor.WrapperOptions;

/**
 * Descriptor for {@link BigInteger} handling.
 *
 * @author Steve Ebersole
 */
public class BigIntegerTypeDescriptor extends AbstractTypeDescriptor<BigInteger> {
	public static final BigIntegerTypeDescriptor INSTANCE = new BigIntegerTypeDescriptor();

	public BigIntegerTypeDescriptor() {
		super( BigInteger.class );
	}

	public String toString(BigInteger value) {
		return value.toString();
	}

	public BigInteger fromString(String string) {
		return new BigInteger( string );
	}

	@Override
	public int extractHashCode(BigInteger value) {
		return value.intValue();
	}

	@Override
	public boolean areEqual(BigInteger one, BigInteger another) {
		return one == another || ( one != null && another != null && one.compareTo( another ) == 0 );
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(BigInteger value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( BigInteger.class.isAssignableFrom( type ) ) {
			return (X) value;
		}
		if ( BigDecimal.class.isAssignableFrom( type ) ) {
			return (X) new BigDecimal( value );
		}
		if ( Byte.class.isAssignableFrom( type ) ) {
			return (X) Byte.valueOf( value.byteValue() );
		}
		if ( Short.class.isAssignableFrom( type ) ) {
			return (X) Short.valueOf( value.shortValue() );
		}
		if ( Integer.class.isAssignableFrom( type ) ) {
			return (X) Integer.valueOf( value.intValue() );
		}
		if ( Long.class.isAssignableFrom( type ) ) {
			return (X) Long.valueOf( value.longValue() );
		}
		if ( Double.class.isAssignableFrom( type ) ) {
			return (X) Double.valueOf( value.doubleValue() );
		}
		if ( Float.class.isAssignableFrom( type ) ) {
			return (X) Float.valueOf( value.floatValue() );
		}
		throw unknownUnwrap( type );
	}

	/**
	 * {@inheritDoc}
	 */
	public <X> BigInteger wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( BigInteger.class.isInstance( value ) ) {
			return (BigInteger) value;
		}
		if ( BigDecimal.class.isInstance( value ) ) {
			return ( (BigDecimal) value ).toBigIntegerExact();
		}
		if ( Number.class.isInstance( value ) ) {
			return BigInteger.valueOf( ( (Number) value ).longValue() );
		}
		throw unknownWrap( value.getClass() );
	}
}
