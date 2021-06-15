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
package org.hibernate.testing.junit4;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
public class ExtraAssertions {
	public static void assertClassAssignability(Class expected, Class actual) {
		if ( ! expected.isAssignableFrom( actual ) ) {
			Assert.fail(
					"Expected class [" + expected.getName() + "] was not assignable from actual [" +
							actual.getName() + "]"
			);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T assertTyping(Class<T> expectedType, Object value) {
		if ( ! expectedType.isInstance( value ) ) {
			Assert.fail(
					String.format(
							"Expecting value of type [%s], but found [%s]",
							expectedType.getName(),
							value == null ? "<null>" : value
					)
			);
		}
		return (T) value;
	}

	public static void assertJdbcTypeCode(int expected, int actual) {
		if ( expected != actual ) {
			final String message = String.format(
					"JDBC type codes did not match...\n" +
							"Expected: %s (%s)\n" +
							"Actual  : %s (%s)",
					jdbcTypeCodeMap().get( expected ),
					expected,
					jdbcTypeCodeMap().get( actual ),
					actual
			);
			fail( message );
		}
	}

	private static Map<Integer,String> jdbcTypeCodeMap;

	private static synchronized Map<Integer,String> jdbcTypeCodeMap() {
		if ( jdbcTypeCodeMap == null ) {
			jdbcTypeCodeMap = generateJdbcTypeCache();
		}
		return jdbcTypeCodeMap;
	}

	private static Map generateJdbcTypeCache() {
		final Field[] fields = Types.class.getFields();
		Map cache = new HashMap( (int)( fields.length * .75 ) + 1 );
		for ( int i = 0; i < fields.length; i++ ) {
			final Field field = fields[i];
			if ( Modifier.isStatic( field.getModifiers() ) ) {
				try {
					cache.put( field.get( null ), field.getName() );
				}
				catch ( Throwable ignore ) {
				}
			}
		}
		return cache;
	}
}
