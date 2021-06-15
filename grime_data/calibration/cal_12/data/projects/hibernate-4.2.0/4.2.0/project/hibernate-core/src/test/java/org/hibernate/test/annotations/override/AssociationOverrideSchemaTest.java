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
package org.hibernate.test.annotations.override;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.mapping.Table;
import org.hibernate.test.util.SchemaUtil;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@RequiresDialect({ H2Dialect.class })
@TestForIssue(jiraKey = "HHH-6662")
public class AssociationOverrideSchemaTest extends BaseCoreFunctionalTestCase {
	public static final String SCHEMA_NAME = "OTHER_SCHEMA";
	public static final String TABLE_NAME = "BLOG_TAGS";
	public static final String ID_COLUMN_NAME = "BLOG_ID";
	public static final String VALUE_COLUMN_NAME = "BLOG_TAG";

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Entry.class, BlogEntry.class };
	}

	@Override
	protected String createSecondSchema() {
		return SCHEMA_NAME;
	}

	@Test
	public void testJoinTableSchemaName() {
		Iterator<Table> tableIterator = configuration().getTableMappings();
		while ( tableIterator.hasNext() ) {
			Table table = tableIterator.next();
			if ( TABLE_NAME.equals( table.getName() ) ) {
				Assert.assertEquals( SCHEMA_NAME, table.getSchema() );
				return;
			}
		}
		Assert.fail();
	}

	@Test
	public void testJoinTableJoinColumnName() {
		Assert.assertTrue( SchemaUtil.isColumnPresent( TABLE_NAME, ID_COLUMN_NAME, configuration() ) );
	}

	@Test
	public void testJoinTableColumnName() {
		Assert.assertTrue( SchemaUtil.isColumnPresent( TABLE_NAME, VALUE_COLUMN_NAME, configuration() ) );
	}
}