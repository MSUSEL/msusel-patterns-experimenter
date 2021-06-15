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
import javax.persistence.UniqueConstraint;

import org.junit.Test;

import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.TableSpecification;
import org.hibernate.metamodel.relational.UniqueKey;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * test for {@link javax.persistence.UniqueConstraint}
 *
 * @author Strong Liu
 */
public class UniqueConstraintBindingTest extends BaseAnnotationBindingTestCase {
	@Test
	@Resources(annotatedClasses = TableWithUniqueConstraint.class)
	public void testTableUniqueConstraints() {
		EntityBinding binding = getEntityBinding( TableWithUniqueConstraint.class );
		TableSpecification table = binding.getPrimaryTable();
		Iterable<UniqueKey> uniqueKeyIterable = table.getUniqueKeys();
		assertNotNull( uniqueKeyIterable );
		int i = 0;
		for ( UniqueKey key : uniqueKeyIterable ) {
			i++;
			assertEquals( "u1", key.getName() );
			assertTrue( table == key.getTable() );
			assertNotNull( key.getColumns() );
			int j = 0;
			for ( Column column : key.getColumns() ) {
				j++;
			}
			assertEquals( "There should be two columns in the unique constraint", 2, j );
		}
		assertEquals( "There should only be one unique constraint", 1, i );
	}

	@Entity
	@Table(uniqueConstraints = { @UniqueConstraint(name = "u1", columnNames = { "name", "age" }) })
	class TableWithUniqueConstraint {
		@Id
		int id;
		String name;
		int age;
	}
}
