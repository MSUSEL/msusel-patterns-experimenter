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
package org.hibernate.ejb.criteria.predicate;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Steve Ebersole
 */
public class ImplicitNumericExpressionTypeDeterminer {
	/**
	 * Determine the appropriate runtime result type for a numeric expression according to
	 * section "6.5.7.1 Result Types of Expressions" of the JPA spec.
	 * <p/>
	 * Note that it is expected that the caveats about quotient handling have already been handled.
	 *
	 * @param types The argument/expression types
	 *
	 * @return The appropriate numeric result type.
	 */
	public static Class<? extends Number> determineResultType(Class<? extends Number>... types) {
		Class<? extends Number> result = Number.class;

		for ( Class<? extends Number> type : types ) {
			if ( Double.class.equals( type ) ) {
				result = Double.class;
			}
			else if ( Float.class.equals( type ) ) {
				result = Float.class;
			}
			else if ( BigDecimal.class.equals( type ) ) {
				result = BigDecimal.class;
			}
			else if ( BigInteger.class.equals( type ) ) {
				result = BigInteger.class;
			}
			else if ( Long.class.equals( type ) ) {
				result = Long.class;
			}
			else if ( isIntegralType( type ) ) {
				result = Integer.class;
			}
		}

		return result;
	}

	private static boolean isIntegralType(Class<? extends Number> type) {
		return Integer.class.equals( type ) ||
				Short.class.equals( type );

	}
}
