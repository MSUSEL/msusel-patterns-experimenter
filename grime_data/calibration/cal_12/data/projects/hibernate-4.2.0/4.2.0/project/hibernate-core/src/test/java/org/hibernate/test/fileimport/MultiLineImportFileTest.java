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
package org.hibernate.test.fileimport;

import java.math.BigInteger;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-2403")
@RequiresDialect(value = H2Dialect.class,
		jiraKey = "HHH-6286",
		comment = "Only running the tests against H2, because the sql statements in the import file are not generic. " +
				"This test should actually not test directly against the db")
public class MultiLineImportFileTest extends BaseCoreFunctionalTestCase {
	@Override
	public void configure(Configuration cfg) {
		cfg.setProperty( Environment.HBM2DDL_IMPORT_FILES, "/org/hibernate/test/fileimport/multi-line-statements.sql" );
		cfg.setProperty(
				Environment.HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR,
				MultipleLinesSqlCommandExtractor.class.getName()
		);
	}

	@Override
	public String[] getMappings() {
		return NO_MAPPINGS;
	}

	@Test
	public void testImportFile() throws Exception {
		Session s = openSession();
		final Transaction tx = s.beginTransaction();

		BigInteger count = (BigInteger) s.createSQLQuery( "SELECT COUNT(*) FROM test_data" ).uniqueResult();
		assertEquals( "Incorrect row number", 3L, count.longValue() );

		final String multiLineText = (String) s.createSQLQuery( "SELECT text FROM test_data WHERE id = 2" )
				.uniqueResult();
		//  "Multi-line comment line 1\r\n-- line 2'\r\n/* line 3 */"
		final String expected = String.format( "Multi-line comment line 1%n-- line 2'%n/* line 3 */" );
		assertEquals( "Multi-line string inserted incorrectly", expected, multiLineText );

		String empty = (String) s.createSQLQuery( "SELECT text FROM test_data WHERE id = 3" ).uniqueResult();
		assertNull( "NULL value inserted incorrectly", empty );

		tx.commit();
		s.close();
	}
}
