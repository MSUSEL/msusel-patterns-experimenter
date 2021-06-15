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
package org.hibernate.dialect;

import java.sql.Types;

import org.junit.Test;

import org.hibernate.mapping.Column;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;

/**
 * DB2 dialect related test cases
 *
 * @author Hardy Ferentschik
 */

public class DB2DialectTestCase extends BaseUnitTestCase {
	private final DB2Dialect dialect = new DB2Dialect();

	@Test
	@TestForIssue(jiraKey = "HHH-6866")
	public void testGetDefaultBinaryTypeName() {
		String actual = dialect.getTypeName( Types.BINARY );
		assertEquals(
				"The default column length is 255, but char length on DB2 is limited to 254",
				"varchar($l) for bit data",
				actual
		);
	}

	@Test
	@TestForIssue(jiraKey = "HHH-6866")
	public void testGetExplicitBinaryTypeName() {
		// lower bound
		String actual = dialect.getTypeName( Types.BINARY, 1, Column.DEFAULT_PRECISION, Column.DEFAULT_SCALE );
		assertEquals(
				"Wrong binary type",
				"char(1) for bit data",
				actual
		);

		// upper bound
		actual = dialect.getTypeName( Types.BINARY, 254, Column.DEFAULT_PRECISION, Column.DEFAULT_SCALE );
		assertEquals(
				"Wrong binary type. 254 is the max length in DB2",
				"char(254) for bit data",
				actual
		);

		// exceeding upper bound
		actual = dialect.getTypeName( Types.BINARY, 255, Column.DEFAULT_PRECISION, Column.DEFAULT_SCALE );
		assertEquals(
				"Wrong binary type. Should be varchar for length > 254",
				"varchar(255) for bit data",
				actual
		);
	}
}
