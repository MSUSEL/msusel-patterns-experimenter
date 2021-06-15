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
import org.hibernate.HibernateException;
import org.hibernate.type.descriptor.WrapperOptions;

/**
 * Descriptor for {@link Character} handling.
 *
 * @author Steve Ebersole
 */
public class CharacterTypeDescriptor extends AbstractTypeDescriptor<Character> {
	public static final CharacterTypeDescriptor INSTANCE = new CharacterTypeDescriptor();

	public CharacterTypeDescriptor() {
		super( Character.class );
	}

	public String toString(Character value) {
		return value.toString();
	}

	@SuppressWarnings({ "UnnecessaryBoxing" })
	public Character fromString(String string) {
		if ( string.length() != 1 ) {
			throw new HibernateException( "multiple or zero characters found parsing string" );
		}
		return Character.valueOf( string.charAt( 0 ) );
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(Character value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Character.class.isAssignableFrom( type ) ) {
			return (X) value;
		}
		if ( String.class.isAssignableFrom( type ) ) {
			return (X) value.toString();
		}
		if ( Number.class.isAssignableFrom( type ) ) {
			return (X) Short.valueOf( (short)value.charValue() );
		}
		throw unknownUnwrap( type );
	}

	@SuppressWarnings({ "UnnecessaryBoxing" })
	public <X> Character wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Character.class.isInstance( value ) ) {
			return (Character) value;
		}
		if ( String.class.isInstance( value ) ) {
			final String str = (String) value;
			return Character.valueOf( str.charAt(0) );
		}
		if ( Number.class.isInstance( value ) ) {
			final Number nbr = (Number) value;
			return Character.valueOf( (char)nbr.shortValue() );
		}
		throw unknownWrap( value.getClass() );
	}
}
