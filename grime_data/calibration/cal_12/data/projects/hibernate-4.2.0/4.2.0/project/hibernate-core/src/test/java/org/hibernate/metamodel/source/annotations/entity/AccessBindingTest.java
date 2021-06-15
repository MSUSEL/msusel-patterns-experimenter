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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;

import org.hibernate.AnnotationException;
import org.hibernate.metamodel.binding.EntityBinding;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for different types of attribute access
 *
 * @author Hardy Ferentschik
 */

public class AccessBindingTest extends BaseAnnotationBindingTestCase {
	@Entity
	class FieldAccess {
		@Id
		private int id;
	}

	@Test
	@Resources(annotatedClasses = { FieldAccess.class })
	public void testDefaultFieldAccess() {
		EntityBinding binding = getEntityBinding( FieldAccess.class );
		assertEquals( "Wrong access type", "field", binding.locateAttributeBinding( "id" ).getPropertyAccessorName() );
	}

	@Entity
	class PropertyAccess {
		private int id;

		@Id
		public int getId() {
			return id;
		}
	}

	@Test
	@Resources(annotatedClasses = { PropertyAccess.class })
	public void testDefaultPropertyAccess() {
		EntityBinding binding = getEntityBinding( PropertyAccess.class );
		assertEquals( "Wrong access type", "property", binding.locateAttributeBinding( "id" ).getPropertyAccessorName() );
	}


	@Entity
	class NoAccess {
		private int id;

		public int getId() {
			return id;
		}
	}

	@Test(expected = AnnotationException.class)
	@Resources(annotatedClasses = { NoAccess.class })
	public void testNoAccess() {
		// actual error happens when the binding gets created
	}

	@Entity
	class MixedAccess {
		@Id
		private int id;

		private String name;

		@Access(AccessType.PROPERTY)
		public String getName() {
			return name;
		}
	}

	@Test
	@Resources(annotatedClasses = { MixedAccess.class })
	public void testMixedAccess() {
		EntityBinding binding = getEntityBinding( MixedAccess.class );
		assertEquals( "Wrong access type", "field", binding.locateAttributeBinding( "id" ).getPropertyAccessorName() );
		assertEquals(
				"Wrong access type",
				"property",
				binding.locateAttributeBinding( "name" ).getPropertyAccessorName()
		);
	}

	@Entity
	class Base {
		@Id
		int id;
	}

	@Entity
	@Access(AccessType.PROPERTY)
	class ClassConfiguredAccess extends Base {
		private String name;

		public String getName() {
			return name;
		}
	}

	@Test
	@Resources(annotatedClasses = { ClassConfiguredAccess.class, Base.class })
	public void testExplicitClassConfiguredAccess() {
		EntityBinding binding = getEntityBinding( Base.class );
		assertEquals(
				"Wrong access type",
				"field",
				binding.locateAttributeBinding( "id" ).getPropertyAccessorName()
		);


		binding = getEntityBinding( ClassConfiguredAccess.class );
		assertEquals(
				"Wrong access type",
				"property",
				binding.locateAttributeBinding( "name" ).getPropertyAccessorName()
		);
	}

}


