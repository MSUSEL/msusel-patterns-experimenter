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
package org.hibernate.test.dialect.function;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.hibernate.dialect.function.AnsiTrimEmulationFunction;

import static org.junit.Assert.assertEquals;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class AnsiTrimEmulationFunctionTest  {
	private static final String trimSource = "a.column";
    @Test
	public void testBasicSqlServerProcessing() {
		AnsiTrimEmulationFunction function = new AnsiTrimEmulationFunction();

		performBasicSpaceTrimmingTests( function );

		final String expectedTrimPrep = "replace(replace(a.column,' ','${space}$'),'-',' ')";
		final String expectedPostTrimPrefix = "replace(replace(";
		final String expectedPostTrimSuffix = ",' ','-'),'${space}$',' ')";

		// -> trim(LEADING '-' FROM a.column)
		String rendered = function.render( null, argList( "LEADING", "'-'", "FROM", trimSource ), null );
		String expected = expectedPostTrimPrefix + "ltrim(" + expectedTrimPrep + ")" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );

		// -> trim(TRAILING '-' FROM a.column)
		rendered = function.render( null, argList( "TRAILING", "'-'", "FROM", trimSource ), null );
		expected = expectedPostTrimPrefix + "rtrim(" + expectedTrimPrep + ")" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );

		// -> trim(BOTH '-' FROM a.column)
		rendered = function.render( null, argList( "BOTH", "'-'", "FROM", trimSource ), null );
		expected = expectedPostTrimPrefix + "ltrim(rtrim(" + expectedTrimPrep + "))" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );

		// -> trim('-' FROM a.column)
		rendered = function.render( null, argList( "'-'", "FROM", trimSource ), null );
		expected = expectedPostTrimPrefix + "ltrim(rtrim(" + expectedTrimPrep + "))" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );
	}
    @Test
	public void testBasicSybaseProcessing() {
		AnsiTrimEmulationFunction function = new AnsiTrimEmulationFunction(
				AnsiTrimEmulationFunction.LTRIM,
				AnsiTrimEmulationFunction.RTRIM,
				"str_replace"
		);

		performBasicSpaceTrimmingTests( function );

		final String expectedTrimPrep = "str_replace(str_replace(a.column,' ','${space}$'),'-',' ')";
		final String expectedPostTrimPrefix = "str_replace(str_replace(";
		final String expectedPostTrimSuffix = ",' ','-'),'${space}$',' ')";

		// -> trim(LEADING '-' FROM a.column)
		String rendered = function.render( null, argList( "LEADING", "'-'", "FROM", trimSource ), null );
		String expected = expectedPostTrimPrefix + "ltrim(" + expectedTrimPrep + ")" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );

		// -> trim(TRAILING '-' FROM a.column)
		rendered = function.render( null, argList( "TRAILING", "'-'", "FROM", trimSource ), null );
		expected = expectedPostTrimPrefix + "rtrim(" + expectedTrimPrep + ")" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );

		// -> trim(BOTH '-' FROM a.column)
		rendered = function.render( null, argList( "BOTH", "'-'", "FROM", trimSource ), null );
		expected = expectedPostTrimPrefix + "ltrim(rtrim(" + expectedTrimPrep + "))" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );

		// -> trim('-' FROM a.column)
		rendered = function.render( null, argList( "'-'", "FROM", trimSource ), null );
		expected = expectedPostTrimPrefix + "ltrim(rtrim(" + expectedTrimPrep + "))" + expectedPostTrimSuffix;
		assertEquals( expected, rendered );
	}

	private void performBasicSpaceTrimmingTests(AnsiTrimEmulationFunction function) {
		// -> trim(a.column)
		String rendered = function.render( null, argList( trimSource ), null );
		assertEquals( "ltrim(rtrim(a.column))", rendered );

		// -> trim(FROM a.column)
		rendered = function.render( null, argList( "FROM", trimSource ), null );
		assertEquals( "ltrim(rtrim(a.column))", rendered );

		// -> trim(BOTH FROM a.column)
		rendered = function.render( null, argList( "BOTH", "FROM", trimSource ), null );
		assertEquals( "ltrim(rtrim(a.column))", rendered );

		// -> trim(BOTH ' ' FROM a.column)
		rendered = function.render( null, argList( "BOTH", "' '", "FROM", trimSource ), null );
		assertEquals( "ltrim(rtrim(a.column))", rendered );

		// -> trim(LEADING FROM a.column)
		rendered = function.render( null, argList( "LEADING", "FROM", trimSource ), null );
		assertEquals( "ltrim(a.column)", rendered );

		// -> trim(LEADING ' ' FROM a.column)
		rendered = function.render( null, argList( "LEADING", "' '", "FROM", trimSource ), null );
		assertEquals( "ltrim(a.column)", rendered );

		// -> trim(TRAILING FROM a.column)
		rendered = function.render( null, argList( "TRAILING", "FROM", trimSource ), null );
		assertEquals( "rtrim(a.column)", rendered );

		// -> trim(TRAILING ' ' FROM a.column)
		rendered = function.render( null, argList( "TRAILING", "' '", "FROM", trimSource ), null );
		assertEquals( "rtrim(a.column)", rendered );
	}

	private List argList(String... args) {
		return Arrays.asList( args );
	}

}
