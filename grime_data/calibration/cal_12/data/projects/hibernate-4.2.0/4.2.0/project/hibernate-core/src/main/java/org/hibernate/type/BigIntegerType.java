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

import java.math.BigInteger;
import java.sql.Types;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.java.BigIntegerTypeDescriptor;
import org.hibernate.type.descriptor.sql.NumericTypeDescriptor;

/**
 * A type that maps between a {@link Types#NUMERIC NUMERIC} and {@link BigInteger}.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class BigIntegerType
		extends AbstractSingleColumnStandardBasicType<BigInteger>
		implements DiscriminatorType<BigInteger> {

	public static final BigIntegerType INSTANCE = new BigIntegerType();

	public BigIntegerType() {
		super( NumericTypeDescriptor.INSTANCE, BigIntegerTypeDescriptor.INSTANCE );
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "big_integer";
	}

	@Override
	protected boolean registerUnderJavaType() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String objectToSQLString(BigInteger value, Dialect dialect) {
		return BigIntegerTypeDescriptor.INSTANCE.toString( value );
	}

	/**
	 * {@inheritDoc}
	 */
	public BigInteger stringToObject(String string) {
		return BigIntegerTypeDescriptor.INSTANCE.fromString( string );
	}
}
