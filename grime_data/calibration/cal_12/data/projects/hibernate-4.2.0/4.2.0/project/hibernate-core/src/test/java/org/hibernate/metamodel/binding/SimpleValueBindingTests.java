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
package org.hibernate.metamodel.binding;

import java.sql.Types;

import org.junit.Test;

import org.hibernate.EntityMode;
import org.hibernate.internal.util.ValueHolder;
import org.hibernate.metamodel.domain.Entity;
import org.hibernate.metamodel.domain.SingularAttribute;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.Datatype;
import org.hibernate.metamodel.relational.Schema;
import org.hibernate.metamodel.relational.Size;
import org.hibernate.metamodel.relational.Table;
import org.hibernate.service.classloading.spi.ClassLoadingException;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertSame;

/**
 * Basic binding "smoke" tests
 *
 * @author Steve Ebersole
 */
public class SimpleValueBindingTests extends BaseUnitTestCase {
	public static final Datatype BIGINT = new Datatype( Types.BIGINT, "BIGINT", Long.class );
	public static final Datatype VARCHAR = new Datatype( Types.VARCHAR, "VARCHAR", String.class );


	@Test
	public void testBasicMiddleOutBuilding() {
		Table table = new Table( new Schema( null, null ), "the_table" );
		Entity entity = new Entity( "TheEntity", "NoSuchClass", makeJavaType( "NoSuchClass" ), null );
		EntityBinding entityBinding = new EntityBinding( InheritanceType.NO_INHERITANCE, EntityMode.POJO );
		entityBinding.setEntity( entity );
		entityBinding.setPrimaryTable( table );

		SingularAttribute idAttribute = entity.createSingularAttribute( "id" );
		BasicAttributeBinding attributeBinding = entityBinding.makeBasicAttributeBinding( idAttribute );
		attributeBinding.getHibernateTypeDescriptor().setExplicitTypeName( "long" );
		assertSame( idAttribute, attributeBinding.getAttribute() );

		entityBinding.getHierarchyDetails().getEntityIdentifier().setValueBinding( attributeBinding );

		Column idColumn = table.locateOrCreateColumn( "id" );
		idColumn.setDatatype( BIGINT );
		idColumn.setSize( Size.precision( 18, 0 ) );
		table.getPrimaryKey().addColumn( idColumn );
		table.getPrimaryKey().setName( "my_table_pk" );
		//attributeBinding.setValue( idColumn );
	}

	ValueHolder<Class<?>> makeJavaType(final String name) {
		return new ValueHolder<Class<?>>(
				new ValueHolder.DeferredInitializer<Class<?>>() {
					@Override
					public Class<?> initialize() {
						try {
							return Class.forName( name );
						}
						catch ( Exception e ) {
							throw new ClassLoadingException( "Could not load class : " + name, e );
						}
					}
				}
		);
	}
}
