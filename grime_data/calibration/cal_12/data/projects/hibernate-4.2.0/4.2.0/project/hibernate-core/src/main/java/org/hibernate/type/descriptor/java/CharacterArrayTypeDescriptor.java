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

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.util.Arrays;

import org.hibernate.engine.jdbc.CharacterStream;
import org.hibernate.engine.jdbc.internal.CharacterStreamImpl;
import org.hibernate.type.descriptor.WrapperOptions;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class CharacterArrayTypeDescriptor extends AbstractTypeDescriptor<Character[]> {
	public static final CharacterArrayTypeDescriptor INSTANCE = new CharacterArrayTypeDescriptor();

	@SuppressWarnings({ "unchecked" })
	public CharacterArrayTypeDescriptor() {
		super( Character[].class, ArrayMutabilityPlan.INSTANCE );
	}

	public String toString(Character[] value) {
		return new String( unwrapChars( value ) );
	}

	public Character[] fromString(String string) {
		return wrapChars( string.toCharArray() );
	}

	@Override
	public boolean areEqual(Character[] one, Character[] another) {
		return one == another
				|| ( one != null && another != null && Arrays.equals( one, another ) );
	}

	@Override
	public int extractHashCode(Character[] chars) {
		int hashCode = 1;
		for ( Character aChar : chars ) {
			hashCode = 31 * hashCode + aChar;
		}
		return hashCode;
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(Character[] value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Character[].class.isAssignableFrom( type ) ) {
			return (X) value;
		}
		if ( String.class.isAssignableFrom( type ) ) {
			return (X) new String( unwrapChars( value ) );
		}
		if ( Clob.class.isAssignableFrom( type ) ) {
			return (X) options.getLobCreator().createClob( new String( unwrapChars( value ) ) );
		}
		if ( Reader.class.isAssignableFrom( type ) ) {
			return (X) new StringReader( new String( unwrapChars( value ) ) );
		}
		if ( CharacterStream.class.isAssignableFrom( type ) ) {
			return (X) new CharacterStreamImpl( new String( unwrapChars( value ) ) );
		}
		throw unknownUnwrap( type );
	}

	public <X> Character[] wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Character[].class.isInstance( value ) ) {
			return (Character[]) value;
		}
		if ( String.class.isInstance( value ) ) {
			return wrapChars( ( (String) value ).toCharArray() );
		}
		if ( Clob.class.isInstance( value ) ) {
			return wrapChars( DataHelper.extractString( ( (Clob) value ) ).toCharArray() );
		}
		if ( Reader.class.isInstance( value ) ) {
			return wrapChars( DataHelper.extractString( (Reader) value ).toCharArray() );
		}
		throw unknownWrap( value.getClass() );
	}

	@SuppressWarnings({ "UnnecessaryBoxing" })
	private Character[] wrapChars(char[] chars) {
		if ( chars == null ) {
			return null;
		}
		final Character[] result = new Character[chars.length];
		for ( int i = 0; i < chars.length; i++ ) {
			result[i] = Character.valueOf( chars[i] );
		}
		return result;
	}

	@SuppressWarnings({ "UnnecessaryUnboxing" })
	private char[] unwrapChars(Character[] chars) {
		if ( chars == null ) {
			return null;
		}
		final char[] result = new char[chars.length];
		for ( int i = 0; i < chars.length; i++ ) {
			result[i] = chars[i].charValue();
		}
		return result;
	}
}
