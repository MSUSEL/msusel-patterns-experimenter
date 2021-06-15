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
package org.hibernate.type;

import java.io.Serializable;
import java.util.Comparator;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.descriptor.java.ByteTypeDescriptor;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#TINYINT TINYINT} and {@link Byte}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
@SuppressWarnings({ "UnnecessaryBoxing" })
public class ByteType
		extends AbstractSingleColumnStandardBasicType<Byte>
		implements PrimitiveType<Byte>, DiscriminatorType<Byte>, VersionType<Byte> {

	public static final ByteType INSTANCE = new ByteType();

	private static final Byte ZERO = Byte.valueOf( (byte)0 );

	public ByteType() {
		super( TinyIntTypeDescriptor.INSTANCE, ByteTypeDescriptor.INSTANCE );
	}

	public String getName() {
		return "byte";
	}

	@Override
	public String[] getRegistrationKeys() {
		return new String[] { getName(), byte.class.getName(), Byte.class.getName() };
	}

	public Serializable getDefaultValue() {
		return ZERO;
	}

	public Class getPrimitiveClass() {
		return byte.class;
	}

	public String objectToSQLString(Byte value, Dialect dialect) {
		return toString( value );
	}

	public Byte stringToObject(String xml) {
		return fromString( xml );
	}

	public Byte fromStringValue(String xml) {
		return fromString( xml );
	}

	@SuppressWarnings({ "UnnecessaryUnboxing" })
	public Byte next(Byte current, SessionImplementor session) {
		return Byte.valueOf( (byte) ( current.byteValue() + 1 ) );
	}

	public Byte seed(SessionImplementor session) {
		return ZERO;
	}

	public Comparator<Byte> getComparator() {
		return getJavaTypeDescriptor().getComparator();
	}
}
