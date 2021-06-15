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
package org.hibernate.metamodel.source.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.junit.Test;

import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.relational.Identifier;

import static org.junit.Assert.assertEquals;

/**
 * @author Strong Liu
 */
public class QuotedIdentifierTest extends BaseAnnotationBindingTestCase {
	private final String ormPath = "org/hibernate/metamodel/source/annotations/xml/orm-quote-identifier.xml";

	@Test
	@Resources(annotatedClasses = { Item.class, Item2.class, Item3.class, Item4.class }, ormXmlPath = ormPath)
	public void testDelimitedIdentifiers() {
		EntityBinding item = getEntityBinding( Item.class );
		assertIdentifierEquals( "`QuotedIdentifierTest$Item`", item );

		item = getEntityBinding( Item2.class );
		assertIdentifierEquals( "`TABLE_ITEM2`", item );

		item = getEntityBinding( Item3.class );
		assertIdentifierEquals( "`TABLE_ITEM3`", item );

		item = getEntityBinding( Item4.class );
		assertIdentifierEquals( "`TABLE_ITEM4`", item );
	}

    //todo check if the column names are quoted

	private void assertIdentifierEquals(String expected, EntityBinding realValue) {
		org.hibernate.metamodel.relational.Table table = (org.hibernate.metamodel.relational.Table) realValue.getPrimaryTable();
		assertEquals( Identifier.toIdentifier( expected ), table.getTableName() );
	}

	@Entity
	private static class Item {
		@Id
		Long id;
	}

	@Entity
	@Table(name = "TABLE_ITEM2")
	private static class Item2 {
		@Id
		Long id;
	}

	@Entity
	@Table(name = "`TABLE_ITEM3`")
	private static class Item3 {
		@Id
		Long id;
	}

	@Entity
	@Table(name = "\"TABLE_ITEM4\"")
	private static class Item4 {
		@Id
		Long id;
	}
}
