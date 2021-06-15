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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.SingularAttributeBinding;
import org.hibernate.metamodel.domain.NonEntity;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.testing.FailureExpected;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for {@link javax.persistence.MappedSuperclass} {@link javax.persistence.AttributeOverrides}
 * and {@link javax.persistence.AttributeOverride}.
 *
 * @author Hardy Ferentschik
 */
@FailureExpected(jiraKey = "HHH-6447", message = "Work in progress")
public class MappedSuperclassTest extends BaseAnnotationBindingTestCase {
	@Test
//	@Resources(annotatedClasses = { MyMappedSuperClass.class, MyEntity.class, MyMappedSuperClassBase.class })
	public void testSimpleAttributeOverrideInMappedSuperclass() {
		EntityBinding binding = getEntityBinding( MyEntity.class );
		SingularAttributeBinding nameBinding = (SingularAttributeBinding) binding.locateAttributeBinding( "name" );
		assertNotNull( "the name attribute should be bound to MyEntity", nameBinding );

		Column column = (Column) nameBinding.getValue();
		assertEquals( "Wrong column name", "MY_NAME", column.getColumnName().toString() );
	}

	@Test
//	@Resources(annotatedClasses = { MyMappedSuperClass.class, MyEntity.class, MyMappedSuperClassBase.class })
	public void testLastAttributeOverrideWins() {
		EntityBinding binding = getEntityBinding( MyEntity.class );
		SingularAttributeBinding fooBinding = (SingularAttributeBinding) binding.locateAttributeBinding( "foo" );
		assertNotNull( "the foo attribute should be bound to MyEntity", fooBinding );

		Column column = (Column) fooBinding.getValue();
		assertEquals( "Wrong column name", "MY_FOO", column.getColumnName().toString() );
	}

	@Test
//	@Resources(annotatedClasses = { SubclassOfNoEntity.class, NoEntity.class })
	public void testNonEntityBaseClass() {
		EntityBinding binding = getEntityBinding( SubclassOfNoEntity.class );
		assertEquals( "Wrong entity name", SubclassOfNoEntity.class.getName(), binding.getEntity().getName() );
		assertEquals( "Wrong entity name", NoEntity.class.getName(), binding.getEntity().getSuperType().getName() );
		assertTrue( binding.getEntity().getSuperType() instanceof NonEntity );
	}

	@MappedSuperclass
	class MyMappedSuperClassBase {
		@Id
		private int id;
		String foo;
	}

	@MappedSuperclass
	@AttributeOverride(name = "foo", column = @javax.persistence.Column(name = "SUPER_FOO"))
	class MyMappedSuperClass extends MyMappedSuperClassBase {
		String name;
	}

	@Entity
	@AttributeOverrides( {
			@AttributeOverride(name = "name", column = @javax.persistence.Column(name = "MY_NAME")),
			@AttributeOverride(name = "foo", column = @javax.persistence.Column(name = "MY_FOO"))
	})
	class MyEntity extends MyMappedSuperClass {
		private Long count;

	}

	class NoEntity {
		String name;
		int age;
	}

	@Entity
	class SubclassOfNoEntity extends NoEntity {
		@Id
		private int id;
	}
}


