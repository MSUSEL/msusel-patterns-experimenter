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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import org.junit.Test;

import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.InheritanceType;

import static junit.framework.Assert.assertEquals;

/**
 * @author Hardy Ferentschik
 */
public class TableNameTest extends BaseAnnotationBindingTestCase {
	@Entity
	class A {
		@Id
		@GeneratedValue
		private int id;
	}

	@Entity
	class B extends A {
	}

	@Test
	@Resources(annotatedClasses = { A.class, B.class })
	public void testSingleInheritanceDefaultTableName() {
		EntityBinding binding = getEntityBinding( A.class );
		assertEquals( "wrong inheritance type", InheritanceType.SINGLE_TABLE, binding.getHierarchyDetails().getInheritanceType() );
		assertEquals(
				"wrong table name",
				"TableNameTest$A",
				( (org.hibernate.metamodel.relational.Table) binding.getPrimaryTable() ).getTableName().getName()
		);

		binding = getEntityBinding( B.class );
		assertEquals( "wrong inheritance type", InheritanceType.SINGLE_TABLE, binding.getHierarchyDetails().getInheritanceType() );
		assertEquals(
				"wrong table name",
				"TableNameTest$A",
				( (org.hibernate.metamodel.relational.Table) binding.getPrimaryTable() ).getTableName().getName()
		);
	}

	@Entity
	@Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
	@Table(name = "FOO")
	class JoinedA {
		@Id
		@GeneratedValue
		private int id;
	}

	@Entity
	class JoinedB extends JoinedA {
	}

	@Test
	@Resources(annotatedClasses = { JoinedA.class, JoinedB.class })
	public void testJoinedSubclassDefaultTableName() {
		EntityBinding binding = getEntityBinding( JoinedA.class );
		assertEquals( "wrong inheritance type", InheritanceType.JOINED, binding.getHierarchyDetails().getInheritanceType() );
		assertEquals(
				"wrong table name",
				"FOO",
				( (org.hibernate.metamodel.relational.Table) binding.getPrimaryTable() ).getTableName().getName()
		);

		binding = getEntityBinding( JoinedB.class );
		assertEquals( "wrong inheritance type", InheritanceType.JOINED, binding.getHierarchyDetails().getInheritanceType() );
		assertEquals(
				"wrong table name",
				"TableNameTest$JoinedB",
				( (org.hibernate.metamodel.relational.Table) binding.getPrimaryTable() ).getTableName().getName()
		);
	}


	@Entity
	@Inheritance(strategy = javax.persistence.InheritanceType.TABLE_PER_CLASS)
	class TablePerClassA {
		@Id
		@GeneratedValue
		private int id;
	}

	@Entity
	class TablePerClassB extends TablePerClassA {
	}

	@Test
	@Resources(annotatedClasses = { TablePerClassA.class, TablePerClassB.class })
	public void testTablePerClassDefaultTableName() {
		EntityBinding binding = getEntityBinding( TablePerClassA.class );
		assertEquals( "wrong inheritance type", InheritanceType.TABLE_PER_CLASS, binding.getHierarchyDetails().getInheritanceType() );
		assertEquals(
				"wrong table name",
				"TableNameTest$TablePerClassA",
				( (org.hibernate.metamodel.relational.Table) binding.getPrimaryTable() ).getTableName().getName()
		);

		binding = getEntityBinding( TablePerClassB.class );
		assertEquals( "wrong inheritance type", InheritanceType.TABLE_PER_CLASS, binding.getHierarchyDetails().getInheritanceType() );
		assertEquals(
				"wrong table name",
				"TableNameTest$TablePerClassB",
				( (org.hibernate.metamodel.relational.Table) binding.getPrimaryTable() ).getTableName().getName()
		);
	}
}


