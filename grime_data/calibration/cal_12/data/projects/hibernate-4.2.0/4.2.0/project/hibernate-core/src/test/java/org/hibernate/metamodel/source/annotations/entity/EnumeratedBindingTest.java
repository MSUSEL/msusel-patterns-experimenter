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

import java.sql.Types;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Test;

import org.hibernate.metamodel.binding.AttributeBinding;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.HibernateTypeDescriptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Strong Liu
 */
public class EnumeratedBindingTest extends BaseAnnotationBindingTestCase {
	@Entity
	class Item {
		@Id
		long id;
		@Temporal(TemporalType.TIMESTAMP)
		Date orderDate;
		String name;
		@Enumerated(EnumType.STRING)
		OrderType orderType;
		CustomerType customerType;
	}

	enum CustomerType {
		PROGRAMMER, BOSS;
	}

	enum OrderType {
		B2C, C2C, MAIL, DIRECT;
	}

	@Test
	@Resources(annotatedClasses = Item.class)
	public void testEnumeratedTypeAttribute() {
		EntityBinding binding = getEntityBinding( Item.class );

		AttributeBinding attributeBinding = binding.locateAttributeBinding( "customerType" );
		HibernateTypeDescriptor descriptor = attributeBinding.getHibernateTypeDescriptor();
		assertEquals( org.hibernate.type.EnumType.class.getName(), descriptor.getExplicitTypeName() );
		assertEquals( CustomerType.class.getName(), descriptor.getJavaTypeName() );
		assertNotNull( descriptor.getResolvedTypeMapping() );
		assertFalse( descriptor.getTypeParameters().isEmpty() );
		assertEquals(
				CustomerType.class.getName(),
				descriptor.getTypeParameters().get( org.hibernate.type.EnumType.ENUM )
		);
		assertEquals(
				String.valueOf( Types.INTEGER ),
				descriptor.getTypeParameters().get( org.hibernate.type.EnumType.TYPE )
		);


		attributeBinding = binding.locateAttributeBinding( "orderType" );
		descriptor = attributeBinding.getHibernateTypeDescriptor();
		assertEquals( org.hibernate.type.EnumType.class.getName(), descriptor.getExplicitTypeName() );
		assertEquals( OrderType.class.getName(), descriptor.getJavaTypeName() );
		assertNotNull( descriptor.getResolvedTypeMapping() );
		assertFalse( descriptor.getTypeParameters().isEmpty() );
		assertEquals(
				OrderType.class.getName(),
				descriptor.getTypeParameters().get( org.hibernate.type.EnumType.ENUM )
		);
		assertEquals(
				String.valueOf( Types.VARCHAR ),
				descriptor.getTypeParameters().get( org.hibernate.type.EnumType.TYPE )
		);
	}
}
