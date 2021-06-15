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
package org.hibernate.metamodel.source.annotations.util;

import javax.persistence.Id;

import org.junit.Test;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 * @author Hardy Ferentschik
 */
public class TypeDiscoveryTest extends BaseAnnotationIndexTestCase {

	@Test
	public void testImplicitAndExplicitType() {
//		Set<ConfiguredClassHierarchy<EntityClass>> hierarchies = createEntityHierarchies( Entity.class );
//		assertEquals( "There should be only one hierarchy", 1, hierarchies.size() );
//
//		Iterator<EntityClass> iter = hierarchies.iterator().next().iterator();
//		ConfiguredClass configuredClass = iter.next();
//
//		MappedAttribute property = configuredClass.getMappedAttribute( "id" );
//		assertEquals( "Unexpected property type", int.class, property.getJavaType() );
//
//		property = configuredClass.getMappedAttribute( "string" );
//		assertEquals( "Unexpected property type", String.class, property.getJavaType() );
//
//		property = configuredClass.getMappedAttribute( "customString" );
//		assertEquals( "Unexpected property type", "my.custom.Type", property.getExplicitHibernateTypeName() );
//
//		Map<String, String> typeParameters = property.getExplicitHibernateTypeParameters();
//		assertEquals( "There should be a type parameter", "bar", typeParameters.get( "foo" ) );
	}

	@javax.persistence.Entity
	class Entity {
		@Id
		private int id;
		private String string;
		@Type(type = "my.custom.Type", parameters = { @Parameter(name = "foo", value = "bar") })
		private String customString;
	}
}