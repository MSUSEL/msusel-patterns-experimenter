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
import org.hibernate.type.descriptor.java.IntegerTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#INTEGER INTEGER} and @link Integer}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class IntegerType extends AbstractSingleColumnStandardBasicType<Integer>
		implements PrimitiveType<Integer>, DiscriminatorType<Integer>, VersionType<Integer> {

	public static final IntegerType INSTANCE = new IntegerType();

	@SuppressWarnings({ "UnnecessaryBoxing" })
	public static final Integer ZERO = Integer.valueOf( 0 );

	public IntegerType() {
		super( org.hibernate.type.descriptor.sql.IntegerTypeDescriptor.INSTANCE, IntegerTypeDescriptor.INSTANCE );
	}

	public String getName() {
		return "integer";
	}

	@Override
	public String[] getRegistrationKeys() {
		return new String[] { getName(), int.class.getName(), Integer.class.getName() };
	}

	public Serializable getDefaultValue() {
		return ZERO;
	}

	public Class getPrimitiveClass() {
		return int.class;
	}

	public String objectToSQLString(Integer value, Dialect dialect) throws Exception {
		return toString( value );
	}

	public Integer stringToObject(String xml) {
		return fromString( xml );
	}

	public Integer seed(SessionImplementor session) {
		return ZERO;
	}

	@SuppressWarnings({ "UnnecessaryBoxing", "UnnecessaryUnboxing" })
	public Integer next(Integer current, SessionImplementor session) {
		return Integer.valueOf( current.intValue() + 1 );
	}

	public Comparator<Integer> getComparator() {
		return getJavaTypeDescriptor().getComparator();
	}
}
