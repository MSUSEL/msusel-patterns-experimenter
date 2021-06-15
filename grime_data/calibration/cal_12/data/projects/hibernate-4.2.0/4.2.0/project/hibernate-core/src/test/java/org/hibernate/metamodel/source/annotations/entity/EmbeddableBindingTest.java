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
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;

import org.hibernate.annotations.Parent;
import org.hibernate.annotations.Target;
import org.hibernate.metamodel.binding.BasicAttributeBinding;
import org.hibernate.metamodel.binding.ComponentAttributeBinding;
import org.hibernate.metamodel.binding.EntityBinding;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for {@code javax.persistence.Embeddable}.
 *
 * @author Hardy Ferentschik
 */
public class EmbeddableBindingTest extends BaseAnnotationBindingTestCase {
	@Entity
	class User {
		@Id
		private int id;

		@Embedded
		private Phone phone;
	}

	@Embeddable
	class Phone {
		String countryCode;
		String areaCode;
		String number;
	}

	@Test
	@Resources(annotatedClasses = { User.class, Phone.class })
	public void testEmbeddable() {
		EntityBinding binding = getEntityBinding( User.class );

		final String componentName = "phone";
		assertNotNull( binding.locateAttributeBinding( componentName ) );
		assertTrue( binding.locateAttributeBinding( componentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding componentBinding = (ComponentAttributeBinding) binding.locateAttributeBinding(
				componentName
		);

		// todo - is this really correct? Does the path start w/ the class name
		assertEquals(
				"Wrong path",
				"org.hibernate.metamodel.source.annotations.entity.EmbeddableBindingTest$User.phone",
				componentBinding.getPathBase()
		);

		assertNotNull( componentBinding.locateAttributeBinding( "countryCode" ) );
		assertNotNull( componentBinding.locateAttributeBinding( "areaCode" ) );
		assertNotNull( componentBinding.locateAttributeBinding( "number" ) );
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Entity
	@AttributeOverride(name = "embedded.name", column = @Column(name = "FUBAR", length = 42))
	class BaseEntity {
		@Id
		private int id;

		@Embedded
		private EmbeddedEntity embedded;
	}

	@Embeddable
	class EmbeddedEntity {
		String name;
	}

	@Test
	@Resources(annotatedClasses = { BaseEntity.class, EmbeddedEntity.class })
	public void testEmbeddableWithAttributeOverride() {
		EntityBinding binding = getEntityBinding( BaseEntity.class );

		final String componentName = "embedded";
		assertNotNull( binding.locateAttributeBinding( componentName ) );
		assertTrue( binding.locateAttributeBinding( componentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding componentBinding = (ComponentAttributeBinding) binding.locateAttributeBinding(
				componentName
		);

		assertNotNull( componentBinding.locateAttributeBinding( "name" ) );
		BasicAttributeBinding nameAttribute = (BasicAttributeBinding) componentBinding.locateAttributeBinding( "name" );
		org.hibernate.metamodel.relational.Column column = (org.hibernate.metamodel.relational.Column) nameAttribute.getValue();
		assertEquals( "Attribute override specifies a custom column name", "FUBAR", column.getColumnName().getName() );
		assertEquals( "Attribute override specifies a custom size", 42, column.getSize().getLength() );
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Embeddable
	public class Address {
		protected String street;
		protected String city;
		protected String state;
		@Embedded
		protected Zipcode zipcode;
	}

	@Embeddable
	public class Zipcode {
		protected String zip;
		protected String plusFour;
	}

	@Entity
	public class Customer {
		@Id
		protected Integer id;
		protected String name;
		@AttributeOverrides( {
				@AttributeOverride(name = "state",
						column = @Column(name = "ADDR_STATE")),
				@AttributeOverride(name = "zipcode.zip",
						column = @Column(name = "ADDR_ZIP"))
		})
		@Embedded
		protected Address address;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	@Resources(annotatedClasses = { Zipcode.class, Address.class, Customer.class })
	public void testNestedEmbeddable() {
		EntityBinding binding = getEntityBinding( Customer.class );

		final String addressComponentName = "address";
		assertNotNull( binding.locateAttributeBinding( addressComponentName ) );
		assertTrue( binding.locateAttributeBinding( addressComponentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding attributeComponentBinding = (ComponentAttributeBinding) binding.locateAttributeBinding(
				addressComponentName
		);

		assertNotNull( attributeComponentBinding.locateAttributeBinding( "street" ) );
		assertNotNull( attributeComponentBinding.locateAttributeBinding( "city" ) );
		assertNotNull( attributeComponentBinding.locateAttributeBinding( "state" ) );

		BasicAttributeBinding stateAttribute = (BasicAttributeBinding) attributeComponentBinding.locateAttributeBinding(
				"state"
		);
		org.hibernate.metamodel.relational.Column column = (org.hibernate.metamodel.relational.Column) stateAttribute.getValue();
		assertEquals(
				"Attribute override specifies a custom column name",
				"ADDR_STATE",
				column.getColumnName().getName()
		);


		final String zipComponentName = "zipcode";
		assertNotNull( attributeComponentBinding.locateAttributeBinding( zipComponentName ) );
		assertTrue( attributeComponentBinding.locateAttributeBinding( zipComponentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding zipComponentBinding = (ComponentAttributeBinding) attributeComponentBinding.locateAttributeBinding(
				zipComponentName
		);

		BasicAttributeBinding nameAttribute = (BasicAttributeBinding) zipComponentBinding.locateAttributeBinding( "zip" );
		column = (org.hibernate.metamodel.relational.Column) nameAttribute.getValue();
		assertEquals(
				"Attribute override specifies a custom column name",
				"ADDR_ZIP",
				column.getColumnName().getName()
		);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Embeddable
	public class A {
		@Embedded
		@AttributeOverrides( {
				@AttributeOverride(name = "foo", column = @Column(name = "BAR")),
				@AttributeOverride(name = "fubar", column = @Column(name = "A_WINS"))
		})
		private B b;

		public B getB() {
			return b;
		}
	}

	@Embeddable
	public class B {
		private String foo;
		private String fubar;

		public String getFoo() {
			return foo;
		}

		public String getFubar() {
			return fubar;
		}
	}

	@Entity
	public class C {
		@Id
		int id;

		@Embedded
		@AttributeOverride(name = "b.fubar", column = @Column(name = "C_WINS"))
		protected A a;

		public int getId() {
			return id;
		}

		public A getA() {
			return a;
		}
	}

	@Test
	@Resources(annotatedClasses = { A.class, B.class, C.class })
	public void testAttributeOverrideInEmbeddable() {
		EntityBinding binding = getEntityBinding( C.class );

		final String aComponentName = "a";
		assertNotNull( binding.locateAttributeBinding( aComponentName ) );
		assertTrue( binding.locateAttributeBinding( aComponentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding aComponentBinding = (ComponentAttributeBinding) binding.locateAttributeBinding(
				aComponentName
		);

		final String bComponentName = "b";
		assertNotNull( aComponentBinding.locateAttributeBinding( bComponentName ) );
		assertTrue( aComponentBinding.locateAttributeBinding( bComponentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding bComponentBinding = (ComponentAttributeBinding) aComponentBinding.locateAttributeBinding(
				bComponentName
		);

		BasicAttributeBinding attribute = (BasicAttributeBinding) bComponentBinding.locateAttributeBinding( "foo" );
		org.hibernate.metamodel.relational.Column column = (org.hibernate.metamodel.relational.Column) attribute.getValue();
		assertEquals(
				"Attribute override specifies a custom column name",
				"BAR",
				column.getColumnName().getName()
		);

		attribute = (BasicAttributeBinding) bComponentBinding.locateAttributeBinding( "fubar" );
		column = (org.hibernate.metamodel.relational.Column) attribute.getValue();
		assertEquals(
				"Attribute override specifies a custom column name",
				"C_WINS",
				column.getColumnName().getName()
		);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Embeddable
	public class EmbeddableEntity {
		private String test;
		@Parent
		private MainEntity parent;
	}

	@Entity
	public class MainEntity {
		@Id
		private int id;

		@Embedded
		private EmbeddableEntity embedded;
	}

	@Test
	@Resources(annotatedClasses = { MainEntity.class, EmbeddableEntity.class })
	public void testParentReferencingAttributeName() {
		EntityBinding binding = getEntityBinding( MainEntity.class );

		final String componentName = "embedded";
		assertNotNull( binding.locateAttributeBinding( componentName ) );
		assertTrue( binding.locateAttributeBinding( componentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding componentBinding = (ComponentAttributeBinding) binding.locateAttributeBinding(
				componentName
		);

		assertEquals( "Wrong parent reference name", "parent", componentBinding.getParentReference().getName() );
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public interface Car {
		int getHorsePower();
	}

	@Embeddable
	public class CarImpl implements Car {
		@Override
		public int getHorsePower() {
			return 0;
		}
	}

	@Entity
	public class Owner {
		private int id;
		private Car car;

		@Id
		public int getId() {
			return id;
		}

		@Embedded
		@Target(CarImpl.class)
		public Car getCar() {
			return car;
		}
	}

	@Test
	@Resources(annotatedClasses = { Owner.class, CarImpl.class, Car.class })
	public void testTargetAnnotationWithEmbeddable() {
		EntityBinding binding = getEntityBinding( Owner.class );

		final String componentName = "car";
		assertNotNull( binding.locateAttributeBinding( componentName ) );
		assertTrue( binding.locateAttributeBinding( componentName ) instanceof ComponentAttributeBinding );
		ComponentAttributeBinding componentBinding = (ComponentAttributeBinding) binding.locateAttributeBinding(
				componentName
		);

		BasicAttributeBinding attribute = (BasicAttributeBinding) componentBinding.locateAttributeBinding( "horsePower" );
		assertTrue( attribute.getAttribute().isTypeResolved() );
		assertEquals(
				"Wrong resolved type",
				"int",
				attribute.getAttribute().getSingularAttributeType().getClassName()
		);
	}
}


